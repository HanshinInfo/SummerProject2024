package com.example.summerproject2024.Information;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.summerproject2024.DB.DatabaseHelper;
import com.example.summerproject2024.R;

import java.util.ArrayList;

public class University_Town_Info extends Fragment {

    public DatabaseHelper townDB;
    ArrayList<String>[] town_Info;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.university_town_fragment, container, false);

        townDB = new DatabaseHelper(getContext());
        town_Info = townDB.selectBusinessZone();
        
//        참고용 코드
//        TextView test = (TextView) view.findViewById(R.id.test);
//        String testList = "";
//
//        for(int i = 0; i < town_Info.length; i++){
//            for(int j = 0; j < town_Info[i].size(); j++){
//                testList += town_Info[i].get(j);
//            }
//            testList += "\n";
//        }
//
//        test.setText(testList);

        return view;
    }
}