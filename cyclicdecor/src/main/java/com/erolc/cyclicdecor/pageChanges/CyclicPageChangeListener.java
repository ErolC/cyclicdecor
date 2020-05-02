package com.erolc.cyclicdecor.pageChanges;

import android.util.Log;

import androidx.viewpager.widget.ViewPager;

import com.erolc.cyclicdecor.adapter.CyclicAdapter;

/**
 * 这个类是viewpager循环的核心类
 */
public abstract class CyclicPageChangeListener implements ViewPager.OnPageChangeListener {
    private CyclicAdapter cyclic;
    private int currentPosition = -1;

    public CyclicPageChangeListener(CyclicAdapter cyclic){
        this.cyclic = cyclic;
    }

    public abstract  void onPageChange(int toPosition);
    public abstract  void onPageStateChange(int state);

    @Override
    public void onPageScrolled(int i, float v, int i1) {
        //切换过程中,i==position，v是百分比，也就是前面i所表示的页面展示的百分比，i1是真实的横坐标
        if (currentPosition == -1){
            currentPosition = i;
        }
    }

    @Override
    public void onPageSelected(int i) {
        //切换完成之后,如果滑动的太快，则会失去0这个状态,
        currentPosition = i;
        if (i == cyclic.getSubFirstSkipPosition()){
            onPageChange(cyclic.getLastSkipPosition()-2);
        }
        if (i == cyclic.getSubLastSkipPosition()){
            onPageChange(cyclic.getFirstSkipPosition()+2);
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {
        //共三个状态
        //0：正常状态，静止状态
        //1：移动状态，有手指状态
        //2：自由移动状态，无手指状态
        onPageStateChange(i);
        Log.e("tag", "onPageScrollStateChanged: "+i);
        if (i == 0){
            if (currentPosition == cyclic.getFirstSkipPosition()){
                onPageChange(cyclic.getLastSkipPosition()-1);
            }else if (currentPosition == cyclic.getLastSkipPosition()){
                onPageChange(cyclic.getFirstSkipPosition()+1);
            }
        }
    }
}
