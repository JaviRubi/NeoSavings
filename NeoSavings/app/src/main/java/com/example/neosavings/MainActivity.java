package com.example.neosavings;

import android.Manifest;
import android.app.AlarmManager;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.neosavings.databinding.ActivityMainBinding;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Modelo.Categoria;
import com.example.neosavings.ui.Reciver.NotificationWorker;
import com.example.neosavings.ui.Reciver.NotificationWorkerDeudas;
import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private UsuarioRepository mRepository;
    final String CHANNEL_ID_DEUDAS="Notificaciones Deudas";

    private static final int CODE_CAMARA=200;
    private SwitchCompat NightModeSwitch;
    public AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_cuentas, R.id.nav_registros, R.id.nav_stadistic, R.id.nav_pagosProgramados, R.id.nav_presupuestos, R.id.nav_deudas, R.id.nav_objetivos, R.id.nav_exportar, R.id.nav_ayuda)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



        mRepository= new UsuarioRepository(getApplication());
        Categorias();
        String[] permisos=new String[3];
        permisos[0]= Manifest.permission.CAMERA;
        permisos[1]=Manifest.permission.WRITE_EXTERNAL_STORAGE;
        permisos[2]=Manifest.permission.READ_EXTERNAL_STORAGE;

        requestPermissions(permisos,2);

        ConfiguracionesTemas();

        setWorker();

        //doWork();









    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void ConfiguracionesTemas(){
        MenuItem ModoOscuro= binding.navView.getMenu().findItem(R.id.app_bar_switch);
        View view= ModoOscuro.getActionView();
        NightModeSwitch=view.findViewById(R.id.SwitchNav);

        if(AppCompatDelegate.getDefaultNightMode()!=AppCompatDelegate.MODE_NIGHT_NO){
            NightModeSwitch.setChecked(true);
            ModoOscuro.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_dark_mode,null));
            int[] attr = {com.google.android.material.R.attr.colorSurface};
            TypedArray typedArray = obtainStyledAttributes(R.style.Theme_NeoSavings, attr);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(typedArray.getColor(0, Color.BLACK)));
        }else{
            NightModeSwitch.setChecked(false);
            ModoOscuro.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_light_mode,null));
        }

        NightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(MainActivity.this, "MODO OSCURO activado", Toast.LENGTH_SHORT).show();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    ModoOscuro.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_dark_mode,null));

                    int[] attr = {com.google.android.material.R.attr.colorSurface};
                    TypedArray typedArray = obtainStyledAttributes(R.style.Theme_NeoSavings, attr);
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(typedArray.getColor(0, Color.BLACK)));
                }
                else {
                    Toast.makeText(MainActivity.this, "Modo OSCURO desactivado", Toast.LENGTH_SHORT).show();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    ModoOscuro.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_light_mode,null));
                }
            }
        });
    }

    public void setWorker(){



        PeriodicWorkRequest NotificacionesPagosProgramados = new PeriodicWorkRequest.Builder(NotificationWorker.class,4, TimeUnit.HOURS)
                .addTag("notificaciones PagosProgramados")
                .setInitialDelay(25,TimeUnit.MINUTES)
                .build();

        PeriodicWorkRequest NotificacionesDeudas = new PeriodicWorkRequest.Builder(NotificationWorkerDeudas.class,3, TimeUnit.HOURS)
                .addTag("notificaciones Deudas")
                .build();

        WorkManager workManager=WorkManager.getInstance(getApplicationContext());
        workManager.enqueueUniquePeriodicWork(
                "notificaciones Deudas",
                ExistingPeriodicWorkPolicy.KEEP,
                NotificacionesDeudas);

        workManager.enqueueUniquePeriodicWork(
                "notificaciones PagosProgramados",
                ExistingPeriodicWorkPolicy.KEEP,
                NotificacionesPagosProgramados);
    }

    public void Categorias(){


        Categoria categoria=new Categoria();
        categoria.setCategoría("Comida y Bebida");
        categoria.setTipoGasto();
        mRepository.insertCategoria(categoria);

        categoria=new Categoria();
        categoria.setCategoría("Compras");
        categoria.setTipoGasto();
        mRepository.insertCategoria(categoria);

        categoria=new Categoria();
        categoria.setCategoría("Vivienda");
        categoria.setTipoGasto();
        mRepository.insertCategoria(categoria);

        categoria=new Categoria();
        categoria.setCategoría("Transporte");
        categoria.setTipoGasto();
        mRepository.insertCategoria(categoria);

        categoria=new Categoria();
        categoria.setCategoría("Vehiculos");
        categoria.setTipoGasto();
        mRepository.insertCategoria(categoria);

        categoria=new Categoria();
        categoria.setCategoría("Entretenimiento");
        categoria.setTipoGasto();
        mRepository.insertCategoria(categoria);

        categoria=new Categoria();
        categoria.setCategoría("Informatica");
        categoria.setTipoGasto();
        mRepository.insertCategoria(categoria);

        categoria=new Categoria();
        categoria.setCategoría("Impuestos");
        categoria.setTipoGasto();
        mRepository.insertCategoria(categoria);

        categoria=new Categoria();
        categoria.setCategoría("Préstamos");
        categoria.setTipoGasto();
        mRepository.insertCategoria(categoria);

        categoria=new Categoria();
        categoria.setCategoría("Inversiones");
        categoria.setTipoGasto();
        mRepository.insertCategoria(categoria);

        categoria=new Categoria();
        categoria.setCategoría("Otros");
        categoria.setTipoGasto();
        mRepository.insertCategoria(categoria);


        categoria=new Categoria();
        categoria.setCategoría("Ingreso");
        categoria.setTipoIngreso();
        mRepository.insertCategoria(categoria);

        categoria=new Categoria();
        categoria.setCategoría("Cheques");
        categoria.setTipoIngreso();
        mRepository.insertCategoria(categoria);

        categoria=new Categoria();
        categoria.setCategoría("Donaciones");
        categoria.setTipoIngreso();
        mRepository.insertCategoria(categoria);

        categoria=new Categoria();
        categoria.setCategoría("Ingresos Alquiler");
        categoria.setTipoIngreso();
        mRepository.insertCategoria(categoria);

        categoria=new Categoria();
        categoria.setCategoría("Intereses");
        categoria.setTipoIngreso();
        mRepository.insertCategoria(categoria);

        categoria=new Categoria();
        categoria.setCategoría("Lotería/Juegos de Azar");
        categoria.setTipoIngreso();
        mRepository.insertCategoria(categoria);

        categoria=new Categoria();
        categoria.setCategoría("Préstamos");
        categoria.setTipoIngreso();
        mRepository.insertCategoria(categoria);

        categoria=new Categoria();
        categoria.setCategoría("Reembolsos");
        categoria.setTipoIngreso();
        mRepository.insertCategoria(categoria);

        categoria=new Categoria();
        categoria.setCategoría("Regalos");
        categoria.setTipoIngreso();
        mRepository.insertCategoria(categoria);

        categoria=new Categoria();
        categoria.setCategoría("Salarios");
        categoria.setTipoIngreso();
        mRepository.insertCategoria(categoria);

        categoria=new Categoria();
        categoria.setCategoría("Ventas");
        categoria.setTipoIngreso();
        mRepository.insertCategoria(categoria);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            //Cancelado por el usuario
        }
        if ((resultCode == RESULT_OK) && (requestCode == 1)) {
            //Procesar el resultado
            Uri uri = data.getData(); //obtener el uri content
        }
    }

}