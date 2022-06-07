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
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.neosavings.R;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Modelo.Categoria;
import com.example.neosavings.ui.Modelo.Cuenta;
import com.example.neosavings.ui.Modelo.Deuda;
import com.example.neosavings.ui.Modelo.Registro;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Flowable;

public class Formulario_Deudas extends AppCompatActivity {

    private List<Cuenta> ListaCuentas;
    private List<Categoria> ListaCategoriaGastos;
    private List<Categoria> ListaCategoriaIngresos;

    private List<String> spinnerCuentas;

    private List<String> spinnerRecordatorio;

    private Spinner spinner_cuentas;
    private Spinner spinner_Recordatorio;

    private UsuarioRepository mRepository;
    private Deuda deuda;
    private ArrayAdapter<String> adapter;
    private EditText FechaIni;
    private EditText FechaFin;
    private EditText Nombre;
    private EditText Descripcion;
    private EditText Gasto;
    private ToggleButton toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_deudas);

        mRepository = new UsuarioRepository(getBaseContext());

        ListaCuentas = new ArrayList<>();

        spinnerCuentas = new ArrayList<>();

        spinner_cuentas = findViewById(R.id.Spinner_Cuentas);

        FechaIni = findViewById(R.id.editTextDateIni);
        FechaFin = findViewById(R.id.editTextDateFin);
        Gasto = findViewById(R.id.Gastos_Registro);
        Nombre = findViewById(R.id.RegistroNombre);
        Descripcion = findViewById(R.id.registroDescripcion);
        toggleButton= findViewById(R.id.toggleButton2);
        spinner_Recordatorio=findViewById(R.id.spinner_Recordatorio);

        Flowable<List<Cuenta>> allUsers = mRepository.getAllCuentasFW();
        ListaCuentas = allUsers.blockingFirst();

        spinnerCuentas.clear();

        if (ListaCuentas != null) {
            if (ListaCuentas.size() != 0) {
                for (Cuenta u : ListaCuentas) {
                    spinnerCuentas.add(u.getUser().getUsuario());
                }
            } else {
                spinnerCuentas.add("NO HAY CUENTAS");
            }
        }

        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerCuentas);
        spinner_cuentas.setAdapter(adapter);

        setListenersSpinners();

        String caso = (String) getIntent().getExtras().get("CASO");


        if (caso.equals("CREAR")) {

            setTitle("Crear Deuda/Préstamo");

            deuda = new Deuda();

            FechaIni.setText(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
            Calendar calendar=Calendar.getInstance();
            calendar.add(Calendar.YEAR,1);
            FechaFin.setText(new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));

            findViewById(R.id.Button_OK).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean valido = false;
                    try {
                        valido = CrearDeuda();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (valido) {
                        finish();
                    }
                }
            });

        } else {

            setTitle("Editar Deuda/Préstamo");

            int ID_Deuda = (int) getIntent().getExtras().get("DeudaID");
            deuda = mRepository.getDeudaByID(ID_Deuda).blockingFirst();

            findViewById(R.id.Button_OK).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean valido = false;
                    try {
                        valido = ActualizarDeuda();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (valido) {
                        finish();
                    }
                }
            });

            Gasto.setText(deuda.getCosteDeuda());
            Nombre.setText(deuda.getNombre());
            Descripcion.setText(deuda.getDescripcion());
            spinner_cuentas.setSelection(getPosicionCuenta());
            spinner_Recordatorio.setSelection(getPosicionRecordatorio());

            FechaIni.setText(new SimpleDateFormat("dd/MM/yyyy").format(deuda.getFechaInicio()));
            FechaFin.setText(new SimpleDateFormat("dd/MM/yyyy").format(deuda.getFechaVencimiento()));
            toggleButton.setChecked(deuda.isDeuda());
        }
    }

        public void setListenersSpinners () {

            findViewById(R.id.button_CANCEL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
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

            spinnerRecordatorio=new ArrayList<>();
            spinnerRecordatorio.add("Ninguno");
            spinnerRecordatorio.add("Tres días antes");
            spinnerRecordatorio.add("Un día antes");
            spinnerRecordatorio.add("Fecha límite");

            adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerRecordatorio);
            spinner_Recordatorio.setAdapter(adapter);

            spinner_Recordatorio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        public boolean CrearDeuda () throws ParseException {

            deuda.setCosteDeuda(Gasto.getText().toString());
            if (deuda.getCosteDeuda().isEmpty()) {
                deuda.setCosteDeuda("0");
            }

            deuda.setDeuda(toggleButton.isChecked());

            deuda.setNombre(Nombre.getText().toString());

            deuda.setDescripcion(Descripcion.getText().toString());

            String fecha = FechaIni.getText().toString();

            if (fecha.matches("^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/([12][0-9]{3})$")) {
                deuda.setFechaInicio(new SimpleDateFormat("dd/MM/yyyy").parse(fecha));
            } else {
                Toast.makeText(getBaseContext(), "Formato Fecha Inicio No Valida", Toast.LENGTH_SHORT).show();
                return false;
            }

            String fechaFin = FechaFin.getText().toString();

            if (fechaFin.matches("^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/([12][0-9]{3})$")) {
                deuda.setFechaVencimiento(new SimpleDateFormat("dd/MM/yyyy").parse(fechaFin));
            } else {
                Toast.makeText(getBaseContext(), "Formato Fecha Fin No Valida", Toast.LENGTH_SHORT).show();
                return false;
            }

            String text = (String) spinner_cuentas.getSelectedItem();
            int Pos = 0;

            if (text.equals("NO HAY CUENTAS")) {
                Toast.makeText(getBaseContext(), "No se puede asignar un Registro si no hay ningún usuario", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                Pos = spinner_cuentas.getSelectedItemPosition();
                Cuenta user = ListaCuentas.get(Pos);
                deuda.setUserID(user.getUser().getUserID());
                deuda.setNombreCuenta(user.getUser().getUsuario());
            }

            deuda.setRecordatorio((String) spinner_Recordatorio.getSelectedItem());

            FechaNotificacion();

            mRepository.insertDeuda(deuda);

            return true;
        }

        public boolean ActualizarDeuda () throws ParseException {

            deuda.setCosteDeuda(Gasto.getText().toString());
            if (deuda.getCosteDeuda().isEmpty()) {
                deuda.setCosteDeuda("0");
            }

            deuda.setDeuda(toggleButton.isChecked());

            deuda.setNombre(Nombre.getText().toString());

            deuda.setDescripcion(Descripcion.getText().toString());

            String fecha = FechaIni.getText().toString();

            String fechaFin = FechaFin.getText().toString();

            if (fecha.matches("^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/([12][0-9]{3})$")) {
                deuda.setFechaInicio(new SimpleDateFormat("dd/MM/yyyy").parse(fecha));
            } else {
                Toast.makeText(getBaseContext(), "Formato Fecha Inicio No Valida", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (fechaFin.matches("^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/([12][0-9]{3})$")) {
                deuda.setFechaVencimiento(new SimpleDateFormat("dd/MM/yyyy").parse(fechaFin));
            } else {
                Toast.makeText(getBaseContext(), "Formato Fecha Fin No Valida", Toast.LENGTH_SHORT).show();
                return false;
            }

            String text = (String) spinner_cuentas.getSelectedItem();
            int Pos = 0;

            if (text.equals("NO HAY CUENTAS")) {
                Toast.makeText(getBaseContext(), "No se puede asignar un Registro si no hay ningún usuario", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                Pos = spinner_cuentas.getSelectedItemPosition();
                Cuenta user = ListaCuentas.get(Pos);
                deuda.setUserID(user.getUser().getUserID());
                deuda.setNombreCuenta(user.getUser().getUsuario());
            }

            deuda.setRecordatorio((String) spinner_Recordatorio.getSelectedItem());

            FechaNotificacion();

            Registro registro=mRepository.getRegistroByID(deuda.getRegistroIDPrincipal()).blockingFirst();
            registro.setRegistroUserID(deuda.getUserID());
            registro.setCategoria("Préstamos");
            if(deuda.isDeuda()) {
                registro.setDescripcion("Yo -> "+deuda.getNombre());
            }else{
                registro.setDescripcion(deuda.getNombre()+" -> Yo");
            }
            registro.setFormaPago("Dinero en efectivo");
            registro.setGasto(!deuda.isDeuda());
            registro.setFecha(deuda.getFechaInicio());
            registro.setCoste(deuda.getCosteDeuda());
            mRepository.UpdateOnlyRegistro(registro);

            mRepository.Update(deuda);

            return true;
        }

        public int getPosicionCuenta(){

            int index = 0;
            for (Cuenta i : ListaCuentas) {
                if (i.user.getUserID() == deuda.getUserID()) {
                    return index;
                }
                index++;
            }

            return 0;
        }

    public int getPosicionRecordatorio(){

        int index = 0;
        for (String i : spinnerRecordatorio) {
            if (i.equals(deuda.getRecordatorio())){
                return index;
            }
            index++;
        }

        return 0;
    }

    public void FechaNotificacion(){

        Calendar calendar= Calendar.getInstance();
        calendar.setTime(deuda.getFechaVencimiento());
        deuda.setNotificado(false);

        if(deuda.getRecordatorio().equals("Tres días antes")){
            calendar.add(Calendar.DAY_OF_YEAR,-3);
        }else if(deuda.getRecordatorio().equals("Un día antes")) {
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }else if(deuda.getRecordatorio().equals("Fecha límite")){

        }else{
            deuda.setNotificado(true);
        }

        String aux= new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
        try {
            deuda.setFechaNotificacion(new SimpleDateFormat("dd/MM/yyyy").parse(aux));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}