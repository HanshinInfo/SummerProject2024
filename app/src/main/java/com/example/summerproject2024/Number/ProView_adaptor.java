package com.example.summerproject2024.Number;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.summerproject2024.R;

import java.util.List;

public class ProView_adaptor extends RecyclerView.Adapter<ProView_adaptor.MyViewHolder> {

    private List<List<String>> itemList;
    private LayoutInflater mInflater;
    public ProView_adaptor(List<List<String>> itemList) {
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
        holder.setItem(itemList.get(position).get(0),itemList.get(position).get(1));
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mPosition = holder.getAdapterPosition();

                Context context = v.getContext();
                Professor_Num_detail pnd = new Professor_Num_detail(context, itemList.get(mPosition));

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
        public void setItem(String aff, String name){
            this.aff.setText(aff);
            this.name.setText(name);
        }
    }

}
