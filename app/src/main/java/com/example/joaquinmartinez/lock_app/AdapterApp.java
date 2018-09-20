package com.example.joaquinmartinez.lock_app;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;


public class AdapterApp extends RecyclerView.Adapter<AdapterApp.ViewHolderData> {


    ArrayList<App> ListApp;
    private SharePreference preference;

    public AdapterApp(ArrayList<App> listApp) {
        ListApp = listApp;
    }

    @Override
    public ViewHolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        preference = SharePreference.getInstance(parent.getContext());
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_apps, null, false);
        return new ViewHolderData(v);
    }

    @Override
    public void onBindViewHolder(ViewHolderData holder, int position) {
        final String temporal;
        final App item = ListApp.get(position);

        holder.setIsRecyclable(false);
        holder.NameApp.setText(item.getName());
        if (preference.getStrData("List_Lock").contains(item.getName()))
            holder.SwOnOff.setChecked(true);
        else
            holder.SwOnOff.setChecked(false);


        holder.SwOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ListApp.size();
    }

    public class ViewHolderData extends RecyclerView.ViewHolder {

        TextView NameApp;
        Switch SwOnOff;

        public ViewHolderData(View itemView) {
            super(itemView);
            NameApp = (TextView) itemView.findViewById(R.id.NameApp);
            SwOnOff = itemView.findViewById(R.id.SwOnOff);
        }
    }

    public static class App {
        private String name;
        private boolean Flag;

        public boolean isFlag() {
            return Flag;
        }

        public void setFlag(boolean flag) {
            Flag = flag;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
