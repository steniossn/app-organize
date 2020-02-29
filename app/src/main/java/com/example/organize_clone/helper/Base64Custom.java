package com.example.organize_clone.helper;

import android.util.Base64;

public class Base64Custom {


    public static String codificarBase64(String string){
        return Base64.encodeToString(string.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    public static String decodificarBase64(String stringCodificado){
        return new String (Base64.decode(stringCodificado, Base64.DEFAULT ));
    }
}
