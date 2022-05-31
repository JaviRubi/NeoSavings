package com.example.neosavings.ui.Formularios;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.neosavings.R;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Modelo.Categoria;
import com.example.neosavings.ui.Modelo.Cuenta;
import com.example.neosavings.ui.Modelo.Presupuesto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;

public class Formulario_Presupuestos extends AppCompatActivity {

    private Spinner spinner_Categorias;
    private Spinner spinner_cuentas;
    private List<String> spinnerCuentas;
    private List<String> spinnerCategorias;
    private UsuarioRepository mRepository;

    private Presupuesto presupuesto;
    private List<Cuenta> ListaCuentas;
    private List<Categoria> ListaCategoria;
    private long PresupuestoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_presupuestos);

        String caso= (String) getIntent().getExtras().get("CASO");
        if(caso.equals("CREAR")) {

            EditText Fechas = findViewById(R.id.editTextDateIni);
            Fechas.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

            mRepository = new UsuarioRepository(getApplicationContext());
            spinnerCuentas = new ArrayList<>();
            spinnerCategorias = new ArrayList<>();
            ListaCategoria = new ArrayList<>();
            presupuesto = new Presupuesto();

            Flowable<List<Cuenta>> allUsers = mRepository.getAllCuentasFW();
            ListaCuentas = allUsers.blockingFirst();

            Flowable<List<Categoria>> allCategorias = mRepository.getALLCategoriasGastos();
            ListaCategoria = allCategorias.blockingFirst();

            spinnerCuentas.clear();
            spinnerCategorias.clear();

            spinnerCategorias.add("TODAS");

            if (ListaCategoria != null) {
                if (ListaCategoria.size() != 0) {
                    for (Categoria c : ListaCategoria) {
                        spinnerCategorias.add(c.getCategoría());
                    }
                }
            }

            spinnerCuentas.add("TODOS");

            if (ListaCuentas != null) {
                if (ListaCuentas.size() != 0) {
                    for (Cuenta u : ListaCuentas) {
                        spinnerCuentas.add(u.getUser().getUsuario());
                    }
                } else {
                    spinnerCuentas.clear();
                    spinnerCuentas.add("NO HAY CUENTAS");
                }
            }

            spinner_cuentas = (Spinner) findViewById(R.id.Spinner_Cuentas);
            spinner_cuentas.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerCuentas));

            spinner_Categorias = (Spinner) findViewById(R.id.Spinner_Categorias);
            spinner_Categorias.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerCategorias));

            findViewById(R.id.button_CANCEL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            ImageButton Ok = (ImageButton) findViewById(R.id.Button_OK);
            Ok.setOnClickListener(view -> {

                boolean valido = false;
                try {
                    valido = CrearPresupuesto();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (valido) {
                    finish();
                }
            });

        }else{

            mRepository = new UsuarioRepository(getApplicationContext());
            PresupuestoID= (long) getIntent().getExtras().get("PresupuestoID");
            presupuesto=mRepository.getPresupuestosByIDFW(PresupuestoID).blockingFirst();

            EditText Texto = findViewById(R.id.Gastos_Registro);
            Texto.setText(presupuesto.getPresupuesto());

            Texto=findViewById(R.id.RegistroNombre);
            Texto.setText(presupuesto.getName());

            Texto=findViewById(R.id.editTextDateFin);
            Texto.setText(new SimpleDateFormat("dd/MM/yyyy").format(presupuesto.getFechaFin()));

            Texto = findViewById(R.id.editTextDateIni);
            Texto.setText(new SimpleDateFormat("dd/MM/yyyy").format(presupuesto.getFechaInicio()));

            spinnerCuentas = new ArrayList<>();
            spinnerCategorias = new ArrayList<>();
            ListaCategoria = new ArrayList<>();

            Flowable<List<Cuenta>> allUsers = mRepository.getAllCuentasFW();
            ListaCuentas = allUsers.blockingFirst();

            Flowable<List<Categoria>> allCategorias = mRepository.getALLCategoriasGastos();
            ListaCategoria = allCategorias.blockingFirst();

            spinnerCuentas.clear();
            spinnerCategorias.clear();

            spinnerCategorias.add("TODAS");

            if (ListaCategoria != null) {
                if (ListaCategoria.size() != 0) {
                    for (Categoria c : ListaCategoria) {
                        spinnerCategorias.add(c.getCategoría());
                    }
                }
            }

            spinnerCuentas.add("TODOS");

            if (ListaCuentas != null) {
                if (ListaCuentas.size() != 0) {
                    for (Cuenta u : ListaCuentas) {
                        spinnerCuentas.add(u.getUser().getUsuario());
                    }
                } else {
                    spinnerCuentas.clear();
                    spinnerCuentas.add("NO HAY CUENTAS");
                }
            }

            Presupuesto aux=presupuesto;

            spinner_cuentas = (Spinner) findViewById(R.id.Spinner_Cuentas);
            spinner_cuentas.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerCuentas));
            int Pos=getPosicionCuenta();
            spinner_cuentas.setSelection(Pos);
            spinner_Categorias = (Spinner) findViewById(R.id.Spinner_Categorias);
            spinner_Categorias.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerCategorias));
            Pos=getPosicionCategorias();
            spinner_Categorias.setSelection(Pos);
            findViewById(R.id.button_CANCEL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            ImageButton Ok = (ImageButton) findViewById(R.id.Button_OK);
            Ok.setOnClickListener(view -> {

                boolean valido = false;
                try {
                    valido = ActualizarPresupuesto();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (valido) {
                    finish();
                }
            });

        }
    }

    public int getPosicionCuenta(){

        int index=1;
        for (Cuenta i:ListaCuentas){
            if(i.user.getUserID()==presupuesto.getUserID()){
                return index;
            }
            index++;
        }

        return 0;
    }

    public int getPosicionCategorias(){

        int index=1;
        for (Categoria i:ListaCategoria){
            if(i.getCategoría().equals(presupuesto.getCategoria())){
                return index;
            }
            index++;
        }

        return 0;
    }

    public boolean CrearPresupuesto() throws ParseException {

        EditText editText = (EditText)findViewById(R.id.Gastos_Registro);
        presupuesto.setPresupuesto(editText.getText().toString());
        if(presupuesto.getPresupuesto().isEmpty()){
            presupuesto.setPresupuesto("0");
        }

        editText=(EditText) findViewById(R.id.RegistroNombre);
        presupuesto.setName(editText.getText().toString());

        editText=(EditText) findViewById(R.id.editTextDateIni);
        String fecha=editText.getText().toString();

        if(fecha.matches("^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/([12][0-9]{3})$")){
            presupuesto.setFechaInicio(new SimpleDateFormat("dd/MM/yyyy").parse(fecha));
        }else{
            Toast.makeText(getBaseContext(), "Formato Fecha Inicial No Valido", Toast.LENGTH_SHORT).show();
            return false;
        }

        editText=(EditText) findViewById(R.id.editTextDateFin);
        fecha=editText.getText().toString();

        if(fecha.matches("^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/([12][0-9]{3})$")){
            presupuesto.setFechaFin(new SimpleDateFormat("dd/MM/yyyy").parse(fecha));
        }else{
            Toast.makeText(getBaseContext(), "Formato Fecha Final No Valido", Toast.LENGTH_SHORT).show();
            return false;
        }

        String text=(String)spinner_cuentas.getSelectedItem();

        if(text.equals("NO HAY CUENTAS")){
            Toast.makeText(getBaseContext(), "No se puede asignar un Registro si no hay ningún usuario", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(text.equals("TODOS")){
            presupuesto.setUserID(-999);
            presupuesto.setUserName(text);
        }else {
            spinner_cuentas=(Spinner) findViewById(R.id.Spinner_Cuentas);
            int Pos = spinner_cuentas.getSelectedItemPosition()-1;
            Cuenta user=ListaCuentas.get(Pos);
            presupuesto.setUserID(user.getUser().getUserID());
            presupuesto.setUserName(user.getUser().getUsuario());
        }

        spinner_Categorias=(Spinner) findViewById(R.id.Spinner_Categorias);
        int Pos=spinner_Categorias.getSelectedItemPosition();
        presupuesto.setCategoria(spinnerCategorias.get(Pos));

        mRepository.insertPresupuesto(presupuesto);
        return true;

    }

    public boolean ActualizarPresupuesto() throws ParseException {

        EditText editText = (EditText)findViewById(R.id.Gastos_Registro);
        presupuesto.setPresupuesto(editText.getText().toString());
        if(presupuesto.getPresupuesto().isEmpty()){
            presupuesto.setPresupuesto("0");
        }

        editText=(EditText) findViewById(R.id.RegistroNombre);
        presupuesto.setName(editText.getText().toString());

        editText=(EditText) findViewById(R.id.editTextDateIni);
        String fecha=editText.getText().toString();

        if(fecha.matches("^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/([12][0-9]{3})$")){
            presupuesto.setFechaInicio(new SimpleDateFormat("dd/MM/yyyy").parse(fecha));
        }else{
            Toast.makeText(getBaseContext(), "Formato Fecha Inicial No Valido", Toast.LENGTH_SHORT).show();
            return false;
        }

        editText=(EditText) findViewById(R.id.editTextDateFin);
        fecha=editText.getText().toString();

        if(fecha.matches("^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/([12][0-9]{3})$")){
            presupuesto.setFechaFin(new SimpleDateFormat("dd/MM/yyyy").parse(fecha));
        }else{
            Toast.makeText(getBaseContext(), "Formato Fecha Final No Valido", Toast.LENGTH_SHORT).show();
            return false;
        }

        String text=(String)spinner_cuentas.getSelectedItem();

        if(text.equals("NO HAY CUENTAS")){
            Toast.makeText(getBaseContext(), "No se puede asignar un Registro si no hay ningún usuario", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(text.equals("TODOS")){
            presupuesto.setUserID(-999);
            presupuesto.setUserName(text);
        }else {
            spinner_cuentas=(Spinner) findViewById(R.id.Spinner_Cuentas);
            int Pos = spinner_cuentas.getSelectedItemPosition()-1;
            Cuenta user=ListaCuentas.get(Pos);
            presupuesto.setUserID(user.getUser().getUserID());
            presupuesto.setUserName(user.getUser().getUsuario());
        }

        spinner_Categorias=(Spinner) findViewById(R.id.Spinner_Categorias);
        int Pos=spinner_Categorias.getSelectedItemPosition();
        presupuesto.setCategoria(spinnerCategorias.get(Pos));

        mRepository.Update(presupuesto);
        return true;

    }
}