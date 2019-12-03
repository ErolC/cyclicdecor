package com.erolc.cyclicdecor;

import androidx.viewpager.widget.ViewPager;

import com.erolc.cyclicdecor.adapter.CyclicAdapter;
import com.erolc.cyclicdecor.indicators.Indicator;
import com.erolc.cyclicdecor.pageChanges.CyclicPageChangeListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 */
public class CyclicDecor {
    private static final int DEFAULT_PAUSE_TIME = 1000;
    private CyclicAdapter adapter;
    private ViewPager pager;
    private Indicator indicator;
    private int isAuto = 0;
    private boolean isFastSwitch = true;

    private int interval = DEFAULT_PAUSE_TIME;
    private static Runnable runnable;


    private CyclicDecor(ViewPager pager) {
        this.pager = pager;
    }

    public static class Builder {
        private ViewPager pager;
        private CyclicAdapter adapter;
        private Indicator indicator;
        private boolean isAuto;
        private boolean isFastSwitch = true;
        private int interval = 0;

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
         * 是否快速切换
         * @param isFastSwitch
         * @return
         */
        public Builder isFastSwitch(boolean isFastSwitch) {
            this.isFastSwitch = isFastSwitch;
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
            if (interval<700){
                throw new RuntimeException("interval must be greater than 700ms");
            }
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
            if (isAuto)
            this.interval = DEFAULT_PAUSE_TIME;
            return this;
        }

        public CyclicDecor build() {
            CyclicDecor decor = new CyclicDecor(pager);
            decor.indicator = indicator;
            decor.adapter = adapter;
            decor.isAuto = isAuto?1:interval == 0?-1:0;
            decor.interval = interval;
            decor.isFastSwitch = isFastSwitch;
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
                            if (isAuto != -1) start();
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

        if (isAuto == 1) {
            start();
        }
    }

    public void start() {
        if (isAuto == -1) {
            return;
        }
        if (runnable == null) {
            runnable = () -> {
                int position = pager.getCurrentItem() + 1;
                if (isFastSwitch) {
                    pager.setCurrentItem(position);
                }else{
                    try {
                        scrollToItem(position);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
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
