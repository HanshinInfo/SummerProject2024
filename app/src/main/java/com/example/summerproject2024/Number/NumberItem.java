package com.example.summerproject2024.Number;

public class NumberItem {
    String aff;
    String subaff;
    String name;
    String phoneNumber;
    String office;

    NumberItem(String aff, String subaff, String name, String phoneNumber, String office){
        this.aff = aff;
        this.subaff = subaff;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.office = office;
    }

    NumberItem(String aff, String name, String phoneNumber, String office){
        this.aff = aff;
        subaff = "";
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.office = office;
    }
}
