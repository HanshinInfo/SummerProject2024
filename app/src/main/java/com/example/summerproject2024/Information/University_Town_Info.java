package com.example.summerproject2024.Information;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.summerproject2024.DB.DatabaseHelper;
import com.example.summerproject2024.R;

import java.util.ArrayList;
import java.util.List;

public class University_Town_Info extends Fragment {

    private ListView list;
    private DatabaseHelper townDB;
    private ArrayList<String>[] town_Info;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.university_town_fragment, container, false);

        list = view.findViewById(R.id.list);

        townDB = new DatabaseHelper(getContext());
        town_Info = townDB.selectBusinessZone();


        List<Item> items = new ArrayList<>();



        for (int i = 0; i < town_Info.length; i++) {
            String name = town_Info[i].get(0);       // 이름
            String location = town_Info[i].get(1);   // 위치
            String category = town_Info[i].get(2);   // 카테고리

            int imageResourceId = R.drawable.android_test;

            items.add(new Item(name, location, category, imageResourceId));
        }

        CustomAdapter adapter = new CustomAdapter(getContext(), items);
            list.setAdapter(adapter);



        return view;
    }

}

