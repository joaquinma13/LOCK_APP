package com.example.joaquinmartinez.lock_app;

import android.annotation.SuppressLint;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static android.app.usage.UsageStatsManager.INTERVAL_DAILY;

public class MyService extends Service {

    MyTask myTask;
    private boolean cent;

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Servicio creado!", Toast.LENGTH_SHORT).show();
        myTask = new MyTask();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myTask.execute();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Servicio destruído!", Toast.LENGTH_SHORT).show();
        cent = false;
        myTask.cancel(true);
    }



    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cent = true;
        }

        @Override
        protected String doInBackground(String... params) {
            while (cent){
                try {
                    publishProgress();
                    // Stop 5s
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            toastHandler.sendEmptyMessage(0);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            cent = false;
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
                System.out.println("tiempo: " + time);
                List < UsageStats > stats = mUsageStatsManager.queryUsageStats(INTERVAL_DAILY, time - 1000 * 10, time);
                if (stats != null) {
                    SortedMap < Long, UsageStats > mySortedMap = new TreeMap < Long, UsageStats > ();
                    for (UsageStats usageStats: stats) {
                        mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                    }
                    if (mySortedMap != null && !mySortedMap.isEmpty()) {
                        topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                        System.out.println("Salida: "+ topPackageName);

                        if(topPackageName.contains("launcher") || topPackageName.equals("com.mc.miga") || topPackageName.equals("com.example.joaquinmartinez.lock_app")
                                || topPackageName.equals("com.android.vending")  )
                        {
                            Log.e("Permission ", "TE DOY CHANCE WE");
                        }
                        else{
                            Log.e("Denied ", topPackageName);
                            Intent i = new Intent(MyService.this, MainActivity.class);
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




//http://codeworld55.blogspot.com/2015/10/how-to-create-android-app-lock.html#comment-form

//com.lenovo.xlauncher
//com.android.vending -> store
//com.android.settings -> configuracion
//com.miui.home -> inicio lanzador
