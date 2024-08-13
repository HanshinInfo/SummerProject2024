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

        // 이전 달의 마지막 날
        Calendar prevMonthCalendar = (Calendar) calendar.clone();
        prevMonthCalendar.add(Calendar.MONTH, -1);
        int daysInPrevMonth = prevMonthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 이전 달의 마지막 날 추가
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        for (int i = daysInPrevMonth - firstDayOfWeek + 1; i <= daysInPrevMonth; i++) {
            days.add(i);
        }

        // 현재 월의 날짜 추가
        for (int i = 1; i <= daysInCurrentMonth; i++) {
            days.add(i);
        }

        // 다음 달의 시작 날짜 추가
        int daysToAdd = 35 - days.size(); // 총 아이템 개수를 35개로 유지하기 위함 (5주)
        for (int i = 1; i <= daysToAdd; i++) {
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
