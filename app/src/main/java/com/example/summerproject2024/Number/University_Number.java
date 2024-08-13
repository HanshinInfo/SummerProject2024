package com.example.summerproject2024.Number;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.summerproject2024.DB.DatabaseHelper;
import com.example.summerproject2024.R;

import java.util.ArrayList;
import java.util.List;

public class University_Number extends Fragment{
    public DatabaseHelper call_DB;
    ArrayList<String>[] num_Info;
    ArrayList<String>[] pro_Info;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.university_number_fragment, container, false);
        call_DB = new DatabaseHelper(getContext());
        num_Info = call_DB.selectCallNumbersAll();
        pro_Info = call_DB.selectProfessorAll();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<String> itemList = new ArrayList<>();
        for (int i = 0; i < num_Info.length; i++) {
            String str = "";
            for(int j = 0; j < num_Info[i].size(); j++){
                if(j==1 || j==2)
                    str+="\n";
                str = str + num_Info[i].get(j)+" ";
            }
            itemList.add(str);
        }

        for (int i = 0; i < pro_Info.length; i++) {
            String str = "";
            for(int j = 0; j < pro_Info[i].size(); j++){
                if(j==2 || j==3)
                    str+="\n";
                str = str + pro_Info[i].get(j)+" ";
            }
            itemList.add(str);
        }

        NumberView_adapter adapter = new NumberView_adapter(itemList);
        recyclerView.setAdapter(adapter);
        return view;
    }

}