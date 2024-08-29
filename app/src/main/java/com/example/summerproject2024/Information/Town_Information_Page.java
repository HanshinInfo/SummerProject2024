package com.example.summerproject2024.Information;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.summerproject2024.DB.DatabaseHelper;
import com.example.summerproject2024.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Town_Information_Page extends Fragment {

    private DatabaseHelper townDB;

    ImageView image;
    TextView name;
    TextView location;
    TextView hours;
    TextView number;
    String link;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.town_info_detail_page, container, false);

        image = (ImageView) view.findViewById(R.id.image);
        name = (TextView) view.findViewById(R.id.name);
        location = (TextView) view.findViewById(R.id.location);
        hours = (TextView) view.findViewById(R.id.hours);
        number = (TextView) view.findViewById(R.id.number);

        townDB = new DatabaseHelper(getContext());

        String townName = this.getArguments().getString("townName");

        ArrayList<String> data = townDB.selectTownInfo(townName);

        name.setText(townName);
        location.setText(data.get(0));
        number.setText(data.get(1));
        link = data.get(2);

        return view;
    }
}
