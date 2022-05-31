package com.example.neosavings.ui.Registros;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.neosavings.R;
import com.example.neosavings.databinding.FragmentRegistrosBinding;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Modelo.Categoria;
import com.example.neosavings.ui.Modelo.Cuenta;
import com.example.neosavings.ui.Modelo.Registro;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;

public class RegistrosFragment extends Fragment {

    private FragmentRegistrosBinding binding;
    private RegistroFragment Lista;
    UsuarioRepository mRepository;
    private List<Cuenta> ListaCuentas;
    private List<Categoria> ListaCategoria;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RegistrosViewModel galleryViewModel =
                new ViewModelProvider(this).get(RegistrosViewModel.class);

        binding = FragmentRegistrosBinding.inflate(inflater, container, false);
        Lista=new RegistroFragment();




        View root = binding.getRoot();

        mRepository = new UsuarioRepository(getContext());
        ArrayList<String> spinnerCuentas = new ArrayList<>();
        ArrayList<String> spinnerCategorias = new ArrayList<>();

        Flowable<List<Cuenta>> allUsers = mRepository.getAllCuentasFW();
        ListaCuentas=allUsers.blockingFirst();

        Flowable<List<Categoria>> allCategorias=mRepository.getALLCategorias();
        ListaCategoria=allCategorias.blockingFirst();

        spinnerCuentas.clear();
        spinnerCategorias.clear();

        spinnerCategorias.add("Todas");
        if(ListaCategoria!=null) {
            if(ListaCategoria.size()!=0){
                for (Categoria c : ListaCategoria) {
                    spinnerCategorias.add(c.getCategoría());
                }
            }else{
                spinnerCategorias.add("NO HAY CATEGORIAS");
            }
        }


        spinnerCuentas.add("Todas");
        if(ListaCuentas!=null) {
            if(ListaCuentas.size()!=0) {
                for (Cuenta u : ListaCuentas) {
                    spinnerCuentas.add(u.getUser().getUsuario());
                }
            }else{
                spinnerCuentas.add("NO HAY CUENTAS");
            }
        }


        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameLayout2,Lista)
                .commit();

        mRepository=new UsuarioRepository(getContext());
        binding.editTextDate3.setText(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
        binding.imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String fechaIni=binding.editTextDate2.getText().toString();
                String fechaFin=binding.editTextDate3.getText().toString();
                Date fechaIniDT=null;
                Date fechaFinDT = null;

                if(fechaIni.matches("\\d{1,2}/\\d{1,2}/\\d{4}")){
                    try {
                        fechaIniDT=new SimpleDateFormat("dd/MM/yyyy").parse(fechaIni);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    fechaIniDT=new Date(Long.MIN_VALUE);
                }

                if(fechaFin.matches("\\d{1,2}/\\d{1,2}/\\d{4}")){
                    try {
                        fechaFinDT=new SimpleDateFormat("dd/MM/yyyy").parse(fechaFin);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    fechaFinDT=Calendar.getInstance().getTime();
                }



                Flowable<List<Registro>> RegistrosBlock=mRepository.getAllRegistrosbyFecha(fechaIniDT,fechaFinDT);
                List<Registro> ListaRegistros=RegistrosBlock.blockingFirst();
                if(ListaRegistros==null){
                    ListaRegistros=new ArrayList<>();
                }

                List<Registro> AuxList=new ArrayList<>();
                if(binding.spinnerCategorias.getSelectedItemPosition()!=0){
                    Categoria categoria=ListaCategoria.get(binding.spinnerCategorias.getSelectedItemPosition()-1);
                    AuxList.addAll(ListaRegistros);
                    for (Registro r:AuxList){
                        if(!r.getCategoria().equals(categoria.getCategoría())){
                            ListaRegistros.remove(r);
                        }
                    }
                }

                if(binding.spinnerCuentas.getSelectedItemPosition()!=0){
                    Cuenta cuenta=ListaCuentas.get(binding.spinnerCuentas.getSelectedItemPosition()-1);
                    AuxList.clear();
                    AuxList.addAll(ListaRegistros);
                    for (Registro r: AuxList){
                        if(r.getRegistroUserID()!=cuenta.getUser().getUserID()){
                            ListaRegistros.remove(r);
                        }
                    }
                }
                Lista.setRegistros(ListaRegistros);
                Lista.refresh();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.FrameLayout2,Lista)
                        .commit();

            }
        });

        Spinner spinner_cuentas = binding.spinnerCuentas;
        spinner_cuentas.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerCuentas));

        Spinner spinner_Categorias = binding.spinnerCategorias;
        spinner_Categorias.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerCategorias));


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}