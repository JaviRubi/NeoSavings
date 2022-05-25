package com.example.neosavings.ui.PagosProgramados;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PagosProgramadosViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PagosProgramadosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is PagosProgramados fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}