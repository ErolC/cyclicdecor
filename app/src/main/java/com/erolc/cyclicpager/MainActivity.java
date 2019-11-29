package com.erolc.cyclicpager;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.erolc.cyclicpager.databinding.ActivityMainBinding;
import com.erolc.cyclicpager.databinding.ViewItemPageBinding;
import com.erolc.cyclicviewpager.CyclicDecor;
import com.erolc.cyclicviewpager.adapter.CyclicDBAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private CyclicDecor decor;

    private CyclicDBAdapter<Temp> adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new CyclicDBAdapter<Temp>() {
            @Override
            public int getLayout(int position) {
                return R.layout.view_item_page;
            }

            @Override
            public void onBind(ViewDataBinding binding, Temp data, int position) {
                super.onBind(binding, data, position);
                ViewItemPageBinding bind = (ViewItemPageBinding) binding;
            }
        };
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        String[] page = getResources().getStringArray(R.array.temps);
        List<Temp> temps = new ArrayList<>();
        for (String name: page){
            Temp temp = new Temp();
            temp.name = name;
            temp.color = Color.parseColor(name);
            temps.add(temp);
        }
        decor = new CyclicDecor.Builder(binding.pager)
                .setAdapter(adapter)
//                .setIndicator(binding.indicator)
                .automatic(3000)
                .isFastSwitch(false)
                .build();
        adapter.setDataList(temps);


    }


}
