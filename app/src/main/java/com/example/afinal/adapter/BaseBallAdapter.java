package com.example.afinal.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afinal.DetailBaseBallActivity;
import com.example.afinal.R;
import com.example.afinal.models.NewsModel;

import java.util.List;

public class BaseBallAdapter extends RecyclerView.Adapter<BaseBallAdapter.ViewHolder> {

    private List<NewsModel> newsModels;
    Context context;

    public BaseBallAdapter(List<NewsModel> newsModels, Context context) {
        this.newsModels = newsModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news3, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsModel baseBallModel = newsModels.get(position);
        holder.headlineTextView.setText(baseBallModel.getHeadline());
        holder.descriptionTextView.setText(baseBallModel.getDescription());
        holder.publishedTextView.setText(baseBallModel.getPublished());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailBaseBallActivity.class);
            intent.putExtra("headline", baseBallModel.getHeadline());
            intent.putExtra("description", baseBallModel.getDescription());
            intent.putExtra("published", baseBallModel.getPublished());
            intent.putExtra("link", baseBallModel.getLink());
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

