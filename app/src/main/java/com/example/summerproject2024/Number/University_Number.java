package com.example.summerproject2024.Number;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.summerproject2024.DB.DatabaseHelper;
import com.example.summerproject2024.R;

import java.util.ArrayList;
import java.util.List;

public class University_Number extends Fragment {
    public DatabaseHelper call_DB;
    ArrayList<String>[] num_Info;
    ArrayList<String>[] pro_Info;
    List<List<String>> itemList;
    RecyclerView recyclerView;
    NumberView_adapter num_adapter;
    ProView_adaptor pro_adapter;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.university_number_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        call_DB = new DatabaseHelper(getContext());

        //1
        callNumInfo();
        //
        //callProfessorInfo();
        return view;
    }
    private void callNumInfo(){
        num_Info = call_DB.selectCallNumbersAll();

        itemList = new ArrayList<>();
        int ind = 0;
        for(int i=0; i<=num_Info.length; i++){
            itemList.add(new ArrayList<String>());
        }

        for (int i = 0; i < num_Info.length; i++) {
            for(int j = 0; j < num_Info[i].size(); j++){
                itemList.get(ind).add(num_Info[i].get(j));
            }
            ind+=1;
        }
        num_adapter = new NumberView_adapter(itemList);
        recyclerView.setAdapter(num_adapter);
    }

    private void callProfessorInfo(){
        pro_Info = call_DB.selectProfessorAll();

        List<List<String>> itemList = new ArrayList<>();
        int ind = 0;

        for(int i=0; i<=pro_Info.length; i++){
            itemList.add(new ArrayList<String>());
        }
        for (int i = 0; i < pro_Info.length; i++) {
            for(int j = 0; j < pro_Info[i].size(); j++){
                itemList.get(ind).add(pro_Info[i].get(j));
            }
            ind+=1;
        }
        pro_adapter = new ProView_adaptor(itemList);
        recyclerView.setAdapter(pro_adapter);
    }
    private void showDialog(){

    }

}