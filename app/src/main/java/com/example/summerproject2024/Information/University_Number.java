package com.example.summerproject2024.Information;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

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

public class University_Number extends Fragment{
    public DatabaseHelper num_DB;
    ArrayList<String>[] num_Info;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.university_number_fragment, container, false);
        num_DB = new DatabaseHelper(getContext());
        num_Info = num_DB.selectCallNumbersAll();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
        return view;
    }

}