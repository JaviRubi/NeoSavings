package com.example.neosavings.ui.Objetivos;

import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.neosavings.R;
import com.example.neosavings.ui.AlertsDialogs.AlertDialogFel;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Formularios.Formulario_Objetivos;
import com.example.neosavings.ui.Modelo.Objetivo;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class ObjetivoInfo extends AppCompatActivity {

    Objetivo objetivo;
    long ObjetivoID;
    UsuarioRepository mRepository;
    boolean Activado=true;

    TextView mNombre;
    TextView mFechaFin;
    TextView mAhorrado;
    TextView mObjetivo;
    TextView mPorcentaje;
    ProgressBar mProogressbar;

    Button mAñadir;
    ImageButton mPausar;

    EditText mCantidad;
    Double CantidadObjetivo;
    Double Ahorrado;
    private SoundPool soundPool;
    private int sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objetivo_info);

        mRepository=new UsuarioRepository(getBaseContext());
        objetivo=new Objetivo();
        preprarSonido();

        ObjetivoID= (long) getIntent().getExtras().get("ObjetivoID");

        mNombre=findViewById(R.id.textViewNombreObjetivo);
        mFechaFin=findViewById(R.id.textViewFechaFin);
        mAhorrado=findViewById(R.id.textViewAhorrado);
        mObjetivo=findViewById(R.id.textViewCantidadObjetivo);
        mPorcentaje=findViewById(R.id.textViewPorcentaje);
        mProogressbar=findViewById(R.id.circular_determinative_pb);
        mCantidad=findViewById(R.id.editTextNumberDecimal2);
        mAñadir=findViewById(R.id.button);
        mPausar=findViewById(R.id.imageButton);

        ListenerButtonEditarySalir();

        mPausar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objetivo o=new Objetivo();
                o.setEstadoConcluido();
                o.setEstadoPausado();
                if(objetivo.getEstado().equals(o.getEstado())){
                    objetivo.setEstadoActivo();
                    mRepository.Update(objetivo);
                    mPausar.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play_circle, Resources.getSystem().newTheme()));
                    mAñadir.setVisibility(View.VISIBLE);
                    mCantidad.setVisibility(View.VISIBLE);
                    mPausar.setVisibility(View.VISIBLE);
                }else{
                    o.setEstadoActivo();
                    if(o.getEstado().equals(objetivo.getEstado())){
                        objetivo.setEstadoPausado();
                        mRepository.Update(objetivo);
                        mPausar.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause_circle, Resources.getSystem().newTheme()));
                        mAñadir.setVisibility(View.INVISIBLE);
                        mCantidad.setVisibility(View.INVISIBLE);
                        mPausar.setVisibility(View.VISIBLE);
                    }
                }
                o.setEstadoConcluido();
                if(o.getEstado().equals(objetivo.getEstado())){
                    mPausar.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play_circle, Resources.getSystem().newTheme()));
                    mAñadir.setVisibility(View.INVISIBLE);
                    mCantidad.setVisibility(View.INVISIBLE);
                    mPausar.setVisibility(View.INVISIBLE);
                }
            }
        });

        mAñadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Cantidad=mCantidad.getText().toString();
                if(Cantidad.isEmpty()){
                    Cantidad="0";
                }

                Double CantidadAhorradaNueva=Double.valueOf(Cantidad);

                Ahorrado+=CantidadAhorradaNueva;

                objetivo.setAhorrado(String.valueOf(Ahorrado));
                mRepository.Update(objetivo);
                if(Ahorrado>=CantidadObjetivo){
                    objetivo.setEstadoConcluido();
                    mRepository.Update(objetivo);
                    ActualizarObjetivo();
                    AlertDialogFel dialof = new AlertDialogFel();
                    soundPool.play(sound, 1, 1, 0, 0, 1);
                    dialof.show(getSupportFragmentManager(), "AlertDialog2");
                }
            }
        });

        ActualizarObjetivo();



    }

    public void ActualizarObjetivo(){
        objetivo=mRepository.getObjetivoByID(ObjetivoID).blockingFirst();
        Objetivo o=new Objetivo();

        mNombre.setText(objetivo.getNombre());
        mFechaFin.setText(new SimpleDateFormat("dd/MM/yyyy").format(objetivo.getFechaDeseada()));
        DecimalFormat formato=new DecimalFormat("#,###.### €");
        CantidadObjetivo=Double.valueOf(objetivo.getCantidadObjetivo());
        Ahorrado=Double.valueOf(objetivo.getAhorrado());
        mAhorrado.setText("Ahorrado: "+formato.format(Ahorrado));
        mObjetivo.setText("Objetivo: "+formato.format(CantidadObjetivo));

        if(Ahorrado>=CantidadObjetivo){
            mProogressbar.setProgress(100);
            mPorcentaje.setText("100 %");
            objetivo.setEstadoConcluido();
            mRepository.Update(objetivo);
            mCantidad.setVisibility(View.INVISIBLE);
            mAñadir.setVisibility(View.INVISIBLE);
            mPausar.setVisibility(View.INVISIBLE);
        }else{
            int progress=(int) ((Ahorrado/CantidadObjetivo)*100);
            mProogressbar.setProgress(progress);
            mPorcentaje.setText(progress+" %");
            o.setEstadoConcluido();
            if(objetivo.getEstado().equals(o.getEstado())){
                objetivo.setEstadoActivo();
                mRepository.Update(objetivo);
            }
        }

        o.setEstadoActivo();
        if(objetivo.getEstado().equals(o.getEstado())){
            mCantidad.setVisibility(View.VISIBLE);
            mAñadir.setVisibility(View.VISIBLE);
            mPausar.setVisibility(View.VISIBLE);
        }

        o.setEstadoPausado();
        if(objetivo.getEstado().equals(o.getEstado())){
            mPausar.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause_circle, Resources.getSystem().newTheme()));
            mAñadir.setVisibility(View.INVISIBLE);
            mCantidad.setVisibility(View.INVISIBLE);
            mPausar.setVisibility(View.VISIBLE);
        }

        o.setEstadoConcluido();
        if(objetivo.getEstado().equals(o.getEstado())){
            mPausar.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play_circle, Resources.getSystem().newTheme()));
            mAñadir.setVisibility(View.INVISIBLE);
            mCantidad.setVisibility(View.INVISIBLE);
            mPausar.setVisibility(View.INVISIBLE);
        }
    }

    public void preprarSonido() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(6)
                .setAudioAttributes(audioAttributes)
                .build();

        sound = soundPool.load(this, R.raw.felicitacion2, 1);

    }

    public void ListenerButtonEditarySalir(){
        findViewById(R.id.floatingActionButton6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(), Formulario_Objetivos.class);
                intent.putExtra("CASO","UPDATE");
                intent.putExtra("ObjetivoID",ObjetivoID);
                startActivity(intent);
            }
        });

        findViewById(R.id.floatingActionButton7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        ActualizarObjetivo();
        super.onResume();
    }
}