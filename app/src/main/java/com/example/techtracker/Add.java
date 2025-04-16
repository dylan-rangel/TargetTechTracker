package com.example.techtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Add extends AppCompatActivity {
    EditText mobileSales_input, electronicsSales_input;
    Button add_button, acButton, nmpButton, prepaidButton, supportButton, consumerButton;
    java.util.Date currentDate = new java.util.Date();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String storeId = GlobalString.getInstance().returnStoreId();
        mobileSales_input = findViewById(R.id.mobile_sales);
        electronicsSales_input = findViewById(R.id.electronics_sales);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileSalesText = mobileSales_input.getText().toString().trim();
                String electronicsSalesText = electronicsSales_input.getText().toString().trim();
                int mobileSales = 0;
                int electronicsSales = 0;
                boolean fail = false;
                currentDate = new java.util.Date();

                // Check if the mobile sales input is valid, otherwise default to 0
                if (!mobileSalesText.isEmpty()) {
                    try {
                        mobileSales = Integer.parseInt(mobileSalesText);
                    } catch (NumberFormatException e) {
                        mobileSales_input.setError("Please enter a valid number");
                        fail = true;
                    }
                }

                // Check if the electronics sales input is valid, otherwise default to 0
                if (!electronicsSalesText.isEmpty()) {
                    try {
                        electronicsSales = Integer.parseInt(electronicsSalesText);
                    } catch (NumberFormatException e) {
                        electronicsSales_input.setError("Please enter a valid number");
                        fail = true;
                    }
                }

                if (mobileSalesText.isEmpty() && electronicsSalesText.isEmpty()) {
                    mobileSales_input.setError("Please enter a valid number");
                    electronicsSales_input.setError("Please enter a valid number");
                    fail = true;
                }

                if (mobileSalesText.equals("0") || electronicsSalesText.equals("0")) {
                    mobileSales_input.setError("Please enter a valid number");
                    electronicsSales_input.setError("Please enter a valid number");
                    fail = true;
                }

                // If both are valid, proceed with database insertion
                if (mobileSales >= 0 && electronicsSales >= 0 && !fail) {
                    DatabaseHelper myDB = new DatabaseHelper(Add.this);
                    GlobalString.getInstance().setToast("Sale Added");
                    myDB.addSale(mobileSales, electronicsSales, 0, 0, 0, 0, 0, currentDate, storeId);
                    mobileSales_input.setText("");
                    electronicsSales_input.setText("");
                }
            }
        });
        acButton = findViewById(R.id.acButton);
        acButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate = new java.util.Date();
                DatabaseHelper myDB = new DatabaseHelper(Add.this);
                GlobalString.getInstance().setToast("AppleCare Added");
                myDB.addSale(0, 0, 0, 0, 0, 1, 0, currentDate, storeId);
                mobileSales_input.setText("");
                electronicsSales_input.setText("");
            }
        });
        nmpButton = findViewById(R.id.nmpButton);
        nmpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate = new java.util.Date();
                DatabaseHelper myDB = new DatabaseHelper(Add.this);
                GlobalString.getInstance().setToast("NMP Added");
                myDB.addSale(0, 0, 1, 0, 0, 0, 0, currentDate, storeId);
                mobileSales_input.setText("");
                electronicsSales_input.setText("");
            }
        });
        prepaidButton = findViewById(R.id.prepaidButton);
        prepaidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate = new java.util.Date();
                DatabaseHelper myDB = new DatabaseHelper(Add.this);
                GlobalString.getInstance().setToast("Prepaid Added");
                myDB.addSale(0, 0, 0, 1, 0, 0, 0, currentDate, storeId);
                mobileSales_input.setText("");
                electronicsSales_input.setText("");
            }
        });
        supportButton = findViewById(R.id.supportButton);
        supportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate = new java.util.Date();
                DatabaseHelper myDB = new DatabaseHelper(Add.this);
                GlobalString.getInstance().setToast("Support Ticket Added");
                myDB.addSale(0, 0, 0, 0, 1, 0, 0, currentDate, storeId);
                mobileSales_input.setText("");
                electronicsSales_input.setText("");
            }
        });
        consumerButton = findViewById(R.id.consumerButton);
        consumerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate = new java.util.Date();
                DatabaseHelper myDB = new DatabaseHelper(Add.this);
                GlobalString.getInstance().setToast("Consumer Cellular Added");
                myDB.addSale(0, 0, 0, 0, 0, 0, 1, currentDate, storeId);
                mobileSales_input.setText("");
                electronicsSales_input.setText("");
            }
        });
    }
}