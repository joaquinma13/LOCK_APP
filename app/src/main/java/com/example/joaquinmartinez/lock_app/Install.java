package com.example.joaquinmartinez.lock_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Install extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            Toast.makeText(context, " onReceive !!!! PACKAGE_ADDED",
                    Toast.LENGTH_LONG).show();
            final String packageName = intent.getData().getSchemeSpecificPart();
                System.out.println("out: "+ packageName);



            //com.domobile.applock
        }
    }
}
