package com.example.summerproject2024.Campus_map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.summerproject2024.DB.DatabaseHelper;
import com.example.summerproject2024.R;

public class Campus_map extends Fragment {

    //Database
    DatabaseHelper db;

    String building_code;

    //ScrollView
    ScrollView scrollView;
    HorizontalScrollView horizontalScrollView;

    //ImageView
    ImageView campus_map;
    Bitmap bitmap;
    Bitmap resized;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.campus_map, container, false);

        //Database
        db = new DatabaseHelper(getContext());

        //Campus_map
        scrollView = (ScrollView) view.findViewById(R.id.ScrollView);
        horizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.HorizontalScrollView);

        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.campus_map);
        resized = Bitmap.createScaledBitmap(bitmap, 2800, 2000, true);
        campus_map = (ImageView) view.findViewById(R.id.campus_map);
        campus_map.setImageBitmap(resized);

        campus_map.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    building_code = db.selectBuildingCode(x, y);
                    Log.v("building_code?", building_code);

                    if(!building_code.equals("None")){
                        Campus_map_dialog campus_map_dialog = new Campus_map_dialog(getContext());
                        campus_map_dialog.building_code = building_code;

                        campus_map_dialog.show();
                    }
                }
                return false;
            }
        });



        return view;
    }



}
