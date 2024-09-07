package com.example.summerproject2024.Information;

import android.content.Intent;
import android.net.Uri;
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

public class Town_Information_Page extends Fragment {

    private DatabaseHelper townDB;

    ImageView image;
    TextView name;
    TextView location;
    TextView hours;
    TextView number;
    String link;

    HashMap<String, Integer> imageMap;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.town_info_detail_page, container, false);

        image = view.findViewById(R.id.image);
        name = view.findViewById(R.id.name);
        location = view.findViewById(R.id.location);
        hours = view.findViewById(R.id.hours);
        number = view.findViewById(R.id.number);

        townDB = new DatabaseHelper(getContext());

        String townName = this.getArguments().getString("townName");

        ArrayList<String> data = townDB.selectTownInfo(townName);

        imageMap = new HashMap<>();
        createImageMap();

        String hour = "";
        HashMap<String, String> hoursMap = townDB.selectHours(townName);
        String[] days = {"월", "화", "수", "목", "금", "토", "일"};

        if (hoursMap.isEmpty()) {
            hour = "정보 없음";
        } else {
            for (int i = 0; i < days.length - 1; i++) {
                hour += days[i] + hoursMap.get(days[i]) + "\n";
            }
            hour += days[days.length - 1] + hoursMap.get(days[days.length - 1]);
        }

        image.setImageResource(imageMap.get(townName));
        name.setText(townName);
        location.setText(data.get(0));
        hours.setText(hour);
        number.setText(data.get(1));
        link = data.get(2);

        // location(주소) 클릭 시 link로 이동
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(browserIntent);
            }
        });

        return view;
    }

    private void createImageMap() {
        imageMap.put("행복한 콩박사", R.drawable.shop_happy_bean);
        imageMap.put("한신식당", R.drawable.shop_hanshin_restaurant);
        imageMap.put("해우리", R.drawable.shop_hae_uri);
        imageMap.put("태리로제떡볶이", R.drawable.shop_taeri_tteokbockki);
        imageMap.put("우리반점", R.drawable.shop_uribanjum);
        imageMap.put("금덕이네", R.drawable.shop_guemduk);
        imageMap.put("이삭토스트", R.drawable.shop_isaac_toast);
        imageMap.put("복고다방한신대점", R.drawable.shop_bokgo_cafe);
        imageMap.put("미소김밥", R.drawable.shop_smile_gimbap);
        imageMap.put("맘스터치한신대점", R.drawable.shop_moms_touch);
        imageMap.put("CU 편의점", R.drawable.shop_cu);
        imageMap.put("요거프레스", R.drawable.shop_yoger_presso);
        imageMap.put("봉구스밥버거 한신대점", R.drawable.shop_bongousse);
        imageMap.put("나누리", R.drawable.shop_nanuri);
        imageMap.put("코리엔탈깻잎두마리치킨", R.drawable.shop_kketnypdak);
        imageMap.put("GS25 한신대점", R.drawable.shop_gs25);
        imageMap.put("진현가든", R.drawable.shop_jin_heon_garden);
        imageMap.put("해뜨는집", R.drawable.shop_sunrise_house);
        imageMap.put("몽상", R.drawable.shop_mong_sang);
        imageMap.put("카페 리메인", R.drawable.shop_cafe_remain);
        imageMap.put("내가찜한닭", R.drawable.shop_my_jjim_chicken);
        imageMap.put("양산골 주막", R.drawable.shop_yangsan_goal);
        imageMap.put("듬뿍만두샤브", R.drawable.shop_mandu_shabu_shabu);
        imageMap.put("현대E마트", R.drawable.shop_hyundai_e_mart);
        imageMap.put("드렁큰할매", R.drawable.shop_drunken_grandma);
    }
}
