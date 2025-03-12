package com.example.techtracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class SaledayAdapter extends RecyclerView.Adapter<SaledayAdapter.ViewHolder> {
    private Context context;
    Activity activity;
    int position;
    private ArrayList<Integer>totalMobile, totalElectronic, totalProtection, totalPrepaid, totalService, totalAc, totalCC;
    private ArrayList<String>table_id, date, storeId;
    SaledayAdapter(Activity activity,
                   Context context,
                   ArrayList table_id,
                   ArrayList storeId,
                   ArrayList totalMobile,
                   ArrayList totalElectronic,
                   ArrayList totalProtection,
                   ArrayList totalPrepaid,
                   ArrayList totalService,
                   ArrayList totalAc,
                   ArrayList totalCC,
                   ArrayList date){
        this.table_id = table_id;
        this.activity = activity;
        this.context = context;
        this.storeId = storeId;
        this.totalMobile = totalMobile;
        this.totalElectronic = totalElectronic;
        this.totalProtection = totalProtection;
        this.totalPrepaid = totalPrepaid;
        this.totalService = totalService;
        this.totalAc = totalAc;
        this.totalCC = totalCC;
        this.date = date;
    }


    @NonNull
    @Override
    public SaledayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.days, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaledayAdapter.ViewHolder holder, int position) {
        this.position = position;
        if(table_id.get(0) != null)
        {
            holder.Date.setText(date.get(position));
            holder.VariableText.setText("Store: T" + storeId.get(position));
            holder.mobile.setText("Total Mobile: $" + String.valueOf(totalMobile.get(position)));
            holder.electronics.setText("Total Electronics: $" + String.valueOf(totalElectronic.get(position)));
        }
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("date", String.valueOf(date.get(position)));
                intent.putExtra("storeid", String.valueOf(date.get(position)));
                //Have this make an SQL Query using the date and store string
            }
        });
    }

    @Override
    public int getItemCount() {
        return table_id.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView Date, dayid, VariableText, mobile, electronics;
        LinearLayout mainLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Date = itemView.findViewById(R.id.Date);
            VariableText = itemView.findViewById(R.id.VariableText);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            mobile = itemView.findViewById(R.id.TotalMobile);
            electronics = itemView.findViewById(R.id.TotalElectronics);
        }
    }
}
