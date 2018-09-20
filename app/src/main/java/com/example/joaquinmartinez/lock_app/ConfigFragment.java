package com.example.joaquinmartinez.lock_app;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConfigFragment extends Fragment {

    private SharePreference preference;
    private String nom;
    private TextView textView;
    ArrayList<AdapterApp.App> ListApp;
    RecyclerView recyclerView;


    public ConfigFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_config, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preference = SharePreference.getInstance(getContext());
        recyclerView = view.findViewById(R.id.RecyclerApp);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ListApp = new ArrayList<>();
        getApps();
    }

    void getApps(){
        List<PackageInfo> packList = getActivity().getPackageManager().getInstalledPackages(0);
        for (int i=0; i < packList.size(); i++)
        {
            AdapterApp.App app = new AdapterApp.App();
            app.setFlag(false);
            PackageInfo packInfo = packList.get(i);
            if (  (packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {
                String appName = packInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                if (!appName.equals("")){

                    app.setName(appName);
                    ListApp.add(app);
                }
            }
        }
        AdapterApp adapterApp = new AdapterApp(ListApp);
        recyclerView.setAdapter(adapterApp);
    }

    /*
    @Override
    public void sendInput(boolean input) {
        if (input){
            if (preference.getStrData("List_Lock").contains(nom)){
                textView.setTextColor(Color.RED);
                String aux = preference.getStrData("List_Lock").replace("~"+nom,"");
                preference.saveData("List_Lock",aux);
                System.out.println(preference.getStrData("List_Lock"));
            }else {
                preference.saveData("List_Lock",preference.getStrData("List_Lock")+"~"+nom);
                System.out.println(preference.getStrData("List_Lock"));
                textView.setTextColor(Color.GREEN);
            }

        }

    }
    */
}
