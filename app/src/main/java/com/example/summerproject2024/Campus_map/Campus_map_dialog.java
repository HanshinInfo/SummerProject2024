package com.example.summerproject2024.Campus_map;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.summerproject2024.DB.DatabaseHelper;
import com.example.summerproject2024.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Campus_map_dialog extends Dialog {

    DatabaseHelper db;
    String building_code = "";

    TextView building_name;
    ImageView building_image;
    ImageView[] categoryImage;

    Map<String, Integer> categoryHash;
    Map<String, Integer> buildingHash;

    public Campus_map_dialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.campus_map_dialog);

        building_image = (ImageView) findViewById(R.id.building_image);
        building_name = (TextView) findViewById(R.id.building_name);

        categoryHash = new HashMap<String, Integer>();
        putIconHashMap();

        buildingHash = new HashMap<String, Integer>();
        putBuildingHashMap();

        db = new DatabaseHelper(context);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setBuildingInfo(building_code);
        setCategory(building_code);
    }


    private void putBuildingHashMap() {
        buildingHash.put("1", R.drawable.building_01);
        buildingHash.put("2", R.drawable.building_02);
        buildingHash.put("3", R.drawable.building_03);
        buildingHash.put("4", R.drawable.building_04);
        buildingHash.put("5", R.drawable.building_05);
        buildingHash.put("6", R.drawable.building_06);
        buildingHash.put("7", 0);
        buildingHash.put("8", R.drawable.building_08);
        buildingHash.put("9", 0);
        buildingHash.put("10", R.drawable.building_10);
        buildingHash.put("11", R.drawable.building_11);
        buildingHash.put("14", R.drawable.building_14);
        buildingHash.put("17", R.drawable.building_17);
        buildingHash.put("18", R.drawable.building_18);
        buildingHash.put("20", R.drawable.building_20);
    }

    private void putIconHashMap() {
        categoryHash.put("식당", R.drawable.icon_food);
        categoryHash.put("카페", R.drawable.icon_cafe);
        categoryHash.put("남학생휴게실", R.drawable.icon_man);
        categoryHash.put("여학생휴게실", R.drawable.icon_woman);
        categoryHash.put("ATM", R.drawable.icon_atm);
        categoryHash.put("복사기", R.drawable.icon_print);
        categoryHash.put("편의점", R.drawable.icon_store);
        categoryHash.put("우체국", R.drawable.icon_post);
    }

    private void setBuildingInfo(String code){
        building_name.setText(db.selectBuildingInfo(code));
//        building_name.setText(code);
        building_image.setImageResource(buildingHash.get(code));
    }

    private void setCategory(String code) {
        ArrayList<String> category = new ArrayList<String>();
        category = db.selectCategoryUsingAmenity(code);

        categoryImage = new ImageView[5];
        categoryImage[0] = (ImageView) findViewById(R.id.icon1);
        categoryImage[1] = (ImageView) findViewById(R.id.icon2);
        categoryImage[2] = (ImageView) findViewById(R.id.icon3);
        categoryImage[3] = (ImageView) findViewById(R.id.icon4);
        categoryImage[4] = (ImageView) findViewById(R.id.icon5);

        for (ImageView imageView : categoryImage) {
            imageView.setVisibility(View.GONE);
        }

        for(int i = 0; i < category.size(); i++){
            categoryImage[i].setVisibility(View.VISIBLE);
            categoryImage[i].setImageResource(categoryHash.get(category.get(i)));
        }
    }
}
