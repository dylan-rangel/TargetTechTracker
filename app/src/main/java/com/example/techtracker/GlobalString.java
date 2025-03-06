package com.example.techtracker;

import android.content.Context;
import android.content.SharedPreferences;

public class GlobalString {
    private static GlobalString instance;
    private String toastKey;
    private String storeId;
    private static final String PREFS_NAME = "TechTrackerPrefs";  // SharedPreferences file name
    private static final String STORE_ID_KEY = "storeId";  // Key for storing storeId

    private GlobalString() {}

    public static GlobalString getInstance() {
        if (instance == null) {
            instance = new GlobalString();
        }
        return instance;
    }

    // Initialize the SharedPreferences in the context (usually in an Activity or Application)
    public void init(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        storeId = prefs.getString(STORE_ID_KEY, "0001");  // Default value is "T0001"
    }

    public void setStoreId(String s, Context context) {
        storeId = s;
        // Save the storeId to SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(STORE_ID_KEY, storeId);
        editor.apply();  // Asynchronously commit the changes
    }

    public String returnStoreId() {
        return storeId;
    }

    public void setToast(String s){toastKey = s;}
    public String returnToast(){return toastKey;}
}
