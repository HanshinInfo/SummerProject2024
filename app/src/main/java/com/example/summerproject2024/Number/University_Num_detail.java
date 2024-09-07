package com.example.summerproject2024.Number;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.summerproject2024.R;

public class University_Num_detail extends AppCompatActivity {
    TextView d_aff, d_subaff, d_name, d_number, d_office;
    String aff, subaff, name, number, office;

    @Override
    protected void onCreate(Bundle savedInstantState){
        super.onCreate(savedInstantState);
        setContentView(R.layout.number_detail);

        d_aff = findViewById(R.id.detail_aff);
        d_subaff = findViewById(R.id.detail_aff);
        d_name = findViewById(R.id.detail_name);
        d_number = findViewById(R.id.detail_number);
        d_office = findViewById(R.id.detail_office);

        Intent intent = getIntent();

        aff = intent.getExtras().getString("aff");
        subaff = intent.getExtras().getString("subaff");
        name = intent.getExtras().getString("name");
        number = intent.getExtras().getString("number");
        office = intent.getExtras().getString("office");

        d_aff.setText(aff);
        d_subaff.setText(subaff);
        d_name.setText(name);
        d_number.setText(number);
        d_office.setText(office);
    }
}
