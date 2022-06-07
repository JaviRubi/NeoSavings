package com.example.neosavings.ui.AlertsDialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.neosavings.R;


public class AlertDialogFel extends DialogFragment {

    public AlertDialogFel(){
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View myView = inflater.inflate(R.layout.alertdialog_felicitacion, null);

        builder.setView(myView)
                .setPositiveButton(R.string.String_OK, (dialog, id) -> dialog.cancel());
        builder.setCancelable(true);
        return builder.create();
    }

}
