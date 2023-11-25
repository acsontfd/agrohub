package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private ArrayList<ExampleFarm> mExampleList;
    private List<ExampleFarm> exampleListFull;

    private OnItemClickListener mListener;  // Listener member variable

    // Interface for item click events
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Setter method for the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public Button mMapView;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);
            mTextView3 = itemView.findViewById(R.id.textView3);
            mMapView = itemView.findViewById(R.id.mapButton);
        }
    }

    public ExampleAdapter(ArrayList<ExampleFarm> exampleList){
        mExampleList = exampleList;
    }

    public void filterList(ArrayList<ExampleFarm> filteredList) {
        mExampleList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.farm, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);

        evh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    int position = evh.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Notify the listener when an item is clicked
                        mListener.onItemClick(position);
                    }
                }
            }
        });

        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ExampleFarm currentItem = mExampleList.get(position);

        holder.mImageView.setImageResource(currentItem.getmImageResource());
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
        holder.mTextView3.setText("Address: " + currentItem.getmAddress());

        holder.mMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = currentItem.getmAddress();
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(holder.itemView.getContext().getPackageManager()) != null) {
                    holder.itemView.getContext().startActivity(mapIntent);
                } else {
                    Toast.makeText(holder.itemView.getContext(), "No map application found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mExampleList.size();
    }




}