package com.example.summerproject2024.Number;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.summerproject2024.R;

import java.util.List;

public class Professor_Num_detail extends Dialog {
    TextView d_aff, d_name, d_number, d_office;
    String aff, name, number, office;
    List<String> infoList;

    public Professor_Num_detail(@NonNull Context context, List<String> list) {
        super(context);
        this.infoList = list;
        d_aff = findViewById(R.id.pro_aff);
        d_name = findViewById(R.id.pro_name);
        d_number = findViewById(R.id.pro_number);
        d_office = findViewById(R.id.pro_office);

    }

    @Override
    protected void onCreate(Bundle savedInstantState){
        super.onCreate(savedInstantState);
        setContentView(R.layout.professor_detail);

        d_aff.setText(infoList.get(0));
        d_name.setText(infoList.get(1));
        d_number.setText(infoList.get(2));
        d_office.setText(infoList.get(3));
    }
}
