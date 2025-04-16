package com.example.techtracker;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Query extends AppCompatActivity {
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1001;
    RecyclerView recyclerView;
    DatabaseHelper db;
    java.util.Date currentDate = new java.util.Date();
    ArrayList<String> table_id, storeid, date;
    ArrayList<Integer>total_mobile, total_electronic, total_nmp, total_prepaid, total_service, total_ac, total_cc;
    SaledayAdapter adapter;
    int mobile, electronics, nmp, activations;
    TextView kpis, kpis2;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    SimpleDateFormat portDate = new SimpleDateFormat("yyyy-MM-dd");
    String start, end, storeId;
    Date startDate, endDate;
    boolean queryFlag = false;
    boolean baselineFlag = false;

    Button pickDate, baselines;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_query);
        recyclerView = findViewById(R.id.recyclerView);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Query Sales Month @ T" + GlobalString.getInstance().returnStoreId());
        ab.setDisplayHomeAsUpEnabled(true);
        pickDate =  findViewById(R.id.pickDate);
        baselines = findViewById(R.id.baselines);
        mobile = GlobalString.getInstance().returnMobile();
        kpis = findViewById(R.id.kpis);
        kpis2 = findViewById(R.id.kpis2);
        electronics = GlobalString.getInstance().returnElectronics();
        nmp = GlobalString.getInstance().returnNMP();
        activations = GlobalString.getInstance().returnActivations();
        baselines.setText("M: $" + mobile + " E: $" + electronics + " NMP: " + nmp + " ACT: " + activations);
        if ( mobile > 0 && electronics > 0 && nmp > 0 && activations > 0)
        {
            baselineFlag = true;
        }
        if(GlobalString.getInstance().returnStartDate() == "Pick " || GlobalString.getInstance().returnEndDate() == " Date")
        {
            start = GlobalString.getInstance().returnStartDate();
            end = GlobalString.getInstance().returnEndDate();
        }else{
            try {
                startDate = portDate.parse(GlobalString.getInstance().returnStartDate());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            try {
                endDate = portDate.parse(GlobalString.getInstance().returnEndDate());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            start = sdf.format(startDate);
            end = sdf.format(endDate);
            queryFlag = true;
        }
        pickDate.setText(start + " - " + end);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        baselines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBaselines();
            }
        });

        db = new DatabaseHelper(Query.this);
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
        if(queryFlag && baselineFlag)
        {
            storeQueryData();
            ArrayList<Double> percentArray = calculatePercentages();
            kpis2.setText("M: " + percentArray.get(0) + "% E: " + percentArray.get(1) + "% NMP: " + percentArray.get(2) + "% ACT: " + percentArray.get(3) + "%");
        }else{
            storeData();
            kpis2.setText("");
        }
        setTotals();

        adapter = new SaledayAdapter(Query.this, this, table_id, storeid, total_mobile, total_electronic, total_nmp, total_prepaid, total_service, total_ac, total_cc, date);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Query.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.query_menu, menu);
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

    void storeQueryData() {
        Cursor cursor = db.queryStoreDayData(GlobalString.getInstance().returnStartDate(), GlobalString.getInstance().returnEndDate(), GlobalString.getInstance().returnStoreId());
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

    private void setBaselines() {
        //mobile
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mobile baseline");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = input.getText().toString(); // Get the input text
                if (s.isEmpty()) {
                    // Show a toast if input is empty or null
                    Toast.makeText(getApplicationContext(), "Mobile baseline cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    GlobalString.getInstance().setMobile(Integer.parseInt(s), Query.this);
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        //electronics
        AlertDialog.Builder elecBuilder = new AlertDialog.Builder(this);
        elecBuilder.setTitle("Electronics baseline");
        final EditText input2 = new EditText(this);
        input2.setInputType(InputType.TYPE_CLASS_NUMBER);
        elecBuilder.setView(input2);
        elecBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = input2.getText().toString(); // Get the input text
                if (s.isEmpty()) {
                    // Show a toast if input is empty or null
                    Toast.makeText(getApplicationContext(), "Electronics baseline cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    GlobalString.getInstance().setElectronics(Integer.parseInt(s), Query.this);
                }

            }
        });
        elecBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        //activations
        AlertDialog.Builder actBuilder = new AlertDialog.Builder(this);
        actBuilder.setTitle("Activations baseline");
        final EditText input3 = new EditText(this);
        input3.setInputType(InputType.TYPE_CLASS_NUMBER);
        actBuilder.setView(input3);
        actBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = input3.getText().toString(); // Get the input text
                if (s.isEmpty()) {
                    // Show a toast if input is empty or null
                    Toast.makeText(getApplicationContext(), "Activations baseline cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    GlobalString.getInstance().setActivations(Integer.parseInt(s), Query.this);
                    recreate();
                }

            }
        });
        actBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        //NMP
        AlertDialog.Builder nmpBuilder = new AlertDialog.Builder(this);
        nmpBuilder.setTitle("NMP baseline");
        final EditText input4 = new EditText(this);
        input4.setInputType(InputType.TYPE_CLASS_NUMBER);
        nmpBuilder.setView(input4);
        nmpBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = input4.getText().toString(); // Get the input text
                if (s.isEmpty()) {
                    // Show a toast if input is empty or null
                    Toast.makeText(getApplicationContext(), "NMP baseline cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    GlobalString.getInstance().setNmp(Integer.parseInt(s), Query.this);
                }

            }
        });
        nmpBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        actBuilder.show();
        nmpBuilder.show();
        elecBuilder.show();
        builder.show();
    }
    private void openDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String s = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                GlobalString.getInstance().setStartDate(s, Query.this);
                endDate();
            }
        },
                year, month, day);
        dialog.setTitle("Start Date");
        dialog.show();
    }
    private void endDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog end = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String s = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                GlobalString.getInstance().setEndDate(s, Query.this);
                recreate();
            }
        },
                year, month, day);
        end.setTitle("End Date");
        end.show();
    }

    public long calculateSalesDays(Date d1, Date d2){
        long diffInMs = Math.abs(d1.getTime() - d2.getTime());
        return TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS);
    }

    public ArrayList<Double> calculatePercentages()
    {
        ArrayList<Integer> returnArray = getTotals();
        double mobilePercentage = (double) returnArray.get(0) / (double) mobile;
        double electronicPercentage = (double) returnArray.get(1) / (double) electronics;
        int nmpVar = returnArray.get(2) + returnArray.get(5);
        double nmpPercentage = (double) nmpVar / (double) nmp;
        int actVar = returnArray.get(3) + returnArray.get(6);
        double actPercentage =  (double) actVar / (double) activations;
        ArrayList<Double> percentageArray = new ArrayList<>();
        mobilePercentage = mobilePercentage * 100;
        electronicPercentage = electronicPercentage * 100;
        nmpPercentage = nmpPercentage * 100;
        actPercentage = actPercentage * 100;
        percentageArray.add(Math.round(mobilePercentage * 10.0) / 10.0);
        percentageArray.add(Math.round(electronicPercentage * 10.0) / 10.0);
        percentageArray.add(Math.round(nmpPercentage * 10.0) / 10.0);
        percentageArray.add(Math.round(actPercentage * 10.0) / 10.0);
        return percentageArray;
    }

    ArrayList<Integer> getTotals(){
        int totalmobile = 0, totalelectronic = 0, totalprotection = 0, totalprepaid = 0, totalservice = 0, totalapple = 0, totalconsumer = 0;
        ArrayList<Integer> totalArray = new ArrayList<>();
        for(int i=0;i < table_id.size();i++)
        {
            totalmobile = totalmobile + total_mobile.get(i);
            totalelectronic = totalelectronic + total_electronic.get(i);
            totalprotection = totalprotection + total_nmp.get(i);
            totalprepaid = totalprepaid + total_prepaid.get(i);
            totalservice = totalservice + total_service.get(i);
            totalapple = totalapple + total_ac.get(i);
            totalconsumer = totalconsumer + total_cc.get(i);
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
        String protectionresult = String.valueOf(totals.get(2) + totals.get(5));
        String prepaidresult = String.valueOf(totals.get(3) + totals.get(6));
        kpis.setText("M: $" + mobileresult + " E: $" + electronicsresult + " NMP: " + protectionresult + " ACT: " + prepaidresult);
    }

    private void exportPDF(){
        PdfDocument document = new PdfDocument();
        Paint paint = new Paint();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        int x = 40;
        int y = 50;

        paint.setTextSize(16);
        paint.setFakeBoldText(true);
        canvas.drawText("Store " + GlobalString.getInstance().returnStoreId() + " KPI Report", x, y, paint);

        y += 30;
        paint.setTextSize(12);
        paint.setFakeBoldText(false);
        canvas.drawText("Date Range: " + start + " to " + end, x, y, paint);

        y += 30;
        ArrayList<Integer> totals = getTotals();
        ArrayList<Double> percentages = calculatePercentages();

        canvas.drawText("Baselines", x, y, paint);
        y += 20;
        canvas.drawText("Mobile: $" + mobile, x, y, paint);
        canvas.drawText("Electronics: $" + electronics, x + 200, y, paint);
        y += 20;
        canvas.drawText("NMP: " + nmp, x, y, paint);
        canvas.drawText("Activations: " + activations, x + 200, y, paint);

        y += 30;
        canvas.drawText("Totals", x, y, paint);
        y += 20;
        canvas.drawText("Mobile: $" + totals.get(0), x, y, paint);
        canvas.drawText("Electronics: $" + totals.get(1), x + 200, y, paint);
        y += 20;
        canvas.drawText("NMP: " + (totals.get(2) + totals.get(5)), x, y, paint);
        canvas.drawText("Activations: " + (totals.get(3) + totals.get(6)), x + 200, y, paint);

        y += 30;
        canvas.drawText("Percent to Goal", x, y, paint);
        y += 20;
        canvas.drawText("Mobile: " + percentages.get(0) + "%", x, y, paint);
        canvas.drawText("Electronics: " + percentages.get(1) + "%", x + 200, y, paint);
        y += 20;
        canvas.drawText("NMP: " + percentages.get(2) + "%", x, y, paint);
        canvas.drawText("Activations: " + percentages.get(3) + "%", x + 200, y, paint);

        y += 40;
        paint.setFakeBoldText(true);
        canvas.drawText("Daily Breakdown", x, y, paint);
        y += 25;

        paint.setFakeBoldText(false);
        for (int i = 0; i < table_id.size(); i++) {
            if (y > 800) break; // avoid overflowing a single page

            String row = date.get(i) + " | M: $" + total_mobile.get(i) +
                    ", E: $" + total_electronic.get(i) +
                    ", NMP: " + total_nmp.get(i) +
                    ", ACT: " + (total_prepaid.get(i) + total_cc.get(i));

            canvas.drawText(row, x, y, paint);
            y += 20;
        }

        document.finishPage(page);

        // Generate file name
        String fileName = "KPI_Report_" + start.replace("/", "-") + "_to_" + end.replace("/", "-") + ".pdf";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            // For Android 10 and above (no permission required)
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.Downloads.MIME_TYPE, "application/pdf");
            contentValues.put(MediaStore.Downloads.IS_PENDING, 1);

            ContentResolver resolver = getContentResolver();
            Uri collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            Uri fileUri = resolver.insert(collection, contentValues);

            try {
                if (fileUri != null) {
                    OutputStream out = resolver.openOutputStream(fileUri);
                    document.writeTo(out);
                    out.close();

                    contentValues.clear();
                    contentValues.put(MediaStore.Downloads.IS_PENDING, 0);
                    resolver.update(fileUri, contentValues, null, null);

                    Toast.makeText(this, "PDF exported to Downloads", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error creating PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } else {
            // For Android 9 and below (requires WRITE_EXTERNAL_STORAGE permission)
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(downloadsDir, fileName);
            try {
                document.writeTo(new FileOutputStream(file));
                Toast.makeText(this, "PDF exported to: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error creating PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        document.close();
    }
    @Override
    public void onRestart() {
        super.onRestart();
        recreate();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportPDF();
            } else {
                Toast.makeText(this, "Permission denied. Cannot export PDF.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        if (item.getItemId() == R.id.export) {
            exportPDF();
            return true;
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
                        GlobalString.getInstance().setStoreId(storeId, Query.this);
                        Intent intent = new Intent(Query.this, Query.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

}
