package com.example.neosavings.ui.Formularios;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.neosavings.R;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Modelo.Cuenta;
import com.example.neosavings.ui.Modelo.PagosDeudas;
import com.example.neosavings.ui.Modelo.Registro;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Formulario_NuevoPagoDeuda extends AppCompatActivity {

    UsuarioRepository mRepository;
    Integer DeudaID;
    PagosDeudas pagosDeudas;
    Registro registro;

    Spinner spinner_cuentas;
    Spinner spinner_acciones;

    List<Cuenta> listaCuentas;

    List<String> spinnerCuentas = new ArrayList<>();
    List<String> spinnerAcciones = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nuevo_pago_deuda);

        registro=new Registro();
        mRepository=new UsuarioRepository(getBaseContext());

        DeudaID= (Integer) getIntent().getExtras().get("DeudaID");

        pagosDeudas=mRepository.getRegistrosDeudasByID(DeudaID).blockingFirst();

        EditText cantidad=findViewById(R.id.editTextCantidad);
        DecimalFormat formato=new DecimalFormat("#,###.### €");

        cantidad.setHint(formato.format(Double.valueOf(pagosDeudas.getDeuda().getCosteDeuda()))+" para pagar deuda");

        spinner_cuentas=findViewById(R.id.spinner_Cuentas);
        spinner_acciones=findViewById(R.id.spinner_Acción);

        spinnerAcciones.add("Reembolsar la deuda");
        spinnerAcciones.add("Aumentar la deuda");

        spinner_acciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        spinner_acciones.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerAcciones));

        listaCuentas=mRepository.getAllCuentasFW().blockingFirst();
        if(listaCuentas==null){
            listaCuentas=new ArrayList<>();
        }

        if(listaCuentas.size()!=0) {
            for (Cuenta u : listaCuentas) {
                spinnerCuentas.add(u.getUser().getUsuario());
            }
        }else{
            spinnerCuentas.add("NO HAY CUENTAS");
        }

        spinner_cuentas.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerCuentas));


        findViewById(R.id.floatingActionButton4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.floatingActionButton3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valido=false;
                try{
                    valido=CrearNuevoPago();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(valido) {
                    finish();
                }
            }
        });
    }

    private boolean CrearNuevoPago() throws ParseException {
        EditText cantidad=findViewById(R.id.editTextCantidad);
        String pago=cantidad.getText().toString();
        if(pago.isEmpty()){
            Toast.makeText(getBaseContext(), "Introduzca un valor al campo Cantidad", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            registro.setCoste(pago);
        }

        String accion= (String) spinner_acciones.getSelectedItem();
        registro.setDescripcion(accion+ " a nombre de "+pagosDeudas.getDeuda().getNombre());
        String fecha=new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
        registro.setFecha(new SimpleDateFormat("dd/MM/yyyy").parse(fecha));
        registro.setFormaPago("Dinero en efectivo");
        registro.setCategoria("Préstamos");
        registro.setDeudaID(pagosDeudas.getDeuda().getDeudaID());
        if(accion.equals("Reembolsar la deuda")){
            registro.setGasto(pagosDeudas.getDeuda().isDeuda());
        }else{
            registro.setGasto(!pagosDeudas.getDeuda().isDeuda());
        }

        String text=(String)spinner_cuentas.getSelectedItem();
        int Pos=0;

        if(text.equals("NO HAY CUENTAS")){
            Toast.makeText(getBaseContext(), "No se puede asignar un Registro si no hay ningún usuario", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            Pos = spinner_cuentas.getSelectedItemPosition();
            Cuenta user = listaCuentas.get(Pos);
            registro.setRegistroUserID(user.getUser().getUserID());
        }

        mRepository.insertRegistro(registro);

        return true;
    }
}