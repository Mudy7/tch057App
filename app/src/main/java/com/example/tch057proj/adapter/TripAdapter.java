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

    public TripAdapter(Context context, List<Trip> trips) {
        super(context, android.R.layout.simple_spinner_item, trips);
        this.context = context;
        this.trips = trips;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }


    private View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);


        TextView label = row.findViewById(android.R.id.text1);


        label.setText(trips.get(position).getDate());

        return row;
    }
}
