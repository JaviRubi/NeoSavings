package com.example.neosavings.ui.Objetivos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.neosavings.R;
import com.example.neosavings.ui.Formularios.Formulario_Objetivos;
import com.example.neosavings.ui.Modelo.Objetivo;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ObjetivosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ObjetivosFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Objetivo EstadoActivo;
    private Objetivo EstadoPausado;
    private Objetivo EstadoConcluido;

    private String LastEstado;
    public ObjetivosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ObjetivosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ObjetivosFragment newInstance(String param1, String param2) {
        ObjetivosFragment fragment = new ObjetivosFragment();
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
        View view=inflater.inflate(R.layout.fragment_objetivos, container, false);

        TabLayout tabLayout=view.findViewById(R.id.tabLayoutObjetivos);
        EstadoActivo=new Objetivo();
        EstadoPausado=new Objetivo();
        EstadoConcluido=new Objetivo();
        EstadoActivo.setEstadoActivo();
        EstadoPausado.setEstadoPausado();
        EstadoConcluido.setEstadoConcluido();
        LastEstado=EstadoActivo.getEstado();

        view.findViewById(R.id.floatingActionButton5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), Formulario_Objetivos.class);
                intent.putExtra("CASO","CREAR");
                startActivity(intent);
            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ObjetivosFragmentList objetivosFragmentList = null;
                if(tab.getText().equals("ACTIVOS")){
                    LastEstado=EstadoActivo.getEstado();
                    objetivosFragmentList=new ObjetivosFragmentList(EstadoActivo.getEstado());


                }else if(tab.getText().equals("PAUSADOS")){
                    LastEstado=EstadoPausado.getEstado();
                    objetivosFragmentList=new ObjetivosFragmentList(LastEstado);

                }else{
                    LastEstado=EstadoConcluido.getEstado();
                    objetivosFragmentList=new ObjetivosFragmentList(LastEstado);

                }

                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(androidx.navigation.ui.R.animator.nav_default_enter_anim, androidx.navigation.ui.R.animator.nav_default_exit_anim,
                                androidx.navigation.ui.R.animator.nav_default_pop_enter_anim,
                                androidx.navigation.ui.R.animator.nav_default_pop_exit_anim)
                        .replace(R.id.FrameLayoutObjetivos,objetivosFragmentList)
                        .commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    @Override
    public void onResume() {

        ObjetivosFragmentList objetivosFragmentList = new ObjetivosFragmentList(LastEstado);

        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(androidx.navigation.ui.R.animator.nav_default_enter_anim, androidx.navigation.ui.R.animator.nav_default_exit_anim,
                        androidx.navigation.ui.R.animator.nav_default_pop_enter_anim,
                        androidx.navigation.ui.R.animator.nav_default_pop_exit_anim)
                .replace(R.id.FrameLayoutObjetivos,objetivosFragmentList)
                .commit();
        super.onResume();
    }
}