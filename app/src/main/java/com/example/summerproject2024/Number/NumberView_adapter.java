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

    private List<List<String>> itemList;
    private String[] mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    public NumberView_adapter(List<List<String>> itemList) {
        this.itemList = itemList;
    }

    public interface OnItemClickListener {
        void onItemClicked(int position, String data);
    }

    // OnItemClickListener 참조 변수 선언
    private OnItemClickListener itemClickListener;

    // OnItemClickListener 전달 메소드
    public void setOnItemClickListener (OnItemClickListener listener) {
        itemClickListener = listener;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.name);
        }

        public TextView getTextView() {
            return textView;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.number_view_item, parent, false);
        NumberView_adapter.ViewHolder viewHolder = new NumberView_adapter.ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = "";
                int position = viewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    data = viewHolder.getTextView().getText().toString();
                }
                itemClickListener.onItemClicked(position, data);
            }
        });

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //holder.textView.setText(itemList.get(position));
        holder.aff.setText(itemList.get(position).get(0));
        holder.subaff.setText(itemList.get(position).get(1));
        holder.name.setText(itemList.get(position).get(2));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        TextView aff;
        TextView subaff;
        TextView name;
        MyViewHolder(View itemView) {
            super(itemView);
            //textView = itemView.findViewById(R.id.NumberTextView);
            aff = itemView.findViewById(R.id.aff);
            subaff = itemView.findViewById(R.id.subaff);
            name = itemView.findViewById(R.id.name);
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