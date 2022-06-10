package com.example.neosavings;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

    private SwitchCompat NightModeSwitch;
    NavigationView navigationView;

    private MainActivity context;
    SharedPreferences sharedPref;
    private final String SHARED_PREF="SHARED_PREF";
    private final String SAVED_THEME="THEME";
    int Themes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean contains = sharedPref.contains("THEME");
        Themes=sharedPref.getInt(SAVED_THEME,1);

        if(Themes==AppCompatDelegate.MODE_NIGHT_NO){
            if(AppCompatDelegate.getDefaultNightMode()!=AppCompatDelegate.MODE_NIGHT_NO) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }else{
            if(AppCompatDelegate.getDefaultNightMode()!=AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        }
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context=this;

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        navigationView= binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_cuentas, R.id.nav_registros, R.id.nav_stadistic, R.id.nav_pagosProgramados, R.id.nav_presupuestos, R.id.nav_deudas, R.id.nav_objetivos, R.id.nav_ayuda)
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











    }

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

        boolean Default=false;

        int defaultNightMode = AppCompatDelegate.getDefaultNightMode();
        if(defaultNightMode==AppCompatDelegate.MODE_NIGHT_YES) {
            if(!NightModeSwitch.isChecked()){
                NightModeSwitch.setChecked(true);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            Default=true;

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(SAVED_THEME, AppCompatDelegate.MODE_NIGHT_YES);
            editor.apply();
            editor.commit();


            ModoOscuro.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_dark_mode, null));
             int[] attr = {com.google.android.material.R.attr.colorSurface};
            TypedArray typedArray = obtainStyledAttributes(R.style.Theme_NeoSavings, attr);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(typedArray.getColor(0, Color.BLACK)));
        }
        if(defaultNightMode==AppCompatDelegate.MODE_NIGHT_NO) {
            Default=true;
            if(NightModeSwitch.isChecked()){
                NightModeSwitch.setChecked(false);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(SAVED_THEME, AppCompatDelegate.MODE_NIGHT_NO);
            editor.apply();
            editor.commit();

            ModoOscuro.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_light_mode, null));
        }

        if(!Default){
            int defaultValue = AppCompatDelegate.MODE_NIGHT_NO;
            Themes = sharedPref.getInt(SAVED_THEME, defaultValue);
            if(Themes==AppCompatDelegate.MODE_NIGHT_YES){
                if(!NightModeSwitch.isChecked()){
                    NightModeSwitch.setChecked(true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }



            }else{
                if(NightModeSwitch.isChecked()){
                    NightModeSwitch.setChecked(false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }


            }
        }

        NightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Toast.makeText(MainActivity.this, "MODO OSCURO activado", Toast.LENGTH_SHORT).show();

                    ModoOscuro.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_dark_mode,null));
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                     //getWindow().setStatusBarColor(Color.parseColor("#000000"));
                    //navigationView.setBackground(new ColorDrawable(Color.parseColor("#444444")));
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(SAVED_THEME, AppCompatDelegate.MODE_NIGHT_YES);
                    editor.apply();


                } else {
                    Toast.makeText(MainActivity.this, "Modo OSCURO desactivado", Toast.LENGTH_SHORT).show();
                    ModoOscuro.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_light_mode,null));
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                   //context.recreate();
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(SAVED_THEME, AppCompatDelegate.MODE_NIGHT_NO);
                    editor.apply();
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

    public void recrear(){
        this.recreate();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

        int currentNightMode = newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        int[] attr;
        TypedArray typedArray;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                recreate();
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                recreate();
                attr = new int[]{com.google.android.material.R.attr.colorSurface, com.google.android.material.R.attr.colorPrimaryVariant};
                typedArray= obtainStyledAttributes(R.style.Theme_NeoSavings, attr);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(typedArray.getColor(0, Color.BLACK)));


                break;
        }
        super.onConfigurationChanged(newConfig);
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

}