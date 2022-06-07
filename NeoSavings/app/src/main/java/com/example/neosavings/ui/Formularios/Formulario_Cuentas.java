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

import androidx.appcompat.app.AppCompatActivity;

import com.example.neosavings.R;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Modelo.Cuenta;
import com.example.neosavings.ui.Modelo.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

public class Formulario_Cuentas extends AppCompatActivity {

    private List<String> ListaTipo;
    private List<Cuenta> ListaCuentas;
    UsuarioRepository mRepository;
    private Usuario user=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_cuentas);

        mRepository=new UsuarioRepository(getApplicationContext());

        long userID= (long) this.getIntent().getExtras().get("UserID");

        if(userID==-1) {

            setTitle("Crear Usuario");

            ListaTipo = new ArrayList<>();
            ListaTipo.add("Tarjeta de Débito");
            ListaTipo.add("Tarjeta de Crédito");
            ListaTipo.add("Cuenta Bancaria");
            ListaTipo.add("Efectivo");


            FloatingActionButton button = findViewById(R.id.floatingActionButton_CrearCuenta);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CrearUsuario();
                }
            });

            findViewById(R.id.floatingActionButton_Cancelar).setVisibility(View.INVISIBLE);
            findViewById(R.id.floatingActionButton_Cancelar).setClickable(false);

            Spinner spinner_cuentas = (Spinner) findViewById(R.id.spinner_Tipo);
            spinner_cuentas.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, ListaTipo));
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

        }else{

            setTitle("Editar Usuario");

            Flowable<Usuario> usuarioBlock=mRepository.getUserByID((long)this.getIntent().getExtras().get("UserID"));
            user=usuarioBlock.blockingFirst();

            ListaTipo = new ArrayList<>();
            ListaTipo.add("Tarjeta de Débito");
            ListaTipo.add("Tarjeta de Crédito");
            ListaTipo.add("Cuenta Bancaria");
            ListaTipo.add("Efectivo");


            FloatingActionButton button = findViewById(R.id.floatingActionButton_CrearCuenta);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateUsuario();
                }
            });

            findViewById(R.id.floatingActionButton_Cancelar).setVisibility(View.VISIBLE);
            findViewById(R.id.floatingActionButton_Cancelar).setClickable(true);
            findViewById(R.id.floatingActionButton_Cancelar).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            EditText name=(EditText)findViewById(R.id.editTextTextPersonName);
            name.setText(user.getUsuario());

            name=findViewById(R.id.editTextNumberDecimal);
            name.setText(user.getValor());

            Spinner spinner_cuentas = (Spinner) findViewById(R.id.spinner_Tipo);
            spinner_cuentas.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, ListaTipo));
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

            int index=0;
            for (String i:ListaTipo){
                if(user.getTipo().equals(i)){
                    spinner_cuentas.setSelection(index);
                }
                index++;
            }
        }

    }

    public void CrearUsuario(){
        Usuario user= new Usuario();
        EditText name=(EditText)findViewById(R.id.editTextTextPersonName);
        user.setUsuario(name.getText().toString());
        name=findViewById(R.id.editTextNumberDecimal);

        String valorInicial = name.getText().toString();

        if ((!valorInicial.isEmpty()) && name.getText().toString().matches("[+-]?\\d*(\\.\\d+)?")){
            user.setValor(name.getText().toString());
        }else {
            user.setValor("0");
        }


        Spinner spinner_tipos = (Spinner) findViewById(R.id.spinner_Tipo);
        int Pos=spinner_tipos.getSelectedItemPosition();
        user.setTipo(ListaTipo.get(Pos));
        UsuarioRepository mRepository = new UsuarioRepository(getApplication());
        mRepository.insert(user);
       // Flowable<List<Cuenta>> allUsers = mRepository.getAllCuentasFW();
      //  ListaCuentas=allUsers.blockingFirst();
        finish();
    }

    public void updateUsuario(){

        EditText name=(EditText)findViewById(R.id.editTextTextPersonName);
        user.setUsuario(name.getText().toString());
        name=findViewById(R.id.editTextNumberDecimal);

        String valorInicial = name.getText().toString();

        if ((!valorInicial.isEmpty()) && name.getText().toString().matches("[+-]?\\d*(\\.\\d+)?")){
            user.setValor(name.getText().toString());
        }else {
            user.setValor("0");
        }


        Spinner spinner_tipos = (Spinner) findViewById(R.id.spinner_Tipo);
        int Pos=spinner_tipos.getSelectedItemPosition();
        user.setTipo(ListaTipo.get(Pos));
        UsuarioRepository mRepository = new UsuarioRepository(getApplication());
        mRepository.Update(user);
        // Flowable<List<Cuenta>> allUsers = mRepository.getAllCuentasFW();
        //  ListaCuentas=allUsers.blockingFirst();
        finish();

    }
}