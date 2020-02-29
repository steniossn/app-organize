package com.example.organize_clone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organize_clone.R;
import com.example.organize_clone.model.Movimentacao;

import java.text.DecimalFormat;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    List<Movimentacao> movimentacoes;
    Context context;

    public Adapter() {

    }

    //construtor que recebe o list com o model
    public Adapter(List <Movimentacao> movimentacoes , Context context){
        this.movimentacoes = movimentacoes;
        this.context = context;
    }

    @NonNull
    @Override
    //configura a view
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //tranformar o arquivo xml em uma view
        View movimentacoes = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movimentacao_list, parent, false);

        //retorna o objeto com a view configurada pelo xml
        return new MyViewHolder(movimentacoes);
    }

    @Override
    //exibe a view
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //pegar os valores
        Movimentacao movimentacao = movimentacoes.get(position);
        //formatar o saldo para remover zero depois do ponto
        DecimalFormat df = new DecimalFormat("0.##");
        String saldoF = df.format(movimentacao.getValor());
        //atribuir
        holder.valor.setText(saldoF);
        holder.valor.setTextColor(context.getResources().getColor(R.color.colorPrimaryDarkReceita));
        holder.desRec.setText(movimentacao.getCategoria());
        holder.descrição.setText(movimentacao.getDescricao());
        holder.data.setText(movimentacao.getDia());

        if( movimentacao.getTipo().equals("D")){
            holder.valor.setTextColor(context.getResources().getColor(R.color.colorAccentDespesa));
            holder.valor.setText("-" +saldoF);
        }
    }

    @Override
    //usado para definir a quantidade de vezes que as views serão construidas
    public int getItemCount() {
        return movimentacoes.size();
    }

    //classe que ira salvar os dados que serão exibidos nas view do recyclerView
    public class  MyViewHolder extends  RecyclerView.ViewHolder{

        //criar propriedades para espelhar o que estara nas views
        TextView valor;
        TextView data;
        TextView desRec;
        TextView descrição;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // atribui as variaveis os valores que estão sendo passados pelo itemView que foi passado pelo metodo onCreateViewHolder
            valor = itemView.findViewById(R.id.txtValor );
            data = itemView.findViewById(R.id.txtData );
            desRec = itemView.findViewById(R.id.txtDesRec );
            descrição = itemView.findViewById(R.id.txtDescrição );

        }
    }

}
