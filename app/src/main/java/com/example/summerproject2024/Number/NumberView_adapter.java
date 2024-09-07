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

public class NumberView_adapter extends RecyclerView.Adapter<NumberView_adapter.MyViewHolder> {

    private List<List<String>> itemList;
    private String[] mData;
    private LayoutInflater mInflater;
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
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.number_view_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setItem(itemList.get(position).get(0),itemList.get(position).get(1),itemList.get(position).get(2));
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mPosition = holder.getAdapterPosition();

                Context context = v.getContext();

                Intent detailNum = new Intent(context, University_Num_detail.class);
                detailNum.putExtra("aff", itemList.get(mPosition).get(0));
                detailNum.putExtra("subaff", itemList.get(mPosition).get(1));
                detailNum.putExtra("name", itemList.get(mPosition).get(2));
                detailNum.putExtra("number", itemList.get(mPosition).get(3));
                detailNum.putExtra("office", itemList.get(mPosition).get(4));

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
        TextView subaff;
        TextView name;
        CardView card_view;
        public MyViewHolder(View itemView) {
            super(itemView);

            aff = itemView.findViewById(R.id.aff);
            subaff = itemView.findViewById(R.id.subaff);
            name = itemView.findViewById(R.id.name);
            card_view = itemView.findViewById(R.id.layout_container);
        }
        public void setItem(String aff, String subaff, String name){
            this.aff.setText(aff);
            this.subaff.setText(subaff);
            this.name.setText(name);
        }
    }

}