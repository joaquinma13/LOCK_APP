package com.example.joaquinmartinez.lock_app;


import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import static android.app.usage.UsageStatsManager.INTERVAL_DAILY;


/**
 * A simple {@link Fragment} subclass.
 */
public class LockFragment extends Fragment {

    ArrayList<AdapterApp.App> ListApp;
    RecyclerView recyclerView;
    private SharePreference preference;
    PackageManager packageManager;



    public LockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lock, container, false);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        packageManager = getContext().getPackageManager();
        preference = SharePreference.getInstance(getContext());
        recyclerView = view.findViewById(R.id.RecyclerAppOpen);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ListApp = new ArrayList<>();
        getApps();
        /*
        Intent i = getPackageManager().getLaunchIntentForPackage("com.mc.miga");
        startActivity(i);
        Intent intent = new Intent(getActivity(), mFragmentFavorite.class);
        startActivity(intent);
        */
    }


    void getApps(){
        List<ApplicationInfo> packList = getActivity().getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        for (int i=0; i < packList.size(); i++)
        {
            AdapterApp.App app = new AdapterApp.App();
            ApplicationInfo packInfo = packList.get(i);
            if (((packInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) || packInfo.flags == 952745541 || packInfo.flags == -1463042363) {
                String appName = packInfo.loadLabel(getActivity().getPackageManager()).toString();
                String appPack = packInfo.packageName;
                Drawable imageView = packInfo.loadIcon(packageManager);
                if (!appName.equals("") && !appPack.equals("") && preference.getStrData("List_Lock").contains(appPack)) {
                    app.setName(appName);
                    app.setPakete(appPack);
                    app.setImage(imageView);
                    ListApp.add(app);

                }
            }
        }
        Collections.sort(ListApp, new Comparator<AdapterApp.App>() {
            @Override
            public int compare(AdapterApp.App app, AdapterApp.App t1) {
                return app.getName().compareTo(t1.getName());
            }
        });
        AdapterOpen adapterApp = new AdapterOpen(ListApp,getContext());
        recyclerView.setAdapter(adapterApp);
    }



}
