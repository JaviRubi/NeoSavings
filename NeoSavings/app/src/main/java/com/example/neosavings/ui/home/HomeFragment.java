package com.example.neosavings.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.neosavings.R;
import com.example.neosavings.ui.Adapters.RegistroFragment;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Formularios.Formulario_Cuentas;
import com.example.neosavings.ui.Formularios.Formulario_Registros;
import com.example.neosavings.ui.Modelo.Cuenta;
import com.example.neosavings.ui.Modelo.Registro;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<Cuenta> ListaCuentas;
    private List<String> spinnerCuentas;
    private Spinner spinner_cuentas;
    private Cuenta cuenta;
    private List<Registro> ListaRegistros;
    private TextView SaldoActual;
    private int posicion;
    private RegistroFragment Lista;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        view.findViewById(R.id.AddRegistro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getContext(), Formulario_Registros.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.Anadir_Cuenta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getContext(), Formulario_Cuentas.class);
                long extra=-1;
                intent.putExtra("UserID",extra);
                startActivity(intent);
            }
        });

        SaldoActual=(TextView)view.findViewById(R.id.textView_SaldoActual);
        UsuarioRepository mRepository = new UsuarioRepository(getContext());
        Flowable<List<Cuenta>> allUsers = mRepository.getAllCuentasFW();
        ListaCuentas=allUsers.blockingFirst();

        spinnerCuentas=new ArrayList<>();
        spinnerCuentas.clear();
        if(ListaCuentas!=null) {
            if(ListaCuentas.size()!=0) {
                for (Cuenta u : ListaCuentas) {
                    spinnerCuentas.add(u.getUser().getUsuario());
                }
                cuenta=ListaCuentas.get(0);
                SaldoActual.setText(String.valueOf(cuenta.getSaldoActual()));


            }else{
                spinnerCuentas.add("NO HAY CUENTAS");
            }
        }

        spinner_cuentas=(Spinner) view.findViewById(R.id.spinner);
        spinner_cuentas.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,spinnerCuentas));
        spinner_cuentas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int Pos=spinner_cuentas.getSelectedItemPosition();
                if(!ListaCuentas.isEmpty()){
                    cuenta=ListaCuentas.get(Pos);
                    ListaRegistros=cuenta.getRegistros();

                    DecimalFormat formato=new DecimalFormat("#,###.### €");


                    SaldoActual.setText(formato.format( cuenta.getSaldoActual()));
                    if(cuenta.getSaldoActual()<0){
                        SaldoActual.setTextColor(Color.RED);
                    }else{
                        SaldoActual.setTextColor(Color.GREEN);
                    }

                    Lista.setRegistros(cuenta.getRegistros());
                    Lista.refresh();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Lista=new RegistroFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameListaRegistros,Lista)
                .commit();




        return view;

    }

    @Override
    public void onResume() {
        actualizarSpinner();
        super.onResume();
    }

    public void actualizarSpinner(){
        UsuarioRepository mRepository = new UsuarioRepository(getContext());
        Flowable<List<Cuenta>> allUsers = mRepository.getAllCuentasFW();
        ListaCuentas=allUsers.blockingFirst();

        spinnerCuentas=new ArrayList<>();
        spinnerCuentas.clear();
        if(ListaCuentas!=null) {
            if(ListaCuentas.size()!=0) {
                for (Cuenta u : ListaCuentas) {
                    spinnerCuentas.add(u.getUser().getUsuario());
                }
            }else{
                spinnerCuentas.add("NO HAY CUENTAS");
            }
        }
        spinner_cuentas.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,spinnerCuentas));
    }
}