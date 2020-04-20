package com.ask.eventman;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewAdapter2.MyViewHolder> {

    ArrayList<Uri> arr;
    Context context;

    public RecyclerViewAdapter2(ArrayList<Uri> arr, Context context) {
        this.arr = arr;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        MyViewHolder myViewHolder=new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with(context)
                .load(arr.get(position).toString())
                .centerCrop()
                .placeholder(R.drawable.app_icon)
                .into(holder.imgbtn);
        //System.out.println();
        //Toast.makeText(context, ""+arr.get(position).toString(), Toast.LENGTH_SHORT).show();
        arr.get(position).toString();
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgbtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgbtn=itemView.findViewById(R.id.imgitem);

        }
    }

}
