package com.example.neosavings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.neosavings.databinding.ActivityMainBinding;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Modelo.Categoria;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private UsuarioRepository mRepository;

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
                R.id.nav_home, R.id.nav_cuentas, R.id.nav_registros, R.id.nav_stadistic, R.id.nav_pagosProgramados, R.id.nav_presupuestos, R.id.nav_deudas, R.id.nav_objetivos, R.id.nav_importar, R.id.nav_exportar, R.id.nav_ayuda,  R.id.nav_configuracion )
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        mRepository= new UsuarioRepository(getApplication());
        Categorias();


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