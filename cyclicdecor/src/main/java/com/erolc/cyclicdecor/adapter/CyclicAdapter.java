package com.erolc.cyclicdecor.adapter;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;


import com.erolc.cyclicdecor.R;

import java.util.ArrayList;
import java.util.List;

public abstract class CyclicAdapter<T> extends PagerAdapter {
    private List<T> dataList = new ArrayList<>();
    private FirstPositionListener listener;

    /**
     * 设置布局
     * @param position 位置
     * @return 布局id
     */
    public abstract int getLayout(int position);

    /**
     * 将数据和页面进行绑定
     * @param page 页面
     * @param data 数据
     * @param position 位置
     */
    public abstract void onBind(View page,T data,int position);

    /**
     * 在通知数据改变之前，需要产生一些辅助数据。
     */
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

    /**
     * 获得数据
     * @return data
     */
    public List<T> getDataList(){
        return dataList;
    }

    /**
     * 除了append之外唯一设置数据方法。
     * @param datas 数据
     */
    public void setDataList(List<T> datas){
        dataList.clear();
        dataList.addAll(datas);
        notifyDataChange();
    }

    /**
     * 追加方法，将数据追加在特定位置上
     * @param index 位置
     * @param datas 数据
     */
    public void append(int index,List<T> datas) {
        if (dataList.size() < 4) {
            return;
        }
        List<T> ts = dataList.subList(2, dataList.size() - 2);
        ts.addAll(index,datas);
        dataList = ts;
        notifyDataChange();
    }

    /**
     * 追加数据方法
     * @param datas
     */
    public void append(List<T> datas) {
        this.append(getCount(),datas);
    }

    /**
     * 真实的数据数量
     * @return 数据数量
     */
    public int getRealCount() {
        return dataList.size()-4;
    }

    /**
     * 返回真实的数据位置
     * @param position 页面位置
     * @return 对应的数据的位置
     */
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
     * @return position
     */
    public int getSubFirstSkipPosition(){
        return 0;
    }


    /**
     * 这是后跳转的位置
     *
     * @return position
     */
    public int getLastSkipPosition() {
        return dataList.size()-2;
    }

    /**
     * 后替补跳转位置，与前替补跳转位置作用相同
     * @return position
     */
    public int getSubLastSkipPosition() {
        return dataList.size()-1;
    }

    /**
     * 设置监听器，用于在数据设置完毕之后将页面调整到第一个数据的位置
     * @param listener 监听器
     */
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
