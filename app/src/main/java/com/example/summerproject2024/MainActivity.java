package com.example.summerproject2024;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.summerproject2024.Calendar.Calendar_fragment;
import com.example.summerproject2024.Campus_map.Campus_map;
import com.example.summerproject2024.DB.DatabaseHelper;
import com.example.summerproject2024.Information.University_Town_Info;
import com.example.summerproject2024.Mascot.Mascot;
import com.example.summerproject2024.Menu.MenuAdapter;
import com.example.summerproject2024.Menu.MenuGroup;
import com.example.summerproject2024.Menu.MenuLinkAdapter;
import com.example.summerproject2024.Menu.MenuLinkGroup;
import com.example.summerproject2024.Number.University_Number;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;

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
    Mascot mascot_fragment;

    //Buttons
    ImageButton menu_button;

    //Menu
    NavigationView navigationView;
    ExpandableListView menuList;
    ListView menuLinkList;

    //Menu Link
    HashMap<String, String> linkMap;
    ArrayList<MenuGroup> menuGroup;
    ArrayList<MenuLinkGroup> linkGroups;

    //Toolbar
    Toolbar toolbar;
    TextView page_title;

    //Notification
    HashMap<String, String> notice_link_map;
    static long backpressTime = 0;

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
        mascot_fragment = new Mascot();

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

        //notification
        notice_link_map = new HashMap<>();
        notice_link_map.put(getResources().getString(R.string.notice_notification), "https://hs.ac.kr/kor/4953/subview.do");
        notice_link_map.put(getResources().getString(R.string.notice_event), "https://hs.ac.kr/kor/4955/subview.do");
        notice_link_map.put(getResources().getString(R.string.notice_academic), "https://hs.ac.kr/kor/4956/subview.do");
        notice_link_map.put(getResources().getString(R.string.notice_scholarship_community), "https://hs.ac.kr/kor/4957/subview.do");
        notice_link_map.put(getResources().getString(R.string.notice_employment), "https://hs.ac.kr/kor/4958/subview.do");

        //Menu
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        settingMenu();
        settingMenuLink();

        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        page_title = toolbar.findViewById(R.id.page_title);

        DatabaseHelper db = new DatabaseHelper(this);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    // 두 번 누르면 종료
                    if (System.currentTimeMillis() > backpressTime + 2000) {
                        backpressTime = System.currentTimeMillis();
                        Toast.makeText(MainActivity.this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        finish();
                    }
                }
            }
        });
    }

    //Menu-item
    private void settingMenu() {
        menuList = (ExpandableListView) findViewById(R.id.menuList);

        //Campus_map
        menuGroup = new ArrayList<>();
        MenuGroup item = new MenuGroup(getResources().getString(R.string.map_page));
        menuGroup.add(item);

        //Notice
        item = new MenuGroup(getResources().getString(R.string.notification));
        item.child.addAll(notice_link_map.keySet());
        menuGroup.add(item);

        //schedule
        item = new MenuGroup(getResources().getString(R.string.schedule_page));
        menuGroup.add(item);

        //Number
        item = new MenuGroup(getResources().getString(R.string.num_page));
        item.child.add(getResources().getString(R.string.uni_number));
        item.child.add(getResources().getString(R.string.prof_number));
        menuGroup.add(item);

        //Town Info
        item = new MenuGroup(getResources().getString(R.string.town_page));
        menuGroup.add(item);

        //Mascot
        item = new MenuGroup(getResources().getString(R.string.mascot_page));
        item.child.add(getResources().getString(R.string.mascot_cu_o));
        item.child.add(getResources().getString(R.string.mascot_hangomi));
        item.child.add(getResources().getString(R.string.mascot_buzzi));
        item.child.add(getResources().getString(R.string.mascot_hanggu));
        menuGroup.add(item);

        MenuAdapter adapter = new MenuAdapter(getApplicationContext(), R.layout.group_row, R.layout.child_row, menuGroup);
        menuList.setGroupIndicator(null);
        menuList.setAdapter(adapter);

        menuList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                String groupName = menuGroup.get(groupPosition).groupName;

                if(groupName.equals(getResources().getString(R.string.map_page))){
                    page_title.setText(groupName);
                    transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.from_right, 0);
                    transaction.replace(R.id.fragment_container_view, campus_map).commitAllowingStateLoss();
                    closeMenu(-1);
                    return true;
                }

                if(groupName.equals(getResources().getString(R.string.schedule_page))){
                    page_title.setText(groupName);
                    transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.from_right, 0);
                    transaction.replace(R.id.fragment_container_view, calendar_fragment).commitAllowingStateLoss();
                    closeMenu(-1);
                    return true;
                }

                if(groupName.equals(getResources().getString(R.string.town_page))){
                    page_title.setText(groupName);
                    transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.from_right, 0);
                    transaction.replace(R.id.fragment_container_view, university_town_info).commitAllowingStateLoss();
                    closeMenu(-1);
                    return true;
                }

                return false;
            }
        });

        menuList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                transaction = fragmentManager.beginTransaction();
                String child = menuGroup.get(groupPosition).child.get(childPosition);
                Bundle bundle = new Bundle();
                if(menuGroup.get(groupPosition).groupName.equals(getResources().getString(R.string.notification))){
                    for (String s : notice_link_map.keySet()) {
                        if (child.equals(s)) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(notice_link_map.get(s)));
                            startActivity(intent);
                            break;
                        }
                    }
                    return true;
                }

                if(menuGroup.get(groupPosition).groupName.equals(getResources().getString(R.string.num_page))){
                    page_title.setText(child);
                    if(university_number.isVisible()){
                        university_number.settingPage(child);
                    }
                    else{
                        bundle.putString("number_kind", child);
                        university_number.setArguments(bundle);
                        transaction.setCustomAnimations(R.anim.from_right, 0);
                        transaction.replace(R.id.fragment_container_view, university_number).commitAllowingStateLoss();
                    }
                    closeMenu(groupPosition);
                    return true;
                }

                if(menuGroup.get(groupPosition).groupName.equals(getResources().getString(R.string.mascot_page))){
                    page_title.setText(child);
                    if(mascot_fragment.isVisible()){
                        mascot_fragment.SettingPage(child);
                    }
                    else{
                        bundle.putString("mascot_name", child);
                        mascot_fragment.setArguments(bundle);
                        transaction.setCustomAnimations(R.anim.from_right, 0);
                        transaction.replace(R.id.fragment_container_view, mascot_fragment).commitAllowingStateLoss();
                    }
                    closeMenu(groupPosition);
                    return true;
                }
                return false;
            }
        });
    }

    public void closeMenu(int index){
        drawerLayout.closeDrawer(GravityCompat.START);
        for(int i = 0; i < menuGroup.size(); i++){
            if(!menuGroup.get(i).child.isEmpty() && i != index){
                menuList.collapseGroup(i);
            }
        }
    }

    //menu-link
    private void settingMenuLink() {
        menuLinkList = (ListView) findViewById(R.id.menuLinkList);

        linkMap = new HashMap<>();
        linkMap.put(getResources().getString(R.string.homepage), "https://hs.ac.kr/sites/kor/index.do");
        linkMap.put(getResources().getString(R.string.lms), "https://lms.hs.ac.kr/main/MainView.dunet#main");
        linkMap.put(getResources().getString(R.string.hsctis), "https://hsctis.hs.ac.kr/app-nexa/index.html");
        linkMap.put(getResources().getString(R.string.attend), "https://attend.hs.ac.kr/#");
        linkMap.put(getResources().getString(R.string.sugang), "https://sugang.hs.ac.kr/login");

        linkGroups = new ArrayList<>();
        for(String key : linkMap.keySet()){
            linkGroups.add(new MenuLinkGroup(key));
        }

        MenuLinkAdapter menuLinkAdapter = new MenuLinkAdapter(this, linkGroups);
        menuLinkList.setAdapter(menuLinkAdapter);

        menuLinkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(linkMap.get(menuLinkAdapter.getItem(position).link)));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

}