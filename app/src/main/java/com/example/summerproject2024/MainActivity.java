package com.example.summerproject2024;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.summerproject2024.Calendar.Calendar_fragment;
import com.example.summerproject2024.Campus_map.Campus_map;
import com.example.summerproject2024.Number.University_Number;
import com.example.summerproject2024.Information.University_Town_Info;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //FragmentManager
    private static FragmentManager fragmentManager;
    private static FragmentTransaction transaction;

    //Layout
    DrawerLayout drawerLayout;

    //fragment
    Campus_map campus_map;
    University_Number university_number;
    University_Town_Info university_town_info;
    Calendar_fragment calendar_fragment;

    //Buttons
    ImageButton menu_button;

    //Menu
    NavigationView navigationView;

    //Toolbar
    Toolbar toolbar;
    TextView page_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.main);
        menu_button = (ImageButton) findViewById(R.id.menu_button);

        //Fragment Class
        campus_map = new Campus_map();
        university_number = new University_Number();
        university_town_info = new University_Town_Info();
        calendar_fragment = new Calendar_fragment();

        //Change Fragment
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container_view, campus_map).commitAllowingStateLoss();

        //MenuBtn
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.menu_button){
                    if(drawerLayout.isDrawerOpen(R.id.main)){
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                    else{
                        drawerLayout.openDrawer(GravityCompat.START);
                    }
                }
            }
        });

        //Menu
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        page_title = toolbar.findViewById(R.id.page_title);
    }

    //ItemEvent
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        drawerLayout.closeDrawer(GravityCompat.START);
        //Change fragment
        if(menuItem.getItemId() == R.id.menu_map){
            Log.v("menu", "map");
            page_title.setText(getResources().getString(R.string.map_page));
            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container_view, campus_map).commitAllowingStateLoss();
            return true;
        }
        if(menuItem.getItemId() == R.id.menu_schedule){
            Log.v("menu", "schedule");
            page_title.setText(getResources().getString(R.string.schedule_page));
            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container_view, calendar_fragment).commitAllowingStateLoss();
            return true;
        }
        if(menuItem.getItemId() == R.id.menu_university_number){
            Log.v("menu", "number");
            page_title.setText(getResources().getString(R.string.num_page));
            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container_view, university_number).commitAllowingStateLoss();
            return true;
        }
        if(menuItem.getItemId() == R.id.menu_town_info){
            Log.v("menu", "town_info");
            page_title.setText(getResources().getString(R.string.town_page));
            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container_view, university_town_info).commitAllowingStateLoss();
            return true;
        }


        Intent intent = new Intent(Intent.ACTION_VIEW);

        //Link
        if(menuItem.getItemId() == R.id.menu_homepage){
            intent.setData(Uri.parse("https://hs.ac.kr/sites/kor/index.do"));
            startActivity(intent);
            return true;
        }
        if(menuItem.getItemId() == R.id.menu_lms){
            intent.setData(Uri.parse("https://lms.hs.ac.kr/main/MainView.dunet#main"));
            startActivity(intent);
            return true;
        }
        if(menuItem.getItemId() == R.id.menu_hsctis){
            intent.setData(Uri.parse("https://hsctis.hs.ac.kr/app-nexa/index.html"));
            startActivity(intent);
            return true;
        }
        if(menuItem.getItemId() == R.id.menu_attend){
            intent.setData(Uri.parse("https://attend.hs.ac.kr/#"));
            startActivity(intent);
            return true;
        }
        if(menuItem.getItemId() == R.id.menu_sugang){
            intent.setData(Uri.parse("https://sugang.hs.ac.kr/login"));
            startActivity(intent);
            return true;
        }

        return false;
    }
}