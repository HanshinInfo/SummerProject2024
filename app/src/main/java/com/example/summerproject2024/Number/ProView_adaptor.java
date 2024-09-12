package com.example.summerproject2024.Number;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.summerproject2024.R;

import java.util.ArrayList;
import java.util.List;

public class ProView_adaptor extends RecyclerView.Adapter<ProView_adaptor.MyViewHolder> {

    private ArrayList<ProItem> itemList;
    private LayoutInflater mInflater;
    public ProView_adaptor(ArrayList<ProItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.professor_view_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProItem pi = itemList.get(position);
        holder.aff.setText(pi.aff);
        holder.name.setText(pi.name);
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mposion = holder.getAdapterPosition();
                Context context = v.getContext();
                Professor_Num_dialog pnd = new Professor_Num_dialog(context, pi);
                pnd.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        TextView aff;
        TextView name;
        CardView card_view;
        public MyViewHolder(View itemView) {
            super(itemView);

            aff = itemView.findViewById(R.id.aff);
            name = itemView.findViewById(R.id.name);
            card_view = itemView.findViewById(R.id.layout_container);
        }
    }



}
