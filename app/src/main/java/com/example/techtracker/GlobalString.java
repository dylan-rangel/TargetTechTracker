package com.example.techtracker;

import android.content.Context;
import android.content.SharedPreferences;

public class GlobalString {
    private static GlobalString instance;
    private String toastKey;
    private String storeId, startDate, endDate;
    private int mobile, electronics, nmp, activations;
    private static final String PREFS_NAME = "TechTrackerPrefs";  // SharedPreferences file name
    private static final String STORE_ID_KEY = "storeId";
    private static final String START_DATE_KEY = "startDate";
    private static final String END_DATE_KEY = "endDate";
    private static final String MOBILE_KEY = "mobile";
    private static final String ELECTRONICS_KEY = "electronics";
    private static final String NMP_KEY = "nmp";
    private static final String ACTIVATION_KEY = "activation";

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
        startDate = prefs.getString(START_DATE_KEY, "Pick ");
        endDate = prefs.getString(END_DATE_KEY, " Date");
        mobile = prefs.getInt(MOBILE_KEY, 0);
        electronics = prefs.getInt(ELECTRONICS_KEY, 0);
        nmp = prefs.getInt(NMP_KEY, 0);
        activations = prefs.getInt(ACTIVATION_KEY, 0);
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

    public void setStartDate(String s, Context context) {
        startDate = s;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(START_DATE_KEY, startDate);
        editor.apply();
    }

    public String returnStartDate(){
        return startDate;
    }

    public void setEndDate(String s, Context context) {
        endDate = s;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(END_DATE_KEY, endDate);
        editor.apply();
    }

    public String returnEndDate(){
        return endDate;
    }

    public void setMobile(int s, Context context) {
        mobile = s;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(MOBILE_KEY, mobile);
        editor.apply();
    }

    public int returnMobile(){
        return mobile;
    }

    public void setElectronics(int s, Context context) {
        electronics = s;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(ELECTRONICS_KEY, electronics);
        editor.apply();
    }

    public int returnElectronics(){
        return electronics;
    }
    public void setNmp(int s, Context context) {
        nmp = s;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(NMP_KEY, nmp);
        editor.apply();
    }

    public int returnNMP(){
        return nmp;
    }

    public void setActivations(int s, Context context) {
        activations = s;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(ACTIVATION_KEY, activations);
        editor.apply();
    }

    public int returnActivations(){
        return activations;
    }
    public void setToast(String s){toastKey = s;}
    public String returnToast(){return toastKey;}
}
