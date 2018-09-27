package com.example.joaquinmartinez.lock_app;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterOpen extends RecyclerView.Adapter<AdapterOpen.ViewHolderData> {

    ArrayList<AdapterApp.App> ListApp;
    private SharePreference preference;
    private Context context;

    public AdapterOpen(ArrayList<AdapterApp.App> listApp, Context context) {
        ListApp = listApp;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        preference = SharePreference.getInstance(parent.getContext());
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_launcher, null, false);
        return new ViewHolderData(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderData holder, int position) {
        final AdapterApp.App item = ListApp.get(position);
        holder.NameApp.setText(item.getName());
        holder.LogoApp.setImageDrawable(item.getImage());

        holder.relPaquete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = context.getPackageManager().getLaunchIntentForPackage(item.getPakete());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ListApp.size();
    }

    public class ViewHolderData extends RecyclerView.ViewHolder {

        private TextView NameApp;
        private RelativeLayout relPaquete;
        private ImageView LogoApp;

        public ViewHolderData(View itemView) {
            super(itemView);
            NameApp = itemView.findViewById(R.id.AppOpen);
            relPaquete =  itemView.findViewById(R.id.relPaquete); //LogoAppL
            LogoApp = itemView.findViewById(R.id.LogoAppL);
        }
    }
}
