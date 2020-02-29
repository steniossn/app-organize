package com.example.organize_clone.helper;


import android.widget.CalendarView;

import java.text.SimpleDateFormat;

public class DateCustom {

    CalendarView calendar ;

    public static String dataAtual(){
       long data = System.currentTimeMillis();
       SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
       String dataString = simpleDateFormat.format(data);
       return dataString;
    }

    public static String mesAtual(){
        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM  yyyy");
        String dataString = simpleDateFormat.format(data);
        return dataString;


    }

    public static String mes(){
        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM");
        String mesFormatado= simpleDateFormat.format(data);
        return mesFormatado;
    }
    public static String ano(){
        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String anoFormatado= simpleDateFormat.format(data);
        return anoFormatado;
    }
}
