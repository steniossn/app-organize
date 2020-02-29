package com.example.organize_clone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organize_clone.R;
import com.example.organize_clone.config.ConfigFireBase;
import com.example.organize_clone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;


public class LoginActivity extends AppCompatActivity {

    EditText campoEmail , campoSenha;
    Button btEntrar;
    private FirebaseAuth autenticar;

    Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        btEntrar = findViewById(R.id.buttonEntrar);

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if(!email.isEmpty()){
                    if(!senha.isEmpty()){

                        usuario = new Usuario();
                        usuario.setEmail(email);
                        usuario.setSenha(senha);
                        validarLogin();
                    }else{
                        Toast.makeText(getApplicationContext(), "informe uma senha!",
                                Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "informe um email!",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void validarLogin(){
        autenticar = ConfigFireBase.getFireBaseAutenticar();
        autenticar.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    abrirTelaPrincipal();

                }else {  //tratamento de erros

                    //string usada para passar a msg de erro
                    String excecao = "";
                    try{
                        throw task.getException();

                    }catch ( FirebaseAuthInvalidCredentialsException e){
                        excecao = "usuario ou senha invalido!";
                    }catch ( FirebaseAuthInvalidUserException e ){
                        excecao = "usuário invalido!";
                    }catch (Exception e){
                        excecao = "erro "+ e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(),excecao,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
    }
}
