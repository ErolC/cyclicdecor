package com.erolc.cyclicviewpager.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.erolc.cyclicviewpager.R;

import java.util.ArrayList;
import java.util.List;

public abstract class CyclicAdapter<T> extends PagerAdapter {
    private List<T> dataList = new ArrayList<>();
    private FirstPositionListener listener;
    @SuppressLint("UseSparseArrays")

    public abstract int getLayout(int position);

    public abstract void onBind(View page,T data,int position);

    private void notifyDataChange(){
       if (dataList.size()>=2){
           T t = dataList.get(0);
           T t1 = dataList.get(1);
           T t2 = dataList.get(dataList.size()-1);
           T t3 = dataList.get(dataList.size()-2);
           dataList.add(0,t2);
           dataList.add(0,t3);
           dataList.add(t);
           dataList.add(t1);
           if (listener != null){
               listener.onFirstPosition(getCount()-2);
           }
       }
        notifyDataSetChanged();
    }

    public List<T> getDataList(){
        return dataList;
    }

    public void setDataList(List<T> datas){
        dataList.clear();
        dataList.addAll(datas);
        notifyDataChange();
    }

    public void append(int index,List<T> datas) {
        if (dataList.size() < 4) {
            return;
        }
        List<T> ts = dataList.subList(2, dataList.size() - 2);
        ts.addAll(index,datas);
        dataList = ts;
        notifyDataChange();
    }

    public void append(List<T> datas) {
        this.append(getCount(),datas);
    }

    public int getRealCount() {
        return dataList.size()-4;
    }

    public int getRealCurrentPosition(int position) {
        return position-2;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View root = View.inflate(container.getContext(), getLayout(position), null);
        container.addView(root);
        root.setTag(R.id.pagePosition,position);
        onBind(root,dataList.get(position),position);
        root.requestLayout();
        return root;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);

    }

    /**
     * 这个是前跳转的位置
     */
    public int getFirstSkipPosition() {
        return 1;
    }

    /**
     * 这是前替补跳转位置，作为getFirstSkipPosition()该位置失效时使用
     * @return
     */
    public int getSubFirstSkipPosition(){
        return 0;
    }


    /**
     * 这是后跳转的位置
     *
     * @return
     */
    public int getLastSkipPosition() {
        return dataList.size()-2;
    }

    /**
     * 后替补跳转位置，与前替补跳转位置作用相同
     * @return
     */
    public int getSubLastSkipPosition() {
        return dataList.size()-1;
    }


    public void setFirstPositionListener(FirstPositionListener listener) {
        if (listener != null && dataList.size()>0){//当第一次设置监听器，并且数据已经存在，那么就直接反馈给监听器
            listener.onFirstPosition(getCount()-2);
        }
        this.listener = listener;
    }

    public interface FirstPositionListener {
        void onFirstPosition(int position);
    }
}
