package com.example.summerproject2024.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.summerproject2024.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {
    private Context context;
    private List<Integer> days;
    private int selectedPosition = -1;
    private int currentMonth;
    private ScheduleManager scheduleManager;

    public CalendarAdapter(Context context, List<Integer> days, int currentMonth, ScheduleManager scheduleManager) {
        this.context = context;
        this.days = days;
        this.currentMonth = currentMonth;
        this.scheduleManager = scheduleManager;
    }

    public void setScheduleManager(ScheduleManager scheduleManager) {
        this.scheduleManager = scheduleManager; // scheduleManager를 설정하는 메서드
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_calendar_day, parent, false);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        int day = days.get(position);

        if (day == 0) {
            // 빈칸일 경우: TextView를 숨김 처리
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0)); // 높이와 너비를 0으로 설정해 공간을 없앰
        } else {
            // 날짜 포맷 맞추기 ("2024-09-07")
            String date = String.format(Locale.KOREA, "%d-%02d-%02d", getYear(), currentMonth + 1, day);

            // 해당 날짜의 스케줄 확인
            List<String> schedulesForDate = scheduleManager.getSchedulesForDate(date);
            if (!schedulesForDate.isEmpty()) {
                // 스케줄이 있을 경우 날짜 아래에 표시
                holder.scheduleText.setVisibility(View.VISIBLE);
                holder.scheduleText.setText(schedulesForDate.get(0));  // 첫 번째 스케줄만 표시
            } else {
                holder.scheduleText.setVisibility(View.GONE);
            }
        }

        // 날짜 텍스트 설정
        holder.dayText.setText(String.valueOf(day));
        holder.itemView.setClickable(true); // 유효한 날짜는 클릭 가능

        // 현재 위치의 아이템 배경색 설정
        int selectedColor = ContextCompat.getColor(context, R.color.selected_color);
        int defaultColor = ContextCompat.getColor(context, R.color.default_color);

        // 아이템 배경색 설정
        holder.dayText.setBackgroundColor(selectedPosition == position ? selectedColor : defaultColor);

        // 날짜 클릭 리스너 설정
        holder.itemView.setOnClickListener(v -> {
            int previousSelectedPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition(); // 현재 위치 가져오기

            // ScheduleManager에 선택된 날짜 전달
            scheduleManager.setSelectedDate(getYear(), currentMonth + 1, day);

            // 이전에 선택된 아이템 업데이트
            if (previousSelectedPosition != -1) {
                notifyItemChanged(previousSelectedPosition);
            }

            // 새로 선택된 아이템 업데이트
            notifyItemChanged(selectedPosition);
        });
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public void updateData(List<Integer> newDays, int currentMonth) {
        this.days = newDays;
        this.currentMonth = currentMonth;
        notifyDataSetChanged();
    }

    private int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static class CalendarViewHolder extends RecyclerView.ViewHolder {
        TextView dayText;
        TextView scheduleText;  // 스케줄을 표시할 텍스트뷰

        public CalendarViewHolder(View itemView) {
            super(itemView);
            dayText = itemView.findViewById(R.id.dayText);
            scheduleText = itemView.findViewById(R.id.scheduleText);  // 새로운 스케줄 텍스트뷰
        }
    }
}
