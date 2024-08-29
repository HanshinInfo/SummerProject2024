package com.example.summerproject2024.Menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.summerproject2024.R;

import java.util.ArrayList;

public class MenuLinkAdapter extends BaseAdapter{

    Context context;
    LayoutInflater myInf;
    ArrayList<MenuLinkGroup> linkList;

    public MenuLinkAdapter(Context context, ArrayList<MenuLinkGroup> linkList){
        this.context = context;
        this.linkList = linkList;
        myInf = LayoutInflater.from(this.context);
    }
    @Override
    public int getCount() {
        return linkList.size();
    }

    @Override
    public MenuLinkGroup getItem(int position) {
        return linkList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = myInf.inflate(R.layout.menu_link_list, null);

        TextView text = (TextView) view.findViewById(R.id.menu_link);
        text.setText(linkList.get(position).getLink());

        return view;
    }
}
