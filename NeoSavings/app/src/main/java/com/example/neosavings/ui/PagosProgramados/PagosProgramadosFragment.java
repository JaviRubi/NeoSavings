package com.example.neosavings.ui.PagosProgramados;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.neosavings.databinding.FragmentPagosprogramadosBinding;

public class PagosProgramadosFragment extends Fragment {

    private FragmentPagosprogramadosBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PagosProgramadosViewModel slideshowViewModel =
                new ViewModelProvider(this).get(PagosProgramadosViewModel.class);

        binding = FragmentPagosprogramadosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}