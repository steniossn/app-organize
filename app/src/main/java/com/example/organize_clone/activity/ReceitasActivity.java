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


public class ReceitasActivity extends AppCompatActivity {

    private TextInputEditText campoData , campoReceita, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private Double receitaTotal , saldoBD;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        campoData= findViewById(R.id.editTextInputData);
        campoReceita= findViewById(R.id.editTextInputReceita);
        campoDescricao= findViewById(R.id.editTextInputDescricao);
        campoValor= findViewById(R.id.editTextValor);

        campoData.setText(DateCustom.dataAtual());
        recuperarRecTotal();

    }

    public void salvarReceita(View view){

        if(camposPreenchidos()) {
            movimentacao = new Movimentacao();
            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());
            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(campoReceita.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(campoData.getText().toString());
            movimentacao.setTipo("R");


            Double receitaAtualizada = receitaTotal + valorRecuperado;
            Double saldo = saldoBD + valorRecuperado;
            atualizarReceita(receitaAtualizada, saldo);
            //remover dia e separadores da data ficando apenas mes e ano ex: 032019
            String[] splitData = campoData.getText().toString().split("/");
            String idData = splitData[1] + splitData[2];
            movimentacao.salvar(idData);
            finish();
        }
    }

    public Boolean camposPreenchidos(){
        if(!campoValor.getText().toString().trim().isEmpty()){
            if(!campoData.getText().toString().trim().isEmpty()){
                if(!campoReceita.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "adicionado!",Toast.LENGTH_LONG).show();
                    return true;

                }else {
                    Toast.makeText(getApplicationContext(),
                            "preencha receita !",Toast.LENGTH_LONG).show();
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



    public void recuperarRecTotal(){

        ConfigFireBase.usuarioRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // metodo pega as propriedades no db e gera um usuario em model com essas propriedades
                Usuario usuario =dataSnapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
                saldoBD = usuario.getSaldo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void atualizarReceita(Double receita, Double saldo){

        //altera o valor de receita total em usuario no bd
        ConfigFireBase.usuarioRef().child("receitaTotal").setValue(receita);
        ConfigFireBase.usuarioRef().child("saldo").setValue(saldo);
    }

    public void formatarData(){

    }

}
