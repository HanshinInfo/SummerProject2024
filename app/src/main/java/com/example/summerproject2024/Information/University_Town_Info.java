package com.example.summerproject2024.Information;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.summerproject2024.DB.DatabaseHelper;
import com.example.summerproject2024.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class University_Town_Info extends Fragment {

    //FragmentManager
    private static FragmentManager fragmentManager;
    private static FragmentTransaction transaction;

    private Town_Information_Page townInformationPage;

    private ListView list;
    private DatabaseHelper townDB;
    private ArrayList<String>[] town_Info;

    private HashMap<String, Integer> iconMap;

    private int day;
    private String[] dayList = {"일", "월", "화", "수", "목", "금", "토"};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.university_town_fragment, container, false);

        list = view.findViewById(R.id.list);

        day = getCurrentDay();

        townInformationPage = new Town_Information_Page();
        townDB = new DatabaseHelper(getContext());
        town_Info = townDB.selectBusinessZone();

        ArrayList<Item> items = new ArrayList<>();

        iconMap = new HashMap<String, Integer>();

        setIcon();

        for (int i = 0; i < town_Info.length; i++) {
            String name = town_Info[i].get(0);       // 이름
            String category = town_Info[i].get(1);   // 카테고리
            String hours = townDB.selectBusinessHours(name, dayList[day-1]);   // 영업 시간

            int imageResourceId = iconMap.get(category);

            items.add(new Item(name, category, hours, imageResourceId));
        }

        CustomAdapter adapter = new CustomAdapter(getContext(), items);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("townName", "한신식당");
                townInformationPage.setArguments(bundle);
                transaction.replace(R.id.fragment_container_view, townInformationPage).commitAllowingStateLoss();
            }
        });

        return view;
    }

    private int getCurrentDay() {
        Date currentDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }

    private void setIcon(){
        //restaurant
        iconMap.put("음식점", R.drawable.icon_restaurant);
        iconMap.put("샌드위치", R.drawable.icon_restaurant);
        iconMap.put("김밥전문 음식점", R.drawable.icon_restaurant);
        iconMap.put("패스트푸드 음식점", R.drawable.icon_restaurant);
        iconMap.put("한식당", R.drawable.icon_restaurant);
        iconMap.put("일식 음식점", R.drawable.icon_restaurant);

        //caffee
        iconMap.put("카페", R.drawable.icon_caffee);

        //shop
        iconMap.put("편의점", R.drawable.icon_shop);
        iconMap.put("식료품점", R.drawable.icon_shop);

        //beer
        iconMap.put("술집", R.drawable.icon_beer);
    }
}

