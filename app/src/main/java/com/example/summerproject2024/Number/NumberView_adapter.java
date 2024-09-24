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

public class NumberView_adapter extends RecyclerView.Adapter<NumberView_adapter.MyViewHolder> {

    private ArrayList<NumberItem> itemList;
    private String[] mData;
    private LayoutInflater mInflater;
    public NumberView_adapter(ArrayList<NumberItem> itemList)
    {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.number_view_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NumberItem ni = itemList.get(position);
        holder.aff.setText(ni.aff);
        holder.name.setText(ni.name);
        holder.number.setText(ni.phoneNumber);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                University_Num_dialog und = new University_Num_dialog(context, ni);
                und.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView aff;
        TextView number;
        TextView name;
        CardView card_view;
        public MyViewHolder(View itemView) {
            super(itemView);

            aff = itemView.findViewById(R.id.aff);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            card_view = itemView.findViewById(R.id.layout_container);
        }
    }

}