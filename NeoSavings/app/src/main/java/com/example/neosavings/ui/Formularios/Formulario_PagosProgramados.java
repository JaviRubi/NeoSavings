package com.example.neosavings.ui.Formularios;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.neosavings.R;

public class Formulario_PagosProgramados extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_pagos_programados);

        int[] attr = {androidx.appcompat.R.attr.colorPrimary};
        TypedArray typedArray = obtainStyledAttributes(R.style.Theme_NeoSavings, attr);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(typedArray.getColor(0, Color.BLACK)));

        String caso= (String) getIntent().getExtras().get("CASO");

        findViewById(R.id.button_CANCEL_FormularioRegistro2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(caso.equals("CREAR")){

            findViewById(R.id.Button_OK_FormularioRegistro).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean valido=false;
                    try {
                        valido = CrearPagoProgramado();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if(valido) {
                        finish();
                    }
                }
            });

        }else{

            findViewById(R.id.Button_OK_FormularioRegistro).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean valido=false;
                    try {
                        valido = ActualizarPagoProgramado();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if(valido) {
                        finish();
                    }
                }
            });

        }


    }

    public boolean CrearPagoProgramado(){



        return true;
    }

    public boolean ActualizarPagoProgramado(){



        return true;
    }
}