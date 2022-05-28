package com.example.neosavings;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.neosavings.ui.Database.UsuarioRepository;

import java.io.FileNotFoundException;

public class ImageZoom extends AppCompatActivity {

    Bitmap bitmap;
    SubsamplingScaleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);
        UsuarioRepository mRepository=new UsuarioRepository(getBaseContext());
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

        findViewById(R.id.floatingActionButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}