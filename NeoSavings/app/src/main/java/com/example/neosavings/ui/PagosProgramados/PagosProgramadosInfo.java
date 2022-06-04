package com.example.neosavings.ui.PagosProgramados;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.neosavings.R;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Formularios.Formulario_PagosProgramados;
import com.example.neosavings.ui.Modelo.Registro;
import com.example.neosavings.ui.Modelo.RegistrosPagosProgramados;
import com.example.neosavings.ui.Registros.RegistroFragmentV3;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;

public class PagosProgramadosInfo extends AppCompatActivity {

    Integer PagoProgramadoID;
    RegistrosPagosProgramados registrosPagosProgramados;
    List<Registro> Registros;

    UsuarioRepository mRepository;
    private TextView mNombre;
    private TextView mCategoria;
    private TextView mNombreUsuario;
    private TextView mFechaInicio;
    private TextView mFechaFin;
    private TextView mGasto;
    private TextView mNextFecha;
    private TextView mPeriocidad;
    private RegistroFragmentV3 registroFragmentV3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagos_programados_info);

        PagoProgramadoID = (Integer) getIntent().getExtras().get("PagoProgramadoID");
        mRepository=new UsuarioRepository(getBaseContext());
        ActualizarVista();


    }


    public void ActualizarVista() {

        Flowable<RegistrosPagosProgramados> registroPagosProgramadoByID = mRepository.getRegistroPagosProgramadoByID(PagoProgramadoID);
        registrosPagosProgramados = registroPagosProgramadoByID.blockingFirst();

        findViewById(R.id.floatingActionButtonBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (registrosPagosProgramados != null) {
            if (registrosPagosProgramados.getRegistros() == null) {
                registrosPagosProgramados.setRegistros(new ArrayList<>());
            }
            if(registrosPagosProgramados.getPagoProgramado()!=null) {

                mNombre = findViewById(R.id.textViewNombrePresupuesto);
                mNombre.setText(registrosPagosProgramados.getPagoProgramado().getNombre());

                mCategoria = findViewById(R.id.textViewCategoriaPresupuesto);
                mCategoria.setText(registrosPagosProgramados.getPagoProgramado().getCategoría());

                mNombreUsuario = findViewById(R.id.textViewUsuarioPresupuesto);
                mNombreUsuario.setText(registrosPagosProgramados.getPagoProgramado().getNombreUser());

                mFechaInicio = findViewById(R.id.textViewFechaInicioPresupuesto);
                mFechaInicio.setText(new SimpleDateFormat("dd/MM/yyyy").format(registrosPagosProgramados.getPagoProgramado().getFechaInicio()));

                mFechaFin = findViewById(R.id.textViewFechaFinPresupuesto);
                mFechaFin.setText(new SimpleDateFormat("dd/MM/yyyy").format(registrosPagosProgramados.getPagoProgramado().getFechaFin()));

                mGasto = findViewById(R.id.textViewPresupuestoFinal);
                Double presupuesto= Double.valueOf(registrosPagosProgramados.getPagoProgramado().getCoste());

                DecimalFormat formato=new DecimalFormat("#,###.### €");

                if(registrosPagosProgramados.getPagoProgramado().isGasto()){
                    mGasto.setTextColor(Color.RED);
                    mGasto.setText("-"+formato.format(Double.valueOf(presupuesto)));
                }else{
                    mGasto.setTextColor(Color.GREEN);
                    mGasto.setText(formato.format(Double.valueOf(presupuesto)));
                }



                mNextFecha = findViewById(R.id.textViewFechaNextPagoProgramado);
                Date NextPago=registrosPagosProgramados.getNextPago();

                if(NextPago.getTime()>registrosPagosProgramados.getPagoProgramado().getFechaFin().getTime()){
                    mNextFecha.setText("CONCLUIDO");
                }else{
                    mNextFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(NextPago));
                }


                mPeriocidad = findViewById(R.id.textViewUsuarioPagoProgramadoPeriocidad);
                mPeriocidad.setText(registrosPagosProgramados.getPagoProgramado().getPeriodicidad());

                findViewById(R.id.floatingActionButtonEdit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getBaseContext(), Formulario_PagosProgramados.class);
                        intent.putExtra("CASO","UPDATE");
                        intent.putExtra("PagoProgramadoID",registrosPagosProgramados.getPagoProgramado().getPagoProgramadoID());
                        startActivity(intent);
                    }
                });

                registroFragmentV3=new RegistroFragmentV3(registrosPagosProgramados.getRegistros(),this);

                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(androidx.navigation.ui.R.animator.nav_default_enter_anim, androidx.navigation.ui.R.animator.nav_default_exit_anim,
                                androidx.navigation.ui.R.animator.nav_default_pop_enter_anim,
                                androidx.navigation.ui.R.animator.nav_default_pop_exit_anim)
                        .replace(R.id.FrameLayoutRegistrosPagosProgramados,registroFragmentV3)
                        .commit();
            }

        }
    }

    @Override
    protected void onResume() {
        ActualizarVista();
        super.onResume();
    }
}