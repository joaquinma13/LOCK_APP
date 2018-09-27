package com.example.joaquinmartinez.lock_app;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.regex.Pattern;

import static android.app.usage.UsageStatsManager.INTERVAL_DAILY;

public class MyService extends Service {
    private static Timer timer = new Timer();
    private SharePreference preference;

    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    public void onCreate()
    {
        super.onCreate();
        startService();
        preference = SharePreference.getInstance(getApplicationContext());
    }

    private void startService()
    {
        timer.scheduleAtFixedRate(new mainTask(), 0, 100);
    }

    private class mainTask extends TimerTask
    {
        public void run()
        {
            toastHandler.sendEmptyMessage(0);
        }
    }

    public void onDestroy()
    {
        super.onDestroy();
    }

    @SuppressLint("HandlerLeak")
    final Handler toastHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            String topPackageName;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (preference.getBooData("Timer") == false){}
                else{
                    int flag = 0;
                String[] apps = new String[0];
                apps = preference.getStrData("List_Lock").split(Pattern.quote("~"));
                UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
                long time = System.currentTimeMillis();
                List<UsageStats> stats = mUsageStatsManager.queryUsageStats(INTERVAL_DAILY, time - 1000 * 10, time);
                if (stats != null) {
                    SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                    for (UsageStats usageStats : stats) {
                        mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                    }
                    if (mySortedMap != null && !mySortedMap.isEmpty()) {
                        topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                        for (int j = 0; j < apps.length; j++) {
                            if (topPackageName.equals(apps[j])) {
                                flag = 1;
                                break;
                            }
                        }
                        if (topPackageName.contains("home") || topPackageName.contains("launcher") || topPackageName.equals("com.example.joaquinmartinez.lock_app")|| topPackageName.equals("com.android.vending")) {

                        } else if (flag == 1) {
                            flag = 0;
                        }else {
                            if (topPackageName.contains("com.android.systemui"))
                                Toast.makeText(getApplication(), "Accion Denegada!!!", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(getApplication(), MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                    }
                }
            }
            }
            else{
                //aqui va la parte donde pongo el metodo para detener verciones abajo de lolipop 5.1...  "com.android.systemui"
            }
        }
    };
}

//com.android.chrome
//com.facebook.katana
