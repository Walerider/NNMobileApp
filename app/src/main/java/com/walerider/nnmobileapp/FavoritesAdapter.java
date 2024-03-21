package com.walerider.nnmobileapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    interface FavoritesItemClickListener{
        void onItemClick(FavoriteItem favoriteItem, int position);
    }
    private final LayoutInflater inflater;
    private final List<FavoriteItem> favoriteItems;
    private final FavoritesItemClickListener onClickListener;

    public FavoritesAdapter(Context context, List<FavoriteItem> favoriteItems,
                            FavoritesItemClickListener onClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.favoriteItems = favoriteItems;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public FavoritesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.favorite_rec_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.ViewHolder holder, int position) {
        FavoriteItem favoriteItem = favoriteItems.get(position);
        holder.titleView.setText(favoriteItem.getTitle());
        holder.imageView.setImageResource(favoriteItem.getMainImage());
        holder.addressView.setText(favoriteItem.getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onItemClick(favoriteItem,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteItems.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView titleView;
        final TextView addressView;
        ViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.image);
            titleView = view.findViewById(R.id.title);
            addressView = view.findViewById(R.id.address);
        }
    }
}