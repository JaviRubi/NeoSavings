package com.example.neosavings.ui.Deudas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.neosavings.R;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Formularios.Formulario_Deudas;
import com.example.neosavings.ui.Formularios.Formulario_NuevoPagoDeuda;
import com.example.neosavings.ui.Modelo.PagosDeudas;
import com.example.neosavings.ui.Modelo.Registro;
import com.example.neosavings.ui.Registros.RegistroFragmentV5;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DeudasInfo extends AppCompatActivity {

    Integer DeudaID;
    PagosDeudas pagosDeudas;

    UsuarioRepository mRepository;

    TextView mNombre;
    TextView mDescripcion;
    TextView mDeuda;
    TextView mDeudaRestante;
    TextView mFechaInicio;
    TextView mFechaFin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deudas_info);

        mRepository=new UsuarioRepository(getBaseContext());

        mNombre=findViewById(R.id.textViewNombreDeuda);
        mDescripcion=findViewById(R.id.textViewDescripcionDeuda);
        mDeuda=findViewById(R.id.textViewDeuda);
        mDeudaRestante=findViewById(R.id.textViewDeudaRestante);
        mFechaInicio= findViewById(R.id.textViewFechaInicioDeuda);
        mFechaFin= findViewById(R.id.textViewFechaFinDeuda);

        findViewById(R.id.floatingActionButtonEditDeuda).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(), Formulario_Deudas.class);
                intent.putExtra("CASO","UPDATE");
                intent.putExtra("DeudaID",pagosDeudas.getDeuda().getDeudaID());
                startActivity(intent);
            }
        });

        findViewById(R.id.AñadirPagoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(), Formulario_NuevoPagoDeuda.class);
                intent.putExtra("CASO","Crear");
                intent.putExtra("DeudaID",pagosDeudas.getDeuda().getDeudaID());
                startActivity(intent);
            }
        });

         DeudaID = (Integer) getIntent().getExtras().get("DeudaID");
         if(DeudaID!=null){
             ActualizarVista();
         }
    }

    public void ActualizarVista() {

        pagosDeudas=mRepository.getRegistrosDeudasByID(DeudaID).blockingFirst();
        pagosDeudas.ActualizarEstadoPrestamo(getBaseContext());
        if(pagosDeudas.getDeuda().isDeuda()){
            setTitle("Información Deuda");
        }else{
            setTitle("Información Préstamo");
        }

        if(pagosDeudas.getDeuda().isDeuda()){
            mNombre.setText(pagosDeudas.getDeuda().getNombreCuenta()+" -> "+pagosDeudas.getDeuda().getNombre());
        }else{
            mNombre.setText(pagosDeudas.getDeuda().getNombreCuenta()+" <- "+pagosDeudas.getDeuda().getNombre());
        }

        mDescripcion.setText(pagosDeudas.getDeuda().getDescripcion());
        DecimalFormat formato=new DecimalFormat("#,###.### €");

        mDeuda.setText(formato.format(Double.valueOf(pagosDeudas.getDeuda().getCosteDeuda())));

        if(pagosDeudas.GetDeudaRestante()==0){
            mDeudaRestante.setText("CONCLUIDO");
        }else{
            mDeudaRestante.setText(formato.format(pagosDeudas.GetDeudaRestante()));
        }

        mFechaInicio.setText(new SimpleDateFormat("dd/MM/yyyy").format(pagosDeudas.getDeuda().getFechaInicio()));

        mFechaFin.setText(new SimpleDateFormat("dd/MM/yyyy").format(pagosDeudas.getDeuda().getFechaVencimiento()));

        List<Registro> aux = new ArrayList<>(pagosDeudas.getRegistros());

        for (Registro r: pagosDeudas.getRegistros()){
            if(r.getRegistroID()==pagosDeudas.getDeuda().getRegistroIDPrincipal()){
                aux.remove(r);
            }

        }

        RegistroFragmentV5 registroFragmentV5=new RegistroFragmentV5(aux,this);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(androidx.navigation.ui.R.animator.nav_default_enter_anim, androidx.navigation.ui.R.animator.nav_default_exit_anim,
                        androidx.navigation.ui.R.animator.nav_default_pop_enter_anim,
                        androidx.navigation.ui.R.animator.nav_default_pop_exit_anim)
                .replace(R.id.FrameLayoutRegistrosDeuda,registroFragmentV5)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                Log.i("ActionBar", "Atrás!");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        ActualizarVista();
        super.onResume();
    }
}