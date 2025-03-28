package com.example.summerproject2024.Calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.summerproject2024.R;

import java.util.Calendar;
import java.util.List;

public class Calendar_fragment extends Fragment {

    private RecyclerView recyclerView;
    private CalendarAdapter adapter;
    private TextView monthText;
    private Calendar currentCalendar;
    private ScheduleManager scheduleManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.calendar_fragment, container, false);

        monthText = view.findViewById(R.id.monthText);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));

        currentCalendar = Calendar.getInstance(); // 초기화

        List<Integer> days = CalendarUtils.generateCalendarData(currentCalendar);
        int currentMonth = currentCalendar.get(Calendar.MONTH);

        // 어댑터 설정 (먼저 어댑터를 초기화)
        adapter = new CalendarAdapter(getContext(), days, currentMonth, null); // 초기화 시점에서 scheduleManager를 null로 설정
        recyclerView.setAdapter(adapter);

        // ScheduleManager 초기화
        scheduleManager = new ScheduleManager(getContext(), adapter, currentCalendar); // ScheduleManager 초기화
        adapter.setScheduleManager(scheduleManager); // 어댑터에 scheduleManager 설정

        CalendarUtils.updateMonthText(currentCalendar, monthText);

        // 학사 일정 크롤링 및 저장
        scheduleManager.fetchAndStoreAcademicSchedules();

        // 버튼 클릭 리스너 설정
        view.findViewById(R.id.prevMonthButton).setOnClickListener(v -> CalendarUtils.moveToPreviousMonth(currentCalendar, adapter, monthText));
        view.findViewById(R.id.nextMonthButton).setOnClickListener(v -> CalendarUtils.moveToNextMonth(currentCalendar, adapter, monthText));

        // 일정 추가 버튼 클릭 리스너를 ScheduleManager를 통해 설정
        scheduleManager.setAddScheduleButtonListener(view.findViewById(R.id.addScheduleButton));
        scheduleManager.setDeleteScheduleButtonListener(view.findViewById(R.id.deleteScheduleButton));

        return view;
    }
}
