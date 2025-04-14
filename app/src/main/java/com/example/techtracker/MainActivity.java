package com.example.techtracker;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String local_store_id;
    FloatingActionButton add_button;
    DatabaseHelper db;
    ArrayList<String>table_id, date, time, query, storeidArray;
    ArrayList<Integer>mobile_sales, electronic_sales, protection_plans, prepaid, service_tickets, applecare, consumer_cellular;
    CustomAdapter customAdapter;
    TextView totalText;
    java.util.Date currentDate = new java.util.Date();
    private String storeId;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String formattedDate = sdf.format(currentDate);
    private boolean queryFlag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        GlobalString.getInstance().init(this); //this might cause some problems
        local_store_id = GlobalString.getInstance().returnStoreId();
        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        totalText = findViewById(R.id.rex);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        query = getIntentData();
        if (query != null && query.size() >= 2) {
            if (query.get(0).equals(local_store_id) && query.get(1).equals(formattedDate)) {
                // Do nothing, or any specific action when they match
            } else {
                local_store_id = query.get(1);
                formattedDate = query.get(0);
                queryFlag = true; //make this a conditional to add a new sale for a previous sale day
            }
        } else if (query == null || query.isEmpty()) {
        } else {
        }
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
        storeidArray = new ArrayList<>();

        storeData();

        customAdapter = new CustomAdapter(MainActivity.this, this, table_id, mobile_sales, electronic_sales, protection_plans, prepaid, service_tickets, applecare, consumer_cellular, date, time, storeidArray);
        recyclerView.setAdapter(customAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        setTotals();

        ab.setTitle("Target Tech Sales @ T" + local_store_id);
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

    //Adds Data to local arrays
    void storeData() {
        Cursor cursor = db.returnCurrentData(local_store_id, formattedDate);
        if(cursor.getCount() == 0){

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
                storeidArray.add(cursor.getString(10));
            }
        }
    }

    ArrayList<Integer> getTotals(){
        int totalmobile = 0, totalelectronic = 0, totalprotection = 0, totalprepaid = 0, totalservice = 0, totalapple = 0, totalconsumer = 0;
        ArrayList<Integer> totalArray = new ArrayList<>();
        for(int i=0;i < table_id.size();i++)
        {
            totalmobile = totalmobile + mobile_sales.get(i);
            totalelectronic = totalelectronic + electronic_sales.get(i);
            totalprotection = totalprotection + protection_plans.get(i);
            totalprepaid = totalprepaid + prepaid.get(i);
            totalservice = totalservice + service_tickets.get(i);
            totalapple = totalapple + applecare.get(i);
            totalconsumer = totalconsumer + consumer_cellular.get(i);
        }
        totalArray.add(totalmobile);
        totalArray.add(totalelectronic);
        totalArray.add(totalprotection);
        totalArray.add(totalprepaid);
        totalArray.add(totalservice);
        totalArray.add(totalapple);
        totalArray.add(totalconsumer);
        return totalArray; //reuse in finished saleday
    }
    void setTotals(){
        ArrayList<Integer> totals = getTotals();
        String mobileresult = String.valueOf(totals.get(0));
        String electronicsresult = String.valueOf(totals.get(1) + totals.get(0));
        String protectionresult = String.valueOf(totals.get(2));
        String prepaidresult = String.valueOf(totals.get(3));
        String serviceresult = String.valueOf(totals.get(4));
        String appleresult = String.valueOf(totals.get(5));
        String consumerresult = String.valueOf(totals.get(6));
        totalText.setText("M: $" + mobileresult + " E: $" + electronicsresult + " NMPs: " + protectionresult + " PP: " + prepaidresult + " ST: " + serviceresult + " AC: " + appleresult + " CC: " + consumerresult);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    ArrayList<String> getIntentData(){
        ArrayList<String> values = new ArrayList<>();
        if(getIntent().hasExtra("date") && getIntent().hasExtra("storeid"))
        {
            String[] keys = {"date", "storeid"};
            for (String key : keys) {
                values.add(getIntent().getStringExtra(key));
            }
        }
        return values;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            Intent intent = new Intent(MainActivity.this, SaleDays.class);
            startActivity(intent);
            finish();
            return true;
        }
        if(item.getItemId() == R.id.delete_all)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete all sales?");
            builder.setMessage("Are you sure you want to delete all sales from today?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    currentDate.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String compare = sdf.format(currentDate);
                    db.deleteQuery(local_store_id, compare);
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

        if(item.getItemId() == R.id.finishDay)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Finish day?");
            builder.setMessage("Are you sure you want to record all sales from today?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    boolean flag = false;
                    ArrayList<Integer> totals = getTotals();
                    currentDate.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String compare = sdf.format(currentDate);
                    String currentId = local_store_id;
                    Cursor cursor = db.readAllStoreDayData();
                    String position = "null";
                    while (cursor.moveToNext())
                    {
                        if(currentId.equals(cursor.getString(1)) && compare.equals(cursor.getString(9)))
                        {
                            flag = true;
                            position = String.valueOf(cursor.getInt(0));
                            //if true then update current entry, if false add a new one
                        }
                    }

                    if(flag == true && !position.equals("null"))
                    {
                        db.updateStoreDay(position, currentId, String.valueOf(totals.get(0)), String.valueOf(totals.get(1) + totals.get(0)), String.valueOf(totals.get(2)), String.valueOf(totals.get(3)), String.valueOf(totals.get(4)), String.valueOf(totals.get(5)), String.valueOf(totals.get(6)), currentDate);
                    } else {
                        int temp = totals.get(1) + totals.get(0);
                        db.addStoreDay(currentId, totals.get(0), temp, totals.get(2), totals.get(3), totals.get(4), totals.get(5), totals.get(6), sdf.format(currentDate));
                    }
                    Intent intent = new Intent(MainActivity.this, SaleDays.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();
        }

        if(item.getItemId() == R.id.update)
        {
            ArrayList<Integer> totalArray = getTotals();
            Integer electronics = totalArray.get(1) + totalArray.get(0);
            java.util.Date updateTime = new java.util.Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            String time = sdf.format(updateTime);
            String update = "T" + local_store_id + " " + time + "\nAccessories: $" + totalArray.get(0) + "\nElectronics: $" + electronics + "\nProtection Plans: " + totalArray.get(2) + "\nPrepaids: " + totalArray.get(3) + "\nService Tickets: " + totalArray.get(4) + "\nAppleCare: " + totalArray.get(5) + "\nConsumer Cellular: " + totalArray.get(6) + "\n \nPosted using Target Tech Tracker Alpha 1.1.3";

            // Copy to clipboard
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Tech Sales Update", update);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Update copied to clipboard", Toast.LENGTH_SHORT).show();


        }
        if(item.getItemId() == R.id.storeId)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
           builder.setTitle("Update store number");
           final EditText input = new EditText(this);
           input.setInputType(InputType.TYPE_CLASS_NUMBER);
           builder.setView(input);
            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    storeId = input.getText().toString(); // Get the input text
                    // Validate the input
                    if (storeId.isEmpty()) {
                        // Show a toast if input is empty or null
                        Toast.makeText(getApplicationContext(), "Store number cannot be empty", Toast.LENGTH_SHORT).show();
                    } else if (storeId.length() != 4) {
                        // Show a toast if the length is not 4
                        Toast.makeText(getApplicationContext(), "Store number must be 4 digits", Toast.LENGTH_SHORT).show();
                    } else if (!storeId.matches("\\d{4}")) {
                        // Show a toast if the input is not numeric
                        Toast.makeText(getApplicationContext(), "Store number must be numeric", Toast.LENGTH_SHORT).show();
                    } else {
                        GlobalString.getInstance().setStoreId(storeId, MainActivity.this);
                        local_store_id = storeId;
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finishAffinity();  // This finishes all activities in the stack
                    }

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }
}