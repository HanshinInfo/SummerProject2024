package com.example.summerproject2024.Mascot;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.summerproject2024.DB.DatabaseHelper;
import com.example.summerproject2024.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Mascot extends Fragment {

    ImageView image;

    TextView name;
    TextView gender;
    TextView hobby;
    TextView specialty;
    TextView dislike;
    TextView birthBackground;
    TextView source;

    Map<String, Integer> mascotImage;

    DatabaseHelper db;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mascot_fragment, container, false);

        image = (ImageView) view.findViewById(R.id.mascot_image);

        name = (TextView) view.findViewById(R.id.mascot_name);
        gender = (TextView) view.findViewById(R.id.mascot_gender);
        hobby = (TextView) view.findViewById(R.id.mascot_hobby);
        specialty = (TextView) view.findViewById(R.id.mascot_specialty);
        dislike = (TextView) view.findViewById(R.id.mascot_dislike);
        birthBackground = (TextView) view.findViewById(R.id.mascot_birth_bg);
        source = (TextView) view.findViewById(R.id.mascot_source);

        mascotImage = new HashMap<>();
        mascotImage.put(getResources().getString(R.string.mascot_cu_o), R.drawable.mascot_ku_o);
        mascotImage.put(getResources().getString(R.string.mascot_hangomi), R.drawable.mascot_hangomi);
        mascotImage.put(getResources().getString(R.string.mascot_buzzi), R.drawable.mascot_buzzi);
        mascotImage.put(getResources().getString(R.string.mascot_hanggu), R.drawable.mascot_hanggu);

        db = new DatabaseHelper(getContext());
        String name = this.getArguments().getString("mascot_name");
        SettingPage(name);
        return view;
    }

    @SuppressLint("SetTextI18n")
    public void SettingPage(String mascot_name){
        image.setImageResource(mascotImage.get(mascot_name));
        ArrayList<String> info = db.selectMascot(mascot_name);

        name.setText(info.get(0));
        gender.setText("성별 : " + info.get(1));
        hobby.setText("취미 : " + info.get(2));
        specialty.setText("특기 : " + info.get(3));
        dislike.setText("싫어하는 것 : " + info.get(4));
        birthBackground.setText(info.get(5));
        source.setText("출처 : 새내기한신가이드 > 마스코트 소개 > " + info.get(6));

        image.setAlpha(0.0f);
        name.setAlpha(0.0f);
        gender.setAlpha(0.0f);
        hobby.setAlpha(0.0f);
        specialty.setAlpha(0.0f);
        dislike.setAlpha(0.0f);
        birthBackground.setAlpha(0.0f);
        source.setAlpha(0.0f);

        image.animate().alpha(1.0f).setDuration(500);
        name.animate().alpha(1.0f).setDuration(500);
        gender.animate().alpha(1.0f).setDuration(500);
        hobby.animate().alpha(1.0f).setDuration(500);
        specialty.animate().alpha(1.0f).setDuration(500);
        dislike.animate().alpha(1.0f).setDuration(500);
        birthBackground.animate().alpha(1.0f).setDuration(500);
        source.animate().alpha(1.0f).setDuration(500);
    }
}
