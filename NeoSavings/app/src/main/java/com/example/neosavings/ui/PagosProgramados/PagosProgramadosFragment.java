package com.example.neosavings.ui.PagosProgramados;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.neosavings.R;
import com.example.neosavings.databinding.FragmentPagosprogramadosBinding;
import com.example.neosavings.ui.Database.UsuarioRepository;
import com.example.neosavings.ui.Formularios.Formulario_PagosProgramados;
import com.google.android.material.tabs.TabLayout;

public class PagosProgramadosFragment extends Fragment {

    private FragmentPagosprogramadosBinding binding;
    private PagoProgramadoFragment pagoProgramadoFragment;

    private UsuarioRepository mRepository;

    boolean SelectedGastos;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PagosProgramadosViewModel slideshowViewModel =
                new ViewModelProvider(this).get(PagosProgramadosViewModel.class);

        binding = FragmentPagosprogramadosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        root.findViewById(R.id.floatingActionButton_ADD_PagosProgramados).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), Formulario_PagosProgramados.class);
                intent.putExtra("CASO","CREAR");
                startActivity(intent);
            }
        });

        TabLayout tabLayout=root.findViewById(R.id.tabLayout);
        tabLayout.setOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        if(tab.getText().equals("Gastos")){
                            SelectedGastos=true;
                            mRepository=new UsuarioRepository(getContext());
                            pagoProgramadoFragment=new PagoProgramadoFragment(mRepository.getAllPagosProgramadosByGasto(true).blockingFirst());
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .setCustomAnimations(androidx.navigation.ui.R.animator.nav_default_enter_anim, androidx.navigation.ui.R.animator.nav_default_exit_anim,
                                            androidx.navigation.ui.R.animator.nav_default_pop_enter_anim,
                                            androidx.navigation.ui.R.animator.nav_default_pop_exit_anim)
                                    .replace(R.id.FrameLayoutListaPagosProgramados,pagoProgramadoFragment)
                                    .commit();
                        }else{
                            SelectedGastos=false;
                            mRepository=new UsuarioRepository(getContext());
                            pagoProgramadoFragment=new PagoProgramadoFragment(mRepository.getAllPagosProgramadosByGasto(false).blockingFirst());
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .setCustomAnimations(androidx.navigation.ui.R.animator.nav_default_enter_anim, androidx.navigation.ui.R.animator.nav_default_exit_anim,
                                            androidx.navigation.ui.R.animator.nav_default_pop_enter_anim,
                                            androidx.navigation.ui.R.animator.nav_default_pop_exit_anim)
                                    .replace(R.id.FrameLayoutListaPagosProgramados,pagoProgramadoFragment)
                                    .commit();
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        // ...
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        // ...
                    }
                }
        );


        mRepository=new UsuarioRepository(getContext());
        pagoProgramadoFragment=new PagoProgramadoFragment(mRepository.getAllPagosProgramadosByGasto(true).blockingFirst());
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameLayoutListaPagosProgramados,pagoProgramadoFragment)
                .commit();
        return root;
    }

    @Override
    public void onResume() {

        if(SelectedGastos) {
            mRepository=new UsuarioRepository(getContext());
            pagoProgramadoFragment=new PagoProgramadoFragment(mRepository.getAllPagosProgramadosByGasto(true).blockingFirst());
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(androidx.navigation.ui.R.animator.nav_default_enter_anim, androidx.navigation.ui.R.animator.nav_default_exit_anim,
                            androidx.navigation.ui.R.animator.nav_default_pop_enter_anim,
                            androidx.navigation.ui.R.animator.nav_default_pop_exit_anim)
                    .replace(R.id.FrameLayoutListaPagosProgramados, pagoProgramadoFragment)
                    .commit();
        }else {
            mRepository=new UsuarioRepository(getContext());
            pagoProgramadoFragment=new PagoProgramadoFragment(mRepository.getAllPagosProgramadosByGasto(false).blockingFirst());
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(androidx.navigation.ui.R.animator.nav_default_enter_anim, androidx.navigation.ui.R.animator.nav_default_exit_anim,
                            androidx.navigation.ui.R.animator.nav_default_pop_enter_anim,
                            androidx.navigation.ui.R.animator.nav_default_pop_exit_anim)
                    .replace(R.id.FrameLayoutListaPagosProgramados,pagoProgramadoFragment)
                    .commit();

        }
            super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}