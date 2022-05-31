package com.example.neosavings.ui.Presupuestos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.neosavings.R;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Formularios.Formulario_Presupuestos;
import com.example.neosavings.ui.Modelo.Presupuesto;
import com.example.neosavings.ui.Registros.RegistroFragmentV2;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class PresupuestoInfo extends AppCompatActivity {

    private UsuarioRepository mRepositpory;
    private Presupuesto presupuesto;
    private RegistroFragmentV2 registroFragmentV2;
    private long PresupuestoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presupuesto_info);
        PresupuestoID= (long) getIntent().getExtras().get("PresupuestoID");
        mRepositpory=new UsuarioRepository(getBaseContext());
        findViewById(R.id.floatingActionButtonEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(), Formulario_Presupuestos.class);
                intent.putExtra("CASO","EDITAR");
                intent.putExtra("PresupuestoID",PresupuestoID);
                startActivity(intent);
            }
        });

        findViewById(R.id.floatingActionButtonBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ActualizarLayout();
    }




    public void ActualizarLayout(){
        presupuesto=mRepositpory.getPresupuestosByIDFW(PresupuestoID).blockingFirst();

        TextView texto=(TextView) findViewById(R.id.textViewNombrePresupuesto);
        texto.setText(presupuesto.getName());

        texto=(TextView) findViewById(R.id.textViewCategoriaPresupuesto);
        texto.setText(presupuesto.getCategoria());

        texto=(TextView) findViewById(R.id.textViewUsuarioPresupuesto);
        texto.setText(presupuesto.getUserName());

        texto=(TextView) findViewById(R.id.textViewFechaInicioPresupuesto);
        texto.setText(new SimpleDateFormat("dd/MM/yyyy").format(presupuesto.getFechaInicio()));

        texto=(TextView) findViewById(R.id.textViewFechaFinPresupuesto);
        texto.setText(new SimpleDateFormat("dd/MM/yyyy").format(presupuesto.getFechaFin()));

        DecimalFormat formato=new DecimalFormat("#,###.### €");

        Double presupuestoInicial= Double.valueOf(presupuesto.getPresupuesto());
        Double presupuestoFinal=presupuesto.presupuestoHastaAhora(getBaseContext());

        texto=(TextView) findViewById(R.id.textViewPresupuestoInicial);
        texto.setText(formato.format(presupuestoInicial));

        texto=(TextView) findViewById(R.id.textViewPresupuestoFinal);
        texto.setText(formato.format(presupuestoFinal));

        ProgressBar progressBar=findViewById(R.id.progressBarPresupuesto);

        if(presupuestoInicial<presupuestoFinal){
            int progress=100;
            progressBar.setProgress(progress);
        }else {
            int progress=(int) ((presupuestoFinal/presupuestoInicial)*100);
            progressBar.setProgress(progress);
        }

        registroFragmentV2=new RegistroFragmentV2(presupuesto.getRegistros(getBaseContext()),this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameLayoutListaRegistrosPresupuesto,registroFragmentV2)
                .commit();
    }

    @Override
    public void onResume() {
        ActualizarLayout();
        super.onResume();
    }
}