package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;
    private Context context;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single item in the RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        // Retrieve the data for the specific position from the list
        Post post = postList.get(position);

        // Set data to your item layout views
        holder.ratingStar.setRating(post.getRating());
        holder.serviceText.setText(post.getService());
    }

    @Override
    public int getItemCount() {
        // Return the total number of items in the list
        return postList.size();
    }

    // ViewHolder class to hold references to the views for a single item
    static class PostViewHolder extends RecyclerView.ViewHolder {
        RatingBar ratingStar;
        TextView serviceText;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views from the item layout
            ratingStar = itemView.findViewById(R.id.ratingStar);
            serviceText = itemView.findViewById(R.id.serviceText);
        }
    }
}
