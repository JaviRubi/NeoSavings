package com.example.neosavings.ui.Ayuda;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.neosavings.R;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.List;


public class Ayuda_Home extends AppCompatActivity {

    List<CarouselItem> ListaImagenes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ayuda_home);

        String caso= (String) getIntent().getExtras().get("CASO");

        ListaImagenes=new ArrayList<>();
        ImageCarousel carousel=findViewById(R.id.carouselHome);

        switch (caso){
            case "HOME":
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_home_infov2,"Pantalla Home"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_navegacion_infov2,"Información Navegación"));
                carousel.setCaptionTextSize(60);
                break;

            case "USER":
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_lista_cuentas_info,"Pantalla Lista Cuentas"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_lista_cuentas_crear_info,"Cómo Crear una Cuenta"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_lista_cuentas_update_infov2,"Cómo Editar una Cuenta"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_eliminar_cuentas_info,"Cómo Borrar una Cuenta"));
                carousel.setCaptionTextSize(60);
                setTitle("Ayuda Usuarios");
                break;

            case "REGISTROS":
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_filtro_registros_infov2,"Pantalla Registros"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_formulario_registros_infov3,"Formulario Registros"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_preview_imagen_registros_infov2,"Zoom Imagen Registro"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_registros_crear_infov2,"Cómo Crear un Registro"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_registros_update_infov2,"Cómo Editar un Registro"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_eliminar_registros_info,"Cómo Borrar un Registro"));
                carousel.setCaptionTextSize(60);
                setTitle("Ayuda Registros");
                break;

            case "ESTADISTICAS":
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_estadisticas_info,"Pantalla Estadísticas"));
                carousel.setCaptionTextSize(60);
                setTitle("Ayuda Estadísticas");
                break;

            case "PAGOSPROGRAMADOS":
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_pagos_programados_infov2,"Pantalla Pagos Programados"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_detalle_pagos_programados_infov2,"Pantalla Detalle Pago Programado"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_formulario_pagos_programados_info,"Formulario Pago Programado"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_crear_pagos_programados_infov2,"Cómo Crear un Pago Programado"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_editar_pagos_programados_info,"Cómo Editar un Pago Programado"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_eliminar_pagos_programados_info,"Cómo Borrar un Pago Programado"));
                carousel.setCaptionTextSize(40);
                setTitle("Ayuda Pagos Programados");
                break;

            case "DEUDAS":
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_deudas_info,"Pantalla Deudas y Préstamos"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_detalle_deudas_info,"Pantalla Detalle Deuda o Préstamo"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_formulario_deudas_info,"Formulario Deuda o Préstamo"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_formulario_nuevo_pago_deudas_info,"Formulario Nuevo Pago Deuda o Préstamo"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_crear_deudas_info,"Cómo Crear una Deuda o Préstamo"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_crear_nuevo_pago_deudas_info,"Cómo Añadir un Pago"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_editar_deudas_info,"Cómo Editar una Deuda o Préstamo"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_borrar_deudas_info,"Cómo Borrar una Deuda o Préstamo"));
                carousel.setCaptionTextSize(40);
                setTitle("Ayuda Deudas y Préstamos");
                break;

            case "OBJETIVOS":
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_objetivo_info,"Pantalla Objetivos"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_detalle_objetivo_info,"Pantalla Detalle Objetivo"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_crear_objetivo_info,"Cómo Crear un Objetivo"));//
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_editar_objetivo_info,"Cómo Editar un Objetivo"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_borrar_objetivo_info,"Cómo Borrar un Objetivo"));//
                carousel.setCaptionTextSize(50);
                setTitle("Ayuda Objetivos");
                break;

            case "NOTIFICACIONES":
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_presupuestos_info,"Pantalla Presupuestos"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_detalle_presupuestos_info,"Pantalla Detalle Presupuesto"));
                //ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_formulario_pagos_programados_info,"Formulario Presupuesto"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_crear_presupuestos_info,"Cómo Crear un Presupuesto"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_editar_presupuestos_info,"Cómo Editar un Presupuesto"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_borrar_presupuestos_info,"Cómo Borrar un Presupuesto"));

                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_notificacion_presupuesto_info,"Notificación Presupuesto"));//
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_notificacion_pagos_programados_info,"Notificación Pago Programado"));//
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_notificacion_deuda_info,"Notificación Deuda o Préstamo"));//
                carousel.setCaptionTextSize(45);
                setTitle("Ayuda Presupuestos y Notificaciones");
                break;


        }




        carousel.addData(ListaImagenes);
        carousel.setCarouselPaddingBottom(175);
        carousel.setAutoWidthFixing(true);
        carousel.setImageScaleType(ImageView.ScaleType.FIT_CENTER);
        carousel.setCaptionMargin(-30);
        carousel.setBottomShadowHeight(200);
        carousel.setShowTopShadow(false);
        carousel.setShowNavigationButtons(false);
        carousel.setCurrentPosition(0);




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


}