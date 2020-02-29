package com.example.organize_clone.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.example.organize_clone.adapter.Adapter;
import com.example.organize_clone.config.ConfigFireBase;
import com.example.organize_clone.helper.DateCustom;
import com.example.organize_clone.model.Movimentacao;
import com.example.organize_clone.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.organize_clone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {


    boolean visivel =true;

    private RecyclerView recyclerView;
    //lista que vai receber as informações de movimentaçoes
    private List<Movimentacao> lista = new ArrayList<Movimentacao>();
    // o objeto movimentacao vai receber um unico item vindo de lista
    private Movimentacao movimentacao;
    private Usuario usuario;
    private  Adapter adapter;

    // recebe o objeto responsavel pelo evento usuarioRef para poder finalizar o evento em onStop
    private ValueEventListener valueEventListenerUsuario;
    private ValueEventListener valueEventListenerMovimentacoes;

    private Double saldoBD, receitaTotal , despesaTotal;
    private String nomeUsuario;
    private String mesAno;
    //usado nos botoes fabLeft e fabRight
    int somaAtual = Integer.parseInt(DateCustom.mes());
    //ano atual
    int ano = Integer.parseInt(DateCustom.ano());

    ImageView circuloVerde, circuloVermelho;
    TextView txvData , txvSaldo , txvOla;
    FloatingActionButton fabLeft , fabRight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Organize");
        setSupportActionBar(toolbar);

        circuloVerde= findViewById(R.id.imageCirculo_verde);
        circuloVermelho= findViewById(R.id.imageCirculo_vermelho);
        txvData = findViewById(R.id.textViewData);
        txvOla = findViewById(R.id.textViewOla);
        txvSaldo = findViewById(R.id.textViewSaldo);
        fabLeft = findViewById(R.id.fabLeft);
        fabRight = findViewById(R.id.fabRight);
        recyclerView = findViewById(R.id.recyclerView);

        //configurar adapter
        // passar os valores pelo costrutor do adapter
        adapter = new Adapter(lista, this);

        //configurar recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //quantidade fixa para carregamento
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        circuloVerde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ReceitasActivity.class));
            }
        });

        circuloVermelho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DespesasActivity.class));

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set visible das imagens circle para true
                //set visible dos textos para true
                carregarBotao();
            }
        });

        fabLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                somaAtual --;
                txvData.setText(geraMes(somaAtual));
                String mesFormatado = String.format("%02d", somaAtual );
                //remover ouvinte para não gerar varios ouvintes
                ConfigFireBase.movimentacaoRef().removeEventListener(valueEventListenerMovimentacoes);
                //
                carregarMovimentacao( mesFormatado ,String.valueOf(ano));
            }
        });

        fabRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                somaAtual++;
                txvData.setText(geraMes(somaAtual));
                //formata a numeração preenchendo com zero a esquerda conforme o valor passado nesse caso 2
                String mesFormatado = String.format("%02d", somaAtual );
                //remover ouvinte para não gerar varios ouvintes
                ConfigFireBase.movimentacaoRef().removeEventListener(valueEventListenerMovimentacoes);
                carregarMovimentacao( mesFormatado ,String.valueOf(ano));

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //metodo pega o arquivo xml e converte em uma view
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSair:
                //deslogar usuario
                ConfigFireBase.getAutenticar().signOut();
                //abrir tela de cadastro
                startActivity(new Intent(this, MainActivity.class));
                //fechar tela atual

                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    //metodo muda visibilidade de dois botoes acima do  FloatingActionButton principal
    public void carregarBotao(){
        if(visivel){
            circuloVerde.setVisibility(View.INVISIBLE);
            circuloVermelho.setVisibility(View.INVISIBLE);
            visivel=false;
        }else if(!visivel){
            circuloVerde.setVisibility(View.VISIBLE);
            circuloVermelho.setVisibility(View.VISIBLE);
            visivel=true
            ;
        }
    }

    //usado nos botoes fabLeft e fabRight
    public String geraMes(int n){

        //array com os meses sendo o indix 0 vazio
        String[] meses = {"","Janeiro", "Fevereiro", "Março" , "Abril" , "Maio" , "Junho" , "Julho" , "Agosto" , "Setembro" , "Outubro" , "Novembro" , "Dezembro"};

        //muda o mes conforme é feito o clic
        if(n > 12 ){
            //atualiza a variavel
            somaAtual = 1;
            //acrecenta um ano ao ano atual
            ano++;
            return meses[somaAtual] + " " + ano ;
        }else if (n == 0 ){
            //atualiza a variavel
            somaAtual = 12;
            //remove um ano do ano atual
            ano--;
            return meses[somaAtual] +" "+ano ;
        }else{
            return meses[somaAtual]  +" "+ ano;
        }


    }

    //atualiza o listView com as movimentaçoes do mes anexa um ouvinte de database
    public void carregarMovimentacao(String mes, String ano){


        //pegar os valores do banco de dados
        mesAno = mes + ano;
        //adiciona um ouvinte
        valueEventListenerMovimentacoes = ConfigFireBase.movimentacaoRef().child(mesAno).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //limpa a lista para não duplicar dados
                lista.clear();

                //pegar cada nó filho de movimentacao.usuario.mesAno
               for(DataSnapshot dados : dataSnapshot.getChildren()){
                   //coloca os dados na classe modelo Movimentacao
                    Movimentacao mov = dados.getValue(Movimentacao.class);

                    //pego o key para identificar o item selecionado no metodo swipe
                    mov.setKey(dados.getKey());

                    //separo o dia da data
                    String[] splitData = mov.getData().split("/");
                    //passo o dia para o modelo
                    mov.setDia(splitData[0]);
                    //adiciona a classe a um array
                    lista.add(mov);

                    //sera exibido os dados configurados na classe Adapter
               }
               //informa ao adapter que houve modificação
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    //carrega o saldo pego no banco de dados em uma textView do app
    public void carregarSaldo(){


        valueEventListenerUsuario = ConfigFireBase.usuarioRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // metodo pega as propriedades no db e gera um usuario em model com essas propriedades
                usuario =dataSnapshot.getValue(Usuario.class);
                saldoBD = Double.valueOf(usuario.getSaldo());
                nomeUsuario = usuario.getNome();

                DecimalFormat df = new DecimalFormat("0.##");
                String saldoF = df.format(saldoBD);

                txvOla.setText( "Olá, "+ nomeUsuario);
                txvSaldo.setText("R$ " + saldoF );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //atualiza o saldo quando o usuario exclui uma movimentação
    public void atualizarSaldo(){

        switch (movimentacao.getTipo()){
            case "D":
                saldoBD = saldoBD + movimentacao.getValor();
                despesaTotal = usuario.getDespesaTotal() - movimentacao.getValor();
                ConfigFireBase.usuarioRef().child("despesaTotal").setValue(despesaTotal);
                ConfigFireBase.usuarioRef().child("saldo").setValue(saldoBD);
                break;
            case "R":
                saldoBD = saldoBD - movimentacao.getValor();
                receitaTotal = usuario.getReceitaTotal() - movimentacao.getValor();
                ConfigFireBase.usuarioRef().child("receitaTotal").setValue(receitaTotal);
                ConfigFireBase.usuarioRef().child("saldo").setValue(saldoBD);
                break;

        }
    }

    //metodo que adiciona evento de deslisar nos itens do recyclerView
    public void swipe(){

        //instancia a classe que manipula os eventos de itens do recyclerView
        ItemTouchHelper.Callback itemCallback = new ItemTouchHelper.Callback() {
            @Override
            //pega o movimento realizado no item
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                //configurar para movimentos de pegar e soltar como false
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                //configurar para o deslizar poder ser feito do inicio para o final e para o inicio
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirMovimentacao(viewHolder);

            }
        };

        //anexar o objeto itemCallback ao recyclerView
        new ItemTouchHelper(itemCallback).attachToRecyclerView(recyclerView);

    }

    //excluir do banco de dados
    public void excluirMovimentacao(final RecyclerView.ViewHolder viewHolder){
        //cria o objeto
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Excluir");
        alertDialog.setMessage("tem certeza que quer excluir permanentemente este intem?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //recebe a posição do item no adapter viewHolder vem do parametro do metodo
                int position = viewHolder.getAdapterPosition();
                //adiciona ao objeto movimentacao um item recuperado do array lista
                movimentacao = lista.get(position);
                //acessa o database ate o nó movimentacao.usuario.mesAno.key e remove o nó
                ConfigFireBase.movimentacaoRef().child(mesAno).child(movimentacao.getKey()).removeValue();
                //informa ao adapter q foi removido um item na posição tal
                adapter.notifyItemRemoved(position);
                atualizarSaldo();


            }
        });

        alertDialog.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"cancelado", Toast.LENGTH_SHORT).show();
                //atualiza os dados do recyclerView ao notificar a mudança ao database
                adapter.notifyDataSetChanged();
            }
        });

        //cria o objeto alertDialog modificado
        AlertDialog alert = alertDialog.create();
        //constroi
        alert.show();

    }

    @Override
    protected void onStart(){
        super.onStart();
        visivel = true;
        carregarBotao();
        //anexa um evento do firebase
        carregarSaldo();
        txvData.setText(DateCustom.mesAtual());
        /*formatação % simbolo coringa que informa que sera feito uma formatação
        * 0 é caracter que sera usado para preenchimento mas poderia ser outro caracter
        * 2 é a quantidade de casas decimais que a formatação tera
        * d referece a digito para informar que é um numero*/
        String mesFormatado = String.format("%02d", somaAtual );
        carregarMovimentacao(mesFormatado,String.valueOf(ano));
        swipe();

    }


    @Override
    protected void onStop() {
        super.onStop();
        //desanexa o evento do firebase para parar de receber dele



    }
}
