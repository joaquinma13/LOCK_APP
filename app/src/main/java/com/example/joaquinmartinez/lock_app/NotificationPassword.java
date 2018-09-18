package com.example.joaquinmartinez.lock_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class NotificationPassword extends AppCompatDialogFragment{

    EditText username;
    EditText password;

    public interface NoticeDialogListener {
        void applyText(String username, String password);
    }

    NoticeDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " Error en la Implementacion");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_password, null);
        builder.setView(view)
                .setTitle("LOGIN")
                .setPositiveButton("ENTRAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String Username = username.getText().toString();
                        String Password = password.getText().toString();
                        mListener.applyText(Username,Password);
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        return builder.create();
    }


}
