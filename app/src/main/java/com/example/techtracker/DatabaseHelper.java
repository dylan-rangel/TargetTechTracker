package com.example.techtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Sales.db";
    private static final int DATABASE_VERSION = 2; // Increment the version number
    private static final String SALES_TABLE_NAME = "sales_day";
    private static final String STORE_DAY_TABLE_NAME = "store_day"; // New table name

    // Sales table columns
    private static final String COLUMN_ID = "_id";
    private static final String MOBILE_SALE = "mobile_sale";
    private static final String ELECTRONIC_SALE = "electronic_sale";
    private static final String PROTECTION_PLAN = "protection_plan";
    private static final String PREPAID = "prepaid";
    private static final String SERVICE_TICKET = "service_ticket";
    private static final String APPLE_CARE = "apple_care";
    private static final String CONSUMER_CELL = "consumer_cell";
    private static final String SALE_DATE = "sale_date";
    private static final String SALE_TIME = "sale_time";

    // StoreDay table columns
    private static final String STORE_ID = "store_id";
    private static final String TOTAL_MOBILE = "total_mobile";
    private static final String TOTAL_ELECTRONIC = "total_electronic";
    private static final String TOTAL_NMP = "total_nmp";
    private static final String TOTAL_PP = "total_pp";
    private static final String TOTAL_SERVICE = "total_service";
    private static final String TOTAL_AC = "total_ac";
    private static final String TOTAL_CC = "total_cc";

    DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the sales_day table
        String salesTableQuery = "CREATE TABLE IF NOT EXISTS " + SALES_TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MOBILE_SALE + " INTEGER, "
                + ELECTRONIC_SALE + " INTEGER, "
                + PROTECTION_PLAN + " INTEGER, "
                + PREPAID + " INTEGER, "
                + SERVICE_TICKET + " INTEGER, "
                + APPLE_CARE + " INTEGER, "
                + CONSUMER_CELL + " INTEGER, "
                + SALE_DATE + " TEXT, "
                + SALE_TIME + " TEXT, "
                + STORE_ID + " TEXT"
                + ");";
        db.execSQL(salesTableQuery);

        // Create the store_day table
        String storeDayTableQuery = "CREATE TABLE IF NOT EXISTS " + STORE_DAY_TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + STORE_ID + " TEXT, "
                + TOTAL_MOBILE + " INTEGER, "
                + TOTAL_ELECTRONIC + " INTEGER, "
                + TOTAL_NMP + " INTEGER, "
                + TOTAL_PP + " INTEGER, "
                + TOTAL_SERVICE + " INTEGER, "
                + TOTAL_AC + " INTEGER, "
                + TOTAL_CC + " INTEGER, "
                + SALE_DATE + " TEXT"
                + ");";
        db.execSQL(storeDayTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // In case of database upgrade, add the new table
            String createStoreDayTableQuery = "CREATE TABLE IF NOT EXISTS " + STORE_DAY_TABLE_NAME + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + STORE_ID + " TEXT, "
                    + TOTAL_MOBILE + " INTEGER, "
                    + TOTAL_ELECTRONIC + " INTEGER, "
                    + TOTAL_NMP + " INTEGER, "
                    + TOTAL_PP + " INTEGER, "
                    + TOTAL_SERVICE + " INTEGER, "
                    + TOTAL_AC + " INTEGER, "
                    + TOTAL_CC + " INTEGER, "
                    + SALE_DATE + " TEXT"
                    + ");";
            db.execSQL(createStoreDayTableQuery);
        }
    }

    // Add sale to sales_day table
    void addSale(int mobileSale, int electronicSale, int protectionPlan, int prepaid, int serviceTicket, int applecare, int consumerCell, Date saleDate, String storeid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdft = new SimpleDateFormat("hh:mm a");
        String date = sdf.format(saleDate);
        String time = sdft.format(saleDate);
        cv.put(MOBILE_SALE, mobileSale);
        cv.put(ELECTRONIC_SALE, electronicSale);
        cv.put(PROTECTION_PLAN, protectionPlan);
        cv.put(PREPAID, prepaid);
        cv.put(SERVICE_TICKET, serviceTicket);
        cv.put(APPLE_CARE, applecare);
        cv.put(CONSUMER_CELL, consumerCell);
        cv.put(SALE_DATE, date);
        cv.put(SALE_TIME, time);
        cv.put(STORE_ID, storeid);

        long result = db.insert(SALES_TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, GlobalString.getInstance().returnToast(), Toast.LENGTH_SHORT).show();
        }
    }

    // Add StoreDay entry to store_day table
    void addStoreDay(String storeId, int totalMobile, int totalElectronic, int totalNMP, int totalPP, int totalService, int totalAC, int totalCC, String saleDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(STORE_ID, storeId);
        cv.put(TOTAL_MOBILE, totalMobile);
        cv.put(TOTAL_ELECTRONIC, totalElectronic);
        cv.put(TOTAL_NMP, totalNMP);
        cv.put(TOTAL_PP, totalPP);
        cv.put(TOTAL_SERVICE, totalService);
        cv.put(TOTAL_AC, totalAC);
        cv.put(TOTAL_CC, totalCC);
        cv.put(SALE_DATE, saleDate);

        long result = db.insert(STORE_DAY_TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed to Add Store Day", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Store Day Added", Toast.LENGTH_SHORT).show();
        }
    }

    // Read all data from store_day table
    Cursor readAllStoreDayData() {
        String query = "SELECT * FROM " + STORE_DAY_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateData(String row_id, String mobilesale, String electronicsale, String protectionPlan, String prepaid, String serviceTicket, String applecare, String consumerCell, String date, String time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(MOBILE_SALE, mobilesale);
        cv.put(ELECTRONIC_SALE, electronicsale);
        cv.put(PROTECTION_PLAN, protectionPlan);
        cv.put(PREPAID, prepaid);
        cv.put(SERVICE_TICKET, serviceTicket);
        cv.put(APPLE_CARE, applecare);
        cv.put(CONSUMER_CELL, consumerCell);
        cv.put(SALE_DATE, date);
        cv.put(SALE_TIME, time);

        long result = db.update(SALES_TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if(result ==-1){
            Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Updated!", Toast.LENGTH_SHORT).show();
        }
    }

    void updateStoreDay(String rowId, String storeId, String totalMobile, String totalElectronic, String totalNMP, String totalPP, String totalService, String totalAC, String totalCC, Date saleDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(saleDate);

        // Set the new values for the store day entry
        cv.put(STORE_ID, storeId);
        cv.put(TOTAL_MOBILE, totalMobile);
        cv.put(TOTAL_ELECTRONIC, totalElectronic);
        cv.put(TOTAL_NMP, totalNMP);
        cv.put(TOTAL_PP, totalPP);
        cv.put(TOTAL_SERVICE, totalService);
        cv.put(TOTAL_AC, totalAC);
        cv.put(TOTAL_CC, totalCC);
        cv.put(SALE_DATE, date);

        // Update the store_day entry with the new values
        long result = db.update(STORE_DAY_TABLE_NAME, cv, "_id=?", new String[]{rowId});
        if (result == -1) {
            Toast.makeText(context, "Failed to update Store Day", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Store Day Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    // Read all data from sales_day table
    Cursor readAllData() {
        String query = "SELECT * FROM " + SALES_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    Cursor returnCurrentData(String storeId, String date) {
        String query = "SELECT * FROM " + SALES_TABLE_NAME + " WHERE store_id = " + storeId + " AND sale_date = '" + date + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void deleteOneDay(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(STORE_DAY_TABLE_NAME, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Deleted!", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteQuery(String storeId, String date) {
        String query = "store_id = " + storeId + " AND sale_date = '" + date + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        if (db != null) {
            int rowsDeleted = db.delete(SALES_TABLE_NAME, query, null);
            if (rowsDeleted > 0) {
                Toast.makeText(context, "Successfully Deleted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
            }
        }
    }


    void deleteOneRow(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(SALES_TABLE_NAME, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Deleted!", Toast.LENGTH_SHORT).show();
        }
    }

    void clear() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + SALES_TABLE_NAME);
    }

    void clearStoreDayData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + STORE_DAY_TABLE_NAME);
    }
}
