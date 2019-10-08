package com.erolc.cyclicviewpager.adapter;

import android.databinding.BindingAdapter;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;

import com.erolc.cyclicviewpager.indicators.CyclicIndicator;

public class indicatorAdapter {

    @BindingAdapter("app:selectColor")
    public static void selectColor(CyclicIndicator indicator,@ColorInt int color) {
        indicator.setSelectColor(color);
    }

    @BindingAdapter("app:defaultColor")
    public static void defaultColor(CyclicIndicator indicator, @ColorRes int color) {
        indicator.setDefaultColor(color);
    }

    @BindingAdapter("app:gravity")
    public static void gravity(CyclicIndicator indicator, int gravity) {
        indicator.setGravity(gravity);
    }

    @BindingAdapter("app:interval")
    public static void interval(CyclicIndicator indicator, int interval) {
        indicator.setInterval(interval);
    }

    @BindingAdapter("app:round")
    public static void setRound(CyclicIndicator indicator,boolean isRound){
        indicator.setExtremityRound(isRound);
    }

}
