package com.erolc.cyclicdecor.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erolc.cyclicdecor.BR;
import com.erolc.cyclicdecor.R;

/**
 * 结合DataBinding的adapter
 * @param <T> 数据的类型
 */
public abstract class CyclicDBAdapter<T> extends CyclicAdapter<T> {
    private LayoutInflater inflate;

    public abstract int getLayout(int position);
    /**
     * {@hide}
     */
    @Deprecated
    @Override
    public void onBind( View page,T data, int position) {}

    /**
     * 将数据和页面进行绑定
     * @param binding 页面
     * @param data 数据
     * @param position 位置
     */
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
