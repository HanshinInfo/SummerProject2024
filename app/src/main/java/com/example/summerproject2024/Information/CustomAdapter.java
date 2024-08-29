package com.example.summerproject2024.Information;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.summerproject2024.R;

import java.util.List;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private List<Item> items;

    public CustomAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }


    @Override
    public Object getItem(int position) {
        return items.get(position);
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
        TextView categoryView = convertView.findViewById(R.id.category);
        TextView hoursView = convertView.findViewById(R.id.hours);


        Item item = items.get(position);

        nameView.setText(item.getName());
        categoryView.setText(item.getCategory());
        hoursView.setText(item.getHours());
        imageView.setImageResource(item.getImageResourceId());

        return convertView;
    }
}
