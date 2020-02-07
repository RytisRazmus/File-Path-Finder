package com.razmus.daugiagijispirmas.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.razmus.daugiagijispirmas.R;


import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.imageViewHolder>{

    private ArrayList<String> foundDirectories;

    public DirectoryAdapter(ArrayList<String> foundDirectories)
    {
        this.foundDirectories = foundDirectories;
    }



    @Override
    public imageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.directory_item, parent, false);
        return new imageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull imageViewHolder holder, int i) {
        holder.dirTextView.setText(foundDirectories.get(i));
    }

    @Override
    public int getItemCount() {
        return foundDirectories.size();
    }

    public class imageViewHolder extends RecyclerView.ViewHolder {
        TextView dirTextView;
        public imageViewHolder(View itemView){
            super(itemView);
            dirTextView = itemView.findViewById(R.id.direcotryTextView);
        }
    }
}


