package com.example.techtracker;

public class GlobalString {
    private static GlobalString instance;
    private String toastKey;

    private GlobalString() {}

    public static GlobalString getInstance() {
        if (instance == null) {
            instance = new GlobalString();
        }
        return instance;
    }

    public void setToast(String value) {
        toastKey = value;
    }

    public String returnToast() {
        return toastKey;
    }
}

