package com.example.neosavings.ui.Ayuda;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.neosavings.R;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.imaginativeworld.whynotimagecarousel.model.CarouselType;

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

        switch (caso){
            case "HOME":
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_home_info,"Información Home"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_navegacion_menu,"Información Navegación"));
                break;
            case "USER":
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_lista_cuentas_info,"Información Lista Cuentas"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_crear_cuentas_info,"Cómo Crear una Cuenta"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_editar_cuentas_info,"Cómo Editar una Cuenta"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_eliminar_cuentas_info,"Cómo Borrar una Cuenta"));
                setTitle("Ayuda Usuarios");
                break;
            case "REGISTROS":
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_filtro_registros_info,"Información Pantalla Registros"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_formulario_registros_info,"Información Formulario Registros"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_preview_imagen_registros_info,"Zoom Imagen Registro"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_crear_registros_info,"Cómo Crear un Registro"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_editar_registros_info,"Cómo Editar un Registro"));
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_eliminar_registros_info,"Cómo Borrar un Registro"));
                setTitle("Ayuda Registros");
                break;
            case "ESTADISTICAS":
                ListaImagenes.add(new CarouselItem(R.drawable.pantalla_ayuda_estadisticas_info,"Información Pantalla Estadísticas"));
                setTitle("Ayuda Estadísticas");
                break;


        }


        ImageCarousel carousel=findViewById(R.id.carouselHome);

        carousel.addData(ListaImagenes);
        carousel.setCarouselPaddingBottom(175);
        carousel.setAutoWidthFixing(true);
        carousel.setImageScaleType(ImageView.ScaleType.FIT_CENTER);
        carousel.setCaptionMargin(-30);
        carousel.setBottomShadowHeight(200);
        carousel.setShowTopShadow(false);
        carousel.setShowNavigationButtons(false);
        carousel.setCarouselType(CarouselType.BLOCK);
        carousel.setCaptionTextSize(60);
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