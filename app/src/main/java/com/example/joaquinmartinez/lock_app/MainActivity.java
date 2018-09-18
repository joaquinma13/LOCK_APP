package com.example.joaquinmartinez.lock_app;


import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.regex.Pattern;

import static android.app.usage.UsageStatsManager.INTERVAL_DAILY;

public class MainActivity extends AppCompatActivity implements NotificationPassword.NoticeDialogListener{
    ImageView config;
    ImageView out;
    ImageView back;
    ImageView enter;
    int mode;
    String Username;
    String Passsword;
    ConfigFragment configFragment;
    LockFragment lockFragment;
    private static Timer timer = new Timer();
    int flag;
    private SharePreference preference;
    String[] apps = new String[0];

    private NotificationPassword notificationHandler;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        config = findViewById(R.id.Config);
        out = findViewById(R.id.Out);
        back = findViewById(R.id.Back);
        enter = findViewById(R.id.Enter);
        lockFragment = new LockFragment();
        configFragment = new ConfigFragment();
        preference = SharePreference.getInstance(this);
        apps = preference.getStrData("List_Lock").split(Pattern.quote("~"));
        startService(new Intent(this, MyService.class));

        try {
            PackageManager packageManager = this.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) this.getSystemService(Context.APP_OPS_SERVICE);
            mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
        }catch (PackageManager.NameNotFoundException e) {}

        if (mode != 0){
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, "Se necesitan Permisos!!!", Toast.LENGTH_SHORT).show();
        }
        else if(mode == 0 ){
            getSupportFragmentManager().beginTransaction().add(R.id.content_frame,lockFragment).commit();
        }

        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoticeDialog();
                flag = 1;
            }
        });

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoticeDialog();
                flag = 2;

            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoticeDialog();
                flag = 3;

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,lockFragment);
                ft.commit();
                config.setVisibility(View.VISIBLE);
                back.setVisibility(View.GONE);
            }
        });
    }



    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new NotificationPassword();
        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
    }



    @Override
    public void applyText(String username, String password) {
        Username = username;
        Passsword = password;
        if (flag == 1){
            if (Username.equals("MC Collect") && Passsword.equals("12345") ){
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,configFragment);
                ft.commit();
                config.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);
            }
            else
                Toast.makeText(getApplication(), "Acceso Denegado!!!", Toast.LENGTH_SHORT).show();
        }else if (flag == 2){
            if (Username.equals("MC Collect") && Passsword.equals("12345") ){
                Toast.makeText(getApplication(), "Servicio Detenido!!!", Toast.LENGTH_SHORT).show();
                out.setVisibility(View.GONE);
                enter.setVisibility(View.VISIBLE);
            }
            else
                Toast.makeText(getApplication(), "Petición Denegada!!!", Toast.LENGTH_SHORT).show();
        }else if(flag == 3){
            if (Username.equals("MC Collect") && Passsword.equals("12345") ){
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0 );
                Toast.makeText(getApplication(), "Servicio Iniciado!!!", Toast.LENGTH_SHORT).show();
                out.setVisibility(View.VISIBLE);
                enter.setVisibility(View.GONE);
            }
            else
                Toast.makeText(getApplication(), "Petición Denegada!!!", Toast.LENGTH_SHORT).show();
        }
    }

}
