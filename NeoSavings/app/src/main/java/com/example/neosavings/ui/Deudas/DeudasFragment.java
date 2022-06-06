package com.example.neosavings.ui.Deudas;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.neosavings.R;
import com.example.neosavings.ui.Formularios.Formulario_Deudas;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeudasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeudasFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TabLayout tabLayout;
    Boolean Selected=true;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DeudasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeudasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeudasFragment newInstance(String param1, String param2) {
        DeudasFragment fragment = new DeudasFragment();
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

        View view=inflater.inflate(R.layout.fragment_deudas, container, false);

        DeudaFragment deudaFragment=new DeudaFragment(true);

        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(androidx.navigation.ui.R.animator.nav_default_enter_anim, androidx.navigation.ui.R.animator.nav_default_exit_anim,
                        androidx.navigation.ui.R.animator.nav_default_pop_enter_anim,
                        androidx.navigation.ui.R.animator.nav_default_pop_exit_anim)
                .replace(R.id.FrameLayoutDeudas,deudaFragment)
                .commit();

        tabLayout=view.findViewById(R.id.tabLayout2);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().equals("DEUDAS")){
                    Selected=true;
                    DeudaFragment deudaFragment=new DeudaFragment(true);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(androidx.navigation.ui.R.animator.nav_default_enter_anim, androidx.navigation.ui.R.animator.nav_default_exit_anim,
                                    androidx.navigation.ui.R.animator.nav_default_pop_enter_anim,
                                    androidx.navigation.ui.R.animator.nav_default_pop_exit_anim)
                            .replace(R.id.FrameLayoutDeudas,deudaFragment)
                            .commit();
                }else{
                    Selected=false;

                    DeudaFragment deudaFragment=new DeudaFragment(false);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(androidx.navigation.ui.R.animator.nav_default_enter_anim, androidx.navigation.ui.R.animator.nav_default_exit_anim,
                                    androidx.navigation.ui.R.animator.nav_default_pop_enter_anim,
                                    androidx.navigation.ui.R.animator.nav_default_pop_exit_anim)
                            .replace(R.id.FrameLayoutDeudas,deudaFragment)
                            .commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        view.findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), Formulario_Deudas.class);
                intent.putExtra("CASO","CREAR");
                startActivity(intent);
            }
        });
        return view;

    }

    @Override
    public void onResume() {

        DeudaFragment deudaFragment=new DeudaFragment(Selected);

        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(androidx.navigation.ui.R.animator.nav_default_enter_anim, androidx.navigation.ui.R.animator.nav_default_exit_anim,
                        androidx.navigation.ui.R.animator.nav_default_pop_enter_anim,
                        androidx.navigation.ui.R.animator.nav_default_pop_exit_anim)
                .replace(R.id.FrameLayoutDeudas,deudaFragment)
                .commit();

        super.onResume();
    }
}