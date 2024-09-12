package com.example.summerproject2024.Number;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.summerproject2024.R;

import java.util.ArrayList;
import java.util.List;

public class University_Num_dialog extends Dialog{
    TextView d_aff, d_subaff, d_name, d_number, d_office;
    NumberItem info;

    public University_Num_dialog(@NonNull Context context, NumberItem ni) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        d_aff = findViewById(R.id.detail_aff);
        d_subaff = findViewById(R.id.detail_subaff);
        d_name = findViewById(R.id.detail_name);
        d_number = findViewById(R.id.detail_number);
        d_office = findViewById(R.id.detail_office);
        this.info = ni;
    }


    @Override
    protected void onStart(){
        super.onStart();
        setContentView(R.layout.number_detail);
        d_aff.setText(info.aff);
        d_subaff.setText(info.subaff);
        d_name.setText(info.name);
        d_number.setText(info.phoneNumber);
        d_office.setText(info.office);

    }
}
