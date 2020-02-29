package com.example.organize_clone.model;

import com.example.organize_clone.config.ConfigFireBase;
import com.example.organize_clone.helper.Base64Custom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Movimentacao {

    private String key;
    private String data;
    private String dia;
    private String categoria;
    private String descricao;
    private String tipo;
    private Double valor;


    public Movimentacao() {
    }

    public Movimentacao(String data, String categoria, String descricao, Double valor) {
        //this.dia = dia;
        this.data = data;
        this.categoria = categoria;
        this.descricao = descricao;
        this.valor = valor;
    }

    public void salvar(String data){


            //recuperar o email do usuario logado
            FirebaseAuth autenticar = ConfigFireBase.getFireBaseAutenticar();
            //passar o email para base64
            String idUsuario = Base64Custom.codificarBase64(autenticar.getCurrentUser().getEmail());
            //objeto que trabalha com o bd firebasedatabese
            DatabaseReference reference = ConfigFireBase.getFireBaseDB();
            reference.child("movimentacao")
                    .child(idUsuario)
                    .child(data)
                    .push()//cria um id unico
                    .setValue(this);

    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
