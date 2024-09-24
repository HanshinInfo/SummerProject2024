package com.example.summerproject2024.Number;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.summerproject2024.MainActivity;
import com.example.summerproject2024.R;

import java.util.ArrayList;
import java.util.List;

public class University_Num_dialog extends Dialog{
    TextView d_aff, d_subaff, d_name, d_number, d_office, subaff;
    NumberItem info;

    public University_Num_dialog(@NonNull Context context, NumberItem ni) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.number_detail);
        d_aff = findViewById(R.id.detail_aff);
        d_subaff = findViewById(R.id.detail_subaff);
        d_name = findViewById(R.id.detail_name);
        d_number = findViewById(R.id.detail_number);
        d_office = findViewById(R.id.detail_office);
        subaff = findViewById(R.id.subaff);
        this.info = ni;
    }

    protected void onStart(){
        super.onStart();

        if(info.subaff.isEmpty()){
            callProInfo();
        }
        else{
            callNumInfo();
        }
    }

    public void callNumInfo(){
        d_subaff.setVisibility(View.VISIBLE);
        subaff.setVisibility(View.VISIBLE);

        d_aff.setText(info.aff);
        d_subaff.setText(info.subaff);
        d_name.setText(info.name);
        d_number.setText(info.phoneNumber);
        d_office.setText(info.office);
    }

    public void callProInfo(){
        d_subaff.setVisibility(View.GONE);
        subaff.setVisibility(View.GONE);

        d_aff.setText(info.aff);
        d_subaff.setText(info.subaff);
        d_name.setText(info.name);
        d_number.setText(info.phoneNumber);
        d_office.setText(info.office);
    }
}
