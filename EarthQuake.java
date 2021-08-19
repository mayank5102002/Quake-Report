package com.example.android.quakereport;

import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EarthQuake {
    private double mMagnitude;
    private String mCity;
    private long mDate;
    private String murl;

    public EarthQuake(String city, double magnitude, long date, String url){
        mMagnitude = magnitude;
        mCity = city;
        mDate = date;
        murl = url;
    }

    public double getMagnitude(){return mMagnitude;}
    public String getCity(){return mCity;}
    public long getDate(){return mDate;}
    public String geturl(){return murl;}
    public String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(dateObject);
    }
    public String formatTime(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        return dateFormat.format(dateObject);
    }
}
