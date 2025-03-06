package com.example.techtracker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class UpdateActivity extends AppCompatActivity {

    EditText updateMobile_sales, updateElectronics_sales;
    Button updateButton, deleteButton;
    ArrayList<String> data = new ArrayList<>();

    String mobile, electronic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        updateMobile_sales = findViewById(R.id.updateMobile_sales);
        updateElectronics_sales = findViewById(R.id.updateElectronics_sales);
        updateButton = findViewById(R.id.update_button);
        deleteButton = findViewById(R.id.delete_button);
        data = getIntentData();
        setIntentData(data);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Sale Id: " + data.get(0));
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //temporary until i resolve how nmp/tally values will work
                mobile = updateMobile_sales.getText().toString().trim();
                electronic = updateElectronics_sales.getText().toString().trim();
                if(mobile.equals(""))
                {
                    mobile = "0";
                }
                if(electronic.equals(""))
                {
                    electronic = "0";
                }
                DatabaseHelper db = new DatabaseHelper(UpdateActivity.this);
                db.updateData(data.get(0), mobile, electronic, data.get(3), data.get(4), data.get(5), data.get(6), data.get(7), data.get(8), data.get(9));
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
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
    void setIntentData(ArrayList<String> array) {
        if (!array.get(1).equals("0")) {
            updateMobile_sales.setText(array.get(1));
        } else {
            updateMobile_sales.setText("");
        }
        if (!array.get(2).equals("0")) {
            updateElectronics_sales.setText(array.get(2));
        } else {
            updateElectronics_sales.setText("");
        }
    }

    void confirmDialogue(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Sale number " + data.get(0) + "?");
        builder.setMessage("Are you sure you want to delete sale number " + data.get(0) + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseHelper db = new DatabaseHelper(UpdateActivity.this);
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