package com.example.summerproject2024.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScheduleManager {
    private final Context context;
    private final List<Schedule> schedules;
    private String selectedDate;
    private CalendarAdapter adapter;

    public ScheduleManager(Context context) {
        this.context = context;
        this.selectedDate = getCurrentDate();
        this.schedules = loadSchedulesFromFile();
    }

    // 현재 날짜를 반환하는 메서드 (초기화 시 사용)
    private String getCurrentDate() {
        Calendar currentCalendar = Calendar.getInstance();
        int year = currentCalendar.get(Calendar.YEAR);
        int month = currentCalendar.get(Calendar.MONTH) + 1; // 월은 0부터 시작하므로 +1
        int day = currentCalendar.get(Calendar.DAY_OF_MONTH);
        return String.format("%d-%02d-%02d", year, month, day);
    }

    // 선택된 날짜를 설정하는 메서드
    public void setSelectedDate(int year, int month, int day) {
        this.selectedDate = String.format("%d-%02d-%02d", year, month, day);
    }

    // 선택된 날짜를 반환하는 메서드
    public String getSelectedDate() {
        return this.selectedDate;
    }

    // 일정을 파일에 저장
    private void saveSchedulesToFile() {
        try (FileOutputStream fos = context.openFileOutput("schedules.txt", Context.MODE_PRIVATE);
             OutputStreamWriter writer = new OutputStreamWriter(fos)) {

            for (Schedule schedule : schedules) {
                writer.write(schedule.getDate() + " : " + schedule.getDescription() + "\n");
            }
            writer.flush();  // 데이터를 파일에 즉시 저장
            Toast.makeText(context, "Schedules saved successfully!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to save schedules.", Toast.LENGTH_SHORT).show();
        }
    }

    // 파일에서 일정을 로드
    private List<Schedule> loadSchedulesFromFile() {
        List<Schedule> schedules = new ArrayList<>();

        try (FileInputStream fis = context.openFileInput("schedules.txt");
             InputStreamReader isr = new InputStreamReader(fis)) {

            BufferedReader reader = new BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" : ");
                if (parts.length == 2) {
                    String date = parts[0];
                    String description = parts[1];
                    schedules.add(new Schedule(date, description));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to load schedules.", Toast.LENGTH_SHORT).show();
        }

        return schedules;
    }

    // 다이얼로그를 통해 일정 추가
    public void showAddScheduleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Schedule for " + selectedDate);

        final EditText input = new EditText(context);
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String scheduleText = input.getText().toString();
            schedules.add(new Schedule(selectedDate, scheduleText));
            saveSchedulesToFile();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // 다이얼로그를 통해 일정 삭제
    public void showDeleteScheduleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Schedule for " + selectedDate);

        List<String> matchingSchedules = new ArrayList<>();
        for (Schedule schedule : schedules) {
            if (schedule.getDate().equals(selectedDate)) {
                matchingSchedules.add(schedule.getDescription());
            }
        }

        if (matchingSchedules.isEmpty()) {
            Toast.makeText(context, "No schedules found for this date.", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] scheduleArray = matchingSchedules.toArray(new String[0]);
        builder.setItems(scheduleArray, (dialog, which) -> {
            String selectedSchedule = scheduleArray[which];
            schedules.removeIf(schedule -> schedule.getDate().equals(selectedDate) && schedule.getDescription().equals(selectedSchedule));
            saveSchedulesToFile();
            Toast.makeText(context, "Schedule deleted successfully!", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // 스케쥴 불러오는 메소드
    public List<String> getSchedulesForDate(String date) {
        List<String> matchingSchedules = new ArrayList<>();
        for (Schedule schedule : schedules) {
            if (schedule.getDate().equals(date)) {
                matchingSchedules.add(schedule.getDescription());
            }
        }
        return matchingSchedules;
    }

    // 버튼 클릭 리스너 설정
    public void setAddScheduleButtonListener(View button) {
        button.setOnClickListener(v -> showAddScheduleDialog());
    }
    public void setDeleteScheduleButtonListener(View button) {
        button.setOnClickListener(v -> showDeleteScheduleDialog());
    }
}