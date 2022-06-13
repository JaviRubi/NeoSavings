package com.example.neosavings;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.FileNotFoundException;

public class ImageZoom extends AppCompatActivity {

    Bitmap bitmap;
    SubsamplingScaleImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);
        String file=(String)getIntent().getExtras().get("Imagen");
        imageView=findViewById(R.id.ImagenZoom);


        if(file.equals("myImage")) {
            try {
                bitmap=BitmapFactory.decodeStream(this.openFileInput(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            if(bitmap!=null){
                imageView.setImage(ImageSource.bitmap(bitmap));

            }
        }


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