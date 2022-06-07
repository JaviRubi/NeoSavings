package com.example.neosavings.ui.Formularios;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.neosavings.R;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Modelo.Objetivo;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Formulario_Objetivos extends AppCompatActivity {

    Objetivo objetivo;
    long ObjetivoID;
    UsuarioRepository mRepository;

    EditText mNombre;
    EditText mFechaFin;
    EditText mAhorrado;
    EditText mObjetivo;
    EditText mDescripcion;

    EditText mCantidad;
    Double CantidadObjetivo;
    Double CantidadAhorrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_objetivos);

        mRepository= new UsuarioRepository(getBaseContext());

        mNombre=findViewById(R.id.RegistroNombre);
        mFechaFin=findViewById(R.id.editTextDateFin);
        mAhorrado=findViewById(R.id.editTextNumberYaAhorrado);
        mObjetivo=findViewById(R.id.editTextNumberObjetivo);
        mDescripcion=findViewById(R.id.editTextTextMultiLineDescripcion);


        String caso=(String) getIntent().getExtras().get("CASO");

        findViewById(R.id.floatingActionButtonBackObjetivo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(caso.equals("CREAR")){
            setTitle("Crear Objetivo");
            objetivo=new Objetivo();
            objetivo.setEstadoActivo();
            findViewById(R.id.floatingActionButtonOKObjetivo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean valido=false;
                    try{
                        valido=CrearObjetivo();
                    }catch (Exception e){}

                    if (valido) {
                        finish();
                    }
                }
            });
        }else{
            setTitle("Editar Objetivo");

            ObjetivoID= (long) getIntent().getExtras().get("ObjetivoID");
            objetivo=mRepository.getObjetivoByID(ObjetivoID).blockingFirst();

            mNombre.setText(objetivo.getNombre());
            mFechaFin.setText(new SimpleDateFormat("dd/MM/yyyy").format(objetivo.getFechaDeseada()));
            mAhorrado.setText(objetivo.getAhorrado());
            mObjetivo.setText(objetivo.getCantidadObjetivo());
            mDescripcion.setText(objetivo.getDescripción());

            findViewById(R.id.floatingActionButtonOKObjetivo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean valido=false;
                    try{
                        valido=ActualizarObjetivo();
                    }catch (Exception e){}

                    if (valido) {
                        finish();
                    }
                }
            });
        }

    }

    private Boolean ActualizarObjetivo() throws ParseException {
        if(mNombre.getText().toString().isEmpty()){
            Toast.makeText(getBaseContext(), "Introduzca un valor al campo Nombre", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            objetivo.setNombre(mNombre.getText().toString());
        }

        String fecha=mFechaFin.getText().toString();
        if (fecha.matches("^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/([12][0-9]{3})$")) {
            objetivo.setFechaDeseada(new SimpleDateFormat("dd/MM/yyyy").parse(fecha));
        } else {
            Toast.makeText(getBaseContext(), "Formato Fecha Fin Esperada No Valida", Toast.LENGTH_SHORT).show();
            return false;
        }

        objetivo.setDescripción(mDescripcion.getText().toString());

        if(mObjetivo.getText().toString().isEmpty()){
            objetivo.setCantidadObjetivo("0");
            CantidadObjetivo= Double.valueOf(0);
        }else{
            objetivo.setCantidadObjetivo(mObjetivo.getText().toString());
            CantidadObjetivo=Double.valueOf(mObjetivo.getText().toString());
        }

        if(mAhorrado.getText().toString().isEmpty()){
            objetivo.setAhorrado("0");
            CantidadAhorrado=Double.valueOf(0);
        }else{
            objetivo.setAhorrado(mAhorrado.getText().toString());
            CantidadAhorrado=Double.valueOf(mAhorrado.getText().toString());
        }

        if(CantidadAhorrado>=CantidadObjetivo){
            objetivo.setEstadoConcluido();
        }

        mRepository.insertObjetivo(objetivo);

        return true;
    }

    private Boolean CrearObjetivo() throws ParseException {

        if(mNombre.getText().toString().isEmpty()){
            Toast.makeText(getBaseContext(), "Introduzca un valor al campo Nombre", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            objetivo.setNombre(mNombre.getText().toString());
        }

        String fecha=mFechaFin.getText().toString();
        if (fecha.matches("^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/([12][0-9]{3})$")) {
            objetivo.setFechaDeseada(new SimpleDateFormat("dd/MM/yyyy").parse(fecha));
        } else {
            Toast.makeText(getBaseContext(), "Formato Fecha Fin Esperada No Valida", Toast.LENGTH_SHORT).show();
            return false;
        }

        objetivo.setDescripción(mDescripcion.getText().toString());

        if(mObjetivo.getText().toString().isEmpty()){
            objetivo.setCantidadObjetivo("0");
            CantidadObjetivo= Double.valueOf(0);
        }else{
            objetivo.setCantidadObjetivo(mObjetivo.getText().toString());
            CantidadObjetivo=Double.valueOf(mObjetivo.getText().toString());
        }

        if(mAhorrado.getText().toString().isEmpty()){
            objetivo.setAhorrado("0");
            CantidadAhorrado=Double.valueOf(0);
        }else{
            objetivo.setAhorrado(mAhorrado.getText().toString());
            CantidadAhorrado=Double.valueOf(mAhorrado.getText().toString());
        }

        Objetivo o=new Objetivo();
        o.setEstadoPausado();

        if(objetivo.getEstado().equals(o.getEstado())){
            objetivo.setEstadoPausado();
        }else{
            objetivo.setEstadoActivo();
        }

        if(CantidadAhorrado>=CantidadObjetivo){
            objetivo.setEstadoConcluido();
        }

        mRepository.insertObjetivo(objetivo);

        return true;
    }
}