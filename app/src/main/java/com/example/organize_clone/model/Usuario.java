package com.example.organize_clone.model;

import com.example.organize_clone.config.ConfigFireBase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;



public class Usuario {
    private String nome;
    private String email;
    private String senha;
    private String idUsuario;
    private Double despesaTotal= 0.00;
    private Double receitaTotal= 0.00;
    private Double saldo = 0.00;





    public Usuario() {
    }

    public void salvarUsuario(){
        //objeto que permite salvar dados no database
        DatabaseReference  firebaseDB = ConfigFireBase.getFireBaseDB();
        firebaseDB.child("Usuarios") //cria o nó para armazenar todos os usuarios do app
                .child(this.idUsuario)  //criar o nó referente ao email codificado do usuario
                .setValue(this); //salvar o objeto usuario
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Double getDespesaTotal() {
        return despesaTotal;
    }

    public void setDespesaTotal(Double despesaTotal) {
        this.despesaTotal = despesaTotal;
    }

    public Double getReceitaTotal() {
        return receitaTotal;
    }

    public void setReceitaTotal(Double receitaTotal) {
        this.receitaTotal = receitaTotal;
    }

    //essa anotação faz o firebase ignorar essa informação na hr de salvar o objeto this
    @Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
