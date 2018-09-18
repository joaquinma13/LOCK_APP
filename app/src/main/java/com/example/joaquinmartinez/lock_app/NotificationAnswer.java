package com.example.joaquinmartinez.lock_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class NotificationAnswer extends DialogFragment {

    Button aceptar;
    Button cancelar;


    public interface OnInputSelected{
        void sendInput(boolean input);
    }
    public OnInputSelected mOnInputSelected;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_answer, container, false);

        aceptar = view.findViewById(R.id.btnAceptar);
        cancelar = view.findViewById(R.id.btnCancelar);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnInputSelected.sendInput(true);
                getDialog().dismiss();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnInputSelected.sendInput(false);
                getDialog().dismiss();
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputSelected = (OnInputSelected) getTargetFragment();
        }catch (ClassCastException e){
            Log.e("gfdsjhfgdskj", "onAttach: ClassCastException : " + e.getMessage() );
        }
    }

}
