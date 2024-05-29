package com.example.afinal.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afinal.DetailFotballActivity;
import com.example.afinal.R;
import com.example.afinal.models.NewsModel;

import java.util.List;

public class FotballAdapter extends RecyclerView.Adapter<FotballAdapter.ViewHolder> {

    private List<NewsModel> newsModels;
    private Context context;

    public FotballAdapter(List<NewsModel> newsModels, Context context) {
        this.newsModels = newsModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsModel newsModel = newsModels.get(position);
        holder.headlineTextView.setText(newsModel.getHeadline());
        holder.descriptionTextView.setText(newsModel.getDescription());
        holder.publishedTextView.setText(newsModel.getPublished());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailFotballActivity.class);
            intent.putExtra("headline", newsModel.getHeadline());
            intent.putExtra("description", newsModel.getDescription());
            intent.putExtra("published", newsModel.getPublished());
            intent.putExtra("link", newsModel.getLink());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return newsModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView headlineTextView;
        public TextView descriptionTextView;
        public TextView publishedTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            headlineTextView = itemView.findViewById(R.id.TV_Judul);
            descriptionTextView = itemView.findViewById(R.id.TV_deskripsi);
            publishedTextView = itemView.findViewById(R.id.publishedTextView);
        }
    }
}
