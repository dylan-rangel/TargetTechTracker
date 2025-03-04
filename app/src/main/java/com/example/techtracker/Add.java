package com.example.techtracker;

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

public class Add extends AppCompatActivity {

    EditText mobileSales_input, electronicsSales_input;
    Button add_button;


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

        mobileSales_input = findViewById(R.id.mobile_sales);
        electronicsSales_input = findViewById(R.id.mobile_sales);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileSalesText = mobileSales_input.getText().toString().trim();
                String electronicsSalesText = electronicsSales_input.getText().toString().trim();
                int mobileSales = 0;
                int electronicsSales = 0;
                boolean fail = false;

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
                    electronicsSales_input.setError("Please enter a valid number");
                    fail = true;
                }

                // If both are valid, proceed with database insertion
                if (mobileSales >= 0 && electronicsSales >= 0 && !fail) {
                    DatabaseHelper myDB = new DatabaseHelper(Add.this);
                    myDB.addSale(mobileSales, electronicsSales, 0, 0, 0, 0, 0, "01-01-2025");
                }
            }
        });
    }
}
