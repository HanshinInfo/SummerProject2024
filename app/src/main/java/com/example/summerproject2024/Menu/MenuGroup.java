package com.example.summerproject2024.Menu;

import java.util.ArrayList;

public class MenuGroup {

    public ArrayList<String> child;
    public String groupName;

    public MenuGroup(String name){
        groupName = name;
        child = new ArrayList<String>();
    }
}
