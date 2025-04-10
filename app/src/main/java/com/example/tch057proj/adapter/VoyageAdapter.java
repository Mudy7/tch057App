package com.example.tch057proj.adapter;

import android.content.Context;
import android.view.*;
import android.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.tch057proj.R;
import com.example.tch057proj.modeles.Voyage;

import java.util.List;

public class VoyageAdapter extends RecyclerView.Adapter<VoyageAdapter.VoyageViewHolder> {

    private Context context;
    private List<Voyage> voyageList;
    private OnItemClickListener onItemClickListener;  // Add OnItemClickListener

    public interface OnItemClickListener {
        void onItemClick(Voyage voyage);  // Define method to handle item click
    }

    public VoyageAdapter(Context context, List<Voyage> voyageList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.voyageList = voyageList;
        this.onItemClickListener = onItemClickListener;  // Set the listener
    }

    @Override
    public VoyageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_voyage, parent, false);
        return new VoyageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VoyageViewHolder holder, int position) {
        Voyage voyage = voyageList.get(position);

        holder.tvDestination.setText(voyage.getDestination());
        holder.tvPrix.setText(voyage.getPrix() + " $");

        Glide.with(context)
                .load(voyage.getImage_url())
                .placeholder(R.drawable.ic_launcher_background)  // Provide placeholder image
                .into(holder.imageVoyage);

        // Set click listener on the item
        holder.itemView.setOnClickListener(v -> {
            onItemClickListener.onItemClick(voyage);  // Pass the clicked Voyage to the listener
        });
    }

    @Override
    public int getItemCount() {
        return voyageList.size();
    }

    public static class VoyageViewHolder extends RecyclerView.ViewHolder {
        TextView tvDestination, tvPrix;  // Removed tvResume
        ImageView imageVoyage;

        public VoyageViewHolder(View itemView) {
            super(itemView);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvPrix = itemView.findViewById(R.id.tvPrix);
            imageVoyage = itemView.findViewById(R.id.imageVoyage);
        }
    }
}
