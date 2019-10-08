package com.erolc.cyclicviewpager;

import android.support.v4.view.ViewPager;

import com.erolc.cyclicviewpager.adapter.CyclicAdapter;
import com.erolc.cyclicviewpager.indicators.Indicator;
import com.erolc.cyclicviewpager.pageChanges.CyclicPageChangeListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 这个类是viewpager的包装类
 */
public class CyclicDecor {
    private static final int DEFAULT_PAUSE_TIME = 1000;
    private CyclicAdapter adapter;
    private ViewPager pager;
    private Indicator indicator;
    private boolean isAuto;

    private int interval = DEFAULT_PAUSE_TIME;
    private static Runnable runnable;


    private CyclicDecor(ViewPager pager) {
        this.pager = pager;
    }

    public static class Builder {
        private ViewPager pager;
        private CyclicAdapter adapter;
        private Indicator indicator;
        private boolean isAuto = true;
        private int interval = DEFAULT_PAUSE_TIME;

        public Builder(ViewPager pager) {
            this.pager = pager;
        }

        public Builder setAdapter(CyclicAdapter adapter) {
            this.adapter = adapter;
            return this;
        }

        public Builder setIndicator(Indicator indicator) {
            this.indicator = indicator;
            return this;
        }

        /**
         * 由于是默认开启自动轮播，那么只需要设置时间就够了
         * 如果不想自动开启，则调用下面的同名方法
         *
         * @param interval 时间间隔，自动轮播的时间间隔
         * @return 建造者本身
         */
        public Builder automatic(int interval) {
            this.interval = interval;
            return this;
        }

        /**
         * 设置是否开启自动轮播，默认开启
         *
         * @param isAuto 是否开启自动轮播
         * @return 建造者本身
         */
        public Builder automatic(boolean isAuto) {
            this.isAuto = isAuto;
            return this;
        }

        public CyclicDecor build() {
            CyclicDecor decor = new CyclicDecor(pager);
            decor.indicator = indicator;
            decor.adapter = adapter;
            decor.isAuto = isAuto;
            decor.interval = interval;
            decor.config();
            return decor;
        }
    }

    private void config() {
        if (adapter != null) {
            pager.setAdapter(adapter);
            adapter.setFirstPositionListener(position -> pager.post(() -> {
                if (indicator != null) {
                    indicator.setAdapter(adapter);
                }
                pager.setCurrentItem(position);
                //下面问题目前的解决办法，就是加载真实页数的view
                if (adapter.getCount() > 4) {
                    pager.setOffscreenPageLimit(adapter.getRealCount());
                }
            }));

            if (adapter.getCount() == 1) {
                return;
            }
            pager.addOnPageChangeListener(new CyclicPageChangeListener(adapter) {

                @Override
                public void onPageStateChange(int state) {
                    switch (state) {
                        case 0:
                            if (isAuto) start();
                            break;
                        case 1:
                            stop();
                            break;
                    }
                }

                @Override
                public void onPageChange(int toPosition) {
                    pager.setCurrentItem(toPosition, false);
                }
            });
        }


        if (indicator != null) {
            pager.addOnPageChangeListener(indicator);
        }

        if (isAuto) {
            start();
        }
    }

    public void start() {
        if (runnable == null) {
            runnable = () -> {
                pager.setCurrentItem(pager.getCurrentItem() + 1);
                pager.postDelayed(runnable, interval);
            };
            pager.postDelayed(runnable, interval);
        }
    }

    private void scrollToItem(int position) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method scrollToItem = ViewPager.class.getDeclaredMethod("setCurrentItemInternal", int.class, boolean.class, boolean.class, int.class);
        scrollToItem.setAccessible(true);
        scrollToItem.invoke(pager,position,true,false,1);
    }

    public void stop() {
        pager.removeCallbacks(runnable);
        runnable = null;
    }

}
