package com.example.summerproject2024.Information;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.summerproject2024.DB.DatabaseHelper;
import com.example.summerproject2024.MyAdapter;
import com.example.summerproject2024.R;

import java.util.ArrayList;
import java.util.List;

public class University_Number extends AppCompatActivity{
    public DatabaseHelper num_DB;
    ArrayList<String>[] num_Info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        num_DB = new DatabaseHelper(getBaseContext());
        num_Info = num_DB.selectCallNumbersAll();
        RecyclerView recyclerView = findViewById(R.id.rview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> itemList = new ArrayList<>();
        for (int i = 0; i < num_Info.length; i++) {
            String str = "";
            for(int j = 0; j < num_Info[i].size(); j++){
                str = str + num_Info[i].get(j)+" ";
            }
            itemList.add(str);
        }

        MyAdapter adapter = new MyAdapter(itemList);
        recyclerView.setAdapter(adapter);
    }
}