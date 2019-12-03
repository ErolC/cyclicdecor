package com.erolc.cyclicdecor.indicators;

import androidx.viewpager.widget.ViewPager;

import com.erolc.cyclicdecor.adapter.CyclicAdapter;

public interface Indicator extends ViewPager.OnPageChangeListener {

    void setAdapter(CyclicAdapter adapter);
}
