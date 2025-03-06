package com.example.techtracker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class DeleteActivity extends AppCompatActivity {

    Button delete;
    ArrayList<String> data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delete);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        delete = findViewById(R.id.deleteTally);
        data = getIntentData();
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Sale Id: " + data.get(0));
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialogue();
            }
        });
    }
    ArrayList<String> getIntentData(){
        ArrayList<String> values = new ArrayList<>();
        if(getIntent().hasExtra("id") && getIntent().hasExtra("Mobile") && getIntent().hasExtra("Electronic"))
        {
            String[] keys = {"id", "Mobile", "Electronic", "Protection", "Prepaid", "Service", "AppleCare", "Consumer", "Date", "Time"};
            for (String key : keys) {
                values.add(getIntent().getStringExtra(key));
            }
        }
        return values;
    }
    void confirmDialogue(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Sale number " + data.get(0) + "?");
        builder.setMessage("Are you sure you want to delete sale number " + data.get(0) + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseHelper db = new DatabaseHelper(DeleteActivity.this);
                db.deleteOneRow(data.get(0));
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
}