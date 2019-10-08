package com.erolc.cyclicviewpager.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erolc.cyclicviewpager.BR;
import com.erolc.cyclicviewpager.R;

public abstract class CyclicDBAdapter<T> extends CyclicAdapter<T> {
    private LayoutInflater inflate;

    public abstract int getLayout(int position);
    /**
     * {@hide}
     */
    @Deprecated
    @Override
    public void onBind( View page,T data, int position) {}

    public void onBind(ViewDataBinding binding, T data, int position) {
        binding.setVariable(BR.item,data);
        binding.executePendingBindings();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        super.instantiateItem(container,position);
        if (inflate == null) {
            inflate = LayoutInflater.from(container.getContext());
        }
        ViewDataBinding binding = DataBindingUtil.inflate(this.inflate, getLayout(position), container, true);
        onBind(binding,getDataList().get(position),position);
        binding.getRoot().setTag(R.id.pagePosition,position);
        return binding.getRoot();
    }
}