package com.example.summerproject2024.Number;
import static android.content.Context.INPUT_METHOD_SERVICE;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.summerproject2024.DB.DatabaseHelper;
import com.example.summerproject2024.Information.CustomAdapter;
import com.example.summerproject2024.Information.Town_Info_Dialog;
import com.example.summerproject2024.R;

import java.util.ArrayList;
import java.util.List;

public class University_Number extends Fragment {
    public DatabaseHelper call_DB;
    RecyclerView recyclerView;
    EditText editText;
    Spinner spinner;

    String category;
    String[] categorys = {"소속", "이름", "전화번호"};
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.university_number_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        call_DB = new DatabaseHelper(getContext());
        editText = view.findViewById(R.id.editText);
        spinner = view.findViewById(R.id.num_category);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, categorys);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = categorys[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String number = this.getArguments().getString("number_kind");
        settingPage(number);
        return view;
    }

    public void settingPage(String number){
        if(number.equals(getResources().getString(R.string.uni_number))){
            callNumInfo();
        }
        else{
            callProfessorInfo();
        }
    }
    private void callNumInfo(){
        editText.setText("");
        ArrayList<String>[] num_Info = call_DB.selectCallNumbersAll();

        ArrayList<NumberItem> itemList = new ArrayList<>();
        ArrayList<NumberItem> search_list = new ArrayList<>();
        NumberView_adapter num_adapter;

        for (int i = 0; i < num_Info.length; i++) {
            String aff = num_Info[i].get(0);
            String subaff = num_Info[i].get(1);
            String name = num_Info[i].get(2);
            String phoneNumber = num_Info[i].get(3);
            String office = num_Info[i].get(4);
            itemList.add(new NumberItem(aff, subaff, name, phoneNumber, office));
        }
        num_adapter = new NumberView_adapter(itemList);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    InputMethodManager manager;
                    View currentFocus = getActivity().getCurrentFocus();
                    if (currentFocus != null) {
                        manager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                        manager.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    return true;
                }

                return false;
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = editText.getText().toString();
                search_list.clear();

                if(searchText.equals("")){
                    num_adapter.setItems(itemList);
                }
                else {
                    // 검색 단어를 포함하는지 확인
                    for (int i = 0; i < itemList.size(); i++) {
                        if(category.equals("소속")){
                            if (itemList.get(i).aff.toLowerCase().contains(searchText.toLowerCase())) {
                                search_list.add(itemList.get(i));
                            }
                            num_adapter.setItems(search_list);
                        }
                        else if(category.equals("이름")){
                            if (itemList.get(i).name.toLowerCase().contains(searchText.toLowerCase())) {
                                search_list.add(itemList.get(i));
                            }
                            num_adapter.setItems(search_list);
                        }
                        else {
                            if (itemList.get(i).phoneNumber.toLowerCase().contains(searchText.toLowerCase())) {
                                search_list.add(itemList.get(i));
                            }
                            num_adapter.setItems(search_list);
                        }
                    }
                }
            }
        });

        recyclerView.setAdapter(num_adapter);
    }

    private void callProfessorInfo(){
        editText.setText("");
        ArrayList<String>[] pro_Info = call_DB.selectProfessorAll();

        ArrayList<NumberItem> itemList = new ArrayList<>();
        ArrayList<NumberItem> search_list = new ArrayList<>();
        NumberView_adapter pro_adapter;

        for (int i = 0; i < pro_Info.length; i++) {
            String aff = pro_Info[i].get(0);
            String name = pro_Info[i].get(1);
            String phoneNumber = pro_Info[i].get(2);
            String office = pro_Info[i].get(3);
            itemList.add(new NumberItem(aff, name, phoneNumber, office));
        }
        pro_adapter = new NumberView_adapter(itemList);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    InputMethodManager manager;
                    View currentFocus = getActivity().getCurrentFocus();
                    if (currentFocus != null) {
                        manager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                        manager.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    return true;
                }

                return false;
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = editText.getText().toString();
                search_list.clear();

                if(searchText.equals("")){
                    pro_adapter.setItems(itemList);
                }
                else {
                    // 검색 단어를 포함하는지 확인
                    for (int i = 0; i < itemList.size(); i++) {
                        if(category.equals("소속")){
                            if (itemList.get(i).aff.toLowerCase().contains(searchText.toLowerCase())) {
                                search_list.add(itemList.get(i));
                            }
                            pro_adapter.setItems(search_list);
                        }
                        else if(category.equals("이름")){
                            if (itemList.get(i).name.toLowerCase().contains(searchText.toLowerCase())) {
                                search_list.add(itemList.get(i));
                            }
                            pro_adapter.setItems(search_list);
                        }
                        else {
                            if (itemList.get(i).phoneNumber.toLowerCase().contains(searchText.toLowerCase())) {
                                search_list.add(itemList.get(i));
                            }
                            pro_adapter.setItems(search_list);
                        }
                    }
                }
            }
        });
        recyclerView.setAdapter(pro_adapter);
    }
}