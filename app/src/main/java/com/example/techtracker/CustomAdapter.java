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

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private Context context;
    Activity activity;
    private ArrayList<String>table_id, date, time;
    private ArrayList<Integer>mobile_sales, electronic_sales, protection_plans, prepaid, service_tickets, applecare, consumer_cellular;
    int position;
    CustomAdapter(Activity activity,
                  Context context,
                  ArrayList table_id,
                  ArrayList mobile_sales,
                  ArrayList electronic_sales,
                  ArrayList protection_plans,
                  ArrayList prepaid,
                  ArrayList service_tickets,
                  ArrayList applecare,
                  ArrayList consumer_cellular,
                  ArrayList date,
                  ArrayList time){
        this.table_id = table_id;
        this.activity = activity;
        this.context = context;
        this.mobile_sales = mobile_sales;
        this.electronic_sales = electronic_sales;
        this.protection_plans = protection_plans;
        this.prepaid = prepaid;
        this.service_tickets = service_tickets;
        this.applecare = applecare;
        this.consumer_cellular = consumer_cellular;
        this.date = date;
        this.time = time;

    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {
        this.position = position;

        if (mobile_sales.get(position) != 0 && electronic_sales.get(position) == 0) {
            holder.saleid.setText(String.valueOf(table_id.get(position)));
            holder.VariableText1.setText("Mobile: $" + String.valueOf(mobile_sales.get(position)));
            holder.VariableText2.setText("");
            holder.DateText.setText(date.get(position));
            holder.TimeText.setText(time.get(position));
        }
        if (electronic_sales.get(position) != 0 && mobile_sales.get(position) == 0) {
            holder.saleid.setText(String.valueOf(table_id.get(position)));
            holder.VariableText1.setText("Non-Mobile: $" + String.valueOf(electronic_sales.get(position)));
            holder.VariableText2.setText("");
            holder.DateText.setText(date.get(position));
            holder.TimeText.setText(time.get(position));
        }
        if (electronic_sales.get(position) != 0 && mobile_sales.get(position) != 0) {
            holder.saleid.setText(String.valueOf(table_id.get(position)));
            holder.VariableText1.setText("Mobile: $" + String.valueOf(mobile_sales.get(position)));
            holder.VariableText2.setText("Non-Mobile: $" + String.valueOf(electronic_sales.get(position)));
            holder.DateText.setText(date.get(position));
            holder.TimeText.setText(time.get(position));
        }
        if (protection_plans.get(position) != 0){
            holder.saleid.setText(String.valueOf(table_id.get(position)));
            holder.VariableText1.setText("Protection Plan");
            holder.VariableText2.setText("");
            holder.DateText.setText(date.get(position));
            holder.TimeText.setText(time.get(position));
        }
        if (prepaid.get(position) != 0){
            holder.saleid.setText(String.valueOf(table_id.get(position)));
            holder.VariableText1.setText("Prepaid");
            holder.VariableText2.setText("");
            holder.DateText.setText(date.get(position));
            holder.TimeText.setText(time.get(position));
        }
        if (service_tickets.get(position) != 0){
            holder.saleid.setText(String.valueOf(table_id.get(position)));
            holder.VariableText1.setText("Service Ticket");
            holder.VariableText2.setText("");
            holder.DateText.setText(date.get(position));
            holder.TimeText.setText(time.get(position));
        }
        if (applecare.get(position) != 0){
            holder.saleid.setText(String.valueOf(table_id.get(position)));
            holder.VariableText1.setText("AppleCare");
            holder.VariableText2.setText("");
            holder.DateText.setText(date.get(position));
            holder.TimeText.setText(time.get(position));
        }
        if (consumer_cellular.get(position) != 0){
            holder.saleid.setText(String.valueOf(table_id.get(position)));
            holder.VariableText1.setText("Consumer Cellular");
            holder.VariableText2.setText("");
            holder.DateText.setText(date.get(position));
            holder.TimeText.setText(time.get(position));
        }
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
                Intent delete = new Intent(context, DeleteActivity.class);
                intent.putExtra("id", String.valueOf(table_id.get(position)));
                intent.putExtra("Mobile", String.valueOf(mobile_sales.get(position)));
                intent.putExtra("Electronic", String.valueOf(electronic_sales.get(position)));
                intent.putExtra("Protection", String.valueOf(protection_plans.get(position)));
                intent.putExtra("Prepaid", String.valueOf(prepaid.get(position)));
                intent.putExtra("Service", String.valueOf(service_tickets.get(position)));
                intent.putExtra("AppleCare", String.valueOf(applecare.get(position)));
                intent.putExtra("Consumer", String.valueOf(consumer_cellular.get(position)));
                intent.putExtra("Date", String.valueOf(date.get(position)));
                intent.putExtra("Time", String.valueOf(time.get(position)));
                boolean flag = true;

                ArrayList<String> itemsToCheck = new ArrayList<>();
                itemsToCheck.add(String.valueOf(protection_plans.get(position)));
                itemsToCheck.add(String.valueOf(prepaid.get(position)));
                itemsToCheck.add(String.valueOf(service_tickets.get(position)));
                itemsToCheck.add(String.valueOf(applecare.get(position)));
                itemsToCheck.add(String.valueOf(consumer_cellular.get(position)));
                for (String item : itemsToCheck) {
                    if (!item.equals("0")) {
                        flag = false;
                        break;
                    }
                }
                if(flag) {
                    activity.startActivityForResult(intent, 1);
                } else {
                    delete.putExtra("id", String.valueOf(table_id.get(position)));
                    delete.putExtra("Mobile", String.valueOf(mobile_sales.get(position)));
                    delete.putExtra("Electronic", String.valueOf(electronic_sales.get(position)));
                    delete.putExtra("Protection", String.valueOf(protection_plans.get(position)));
                    delete.putExtra("Prepaid", String.valueOf(prepaid.get(position)));
                    delete.putExtra("Service", String.valueOf(service_tickets.get(position)));
                    delete.putExtra("AppleCare", String.valueOf(applecare.get(position)));
                    delete.putExtra("Consumer", String.valueOf(consumer_cellular.get(position)));
                    delete.putExtra("Date", String.valueOf(date.get(position)));
                    delete.putExtra("Time", String.valueOf(time.get(position)));
                    activity.startActivityForResult(delete, 1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return table_id.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView saleid, VariableText1, VariableText2, DateText, TimeText;
        LinearLayout mainLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            saleid = itemView.findViewById(R.id.saleid);
            VariableText1 = itemView.findViewById(R.id.VariableText1);
            VariableText2 = itemView.findViewById(R.id.VariableText2);
            DateText = itemView.findViewById(R.id.DateText);
            TimeText = itemView.findViewById(R.id.TimeText);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
