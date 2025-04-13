package com.example.tch057proj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tch057proj.R;
import com.example.tch057proj.modeles.Reservation;

import java.util.List;

public class ReservationAdapter extends BaseAdapter {

    private Context context;
    private List<Reservation> reservations;

    public ReservationAdapter(Context context, List<Reservation> reservations) {
        this.context = context;
        this.reservations = reservations;
    }

    @Override
    public int getCount() {
        return reservations.size();
    }

    @Override
    public Object getItem(int position) {
        return reservations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return reservations.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Reservation reservation = reservations.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.reservation_item, parent, false);
        }

        TextView tvVoyageId = convertView.findViewById(R.id.tvVoyageId);
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvPlaces = convertView.findViewById(R.id.tvPlaces);
        TextView tvPrix = convertView.findViewById(R.id.tvPrix);

        tvVoyageId.setText("Voyage ID : " + reservation.getVoyageId());
        tvDate.setText("Date : " + reservation.getDateVoyage());
        tvPlaces.setText("Nombre de places : " + reservation.getNbPlaces());
        tvPrix.setText("Prix total : " + reservation.getPrixTotal() + " $");

        return convertView;
    }
}
