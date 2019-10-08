package com.erolc.cyclicviewpager.indicators;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.erolc.cyclicviewpager.R;
import com.erolc.cyclicviewpager.adapter.CyclicAdapter;

/**
 * Created by ErolC on 2018/3/21.
 */

public class CyclicIndicator extends View implements Indicator {

    public static final int CENTER = 0;
    public static final int START = 1;
    public static final int END = 2;

    public static final int CIRCLE = 3;
    public static final int RECT = 4;

    private int mCurrentPage;

    private Paint mPaintSelect = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPaintDefault = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int selectColor = Color.parseColor("#FFFFFF");
    private int defaultColor = Color.parseColor("#818181");

    private int interval = 10;
    private int itemHeight = 0;
    private int itemWidth = 10;

    private int gravity = CENTER;
    private int r;

    private int mIndicatorCount;
    private CyclicAdapter adapter;
    private int shape = CIRCLE;

    private boolean isExtremityRound = true;


    public CyclicIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    public CyclicIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CyclicIndicator);
            interval = ta.getDimensionPixelSize(R.styleable.CyclicIndicator_interval, interval);
            itemHeight = ta.getDimensionPixelSize(R.styleable.CyclicIndicator_itemHeight, itemHeight);
            itemWidth = ta.getDimensionPixelSize(R.styleable.CyclicIndicator_itemWidth, itemWidth);
            selectColor = ta.getColor(R.styleable.CyclicIndicator_selectColor, Color.parseColor("#FFFFFF"));
            defaultColor = ta.getColor(R.styleable.CyclicIndicator_defaultColor, Color.parseColor("#818181"));
            gravity = ta.getInteger(R.styleable.CyclicIndicator_gravity, CENTER);
            isExtremityRound = ta.getBoolean(R.styleable.CyclicIndicator_round, isExtremityRound);
            shape = ta.getInteger(R.styleable.CyclicIndicator_shape, CIRCLE);
            ta.recycle();
        }
        if (isExtremityRound) {
            mPaintSelect.setStrokeCap(Paint.Cap.ROUND);
            mPaintDefault.setStrokeCap(Paint.Cap.ROUND);
        }
        mPaintDefault.setColor(defaultColor);
        mPaintSelect.setColor(selectColor);
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public void setSelectColor(int color) {
        selectColor = color;
        mPaintSelect.setColor(selectColor);
    }

    public void setDefaultColor(int color) {
        defaultColor = color;
        mPaintDefault.setColor(defaultColor);
    }

    public void setInterval(int interval) {
        this.interval = interval;
        invalidate();
    }


    public void setExtremityRound(boolean isExtremityRound) {
        this.isExtremityRound = isExtremityRound;
        if (isExtremityRound) {
            mPaintSelect.setStrokeCap(Paint.Cap.ROUND);
            mPaintDefault.setStrokeCap(Paint.Cap.ROUND);
        }
        invalidate();
    }

    public void setAdapter(CyclicAdapter adapter) {
        this.adapter = adapter;
        mIndicatorCount = adapter.getRealCount();
        mCurrentPage = adapter.getRealCount() % mIndicatorCount;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int realHeight;
        if (mode == MeasureSpec.AT_MOST) {
            r = 5;
            if (itemHeight == 0) {
                itemHeight = 5;
            }
            realHeight = getPaddingTop() + getPaddingBottom() + (shape == RECT ? itemHeight*2 : r * 2);
            realHeight = Math.min(size, realHeight);
        } else {
            r = (size - getPaddingBottom() - getPaddingTop()) / 2;
            if (itemHeight == 0) {
                itemHeight = r*2;
            }
            realHeight = size;
        }

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), realHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (adapter == null || mIndicatorCount == 0) {
            return;
        }
        if (adapter.getRealCount() != 0) {
            int number = adapter.getRealCount();
            int y = getHeight() / 2;
            int startX;
            int w = (shape == RECT ? (number * (itemWidth+(isExtremityRound?itemHeight:0))+itemWidth) : (number * 2 * r)) + (number - 1) * interval;
            if (gravity == CENTER) {
                startX = (getWidth() - w) / 2+(shape == RECT ? (itemWidth+(isExtremityRound?itemHeight:0))/2 : r);
            } else if (gravity == END) {
                startX = (getWidth() - getPaddingRight() - w) - (shape == RECT ? itemWidth / 2 : r);
            } else {
                startX = getPaddingLeft() + (shape == RECT ? itemWidth / 2 : r);
            }

            if (shape == RECT) {
                int top = getHeight() /2;
                mPaintSelect.setStrokeWidth(itemHeight);
                mPaintDefault.setStrokeWidth(itemHeight);
                drawRect(canvas, top, number, startX);
            } else {
                mPaintSelect.setStrokeCap(Paint.Cap.ROUND);
                mPaintDefault.setStrokeCap(Paint.Cap.ROUND);
                mPaintSelect.setStrokeWidth(r*2);
                mPaintDefault.setStrokeWidth(r*2);
                drawCircle(canvas, y, number, startX);
            }
        }
    }

    private void drawRect(Canvas canvas, int top, int number, int startX) {
        for (int i = 0; i < number; i++) {
            if (mCurrentPage == i) {
                canvas.drawLine(startX, top, startX + itemWidth, top, mPaintSelect);
            } else {
                canvas.drawLine(startX, top, startX + itemWidth, top , mPaintDefault);
            }
            startX += (itemWidth + interval+(isExtremityRound?itemHeight:0));
        }

    }

    private void drawCircle(Canvas canvas, int y, int number, int startX) {


        for (int i = 0; i < number; i++) {
            if (mCurrentPage == i) {
                canvas.drawPoint(startX, y, mPaintSelect);
            } else {
                canvas.drawPoint(startX, y, mPaintDefault);
            }
            startX += (r * 2 + interval);
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mIndicatorCount != 0) {
            this.mCurrentPage = adapter.getRealCurrentPosition(position) % mIndicatorCount;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 0) {
            postInvalidate();
        }
    }

    @Override
    public void onPageSelected(int position) {
    }
}
