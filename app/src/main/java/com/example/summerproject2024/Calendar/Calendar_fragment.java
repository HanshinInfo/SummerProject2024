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

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.calendar_fragment, container, false);

        monthText = view.findViewById(R.id.monthText);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));

        currentCalendar = Calendar.getInstance(); // 초기화
        scheduleManager = new ScheduleManager(getContext()); // ScheduleManager 초기화

        List<Integer> days = CalendarUtils.generateCalendarData(currentCalendar);
        int currentMonth = currentCalendar.get(Calendar.MONTH);

        // 어댑터 설정
        adapter = new CalendarAdapter(getContext(), days, currentMonth, scheduleManager);
        recyclerView.setAdapter(adapter);

        CalendarUtils.updateMonthText(currentCalendar, monthText);

        // 버튼 클릭 리스너 설정
        view.findViewById(R.id.prevMonthButton).setOnClickListener(v -> CalendarUtils.moveToPreviousMonth(currentCalendar, adapter, monthText));
        view.findViewById(R.id.nextMonthButton).setOnClickListener(v -> CalendarUtils.moveToNextMonth(currentCalendar, adapter, monthText));

        return view;
    }
}
