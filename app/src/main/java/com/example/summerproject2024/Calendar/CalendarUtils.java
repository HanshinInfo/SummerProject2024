package com.example.summerproject2024.Calendar;

import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class CalendarUtils {

    // 현재 월과 날짜를 기반으로 한 달력 데이터 생성
    public static List<Integer> generateCalendarData(Calendar calendar) {
        List<Integer> days = new ArrayList<>();

        // 현재 달의 첫 날로 설정
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int daysInCurrentMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 현재 달의 첫 번째 날이 시작하는 요일을 가져옴 (1: 일요일, 7: 토요일)
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; // 월요일이 0이 되도록 조정

        // 첫 번째 날짜가 시작되는 칸 이전을 빈칸으로 채움
        for (int i = 0; i < firstDayOfWeek; i++) {
            days.add(0); // 0은 빈칸을 의미
        }

        // 현재 월의 날짜 추가
        for (int i = 1; i <= daysInCurrentMonth; i++) {
            days.add(i);
        }

        return days;
    }

    // 현재 월을 TextView에 설정
    public static void updateMonthText(Calendar calendar, TextView monthText) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMMM", Locale.getDefault());
        String nowMonth = sdf.format(calendar.getTime());
        monthText.setText(nowMonth);
    }

    // RecyclerView의 어댑터 업데이트
    public static void updateAdapterData(CalendarAdapter adapter, List<Integer> days, int currentMonth) {
        adapter.updateData(days, currentMonth);
    }

    // 이전 달로 이동
    public static void moveToPreviousMonth(Calendar currentCalendar, CalendarAdapter adapter, TextView monthText) {
        currentCalendar.add(Calendar.MONTH, -1);
        List<Integer> days = generateCalendarData(currentCalendar);
        int currentMonth = currentCalendar.get(Calendar.MONTH);
        updateMonthText(currentCalendar, monthText);
        updateAdapterData(adapter, days, currentMonth);
    }

    // 다음 달로 이동
    public static void moveToNextMonth(Calendar currentCalendar, CalendarAdapter adapter, TextView monthText) {
        currentCalendar.add(Calendar.MONTH, 1);
        List<Integer> days = generateCalendarData(currentCalendar);
        int currentMonth = currentCalendar.get(Calendar.MONTH);
        updateMonthText(currentCalendar, monthText);
        updateAdapterData(adapter, days, currentMonth);
    }
}
