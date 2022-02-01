package com.developeralamin.update_19;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    int m=1;

    Context context;
    List<ModelClass> countrylist;

    public Adapter(Context context, List<ModelClass> countrylist) {
        this.context = context;
        this.countrylist = countrylist;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recyclerlayout,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, int position) {
        ModelClass modelClass=countrylist.get(position);
        if(m==1) {


            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(modelClass.getCases())));
        }
        else if(m==2)
        {
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(modelClass.getRecovered())));
        }
        else if(m==3)
        {
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(modelClass.getDeaths())));
        }
        else
        {
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(modelClass.getActive())));
        }

        holder.country.setText(modelClass.getCountry());

    }

    @Override
    public int getItemCount() {
        return countrylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cases,country;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            country=itemView.findViewById(R.id.countryname);
            cases=itemView.findViewById(R.id.countrycases);
        }
    }

    public void filter(String charText) {
        if (charText.equals("cases")) {

            m=1;

        } else if(charText.equals("recovered")) {
           m=2;
        }else if(charText.equals("deaths"))
        {
            m=3;
        }
        else
        {
            m=4;
        }
        notifyDataSetChanged();
    }
}
