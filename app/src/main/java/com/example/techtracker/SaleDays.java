package com.example.techtracker;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;
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
                Intent intent = new Intent(SaleDays.this, MainActivity.class);
                startActivity(intent);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.salesmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    void storeData() {
        Cursor cursor = db.readAllStoreDayData();
        if(cursor.getCount() == 0){

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.wipe) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Wipe the database?");
            builder.setMessage("Are you sure you want to delete everything?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db.clear();
                    File file = new File("/data/data/com.example.techtracker/databases/Sales.db");
                    File file2 = new File("/data/data/com.example.techtracker/databases/Sales.db-journal");

                    if (file.exists()) {
                        file.delete();
                        file2.delete();
                    }
                    recreate();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();
        }
        return super.onOptionsItemSelected(item);
    }
}