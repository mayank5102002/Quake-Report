package com.example.android.quakereport;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class EarthQuakeAdapter extends ArrayAdapter {


    public EarthQuakeAdapter(Activity context, ArrayList<EarthQuake> earthQuakes) {
        super(context,0, earthQuakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        EarthQuake current = (EarthQuake) getItem(position);

        TextView mag = (TextView) listItemView.findViewById(R.id.magnitude);
        DecimalFormat formatter = new DecimalFormat("0.0");
        String output = formatter.format(current.getMagnitude());
        mag.setText(output);

        TextView near = (TextView) listItemView.findViewById(R.id.near);
        TextView city = (TextView) listItemView.findViewById(R.id.city);
        String originalLocation = current.getCity();
        String location;
        String locationOffset;
        String separator = " of ";
        if(originalLocation.contains(separator)){
        String[] parts = originalLocation.split(separator);
        locationOffset = parts[0] + separator;
        location = parts[1];}
        else{
            locationOffset = getContext().getString(R.string.near_the);
            location = originalLocation;
        }
        near.setText(locationOffset);
        city.setText(location);

        Date dateObject = new Date(current.getDate());

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        String formattedDate = current.formatDate(dateObject);
        dateView.setText(formattedDate);

        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        String formattedTime = current.formatTime(dateObject);
        timeView.setText(formattedTime);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) mag.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(current.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        return listItemView;
    }
    public int getMagnitudeColor(double mag) {
        int colorID=0;
        int magnitude = (int) Math.floor(mag);
        switch(magnitude){
            case 0:
            case 1:colorID = R.color.magnitude1;
            break;
            case 2:colorID = R.color.magnitude2;
            break;
            case 3:colorID = R.color.magnitude3;
            break;
            case 4:colorID = R.color.magnitude4;
            break;
            case 5:colorID = R.color.magnitude5;
            break;
            case 6:colorID = R.color.magnitude6;
            break;
            case 7:colorID = R.color.magnitude7;
            break;
            case 8:colorID = R.color.magnitude8;
            break;
            case 9:colorID = R.color.magnitude9;
            break;
            case 10:colorID = R.color.magnitude10plus;
            break;
        }
        return ContextCompat.getColor(getContext(),colorID);
    }
}
