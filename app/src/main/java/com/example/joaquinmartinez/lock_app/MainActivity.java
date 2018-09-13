package com.example.joaquinmartinez.lock_app;


import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import static android.app.usage.UsageStatsManager.INTERVAL_DAILY;

public class MainActivity extends AppCompatActivity implements NotificationHandler.NoticeDialogListener{
    ImageView config;
    ImageView out;
    ImageView back;
    int mode;
    String Username;
    String Passsword;
    ConfigFragment configFragment;
    LockFragment lockFragment;
    private static Timer timer = new Timer();
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


        startService();

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
            if (Username.equals("MCollet") && Passsword.equals("12345") ){
                timer.cancel();
            }
            else
                Toast.makeText(getApplication(), "Petición Denegada!!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        return (keyCode == KeyEvent.KEYCODE_BACK ? true : super.onKeyDown(keyCode, event));
    }

    private void startService()
    {
        timer.scheduleAtFixedRate(new mainTask(), 0, 300);
    }

    private class mainTask extends TimerTask
    {
        public void run()
        {
            toastHandler.sendEmptyMessage(0);
        }
    }

    @SuppressLint("HandlerLeak")
    final Handler toastHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            String topPackageName;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
                long time = System.currentTimeMillis();
                List<UsageStats> stats = mUsageStatsManager.queryUsageStats(INTERVAL_DAILY, time - 1000 * 10, time);
                if (stats != null) {
                    SortedMap< Long, UsageStats > mySortedMap = new TreeMap< Long, UsageStats >();
                    for (UsageStats usageStats: stats) {
                        mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                    }
                    if (mySortedMap != null && !mySortedMap.isEmpty()) {
                        topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                        //System.out.println("Salida: "+ topPackageName);

                        if(topPackageName.contains("home") ||topPackageName.contains("launcher") || topPackageName.equals("com.mc.miga") || topPackageName.equals("com.example.joaquinmartinez.lock_app")
                                || topPackageName.equals("com.android.vending") )
                        {
                        }
                        else{
                            Intent i = new Intent(getApplication(), MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }


                    }
                }
            }
            else{
                //aqui va la parte donde pongo el metodo para detener verciones abajo de lolipop 5.1...
            }
        }
    };
}
