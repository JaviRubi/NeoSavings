package com.example.neosavings.ui.Adapters;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.neosavings.R;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Modelo.Categoria;
import com.example.neosavings.ui.Modelo.Cuenta;
import com.example.neosavings.ui.Modelo.Registro;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

public class RegistroInfo extends AppCompatActivity {

    private UsuarioRepository mRepository;
    private Registro registro;

    ToggleButton TipoRegistro;

    Spinner spinner_cuentas;
    Spinner spinner_categorias;


    List<String> spinnerCuentas;
    List<String> spinnerCategorias;
    List<String> spinnerCategoriasIngresos;
    List<String> spinnerCategoriasGastos;

    List<Cuenta> ListaCuentas;
    List<Categoria> ListaCategoriaGastos;
    List<Categoria> ListaCategoriaIngresos;
    List<Categoria> ListaCategoria;
    EditText Descripcion;
    EditText Fecha;
    EditText Gasto;

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        mRepository=new UsuarioRepository(getApplication());
        Flowable<Registro> registroBlock=mRepository.getRegistroByID((long)this.getIntent().getExtras().get("RegistroID"));
        registro=registroBlock.blockingFirst();
        setContentView(R.layout.activity_registro_info);

        spinnerCuentas = new ArrayList<>();
        spinnerCategorias=new ArrayList<>();
        spinnerCategoriasIngresos=new ArrayList<>();
        spinnerCategoriasGastos=new ArrayList<>();

        Flowable<List<Cuenta>> allUsers = mRepository.getAllCuentasFW();
        ListaCuentas =allUsers.blockingFirst();

        Flowable<List<Categoria>> allCategorias=mRepository.getALLCategoriasGastos();
        ListaCategoriaGastos=allCategorias.blockingFirst();

        allCategorias=mRepository.getALLCategoriasIngresos();
        ListaCategoriaIngresos=allCategorias.blockingFirst();

        spinnerCuentas.clear();
        spinnerCategorias.clear();


        if(ListaCategoriaGastos!=null) {
            if(ListaCategoriaGastos.size()!=0){
                for (Categoria c : ListaCategoriaGastos) {
                    spinnerCategoriasGastos.add(c.getCategoría());
                }
            }else{
                spinnerCategorias.add("NO HAY CATEGORIAS");
            }
        }

        if(ListaCategoriaIngresos!=null) {
            if(ListaCategoriaIngresos.size()!=0){
                for (Categoria c : ListaCategoriaIngresos) {
                    spinnerCategoriasIngresos.add(c.getCategoría());
                }
            }else{
                spinnerCategorias.add("NO HAY CATEGORIAS");
            }
        }
        if(registro.isGasto()){
            spinnerCategorias.addAll(spinnerCategoriasGastos);
            ListaCategoria=ListaCategoriaGastos;
        }else{
            spinnerCategorias.addAll(spinnerCategoriasIngresos);
            ListaCategoria=ListaCategoriaIngresos;
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

        TipoRegistro=(ToggleButton) findViewById(R.id.toggleButton_Info);
        TipoRegistro.setChecked(!registro.isGasto());



        Descripcion=(EditText) findViewById(R.id.RegistroDescripcion);
        Descripcion.setText(registro.getDescripcion());

        Fecha = (EditText) findViewById(R.id.editTextDate);
        if(registro.getFecha()!=null) {
            Fecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(registro.getFecha()));
        }

        Gasto = (EditText) findViewById(R.id.Gastos_Registro);
        Gasto.setText(registro.getCoste());

        spinner_cuentas=(Spinner) findViewById(R.id.Spinner_Cuentas);
        spinner_cuentas.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerCuentas));
        spinner_cuentas.setSelection(getPosicionCuenta());

        spinner_categorias=(Spinner) findViewById(R.id.Spinner_Categorias);
        adapter=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerCategorias);
        spinner_categorias.setAdapter(adapter);

        spinner_categorias.setSelection(getPosicionCategorias());

        findViewById(R.id.button_CANCEL_FormularioRegistro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.Button_OK_FormularioRegistro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean end= false;
                try {
                    end = updateRegistro();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(end)
                finish();
            }
        });

        TipoRegistro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    spinnerCategorias.clear();
                    spinnerCategorias.addAll(spinnerCategoriasIngresos);
                    adapter.notifyDataSetChanged();
                }else{
                    spinnerCategorias.clear();
                    spinnerCategorias.addAll(spinnerCategoriasGastos);
                    adapter.notifyDataSetChanged();
                }
            }
        });



    }



    public int getPosicionCuenta(){

        int index=0;
        for (Cuenta i:ListaCuentas){
            if(i.user.getUserID()==registro.getRegistroUserID()){
                return index;
            }
            index++;
        }

        return -1;
    }

    public int getPosicionCategorias(){

        int index=0;
        for (Categoria i:ListaCategoria){
            if(i.getCategoría().equals(registro.getCategoria())){
                return index;
            }
            index++;
        }

        return -1;
    }

    public boolean updateRegistro() throws ParseException {

        EditText editText = (EditText)findViewById(R.id.Gastos_Registro);
        registro.setCoste(editText.getText().toString());
        if(registro.getCoste().isEmpty()){
            registro.setCoste("0");
        }


        ToggleButton toggleButton=(ToggleButton)findViewById(R.id.toggleButton_Info);
        String Tipo=(String)toggleButton.getText();
        registro.setGasto(Tipo.trim().toUpperCase().contains("GASTO"));

        editText=(EditText) findViewById(R.id.RegistroDescripcion);
        registro.setDescripcion(editText.getText().toString());

        editText=(EditText) findViewById(R.id.editTextDate);
        String fecha=editText.getText().toString();

        if(fecha.matches("\\d{1,2}/\\d{1,2}/\\d{4}")){
            registro.setFecha(new SimpleDateFormat("dd/MM/yyyy").parse(fecha));
        }else {
            Toast.makeText(getBaseContext(), "Formato Fecha No Valido", Toast.LENGTH_SHORT).show();
            return false;
        }
        String text=(String)spinner_cuentas.getSelectedItem();

        if(text.equals("NO HAY CUENTAS")){
            Toast.makeText(getBaseContext(), "No se puede asignar un Registro si no hay ningún usuario", Toast.LENGTH_SHORT).show();
            return false;
        }

        spinner_categorias=(Spinner) findViewById(R.id.Spinner_Categorias);
        int Pos=spinner_categorias.getSelectedItemPosition();
        registro.setCategoria(spinnerCategorias.get(Pos));


        spinner_cuentas=(Spinner) findViewById(R.id.Spinner_Cuentas);
        Pos=spinner_cuentas.getSelectedItemPosition();
        Cuenta user=ListaCuentas.get(Pos);
        registro.setRegistroUserID(user.getUser().getUserID());

        mRepository.Update(registro);
        return true;
    }
}