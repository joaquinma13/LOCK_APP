package com.example.joaquinmartinez.lock_app;


import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.regex.Pattern;

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

        PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(MainActivity.this,Home.class ); // launcher activity specified in manifest file as <category android:name="android.intent.category.LAUNCHER" />
        p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

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
            PackageManager packageManager = getPackageManager();
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
                stopService(new Intent(this, MyService.class));
                preference.saveData("Timer",false);
                out.setVisibility(View.GONE);
                enter.setVisibility(View.VISIBLE);
                PackageManager p = getPackageManager();
                ComponentName componentName = new ComponentName(this, Home.class);
                p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            }
            else
                Toast.makeText(getApplication(), "Petición Denegada!!!", Toast.LENGTH_SHORT).show();
        }else if(flag == 3){
            if (Username.equals("MC Collect") && Passsword.equals("12345") ){
                PackageManager p = getPackageManager();
                ComponentName componentName = new ComponentName(MainActivity.this,Home.class ); // launcher activity specified in manifest file as <category android:name="android.intent.category.LAUNCHER" />
                p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                preference.saveData("Timer",true);
                out.setVisibility(View.VISIBLE);
                enter.setVisibility(View.GONE);
            }
            else
                Toast.makeText(getApplication(), "Petición Denegada!!!", Toast.LENGTH_SHORT).show();
        }
    }



}
