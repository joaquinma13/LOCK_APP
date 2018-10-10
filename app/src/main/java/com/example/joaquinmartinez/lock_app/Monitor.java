package com.example.joaquinmartinez.lock_app;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import static android.content.Context.POWER_SERVICE;

public class Monitor extends BroadcastReceiver {
    private SharePreference preference;
    @Override
    public void onReceive(Context context, Intent intent) {
        preference = SharePreference.getInstance(context);
        preference.saveData("Run",false);
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
