package com.example.summerproject2024.Information;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.summerproject2024.R;

import java.util.ArrayList;

public class Town_Info_Adapter extends BaseAdapter {

    private Context context;
    private ArrayList<String>[] items;

    public Town_Info_Adapter(Context context, ArrayList<String>[] items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items[0].size();
    }

    @Override
    public Object getItem(int position) {
        return items[0].get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.town_info_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.image);
        TextView nameView = convertView.findViewById(R.id.name);
        TextView addressView = convertView.findViewById(R.id.address);

        String name = items[0].get(position);
        String address = items[1].get(position);
        int imageResId = Integer.parseInt(items[2].get(position)); // Assuming images are stored as resource IDs in a string array

        nameView.setText(name);
        addressView.setText(address);
        imageView.setImageResource(imageResId);

        return convertView;
    }
}
