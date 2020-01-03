package com.erolc.cyclicdecor.viewPager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.erolc.cyclicdecor.R;

/**
 * 可以自定义方向的viewpager
 */
public class DiffViewPager extends ViewPager {
    public static final int VERTICAL = 1;
    public static final int HORIZONTAL = 0;
    private boolean temp = false;
    private float startX = 0;
    private float startY = 0;
    private int orientation = HORIZONTAL;

    public DiffViewPager(@NonNull Context context) {
        this(context, null);
    }

    public DiffViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.DiffViewPager);
            orientation = typedArray.getInteger(R.styleable.DiffViewPager_orientation, 0);
            typedArray.recycle();
        }
        initPager();
    }


    private void initPager() {
        if (orientation == VERTICAL) {
            setPageTransformer(true, new DefaultTransformer());
        }
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
        initPager();

    }

    /**
     * 拦截touch事件
     *
     * @param ev 获取事件类型的封装类MotionEvent
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (orientation == VERTICAL) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = ev.getX();
                    startY = ev.getY();
                    temp = false;
                    break;

                case MotionEvent.ACTION_MOVE:
                    float y = ev.getY();
                    float x = ev.getX();
                    if (Math.abs(y - startY) > Math.abs(x - startX)) {//当检测到是上下滑动的时候，就应该拦截事件，这样可以解决掉会发生的左右滑动事件冲突
                        temp = true;                                  //因为原理是将上下和左右的移动距离进行交换，所以当上下滑动的时候，实际上事件是左右滑动的。
                        // 而如果viewpager子类是左右滑动的，当上下滑动该viewpager的时候事件冲突就会发生
                    }
                    break;

            }
            boolean intercept = super.onInterceptTouchEvent(swapEvent(ev));
            swapEvent(ev);
            return temp || intercept;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    /**
     * 触摸点击触发该方法
     *
     * @param ev 获取事件类型的封装类MotionEvent
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(orientation == VERTICAL ? swapEvent(ev) : ev);
    }

    /**
     * 交换x轴和y轴的移动距离
     *
     * @param event 获取事件类型的封装类MotionEvent
     */
    private MotionEvent swapEvent(MotionEvent event) {
        //获取宽高
        float width = getWidth();
        float height = getHeight();
        //将Y轴的移动距离转变成X轴的移动距离
        float swappedX = (event.getY() / height) * width;
        //将X轴的移动距离转变成Y轴的移动距离
        float swappedY = (event.getX() / width) * height;
        //重设event的位置
        event.setLocation(swappedX, swappedY);
        return event;
    }


    /**
     * 自定义 ViewPager 切换动画
     * 如果不设置切换动画，还会是水平方向的动画
     */
    public class DefaultTransformer implements ViewPager.PageTransformer {
        public static final String TAG = "simple";

        @Override
        public void transformPage(View view, float position) {
            float transX = view.getWidth() * -position;
            view.setTranslationX(transX);
            float transY = position * view.getHeight();
            view.setTranslationY(transY);
        }
    }
}
