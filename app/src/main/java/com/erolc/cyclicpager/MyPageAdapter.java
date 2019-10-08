package com.erolc.cyclicpager;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class MyPageAdapter extends PagerAdapter {
    private List<String> dataList = new ArrayList<>();
    private LayoutInflater inflater;

    @Override
    public int getCount() {
        return dataList.size();
    }

    public abstract int getLayout(int position);

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (inflater == null)
         inflater = LayoutInflater.from(container.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, getLayout(position), container, true);
        binding.setVariable(BR.item,dataList.get(position));
        return binding.getRoot();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void setDataList(List<String> datas){
        dataList.addAll(datas);
    }

    public void notifiDatachange(){
        notifyDataSetChanged();
    }

}
