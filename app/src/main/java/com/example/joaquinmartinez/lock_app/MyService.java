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
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import static android.app.usage.UsageStatsManager.INTERVAL_DAILY;

public class MyService extends Service {
    private static Timer timer = new Timer();
    public Boolean userAuth = false;
    private Context ctx;
    public String pActivity="";

    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    public void onCreate()
    {
        super.onCreate();
        ctx = this;
        startService();
    }

    private void startService()
    {
        timer.scheduleAtFixedRate(new mainTask(), 0, 500);
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
        Toast.makeText(this, "Service Stopped ...", Toast.LENGTH_SHORT).show();
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
