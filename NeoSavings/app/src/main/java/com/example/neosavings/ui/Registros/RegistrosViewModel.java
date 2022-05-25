package com.example.neosavings.ui.Registros;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegistrosViewModel extends ViewModel {

    private static MutableLiveData<String> mText;

    public RegistrosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Registros fragment");
    }

    public static MutableLiveData<String> getText() {
        return mText;
    }
}