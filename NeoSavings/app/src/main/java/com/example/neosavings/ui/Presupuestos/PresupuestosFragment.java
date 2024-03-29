package com.example.neosavings.ui.Presupuestos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.neosavings.R;
import com.example.neosavings.ui.Formularios.Formulario_Presupuestos;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PresupuestosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PresupuestosFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    PresupuestoFragmentList presupuestoFragmentList;

    public PresupuestosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PresupuestosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PresupuestosFragment newInstance(String param1, String param2) {
        PresupuestosFragment fragment = new PresupuestosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: Rename and change types of parameters
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_presupuestos, container, false);
        view.findViewById(R.id.floatingActionButton_ADDPresupuesto).setOnClickListener(v -> {
            Intent intent=new Intent(getContext(), Formulario_Presupuestos.class);
            intent.putExtra("CASO","CREAR");
            startActivity(intent);
        });

        presupuestoFragmentList=new PresupuestoFragmentList();

        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(androidx.navigation.ui.R.animator.nav_default_enter_anim, androidx.navigation.ui.R.animator.nav_default_exit_anim,
                        androidx.navigation.ui.R.animator.nav_default_pop_enter_anim,
                        androidx.navigation.ui.R.animator.nav_default_pop_exit_anim)
                .replace(R.id.FrameLayoutListaPresupuestos,presupuestoFragmentList)
                .commit();

        return view;
    }

    @Override
    public void onResume() {
        presupuestoFragmentList=new PresupuestoFragmentList();

        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(androidx.navigation.ui.R.animator.nav_default_enter_anim, androidx.navigation.ui.R.animator.nav_default_exit_anim,
                        androidx.navigation.ui.R.animator.nav_default_pop_enter_anim,
                        androidx.navigation.ui.R.animator.nav_default_pop_exit_anim)
                .replace(R.id.FrameLayoutListaPresupuestos,presupuestoFragmentList)
                .commit();
        super.onResume();
    }
}