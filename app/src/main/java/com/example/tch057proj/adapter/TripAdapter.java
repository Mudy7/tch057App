package com.example.tch057proj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tch057proj.R;
import com.example.tch057proj.modeles.Trip;

import java.util.List;

public class TripAdapter extends ArrayAdapter<Trip> {

    private final Context context;
    private final List<Trip> trips;

    // Constructor for the adapter
    public TripAdapter(Context context, List<Trip> trips) {
        super(context, android.R.layout.simple_spinner_item, trips);
        this.context = context;
        this.trips = trips;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent); // For dropdown view
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent); // For normal view
    }

    // Custom method to handle how each Trip is displayed in the Spinner
    private View getCustomView(int position, View convertView, ViewGroup parent) {
        // Inflating the layout for the spinner items
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);

        // Finding the TextView where we want to display the Trip's date
        TextView label = row.findViewById(android.R.id.text1);

        // Setting the Trip date text in the spinner
        label.setText(trips.get(position).getDate());  // Assuming `Trip` has a `getDate()` method

        return row;
    }
}
