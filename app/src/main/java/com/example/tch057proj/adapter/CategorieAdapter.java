package com.example.tch057proj.adapter;

import android.content.Context;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tch057proj.R;
import com.example.tch057proj.modeles.Categorie;

import java.util.List;

public class CategorieAdapter extends RecyclerView.Adapter<CategorieAdapter.CategorieViewHolder> {

    public interface OnCategorieClickListener {
        void onCategorieClick(String categorie);
    }

    private Context context;
    private List<Categorie> categorieList;
    private OnCategorieClickListener listener;

    public CategorieAdapter(Context context, List<Categorie> categorieList, OnCategorieClickListener listener) {
        this.context = context;
        this.categorieList = categorieList;
        this.listener = listener;
    }

    @Override
    public CategorieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategorieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategorieViewHolder holder, int position) {
        Categorie categorie = categorieList.get(position);
        holder.tvCategoryName.setText(categorie.getNom());

        Glide.with(context)
                .load(categorie.getImageUrl())
                .into(holder.imageCategory);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategorieClick(categorie.getNom());
            }
        });
    }

    @Override
    public int getItemCount() {
        return categorieList.size();
    }

    public static class CategorieViewHolder extends RecyclerView.ViewHolder {
        ImageView imageCategory;
        TextView tvCategoryName;

        public CategorieViewHolder(View itemView) {
            super(itemView);
            imageCategory = itemView.findViewById(R.id.imageCategory);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}
