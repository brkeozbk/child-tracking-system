package com.example.vbs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

public class kullanicisozlesmesi extends AppCompatDialogFragment {
    private kullanicisozlesmesi listener;
    private View view;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
         view = inflater.inflate(R.layout.kullanicisozlesmesi, null);
        builder.setView(view)
                .setTitle("Dikkatli Okuyunuz!")
                .setNegativeButton("Anladım", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        return builder.create();

    }

}