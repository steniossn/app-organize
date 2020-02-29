package com.example.organize_clone.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.organize_clone.R;
import com.example.organize_clone.config.ConfigFireBase;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity {

    private FirebaseAuth autenticar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        //setContentView(R.layout.activity_main);

        //criar a tela em slider
        addSlide(new SimpleSlide.Builder()
                .title("titulo")
                .description("descrição")
                .background(android.R.color.holo_blue_bright)
                .image(R.drawable.casa)
                .build()
        );
        */


        // remover botoes
        setButtonBackVisible(false);
        setButtonNextVisible(false);

        //criar a tela slider usando fragment

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_1)
                .build()
        );
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_2)
                .build()
        );
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_3)
                .build()
        );
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_4)
                .build()
        );
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_cadastrar)
                .canGoForward(false)
                .build()
        );
    }

    @Override
    protected void onStart(){
        super.onStart();
        verificarUsuarioLogado();

    }



    public void btCadastrar( View view ){
        startActivity(new Intent(this, CadastrarActivity.class));

    }
    public void btLogar(View view ){

        startActivity(new Intent(this, LoginActivity.class));

    }

    public void verificarUsuarioLogado(){

        autenticar = ConfigFireBase.getFireBaseAutenticar();
        if(autenticar.getCurrentUser() != null){
            startActivity(new Intent(this,PrincipalActivity.class));
            finish();

        }
    }
}

