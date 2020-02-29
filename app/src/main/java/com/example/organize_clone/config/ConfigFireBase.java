package com.example.organize_clone.config;

import com.example.organize_clone.helper.Base64Custom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfigFireBase {

    private static FirebaseAuth autenticar;
    private static DatabaseReference firebaseDB;
    private static DatabaseReference usuarioRef;
    private static DatabaseReference movimentacaoRef;

    //retorna a instancia do firebase
    public static FirebaseAuth getFireBaseAutenticar(){
        if(autenticar == null){
           autenticar = FirebaseAuth.getInstance();
        }
        return autenticar;
    }

    //retorna a instancia do database
    public static DatabaseReference getFireBaseDB(){
        if(firebaseDB == null){
            firebaseDB = FirebaseDatabase.getInstance().getReference();
        }
        return firebaseDB;
    }

    //retorna até o nó com id do usuario em usuarios
    public static DatabaseReference usuarioRef() {

            // email do usuario em base64
            String idUsuario = Base64Custom.codificarBase64(autenticar.getCurrentUser().getEmail());
            //faz referencia ao nó usuario
            return usuarioRef = getFireBaseDB().child("Usuarios").child(idUsuario);

    }
    //retorna ate o nó com id de movimentaçoes
    public static DatabaseReference movimentacaoRef(){
            // email do usuario em base64
            String idUsuario = Base64Custom.codificarBase64(autenticar.getCurrentUser().getEmail());
            //faz referencia ao nó usuario
            return movimentacaoRef = getFireBaseDB().child("movimentacao").child(idUsuario);

    }

    public static FirebaseAuth getAutenticar() {
        return autenticar;
    }

    public static void setAutenticar(FirebaseAuth autenticar) {
        ConfigFireBase.autenticar = autenticar;
    }

    public static DatabaseReference getFirebaseDB() {
        return firebaseDB;
    }

    public static void setFirebaseDB(DatabaseReference firebaseDB) {
        ConfigFireBase.firebaseDB = firebaseDB;
    }

    public static DatabaseReference getUsuarioRef() {
        return usuarioRef;
    }

    public static void setUsuarioRef(DatabaseReference usuarioRef) {
        ConfigFireBase.usuarioRef = usuarioRef;
    }

    public static DatabaseReference getMovimentacaoRef() {
        return movimentacaoRef;
    }

    public static void setMovimentacaoRef(DatabaseReference movimentacaoRef) {
        ConfigFireBase.movimentacaoRef = movimentacaoRef;
    }
}
