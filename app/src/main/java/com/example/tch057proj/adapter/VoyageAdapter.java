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

    public VoyageAdapter(Context context, List<Voyage> voyageList) {
        this.context = context;
        this.voyageList = voyageList;
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
        holder.tvResume.setText(voyage.getDescription());
        holder.tvPrix.setText(voyage.getPrix() + " $");

        Glide.with(context)
                .load(voyage.getImage_url())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imageVoyage);
    }

    @Override
    public int getItemCount() {
        return voyageList.size();
    }

    public static class VoyageViewHolder extends RecyclerView.ViewHolder {
        TextView tvDestination, tvResume, tvPrix;
        ImageView imageVoyage;

        public VoyageViewHolder(View itemView) {
            super(itemView);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvResume = itemView.findViewById(R.id.tvResume);
            tvPrix = itemView.findViewById(R.id.tvPrix);
            imageVoyage = itemView.findViewById(R.id.imageVoyage);
        }
    }
}
