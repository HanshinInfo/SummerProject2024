package com.example.summerproject2024.Information;


public class Item {
    private String name;
    private String location;
    private String category;
    private int imageResourceId;


    public Item(String name, String location, String category, int imageResourceId) {
        this.name = name;
        this.location = location;
        this.imageResourceId = imageResourceId;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getCategory() {
        return category;
    }
}