package com.example.summerproject2024.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class ScheduleManager {
    private final Context context;
    private final List<Schedule> schedules;

    public ScheduleManager(Context context) {
        this.context = context;
        this.schedules = loadSchedulesFromFile();
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
    public void showAddScheduleDialog(String date) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Schedule");

        final EditText input = new EditText(context);
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String scheduleText = input.getText().toString();
            schedules.add(new Schedule(date, scheduleText));
            saveSchedulesToFile();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // 다이얼로그를 통해 일정 삭제
    public void showDeleteScheduleDialog(String date) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Schedule");

        List<String> matchingSchedules = new ArrayList<>();
        for (Schedule schedule : schedules) {
            if (schedule.getDate().equals(date)) {
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
            schedules.removeIf(schedule -> schedule.getDate().equals(date) && schedule.getDescription().equals(selectedSchedule));
            saveSchedulesToFile();
            Toast.makeText(context, "Schedule deleted successfully!", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}