package com.example.techtracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;

    DatabaseHelper db;
    ArrayList<String>table_id, date, time;
    ArrayList<Integer>mobile_sales, electronic_sales, protection_plans, prepaid, service_tickets, applecare, consumer_cellular;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Add.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new DatabaseHelper(MainActivity.this);
        table_id = new ArrayList<>();
        mobile_sales = new ArrayList<>();
        electronic_sales = new ArrayList<>();
        protection_plans = new ArrayList<>();
        prepaid = new ArrayList<>();
        service_tickets = new ArrayList<>();
        applecare = new ArrayList<>();
        consumer_cellular = new ArrayList<>();
        date = new ArrayList<>();
        time = new ArrayList<>();

        storeData();

        customAdapter = new CustomAdapter(MainActivity.this, table_id, mobile_sales, electronic_sales, protection_plans, prepaid, service_tickets, applecare, consumer_cellular, date, time);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }
    void storeData() {
        Cursor cursor = db.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext() ) {
                table_id.add(cursor.getString(0));
                mobile_sales.add(cursor.getInt(1));
                electronic_sales.add(cursor.getInt(2));
                protection_plans.add(cursor.getInt(3));
                prepaid.add(cursor.getInt(4));
                service_tickets.add(cursor.getInt(5));
                applecare.add(cursor.getInt(6));
                consumer_cellular.add(cursor.getInt(7));
                date.add(cursor.getString(8));
                time.add(cursor.getString(9));
            }
        }
    }
}