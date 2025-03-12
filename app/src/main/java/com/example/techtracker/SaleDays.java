package com.example.techtracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SaleDays extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseHelper db;
    java.util.Date currentDate = new java.util.Date();
    ArrayList<String>table_id, storeid, date;
    ArrayList<Integer>total_mobile, total_electronic, total_nmp, total_prepaid, total_service, total_ac, total_cc;
    SaledayAdapter adapter;
    FloatingActionButton refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sale_days);
        recyclerView = findViewById(R.id.recyclerView);
        refresh = findViewById(R.id.refresh);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Target Tech Sales");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
        db = new DatabaseHelper(SaleDays.this);
        table_id = new ArrayList<>();
        storeid = new ArrayList<>();
        total_mobile = new ArrayList<>();
        total_electronic = new ArrayList<>();
        total_nmp = new ArrayList<>();
        total_prepaid = new ArrayList<>();
        total_service = new ArrayList<>();
        total_ac = new ArrayList<>();
        total_cc = new ArrayList<>();
        date = new ArrayList<>();

        storeData();

        adapter = new SaledayAdapter(SaleDays.this, this, table_id, storeid, total_mobile, total_electronic, total_nmp, total_prepaid, total_service, total_ac, total_cc, date);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(SaleDays.this));
    }
    void storeData() {
        Cursor cursor = db.readAllStoreDayData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext() ) {
                table_id.add(cursor.getString(0));
                storeid.add(cursor.getString(1));
                total_mobile.add(cursor.getInt(2));
                total_electronic.add(cursor.getInt(3));
                total_nmp.add(cursor.getInt(4));
                total_prepaid.add(cursor.getInt(5));
                total_service.add(cursor.getInt(6));
                total_ac.add(cursor.getInt(7));
                total_cc.add(cursor.getInt(8));
                date.add(cursor.getString(9));
            }
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        recreate();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

}