package com.example.organize_clone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organize_clone.R;
import com.example.organize_clone.config.ConfigFireBase;
import com.example.organize_clone.helper.Base64Custom;
import com.example.organize_clone.helper.DateCustom;
import com.example.organize_clone.model.Movimentacao;
import com.example.organize_clone.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DespesasActivity extends AppCompatActivity {

    TextInputEditText campoData , campoDespesa , campoDescricao;
    EditText campoValor;
    private Movimentacao movimentacao;
    private DatabaseReference dataBaseRef = ConfigFireBase.getFireBaseDB();
    private FirebaseAuth autenticar = ConfigFireBase.getFireBaseAutenticar();
    private Double despesaTotal;
    private Double saldoBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        //faz referencia aos views da tela
        campoData = findViewById(R.id.TIEData);
        campoDespesa = findViewById(R.id.TIEDespesa);
        campoDescricao = findViewById(R.id.TIEDescricao);
        campoValor = findViewById(R.id.editTextValor);
        campoData.setText(DateCustom.dataAtual());
        recuperarDesTotal();



    }

    public void salvarDespesas(View view) {

        if(camposPreenchidos()) {
            movimentacao = new Movimentacao();
            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());
            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(campoDespesa.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(campoData.getText().toString());
            movimentacao.setTipo("D");
            //remover dia e separadores da data ficando apenas mes e ano ex: 032019
            String[] splitData = campoData.getText().toString().split("/");
            String idData = splitData[1] + splitData[2];


            Double despesaAtualizada = despesaTotal + valorRecuperado;
            Double saldo = saldoBD - valorRecuperado;
            atualizarDespesa(despesaAtualizada, saldo);
            movimentacao.salvar(idData);
            finish();
        }
    }

    public Boolean camposPreenchidos(){
        if(!campoValor.getText().toString().trim().isEmpty()){
            if(!campoData.getText().toString().trim().isEmpty()){
                if(!campoDespesa.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "adicionado!",Toast.LENGTH_LONG).show();
                    return true;

                }else {
                    Toast.makeText(getApplicationContext(),
                            "preencha despesa !",Toast.LENGTH_LONG).show();
                    return false;
                }
            }else {
                Toast.makeText(getApplicationContext(),
                        "preencha a data!",Toast.LENGTH_LONG).show();
                return false;
            }
        }else{
            Toast.makeText(getApplicationContext(),
                    "preencha o valor!",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void recuperarDesTotal(){

        String idUsuario = Base64Custom.codificarBase64(autenticar.getCurrentUser().getEmail());
        //faz referencia ao nó usuario
        DatabaseReference usuarioRef = dataBaseRef.child("Usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // metodo pega as propriedades no db e gera um usuario em model com essas propriedades
                Usuario usuario =dataSnapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
                saldoBD = usuario.getSaldo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void atualizarDespesa(Double despesa, Double saldo){
        String idUsuario = Base64Custom.codificarBase64(autenticar.getCurrentUser().getEmail());
        //faz referencia ao nó usuario
        DatabaseReference usuarioRef = dataBaseRef.child("Usuarios").child(idUsuario);
        //altera o vbalor de despesa total em usuario no bd
        usuarioRef.child("despesaTotal").setValue(despesa);
        usuarioRef.child("saldo").setValue(saldo);
    }
}
