package com.example.neosavings.ui.Formularios;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.neosavings.ImageZoom;
import com.example.neosavings.R;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Modelo.Categoria;
import com.example.neosavings.ui.Modelo.Cuenta;
import com.example.neosavings.ui.Modelo.Registro;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Flowable;

public class Formulario_Registros extends AppCompatActivity {

    private static final int PICK_IMAGE=200;
    private static final int CODE_CAMARA=500;

    public List<Cuenta> ListaCuentas;
    public List<Categoria> ListaCategoriaGastos;
    public List<Categoria> ListaCategoriaIngresos;
    public List<Categoria> ListaCategoriaSelec;

    public List<String> spinnerCuentas;
    public List<String> spinnerCategorias;
    public List<String> spinnerCategoriasIngresos;
    public List<String> spinnerCategoriasGastos;
    public Spinner spinner_cuentas;
    public Spinner spinner_Categorias;
    public UsuarioRepository mRepository;
    public Context context;
    ArrayAdapter<String> adapter;
    private Registro registro;
    private String currentPhotoPath;
    private ImageView Ticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_registros);

        int[] attr = {androidx.appcompat.R.attr.colorPrimary};
        TypedArray typedArray = obtainStyledAttributes(R.style.Theme_NeoSavings, attr);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(typedArray.getColor(0, Color.BLACK)));

        context=this;
        mRepository = new UsuarioRepository(getApplicationContext());
        spinnerCuentas=new ArrayList<>();
        spinnerCategorias =new ArrayList<>();
        spinnerCategoriasGastos=new ArrayList<>();
        spinnerCategoriasIngresos=new ArrayList<>();
        registro=new Registro();

        Flowable<List<Cuenta>> allUsers = mRepository.getAllCuentasFW();
        ListaCuentas=allUsers.blockingFirst();

        Flowable<List<Categoria>> allCategorias=mRepository.getALLCategoriasGastos();
        ListaCategoriaGastos =allCategorias.blockingFirst();

        allCategorias=mRepository.getALLCategoriasIngresos();
        ListaCategoriaIngresos =allCategorias.blockingFirst();

        spinnerCuentas.clear();
        spinnerCategorias.clear();

        if(ListaCategoriaGastos !=null) {
            if(ListaCategoriaGastos.size()!=0){
                for (Categoria c : ListaCategoriaGastos) {
                    spinnerCategorias.add(c.getCategoría());
                    spinnerCategoriasGastos.add(c.getCategoría());
                }
            }else{
                spinnerCategorias.add("NO HAY CATEGORIAS");
            }
        }

        if(ListaCategoriaIngresos !=null) {
            if(ListaCategoriaIngresos.size()!=0){
                for (Categoria c : ListaCategoriaIngresos) {
                    spinnerCategoriasIngresos.add(c.getCategoría());
                }
            }
        }

        if(ListaCuentas!=null) {
            if(ListaCuentas.size()!=0) {
                for (Cuenta u : ListaCuentas) {
                    spinnerCuentas.add(u.getUser().getUsuario());
                }
            }else{
                spinnerCuentas.add("NO HAY CUENTAS");
            }
        }

        EditText Editfecha=(EditText) findViewById(R.id.editTextDate);
        Editfecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));




        ImageButton Ok=(ImageButton) findViewById(R.id.Button_OK_FormularioRegistro);
        Ok.setOnClickListener(view -> {

            boolean valido=false;
            try {
                valido=CrearRegistro();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(valido) {
                finish();
            }
        });

        ImageButton camera=(ImageButton) findViewById(R.id.imageButtonCamera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String timeStamp =new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String imageFileName="PNG_"+timeStamp+"_";
                File storageDir=getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File image = null;
                try {
                    image=File.createTempFile(imageFileName,
                            ".png",storageDir);
                    currentPhotoPath=image.getAbsolutePath();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(image!=null){
                    Uri photoUri= FileProvider.getUriForFile(getBaseContext(),"com.example.neosavings",image);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                }


                if (ContextCompat.checkSelfPermission(
                        getBaseContext(), Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_GRANTED) {
                    // You can use the API that requires the permission.

                    if (ContextCompat.checkSelfPermission(
                            getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_GRANTED) {
                        // You can use the API that requires the permission.
                        startActivityForResult(intent,PICK_IMAGE);
                    } else {
                        // You can directly ask for the permission.
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},CODE_CAMARA);
                    }
                } else {
                    // You can directly ask for the permission.
                    requestPermissions(new String[]{Manifest.permission.CAMERA},CODE_CAMARA);
                }


            }
        });

        spinner_cuentas=(Spinner) findViewById(R.id.Spinner_Cuentas);
        spinner_cuentas.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerCuentas));

        spinner_Categorias=(Spinner) findViewById(R.id.Spinner_Categorias);
        adapter=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerCategorias);
        spinner_Categorias.setAdapter(adapter);

        ToggleButton toggleButton=(ToggleButton)findViewById(R.id.toggleButton);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    spinnerCategorias.clear();
                    ListaCategoriaSelec=ListaCategoriaIngresos;
                    spinnerCategorias.addAll(spinnerCategoriasIngresos);
                    adapter.notifyDataSetChanged();
                }else{
                    spinnerCategorias.clear();
                    ListaCategoriaSelec=ListaCategoriaGastos;
                    spinnerCategorias.addAll(spinnerCategoriasGastos);
                    adapter.notifyDataSetChanged();
                }

            }
        });

        findViewById(R.id.button_CANCEL_FormularioRegistro2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Ticket=(ImageView) findViewById(R.id.imageView3);

        Ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(), ImageZoom.class);
                if(registro.getTicket()==null){
                    intent.putExtra("Imagen","NOIMAGEN");
                    startActivity(intent);
                }else {
                    String fileName = "myImage";//no .png or .jpg needed
                    String path;
                    try {
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        registro.getTicket().compress(Bitmap.CompressFormat.JPEG, 80, bytes);
                        FileOutputStream fo = openFileOutput(fileName, Context.MODE_PRIVATE);
                        fo.write(bytes.toByteArray());
                        // remember close file output
                        fo.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        fileName = null;
                    }
                    intent.putExtra("Imagen",fileName);
                    startActivity(intent);
                }
            }
        });

        spinner_Categorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int[] attr = {com.google.android.material.R.attr.colorOnBackground};
                TypedArray typedArray = obtainStyledAttributes(R.style.Theme_NeoSavings, attr);
                ((TextView) parent.getChildAt(0)).setTextColor(typedArray.getColor(0, Color.LTGRAY));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_cuentas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int[] attr = {com.google.android.material.R.attr.colorOnBackground};
                TypedArray typedArray = obtainStyledAttributes(R.style.Theme_NeoSavings, attr);
                ((TextView) parent.getChildAt(0)).setTextColor(typedArray.getColor(0, Color.LTGRAY));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public boolean CrearRegistro() throws ParseException {


        EditText editText = (EditText)findViewById(R.id.Gastos_Registro);
        registro.setCoste(editText.getText().toString());
        if(registro.getCoste().isEmpty()){
            registro.setCoste("0");
        }


        ToggleButton toggleButton=(ToggleButton)findViewById(R.id.toggleButton);
        String Tipo=(String)toggleButton.getText();
        registro.setGasto(Tipo.trim().toUpperCase().contains("GASTO"));

        editText=(EditText) findViewById(R.id.RegistroDescripcion);
        registro.setDescripcion(editText.getText().toString());

        editText=(EditText) findViewById(R.id.editTextDate);
        String fecha=editText.getText().toString();

        if(fecha.matches("^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/([12][0-9]{3})$")){
            registro.setFecha(new SimpleDateFormat("dd/MM/yyyy").parse(fecha));
        }else{
           /* AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Formato Invalido");
            builder.setMessage("El formato a seguir es dd/MM/yyyy");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create();
            builder.show();*/
            Toast.makeText(getBaseContext(), "Formato Fecha No Valido", Toast.LENGTH_SHORT).show();
            return false;
        }



        String text=(String)spinner_cuentas.getSelectedItem();

        if(text.equals("NO HAY CUENTAS")){
            Toast.makeText(getBaseContext(), "No se puede asignar un Registro si no hay ningún usuario", Toast.LENGTH_SHORT).show();
            return false;
        }

        spinner_Categorias=(Spinner) findViewById(R.id.Spinner_Categorias);
        int Pos=spinner_Categorias.getSelectedItemPosition();
        registro.setCategoria(spinnerCategorias.get(Pos));


        spinner_cuentas=(Spinner) findViewById(R.id.Spinner_Cuentas);
        Pos=spinner_cuentas.getSelectedItemPosition();
        Cuenta user=ListaCuentas.get(Pos);
        registro.setRegistroUserID(user.getUser().getUserID());

        mRepository.insertRegistro(registro);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==PICK_IMAGE && resultCode==RESULT_OK){
            Bitmap rotatedBitmap = null;
            ImageView imageView=(ImageView)findViewById(R.id.imageView3);
            registro.setTicket(BitmapFactory.decodeFile(currentPhotoPath));
            try {
                ExifInterface ei=new ExifInterface(currentPhotoPath);
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);


                switch(orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = rotateImage(registro.getTicket(), 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = rotateImage(registro.getTicket(), 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotatedBitmap = rotateImage(registro.getTicket(), 270);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        rotatedBitmap = registro.getTicket();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            registro.setTicket(rotatedBitmap);
            imageView.setImageBitmap(rotatedBitmap);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CODE_CAMARA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImageButton camera=(ImageButton) findViewById(R.id.imageButtonCamera);
                    camera.callOnClick();
                } else {
                    Toast.makeText(getBaseContext(),"No se pueden realizar fotos",Toast.LENGTH_SHORT).show();
                }
                break;
            // Aquí más casos dependiendo de los permisos
            // case OTRO_CODIGO_DE_PERMISOS...
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}

