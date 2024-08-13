package com.example.summerproject2024.Calendar;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
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

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_calendar_day, parent, false);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        int day = days.get(position);
        holder.dayText.setText(String.valueOf(day));

        // 색상 설정
        int textColor;
        int backgroundColor;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, currentMonth);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        if (position < firstDayOfMonth || position >= firstDayOfMonth + daysInMonth) {
            // 해당 월에 맞지 않는 날짜
            textColor = Color.LTGRAY;
            backgroundColor = Color.TRANSPARENT;
        } else {
            // 해당 월의 날짜
            textColor = Color.BLACK;
            backgroundColor = Color.WHITE;
        }

        holder.dayText.setTextColor(textColor);
        holder.dayText.setBackgroundColor(backgroundColor);

        // 현재 위치의 아이템 배경색 설정
        int selectedColor = ContextCompat.getColor(context, R.color.selected_color);
        int defaultColor = ContextCompat.getColor(context, R.color.default_color);

        // 아이템 배경색 설정
        holder.dayText.setBackgroundColor(selectedPosition == position ? selectedColor : defaultColor);

        // 아이템 클릭 리스너 설정
        holder.itemView.setOnClickListener(v -> {
            int previousSelectedPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition(); // 현재 위치 가져오기

            // 이전에 선택된 아이템 업데이트
            if (previousSelectedPosition != -1) {
                notifyItemChanged(previousSelectedPosition);
            }

            // 새로 선택된 아이템 업데이트
            notifyItemChanged(selectedPosition);

            // 선택된 날짜의 일정을 관리하기 위한 다이얼로그 표시
            String date = String.format("%d-%02d-%02d", getYear(), currentMonth + 1, day);
            showScheduleDialog(date);
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

    private void showScheduleDialog(String date) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Schedule Options");

        String[] options = {"Add Schedule", "Delete Schedule"};
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    scheduleManager.showAddScheduleDialog(date);
                    break;
                case 1:
                    scheduleManager.showDeleteScheduleDialog(date);
                    break;
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    public static class CalendarViewHolder extends RecyclerView.ViewHolder {
        TextView dayText;

        public CalendarViewHolder(View itemView) {
            super(itemView);
            dayText = itemView.findViewById(R.id.dayText);
        }
    }
}
