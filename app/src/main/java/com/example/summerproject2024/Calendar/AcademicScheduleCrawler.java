package com.example.summerproject2024.Calendar;

import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AcademicScheduleCrawler {

    private static final String URL = "https://www.hs.ac.kr/kor/4837/subview.do";

    public List<Schedule> fetchAcademicSchedules() {
        List<Schedule> fetchedSchedules = new ArrayList<>();
        Set<String> existingScheduleDates = new HashSet<>(); // 중복 방지를 위한 Set

        try {
            Document doc = Jsoup.connect(URL).get();
            Elements months = doc.select("li");

            for (Element month : months) {
                Elements days = month.select("dl > dt");
                Elements events = month.select("dl > dd");

                for (int i = 0; i < days.size(); i++) {
                    Element dayElement = days.get(i).selectFirst("span");
                    Element eventElement = events.get(i);

                    if (dayElement == null || eventElement == null) {
                        Log.w("AcademicScheduleCrawler", "Day or event element is null, skipping this entry.");
                        continue;
                    }

                    String day = dayElement.text();
                    String event = eventElement.text();

                    String[] dayParts = day.split(" ");
                    String[] monthDay = dayParts[0].split("-");

                    if (monthDay.length == 2) { // 날짜 형식이 올바른지 확인
                        String eventDate = String.format(
                                "%s-%02d-%02d", Calendar.getInstance().get(Calendar.YEAR),
                                Integer.parseInt(monthDay[0]), Integer.parseInt(monthDay[1])
                        );

                        // 중복 체크를 위해 고유 키 생성
                        String uniqueKey = eventDate + event;
                        if (!existingScheduleDates.contains(uniqueKey)) {
                            fetchedSchedules.add(new Schedule(eventDate, "", "", event));
                            existingScheduleDates.add(uniqueKey); // Set에 추가하여 중복 방지
                        }
                    }
                }
            }
        } catch (IOException e) {
            Log.e("ScheduleManager", "Error fetching academic schedules.", e);
        }
        return fetchedSchedules;
    }
}
