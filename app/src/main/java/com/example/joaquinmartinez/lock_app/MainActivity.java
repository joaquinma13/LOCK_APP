package com.example.joaquinmartinez.lock_app;


import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NotificationHandler.NoticeDialogListener{
    ImageView config;
    ImageView out;
    ImageView back;
    int mode;
    String Username;
    String Passsword;
    ConfigFragment configFragment;
    LockFragment lockFragment;
    boolean flag;

    private  NotificationHandler notificationHandler;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        config = findViewById(R.id.Config);
        out = findViewById(R.id.Out);
        back = findViewById(R.id.Back);
        lockFragment = new LockFragment();
        configFragment = new ConfigFragment();


        /*
        String deviceName = android.os.Build.MODEL;
        String deviceMan = android.os.Build.MANUFACTURER;
        System.out.println("FABRICANTE: "+ deviceMan + "MODELO: "+ deviceName);
        */



        List<PackageInfo> packList = getPackageManager().getInstalledPackages(0);
        for (int i=0; i < packList.size(); i++)
        {
            PackageInfo packInfo = packList.get(i);
            if (  (packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {
                String appName = packInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                Log.e("APP № " + Integer.toString(i), appName);
            }
        }

        try {
            PackageManager packageManager = this.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) this.getSystemService(Context.APP_OPS_SERVICE);
            mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
        }catch (PackageManager.NameNotFoundException e) {}
        System.out.println("valor mode: "+ mode);

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
                flag = true;
            }
        });

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoticeDialog();
                flag = false;
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
        //com.hancom.androidpc.launcher.shared
    }



    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new NotificationHandler();
        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
    }


    @Override
    public void applyText(String username, String password) {
        Username = username;
        Passsword = password;
        if (flag){
            if (Username.equals("MCollet") && Passsword.equals("12345") ){
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,configFragment);
                ft.commit();
                config.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);
            }
            else
                Toast.makeText(getApplication(), "Acceso Denegado!!!", Toast.LENGTH_SHORT).show();
        }else{
            if (Username.equals("MCollet") && Passsword.equals("12345") )
                stopService(new Intent(getApplication(), MyService.class));
            else
                Toast.makeText(getApplication(), "Petición Denegada!!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        return (keyCode == KeyEvent.KEYCODE_BACK ? true : super.onKeyDown(keyCode, event));
    }
}
