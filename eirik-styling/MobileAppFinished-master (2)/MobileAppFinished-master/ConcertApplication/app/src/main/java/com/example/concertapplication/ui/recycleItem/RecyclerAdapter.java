package com.example.concertapplication.ui.recycleItem;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.concertapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private ArrayList<RecycleItem> itemList;

    private OnConcertListener onConcertListener;

    // onclick method to enter a single concert view
    public interface OnConcertListener{
        void onConcertClick(int position);
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView;
        public TextView textView1;
        public TextView textView2;

        OnConcertListener onConcertListener;

        public RecyclerViewHolder(@NonNull View itemView, OnConcertListener onConcertListener) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView1 = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);

            this.onConcertListener = onConcertListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onConcertListener.onConcertClick(getAdapterPosition());
        }
    }

    public RecyclerAdapter(ArrayList<RecycleItem> itemList, OnConcertListener onConcertListener){
        this.itemList = itemList;
        this.onConcertListener = onConcertListener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(v, onConcertListener);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        RecycleItem recycleItem = itemList.get(position);
        Picasso.get().load(Uri.parse(recycleItem.getImageResource())).into(holder.imageView);
        holder.textView1.setText(recycleItem.getText1());
        holder.textView2.setText(recycleItem.getText2());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
