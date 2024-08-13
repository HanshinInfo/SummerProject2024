package com.example.summerproject2024.Number;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.summerproject2024.R;

import java.util.List;

public class NumberView_adapter extends RecyclerView.Adapter<NumberView_adapter.MyViewHolder> {

    private List<String> itemList;
    private String[] mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    public NumberView_adapter(List<String> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.number_view_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;

        MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.NumberTextView);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    String getItem(int id)
    {  return mData[id];   }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}