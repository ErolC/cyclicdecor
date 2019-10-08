package com.erolc.cyclicviewpager.indicators;

import android.support.v4.view.ViewPager;

import com.erolc.cyclicviewpager.adapter.CyclicAdapter;

public interface Indicator extends ViewPager.OnPageChangeListener {

    void setAdapter(CyclicAdapter adapter);
}
