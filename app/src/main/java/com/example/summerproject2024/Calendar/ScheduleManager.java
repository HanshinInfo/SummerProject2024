package com.example.summerproject2024.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.View;
import android.os.Handler;
import android.os.Looper;

import com.example.summerproject2024.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ScheduleManager {
    private final Context context;
    private final List<Schedule> schedules;
    private String selectedDate;
    private Calendar currentCalendar;
    private CalendarAdapter calendarAdapter;
    private final AcademicScheduleCrawler crawler;

    public ScheduleManager(Context context, CalendarAdapter adapter, Calendar currentCalendar) {
        this.context = context;
        this.selectedDate = getCurrentDate();
        this.schedules = loadSchedulesFromFile();
        this.currentCalendar = currentCalendar;
        this.calendarAdapter = adapter;
        this.crawler = new AcademicScheduleCrawler();
        Log.d("ScheduleManager", "Initialized with adapter: " + (adapter != null));
    }

    // 현재 날짜를 반환하는 메서드 (초기화 시 사용)
    private String getCurrentDate() {
        Calendar currentCalendar = Calendar.getInstance();
        int year = currentCalendar.get(Calendar.YEAR);
        int month = currentCalendar.get(Calendar.MONTH) + 1; // 월은 0부터 시작하므로 +1
        int day = currentCalendar.get(Calendar.DAY_OF_MONTH);
        return String.format(Locale.KOREA, "%d-%02d-%02d", year, month, day);
    }

    // 선택된 날짜를 설정하는 메서드
    public void setSelectedDate(int year, int month, int day) {
        this.selectedDate = String.format(Locale.KOREA, "%d-%02d-%02d", year, month, day);
    }

    // 웹에서 학사 일정을 크롤링하여 저장하고 캘린더 업데이트
    public void fetchAndStoreAcademicSchedules() {
        new Thread(() -> {
            List<Schedule> fetchedSchedules = crawler.fetchAcademicSchedules();

            // 중복되지 않은 일정만 추가
            for (Schedule newSchedule : fetchedSchedules) {
                boolean isDuplicate = false;
                for (Schedule existingSchedule : schedules) {
                    if (existingSchedule.getDate().equals(newSchedule.getDate()) &&
                            existingSchedule.getDescription().equals(newSchedule.getDescription())) {
                        isDuplicate = true;
                        break;
                    }
                }
                if (!isDuplicate) {
                    schedules.add(newSchedule);
                }
            }

            saveSchedulesToFile();

            // UI 업데이트
            new Handler(Looper.getMainLooper()).post(() -> {
                if (calendarAdapter != null) {
                    List<Integer> daysList = CalendarUtils.generateCalendarData(currentCalendar);
                    calendarAdapter.updateData(daysList, currentCalendar.get(Calendar.MONTH));
                } else {
                    Log.e("ScheduleManager", "CalendarAdapter is null, unable to update data.");
                }
                Log.i("ScheduleManager", "Academic schedules fetched and updated.");
            });

        }).start();
    }

    // 일정을 파일에 저장
    private void saveSchedulesToFile() {
        try (FileOutputStream fos = context.openFileOutput("schedules.txt", Context.MODE_PRIVATE);
             OutputStreamWriter writer = new OutputStreamWriter(fos)) {

            for (Schedule schedule : schedules) {
                writer.write(schedule.getDate() + " " + schedule.getStartTime() + " - " + schedule.getEndTime() + " : " + schedule.getDescription() + "\n");
            }
            writer.flush(); // 데이터를 파일에 즉시 저장

            Log.i("ScheduleManager", "Schedules saved successfully!");
        } catch (IOException e) {
            Log.e("ScheduleManager", "Failed to save schedules.", e); // 오류 메시지와 예외를 로그로 기록
        }
    }

    // 파일에서 일정을 로드
    private List<Schedule> loadSchedulesFromFile() {
        List<Schedule> schedules = new ArrayList<>();

        try (FileInputStream fis = context.openFileInput("schedules.txt");
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader reader = new BufferedReader(isr)) {

            String line;
            while ((line = reader.readLine()) != null) {
                // "날짜 시작시간 - 종료시간 : 설명" 형식으로 파싱
                String[] parts = line.split(" : ");
                if (parts.length == 2) {
                    String[] dateAndTime = parts[0].split(" ");
                    if (dateAndTime.length == 3) {
                        String date = dateAndTime[0];
                        String startTime = dateAndTime[1];
                        String endTime = dateAndTime[2].replace(" - ", ""); // 종료 시간 파싱
                        String description = parts[1];
                        schedules.add(new Schedule(date, startTime, endTime, description));
                    }
                }
            }
        } catch (IOException e) {
            Log.e("ScheduleManager", "Failed to load schedules.", e); // 오류 메시지와 예외를 로그로 기록
        }
        return schedules;
    }


    // 팝업을 통해 일정 추가
    public void showAddSchedulePopup() {
        // 팝업용 다이얼로그 생성
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.schedule_add_dialog); // ConstraintLayout 기반 XML 레이아웃 사용

        // 팝업 내 UI 요소 연결
        EditText descriptionInput = dialog.findViewById(R.id.schedule_description);
        Spinner startTimeSpinner = dialog.findViewById(R.id.start_time_spinner);
        Spinner endTimeSpinner = dialog.findViewById(R.id.end_time_spinner);
        Button confirmButton = dialog.findViewById(R.id.confirm_button);
        Button cancelButton = dialog.findViewById(R.id.cancel_button);

        // 스피너에 시간 선택 항목 설정
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context, R.array.time_slots, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startTimeSpinner.setAdapter(adapter);
        endTimeSpinner.setAdapter(adapter);

        // 확인 버튼 클릭 리스너
        confirmButton.setOnClickListener(v -> {
            // 키보드 닫기
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

            String description = descriptionInput.getText().toString();
            String startTime = startTimeSpinner.getSelectedItem().toString();
            String endTime = endTimeSpinner.getSelectedItem().toString();

            // 일정 추가 (선택된 날짜, 시작 시간, 종료 시간, 설명)
            schedules.add(new Schedule(selectedDate, startTime, endTime, description));
            saveSchedulesToFile(); // 파일에 저장

            // 달력 업데이트
            if (calendarAdapter != null) { // null 체크 추가
                List<Integer> daysList = CalendarUtils.generateCalendarData(currentCalendar); // 현재 보고 있는 월의 데이터
                calendarAdapter.updateData(daysList, currentCalendar.get(Calendar.MONTH)); // 현재 월로 업데이트
            } else {
                Log.e("ScheduleManager", "CalendarAdapter is null, unable to update data.");
            }

            dialog.dismiss(); // 팝업 닫기
        });

        // 취소 버튼 클릭 리스너
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        // 팝업 띄우기
        dialog.show();
    }

    // 팝업을 통해 일정 삭제
    public void showDeleteSchedulePopup() {
        // 팝업용 다이얼로그 생성
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.schedule_delete_dialog); // 삭제용 다이얼로그 레이아웃

        Spinner scheduleSpinner = dialog.findViewById(R.id.schedule_spinner); // 스피너 연결
        Button confirmButton = dialog.findViewById(R.id.confirm_delete_button);
        Button cancelButton = dialog.findViewById(R.id.cancel_delete_button);

        // 선택된 날짜의 일정을 가져와 스피너에 설정
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

        // 스피너 어댑터 설정
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, matchingSchedules);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scheduleSpinner.setAdapter(adapter);

        // 확인 버튼 클릭 리스너
        confirmButton.setOnClickListener(v -> {
            String selectedSchedule = (String) scheduleSpinner.getSelectedItem();
            schedules.removeIf(schedule -> schedule.getDate().equals(selectedDate) && schedule.getDescription().equals(selectedSchedule));
            saveSchedulesToFile(); // 변경 내용 저장

            if (calendarAdapter != null) {
                calendarAdapter.updateData(CalendarUtils.generateCalendarData(currentCalendar), currentCalendar.get(Calendar.MONTH));
            }

            Toast.makeText(context, "Schedule deleted successfully!", Toast.LENGTH_SHORT).show();
            dialog.dismiss(); // 팝업 닫기
        });

        // 취소 버튼 클릭 리스너
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
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
        button.setOnClickListener(v -> showAddSchedulePopup());
    }
    public void setDeleteScheduleButtonListener(View button) {
        button.setOnClickListener(v -> showDeleteSchedulePopup());
    }
}