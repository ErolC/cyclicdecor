package com.erolc.cyclicpager;

import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.erolc.cyclicdecor.CyclicControl;
import com.erolc.cyclicpager.databinding.ActivityMainBinding;
import com.erolc.cyclicdecor.adapter.CyclicDBAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CyclicDBAdapter<Temp> adapter = new CyclicDBAdapter<Temp>() {
            @Override
            public int getLayout(int position) {
                return R.layout.view_item_page;
            }
        };
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        String[] page = getResources().getStringArray(R.array.temps);
        List<Temp> temps = new ArrayList<>();
        for (String name: page){
            Temp temp = new Temp();
            temp.name = name;
            temp.color = Color.parseColor(name);
            temps.add(temp);
        }
        CyclicControl build = new CyclicControl.Builder(binding.pager)
                .setAdapter(adapter)
                .setIndicator(binding.indicator)
                .automatic(false)
                .isFastSwitch(false)
                .build();
        adapter.setDataList(temps);


    }


}
