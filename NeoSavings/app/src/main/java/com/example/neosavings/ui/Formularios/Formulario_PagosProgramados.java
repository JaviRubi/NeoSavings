package com.example.neosavings.ui.Formularios;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.neosavings.R;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Modelo.Categoria;
import com.example.neosavings.ui.Modelo.Cuenta;
import com.example.neosavings.ui.Modelo.PagoProgramado;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Flowable;

public class Formulario_PagosProgramados extends AppCompatActivity {

    private List<Cuenta> ListaCuentas;
    private List<Categoria> ListaCategoriaGastos;
    private List<Categoria> ListaCategoriaIngresos;

    private List<String> spinnerCuentas;
    private List<String> spinnerCategorias;
    private List<String> spinnerCategoriasIngresos;
    private List<String> spinnerCategoriasGastos;
    private List<String> spinnerPeriodicidad;
    private List<String> spinnerFormaPagos;

    private Spinner spinner_categorias;
    private Spinner spinner_cuentas;
    private Spinner spinner_FormaPago;
    private Spinner spinner_Periocidad;
    private SwitchCompat switch_Recordar;
    private ToggleButton toggleButton;
    private UsuarioRepository mRepository;
    private PagoProgramado pagoProgramado;
    private ArrayAdapter<String> adapter;
    private EditText FechaIni;
    private EditText FechaFin;
    private EditText Nombre;
    private EditText Descripcion;
    private EditText Gasto;


    public boolean CambioFecha = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_pagos_programados);

        mRepository=new UsuarioRepository(getBaseContext());

        ListaCuentas=new ArrayList<>();
        ListaCategoriaGastos=new ArrayList<>();
        ListaCategoriaIngresos=new ArrayList<>();

        spinnerCuentas=new ArrayList<>();
        spinnerCategorias=new ArrayList<>();
        spinnerFormaPagos=new ArrayList<>();
        spinnerPeriodicidad=new ArrayList<>();
        spinnerCategoriasIngresos=new ArrayList<>();
        spinnerCategoriasGastos=new ArrayList<>();

        toggleButton=findViewById(R.id.toggleButton);
        spinner_cuentas=findViewById(R.id.Spinner_Cuentas);
        spinner_categorias=findViewById(R.id.Spinner_Categorias);
        spinner_FormaPago=findViewById(R.id.spinner_FormasPago);
        spinner_Periocidad=findViewById(R.id.spinnerPeriocidad);
        switch_Recordar=findViewById(R.id.switch2);

        FechaIni=findViewById(R.id.editTextDate);
        FechaFin=findViewById(R.id.editTextDate4);
        Gasto=findViewById(R.id.Gastos_Registro);
        Nombre=findViewById(R.id.editTextTextPersonName2);
        Descripcion=findViewById(R.id.RegistroDescripcion);

        Flowable<List<Cuenta>> allUsers = mRepository.getAllCuentasFW();
        ListaCuentas=allUsers.blockingFirst();

        Flowable<List<Categoria>> allCategorias=mRepository.getALLCategoriasGastos();
        ListaCategoriaGastos =allCategorias.blockingFirst();

        allCategorias=mRepository.getALLCategoriasIngresos();
        ListaCategoriaIngresos =allCategorias.blockingFirst();

        spinnerCuentas.clear();
        spinnerCategorias.clear();

        if(ListaCategoriaGastos !=null) {
            if(ListaCategoriaGastos.size()!=0){
                for (Categoria c : ListaCategoriaGastos) {
                    spinnerCategorias.add(c.getCategoría());
                    spinnerCategoriasGastos.add(c.getCategoría());
                }
            }else{
                spinnerCategorias.add("NO HAY CATEGORIAS");
            }
        }

        if(ListaCategoriaIngresos !=null) {
            if(ListaCategoriaIngresos.size()!=0){
                for (Categoria c : ListaCategoriaIngresos) {
                    spinnerCategoriasIngresos.add(c.getCategoría());
                }
            }
        }

        if(ListaCuentas!=null) {
            if(ListaCuentas.size()!=0) {
                for (Cuenta u : ListaCuentas) {
                    spinnerCuentas.add(u.getUser().getUsuario());
                }
            }else{
                spinnerCuentas.add("NO HAY CUENTAS");
            }
        }

        adapter=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerCategorias);
        spinner_categorias.setAdapter(adapter);

        adapter=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerCuentas);
        spinner_cuentas.setAdapter(adapter);



        setListenersSpinners();
        InicializarSpinnerFormaPago();

        String caso= (String) getIntent().getExtras().get("CASO");



        if(caso.equals("CREAR")){

            pagoProgramado=new PagoProgramado();

            FechaIni.setText(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));

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

            int ID_Pago= (int) getIntent().getExtras().get("PagoProgramadoID");
            pagoProgramado=mRepository.getPagoProgramadoByID(ID_Pago).blockingFirst();

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

            Gasto.setText(pagoProgramado.getCoste());
            Nombre.setText(pagoProgramado.getNombre());
            Descripcion.setText(pagoProgramado.getDescripcion());
            spinner_cuentas.setSelection(getPosicionCuenta());
            spinner_FormaPago.setSelection(getPosicionFormaPago());
            spinner_categorias.setSelection(getPosicionCategorias());
            spinner_Periocidad.setSelection(getPosicionPeriodicidad());

            switch_Recordar.setChecked(pagoProgramado.isRecordatorio());

            FechaIni.setText(new SimpleDateFormat("dd/MM/yyyy").format(pagoProgramado.getFechaInicio()));
            FechaFin.setText(new SimpleDateFormat("dd/MM/yyyy").format(pagoProgramado.getFechaFin()));
        }


    }

    public void setListenersSpinners(){

        findViewById(R.id.button_CANCEL_FormularioRegistro2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        spinner_categorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int[] attr = {com.google.android.material.R.attr.colorOnBackground};
                TypedArray typedArray = obtainStyledAttributes(R.style.Theme_NeoSavings, attr);
                ((TextView) parent.getChildAt(0)).setTextColor(typedArray.getColor(0, Color.LTGRAY));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_cuentas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int[] attr = {com.google.android.material.R.attr.colorOnBackground};
                TypedArray typedArray = obtainStyledAttributes(R.style.Theme_NeoSavings, attr);
                ((TextView) parent.getChildAt(0)).setTextColor(typedArray.getColor(0, Color.LTGRAY));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    spinnerCategorias.clear();
                    spinnerCategorias.addAll(spinnerCategoriasIngresos);
                    adapter=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerCategorias);
                    spinner_categorias.setAdapter(adapter);

                }else{
                    spinnerCategorias.clear();
                    spinnerCategorias.addAll(spinnerCategoriasGastos);
                    adapter=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerCategorias);
                    spinner_categorias.setAdapter(adapter);
                }
            }
        });
    }

    public boolean CrearPagoProgramado() throws ParseException {

        pagoProgramado.setCoste(Gasto.getText().toString());
        if(pagoProgramado.getCoste().isEmpty()){
            pagoProgramado.setCoste("0");
        }

        pagoProgramado.setGasto(!toggleButton.isChecked());

        pagoProgramado.setNombre(Nombre.getText().toString());

        pagoProgramado.setDescripcion(Descripcion.getText().toString());

        String fecha=FechaIni.getText().toString();

        if(fecha.matches("^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/([12][0-9]{3})$")){
            pagoProgramado.setFechaInicio(new SimpleDateFormat("dd/MM/yyyy").parse(fecha));
        }else{
            Toast.makeText(getBaseContext(), "Formato Fecha Inicio No Valida", Toast.LENGTH_SHORT).show();
            return false;
        }

        String fechaFin=FechaFin.getText().toString();

        if(fechaFin.matches("^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/([12][0-9]{3})$")){
            pagoProgramado.setFechaFin(new SimpleDateFormat("dd/MM/yyyy").parse(fechaFin));
        }else{
            Toast.makeText(getBaseContext(), "Formato Fecha Fin No Valida", Toast.LENGTH_SHORT).show();
            return false;
        }

        String text=(String)spinner_cuentas.getSelectedItem();
        int Pos=0;

        if(text.equals("NO HAY CUENTAS")){
            Toast.makeText(getBaseContext(), "No se puede asignar un Registro si no hay ningún usuario", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            Pos = spinner_cuentas.getSelectedItemPosition();
            Cuenta user = ListaCuentas.get(Pos);
            pagoProgramado.setUserId(user.getUser().getUserID());
            pagoProgramado.setNombreUser(user.getUser().getUsuario());
        }

        pagoProgramado.setFormaPago((String)spinner_FormaPago.getSelectedItem());

        pagoProgramado.setCategoría((String) spinner_categorias.getSelectedItem());

        pagoProgramado.setPeriodicidad((String) spinner_Periocidad.getSelectedItem());

        pagoProgramado.setRecordatorio(switch_Recordar.isChecked());

        mRepository.insertPagoProgramado(pagoProgramado);

        return true;
    }

    public boolean ActualizarPagoProgramado() throws ParseException {

        pagoProgramado.setCoste(Gasto.getText().toString());
        if(pagoProgramado.getCoste().isEmpty()){
            pagoProgramado.setCoste("0");
        }

        pagoProgramado.setGasto(!toggleButton.isChecked());

        pagoProgramado.setNombre(Nombre.getText().toString());

        pagoProgramado.setDescripcion(Descripcion.getText().toString());

        String fecha=FechaIni.getText().toString();

        String fechaFin=FechaFin.getText().toString();

        if(fecha.matches("^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/([12][0-9]{3})$") && fechaFin.matches("^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/([12][0-9]{3})$")){
            try {
                if(new SimpleDateFormat("dd/MM/yyyy").parse(FechaIni.getText().toString()).getTime()>=pagoProgramado.getFechaInicio().getTime()) {
                    pagoProgramado.setFechaInicio(new SimpleDateFormat("dd/MM/yyyy").parse(FechaIni.getText().toString()));
                    pagoProgramado.setFechaFin(new SimpleDateFormat("dd/MM/yyyy").parse(FechaFin.getText().toString()));
                }else{
                    Toast.makeText(getBaseContext(), "La Fecha de Inicio no puede ser menor a la configurada inicialmente", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else{
            Toast.makeText(getBaseContext(), "Formato Fecha Inicio No Valido", Toast.LENGTH_SHORT).show();
            return false;
        }

        String text=(String)spinner_cuentas.getSelectedItem();
        int Pos=0;

        if(text.equals("NO HAY CUENTAS")){
            Toast.makeText(getBaseContext(), "No se puede asignar un Registro si no hay ningún usuario", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            Pos = spinner_cuentas.getSelectedItemPosition();
            Cuenta user = ListaCuentas.get(Pos);
            pagoProgramado.setUserId(user.getUser().getUserID());
            pagoProgramado.setNombreUser(user.getUser().getUsuario());
        }

        pagoProgramado.setFormaPago((String)spinner_FormaPago.getSelectedItem());

        pagoProgramado.setCategoría((String) spinner_categorias.getSelectedItem());

        pagoProgramado.setPeriodicidad((String) spinner_Periocidad.getSelectedItem());

        pagoProgramado.setRecordatorio(switch_Recordar.isChecked());

        mRepository.Update(pagoProgramado);

        return true;
    }

    public void InicializarSpinnerFormaPago(){
        spinnerFormaPagos=new ArrayList<>();
        spinnerPeriodicidad=new ArrayList<>();

        spinnerFormaPagos.add("Dinero en efectivo");
        spinnerFormaPagos.add("Tarjeta de debito");
        spinnerFormaPagos.add("Tarjeta de crédito");
        spinnerFormaPagos.add("Transferencia bancaria");
        spinnerFormaPagos.add("Cupón");
        spinnerFormaPagos.add("Pago por móvil");
        spinnerFormaPagos.add("Pago por web");
        spinnerFormaPagos.add("Bizum");

        spinnerPeriodicidad.add("DIARIAMENTE");
        spinnerPeriodicidad.add("SEMANALMENTE");
        spinnerPeriodicidad.add("MENSUALMENTE");
        spinnerPeriodicidad.add("TRIMESTRALMENTE");
        spinnerPeriodicidad.add("SEMESTRALMENTE");
        spinnerPeriodicidad.add("ANUALMENTE");


        spinner_FormaPago=(Spinner) findViewById(R.id.spinner_FormasPago);
        spinner_FormaPago.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerFormaPagos));
        spinner_FormaPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int[] attr = {com.google.android.material.R.attr.colorOnBackground};
                TypedArray typedArray = obtainStyledAttributes(R.style.Theme_NeoSavings, attr);
                ((TextView) parent.getChildAt(0)).setTextColor(typedArray.getColor(0, Color.LTGRAY));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_Periocidad.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerPeriodicidad));
        spinner_Periocidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int[] attr = {com.google.android.material.R.attr.colorOnBackground};
                TypedArray typedArray = obtainStyledAttributes(R.style.Theme_NeoSavings, attr);
                ((TextView) parent.getChildAt(0)).setTextColor(typedArray.getColor(0, Color.LTGRAY));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public int getPosicionCuenta(){

        int index=0;
        for (Cuenta i:ListaCuentas){
            if(i.user.getUserID()==pagoProgramado.getUserId()){
                return index;
            }
            index++;
        }

        return 0;
    }

    public int getPosicionCategorias(){

        int index=0;
        for (String i:spinnerCategorias){
            if(i.equals(pagoProgramado.getCategoría())){
                return index;
            }
            index++;
        }

        return 0;
    }

    public int getPosicionFormaPago(){

        int index=0;
        for (String i:spinnerFormaPagos){
            if(i.equals(pagoProgramado.getFormaPago())){
                return index;
            }
            index++;
        }

        return 0;
    }

    public int getPosicionPeriodicidad(){

        int index=0;
        for (String i:spinnerCategorias){
            if(i.equals(pagoProgramado.getPeriodicidad())){
                return index;
            }
            index++;
        }

        return 0;
    }
}