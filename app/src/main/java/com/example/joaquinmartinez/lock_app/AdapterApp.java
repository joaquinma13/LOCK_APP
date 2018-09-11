package com.example.joaquinmartinez.lock_app;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class AdapterApp extends RecyclerView.Adapter<AdapterApp.ViewHolderData> {


    ArrayList<App> ListApp;

    public AdapterApp(ArrayList<App> listApp) {
        ListApp = listApp;
    }

    @Override
    public ViewHolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_apps, null, false);
        return new ViewHolderData(v);
    }

    @Override
    public void onBindViewHolder(ViewHolderData holder, int position) {
        App item = ListApp.get(position);
        holder.NameApp.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return ListApp.size();
    }

    public class ViewHolderData extends RecyclerView.ViewHolder {

        TextView NameApp;

        public ViewHolderData(View itemView) {
            super(itemView);
            NameApp = (TextView) itemView.findViewById(R.id.NameApp);


        }
    }

    public static class App {
        private String name;

        public App(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
