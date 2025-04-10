package com.example.tch057proj.adapter;

import android.content.Context;
import android.view.*;
import android.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.tch057proj.R;
import com.example.tch057proj.modeles.Voyage;

import java.util.List;

public class MiniVoyageAdapter extends RecyclerView.Adapter<MiniVoyageAdapter.MiniViewHolder> {

    private final Context context;
    private final List<Voyage> voyages;
    private final OnItemClickListener onItemClickListener;  // Added

    // Interface for handling item clicks
    public interface OnItemClickListener {
        void onItemClick(Voyage voyage);
    }

    // Updated constructor to accept OnItemClickListener
    public MiniVoyageAdapter(Context context, List<Voyage> voyages, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.voyages = voyages;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MiniViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_voyage_mini, parent, false);
        return new MiniViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MiniViewHolder holder, int position) {
        Voyage voyage = voyages.get(position);
        holder.tvDestination.setText(voyage.getDestination());

        Glide.with(context)
                .load(voyage.getImage_url())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imageVoyage);

        // Set onClickListener to pass the voyage object to the listener
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(voyage));
    }

    @Override
    public int getItemCount() {
        return voyages.size();
    }

    public static class MiniViewHolder extends RecyclerView.ViewHolder {
        ImageView imageVoyage;
        TextView tvDestination;

        public MiniViewHolder(View itemView) {
            super(itemView);
            imageVoyage = itemView.findViewById(R.id.imageVoyage);
            tvDestination = itemView.findViewById(R.id.tvDestination);
        }
    }
}
