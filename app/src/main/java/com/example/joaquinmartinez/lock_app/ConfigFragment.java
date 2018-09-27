package com.example.joaquinmartinez.lock_app;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class ConfigFragment extends Fragment {

    ArrayList<AdapterApp.App> ListApp;
    RecyclerView recyclerView;
    PackageManager packageManager;


    public ConfigFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_config, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        packageManager = getContext().getPackageManager();
        recyclerView = view.findViewById(R.id.RecyclerApp);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ListApp = new ArrayList<>();
        getApps();
    }

    void getApps(){
        List<ApplicationInfo> packList = getActivity().getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        for (int i=0; i < packList.size(); i++)
        {
            AdapterApp.App app = new AdapterApp.App();
            ApplicationInfo packInfo = packList.get(i);
            if (((packInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) || packInfo.flags == 952745541 || packInfo.flags == -1463042363){
                String appName = packInfo.loadLabel(getActivity().getPackageManager()).toString();
                String appPack = packInfo.packageName;
                Drawable imageView = packInfo.loadIcon(packageManager);
                if (!appName.equals("")&& !appPack.equals("") && !appPack.equals("android")) {
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
        AdapterApp adapterApp = new AdapterApp(ListApp);
        recyclerView.setAdapter(adapterApp);
    }
}
