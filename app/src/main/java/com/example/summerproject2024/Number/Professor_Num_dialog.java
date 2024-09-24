package com.example.summerproject2024.Number;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.summerproject2024.R;

import java.util.List;

public class Professor_Num_dialog extends Dialog {
    TextView d_aff, d_name, d_number, d_office;
    String aff, name, number, office;
    ProItem info;

    public Professor_Num_dialog(@NonNull Context context, ProItem list) {
        super(context);
        this.info = list;
        setContentView(R.layout.professor_detail);
        d_aff = findViewById(R.id.pro_aff);
        d_name = findViewById(R.id.pro_name);
        d_number = findViewById(R.id.pro_number);
        d_office = findViewById(R.id.pro_office);


    }

    @Override
    protected void onStart(){
        super.onStart();
        d_aff.setText(info.aff);
        d_name.setText(info.name);
        d_number.setText(info.phoneNum);
        d_office.setText(info.office);
    }
}
