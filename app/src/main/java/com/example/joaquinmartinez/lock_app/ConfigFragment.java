package com.example.joaquinmartinez.lock_app;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConfigFragment extends Fragment {

    ArrayList<AdapterApp.App> ListApp;
    RecyclerView recyclerView;


    public ConfigFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("Estoy inflando Fragment");
        return inflater.inflate(R.layout.fragment_config, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.RecyclerApp);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ListApp = new ArrayList<>();
        List<PackageInfo> packList = getActivity().getPackageManager().getInstalledPackages(0);
        Log.e("TAMAÑO ", String.valueOf(packList.size()));
        for (int i=0; i < packList.size(); i++)
        {
            AdapterApp.App app = new AdapterApp.App("");
            PackageInfo packInfo = packList.get(i);
            if (  (packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {
                String appName = packInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                if (!appName.equals("")){
                    Log.e("APP № " + Integer.toString(i), appName);
                    app.setName(appName);
                    ListApp.add(app);
                }
            }
        }
        AdapterApp adapterApp = new AdapterApp(ListApp);
        recyclerView.setAdapter(adapterApp);

    }



}
