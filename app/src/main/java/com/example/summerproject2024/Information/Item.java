package com.example.summerproject2024.Information;


import android.graphics.drawable.Drawable;

public class Item {
    private String name;
    private String category;
    private String hours;
    private int icon;


    public Item(String name, String category, String hours, int icon) {
        this.name = name;
        this.category = category;
        this.hours = hours;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getHours() {
        return hours;
    }

    public int getImageResourceId() {
        return icon;
    }

    public String getCategory() {
        return category;
    }
}