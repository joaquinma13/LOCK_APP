package com.example.joaquinmartinez.lock_app;


import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Pattern;

import static android.app.usage.UsageStatsManager.INTERVAL_DAILY;

public class MyService extends Service {

    MiTareaAsincrona myTask;
    private SharePreference preference;


    private static final String TAG = "BackgroundSoundService";

    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "onBind()" );
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        preference = SharePreference.getInstance(getApplicationContext());
        myTask = new MiTareaAsincrona();
        Log.i(TAG, "onCreate() , service started...");

    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        myTask.execute();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        myTask.cancel(true);
        Toast.makeText(this, "Service stopped...", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onCreate() , service stopped...");
    }

    @Override
    public void onLowMemory() {
        Log.i(TAG, "onLowMemory()");
    }


    class MiTareaAsincrona extends AsyncTask<String, String, String> {

        private boolean cent;

        @Override
        protected String doInBackground(String... params) {
            while (cent){
                try {
                    publishProgress();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(String... values) {String topPackageName;
            String[] apps = new String[0];
            int flag = 0;
            apps = preference.getStrData("List_Lock").split(Pattern.quote("~"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
                        System.out.println();
                        if (topPackageName.contains("home") || topPackageName.contains("launcher") || topPackageName.equals("com.example.joaquinmartinez.lock_app")|| topPackageName.equals("com.android.vending") || topPackageName.contains("com.android.systemui")) {
                            if (topPackageName.contains("com.android.systemui"))
                                Toast.makeText(getApplication(), "Accion Denegada!!!", Toast.LENGTH_SHORT).show();

                        } else if (flag == 1) {
                            flag = 0;
                        }else {
                            Intent i = new Intent(getApplication(), MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                    }
                }
            }

        }

        @Override
        protected void onPreExecute() {
            cent = true;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            cent = false;
        }
    }

}
