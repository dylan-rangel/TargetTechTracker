package com.example.techtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;

class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Sales.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "sales_day";
    private static final String COLUMN_ID = "_id";
    private static final String MOBILE_SALE = "mobile_sale";
    private static final String ELECTRONIC_SALE = "electronic_sale";
    private static final String PROTECTION_PLAN = "protection_plan";
    private static final String PREPAID = "prepaid";
    private static final String SERVICE_TICKET = "service_ticket";
    private static final String APPLE_CARE = "apple_care";
    private static final String CONSUMER_CELL = "consumer_cell";
    private static final String SALE_DATE = "sale_date";  // Using a proper column name for the date


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String Query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MOBILE_SALE + " INTEGER, "
                + ELECTRONIC_SALE + " INTEGER, "
                + PROTECTION_PLAN + " INTEGER, "
                + PREPAID + " INTEGER, "
                + SERVICE_TICKET + " INTEGER, "
                + APPLE_CARE + " INTEGER, "
                + CONSUMER_CELL + " INTEGER, "
                + SALE_DATE + " DATE"
                + ");";
        db.execSQL(Query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addSale(int mobileSale, int electronicSale, int protectionPlan, int prepaid, int serviceTicket, int applecare, int consumerCell, String saleDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // Ensure the date format is correct
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = sdf.parse(saleDate);
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());  // Ensure date format is correct
            cv.put(SALE_DATE, sqlDate.toString());  // Use the SQL date format
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cv.put(MOBILE_SALE, mobileSale);
        cv.put(ELECTRONIC_SALE, electronicSale);
        cv.put(PROTECTION_PLAN, protectionPlan);
        cv.put(PREPAID, prepaid);
        cv.put(SERVICE_TICKET, serviceTicket);
        cv.put(APPLE_CARE, applecare);
        cv.put(CONSUMER_CELL, consumerCell);

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Sale added", Toast.LENGTH_SHORT).show();
        }
    }
}
