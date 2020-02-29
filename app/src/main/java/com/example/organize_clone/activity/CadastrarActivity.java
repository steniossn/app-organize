package com.example.organize_clone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organize_clone.R;
import com.example.organize_clone.config.ConfigFireBase;
import com.example.organize_clone.helper.Base64Custom;
import com.example.organize_clone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastrarActivity extends AppCompatActivity {


    private EditText campoNome, campoEmail ,campoSenha ;
    private Button buttonCadastrar;

    private FirebaseAuth autenticar;
    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);
        campoNome = findViewById(R.id.editNome);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        buttonCadastrar = findViewById(R.id.btCadastrar);

        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = campoNome.getText().toString();
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if(!nome.isEmpty()){
                    if(!email.isEmpty()){
                        if(!senha.isEmpty()){
                                usuario= new Usuario();
                                usuario.setNome(nome);
                                usuario.setSenha(senha);
                                usuario.setEmail(email);
                                cadastrarUsuario();

                        }else{
                            Toast.makeText(getApplicationContext(), "preencha a senha!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "preencha o email!",
                                Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "preencha o nome!",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void cadastrarUsuario(){
        autenticar = ConfigFireBase.getFireBaseAutenticar();
        autenticar.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setIdUsuario(idUsuario);
                    usuario.salvarUsuario();
                    finish();

                }else{


                    String excecao = "";
                    try{
                        throw task.getException();
                    }catch ( FirebaseAuthWeakPasswordException e){
                        excecao = "informe uma senha com no minimo 6 caracteres!";
                    }catch ( FirebaseAuthInvalidCredentialsException e){
                        excecao = "informe um email valido!";
                    }catch ( FirebaseAuthUserCollisionException e ){
                        excecao = "usuário ja cadastrado!";
                    }catch (Exception e){
                        excecao = "erro "+ e.getMessage();
                        e.printStackTrace();
                    }


                    Toast.makeText(getApplicationContext(), excecao,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
