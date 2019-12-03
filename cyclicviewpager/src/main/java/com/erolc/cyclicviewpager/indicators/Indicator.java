package com.erolc.cyclicviewpager.indicators;

import androidx.viewpager.widget.ViewPager;

import com.erolc.cyclicviewpager.adapter.CyclicAdapter;

public interface Indicator extends ViewPager.OnPageChangeListener {

    void setAdapter(CyclicAdapter adapter);
}
