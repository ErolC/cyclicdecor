package com.erolc.cyclicdecor.adapter;

import androidx.databinding.BindingAdapter;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;

import com.erolc.cyclicdecor.indicators.CyclicIndicator;

/**
 * 指示灯的bindingAdapter
 */
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
