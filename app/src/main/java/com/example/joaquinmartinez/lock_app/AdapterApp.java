package com.example.joaquinmartinez.lock_app;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class AdapterApp extends RecyclerView.Adapter<AdapterApp.ViewHolderData>   {
    ArrayList<App> ListApp;
    private SharePreference preference;

    public AdapterApp(ArrayList<AdapterApp.App> listApp) {
        ListApp = listApp;
    }

    @Override
    public AdapterApp.ViewHolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        preference = SharePreference.getInstance(parent.getContext());
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_apps, null, false);
        return new AdapterApp.ViewHolderData(v);
    }

    @Override
    public void onBindViewHolder(AdapterApp.ViewHolderData holder, int position) {
        final String temporal;
        final AdapterApp.App item = ListApp.get(position);

        holder.setIsRecyclable(false);
        holder.NameApp.setText(item.getName());
        if (preference.getStrData("List_Lock").contains(item.getPakete()))
            holder.SwOnOff.setChecked(true);
        else
            holder.SwOnOff.setChecked(false);

        holder.LogoApp.setImageDrawable(item.getImage());


        holder.SwOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b && !preference.getStrData("List_Lock").contains(item.getPakete())) {
                    if (item.getPakete().equals("android"));
                    else{
                        preference.saveData("List_Lock",preference.getStrData("List_Lock")+"~"+item.getPakete());
                        //System.out.println(preference.getStrData("List_Lock"));
                    }

                }
                else if(b == false){
                    String aux = preference.getStrData("List_Lock").replace("~"+item.getPakete(),"");
                    preference.saveData("List_Lock",aux);
                    //System.out.println(preference.getStrData("List_Lock"));
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
        ImageView LogoApp;

        public ViewHolderData(View itemView) {
            super(itemView);
            NameApp = (TextView) itemView.findViewById(R.id.NameApp);
            SwOnOff = itemView.findViewById(R.id.SwOnOff);
            LogoApp = itemView.findViewById(R.id.LogoApp);
        }
    }

    public static class App {
        private String name;
        private String pakete;
        private  Drawable image;

        public Drawable getImage() {
            return image;
        }

        public void setImage(Drawable image) {
            this.image = image;
        }

        public String getPakete() {
            return pakete;
        }

        public void setPakete(String pakete) {
            this.pakete = pakete;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
