package com.example.summerproject2024.Number;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.summerproject2024.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class University_Num_detail extends Dialog{
    TextView d_aff, d_subaff, d_name, d_number, d_office;
    List<String> infoList;

    public University_Num_detail(@NonNull Context context, List<String> list) {
        super(context);
        this.infoList = list;
        d_aff = findViewById(R.id.detail_aff);
        d_subaff = findViewById(R.id.detail_subaff);
        d_name = findViewById(R.id.detail_name);
        d_number = findViewById(R.id.detail_number);
        d_office = findViewById(R.id.detail_office);
    }


    @Override
    protected void onCreate(Bundle savedInstantState){
        super.onCreate(savedInstantState);
        setContentView(R.layout.number_detail);
        d_aff.setText(infoList.get(0));
        d_subaff.setText(infoList.get(1));
        d_name.setText(infoList.get(2));
        d_number.setText(infoList.get(3));
        d_office.setText(infoList.get(4));

    }
}
