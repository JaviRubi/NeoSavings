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
                ListaImagenes.add(new CarouselItem(R.drawable.ic_home,"Información Lista Cuentas"));
                ListaImagenes.add(new CarouselItem(R.drawable.ic_edit_fill,"Cómo Crear una Cuenta"));
                ListaImagenes.add(new CarouselItem(R.drawable.ic_edit_fill,"Cómo Borrar una Cuenta"));
                ListaImagenes.add(new CarouselItem(R.drawable.ic_edit_fill,"Cómo Editar una Cuenta"));
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
        carousel.setCaptionTextSize(70);
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