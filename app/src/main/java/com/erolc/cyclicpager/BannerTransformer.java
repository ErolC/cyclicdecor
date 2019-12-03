package com.erolc.cyclicpager;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import android.view.View;


/**
 * Created by Erolc on 2019/2/9 0009.
 */

public class BannerTransformer implements ViewPager.PageTransformer {

        private static final float MAX_ALPHA=0.5f;
        private static final float MAX_SCALE_Y=0.9f;
        private static final float MAX_SCALE_X = 0.95f;


        @Override
        public void transformPage(@NonNull View page, float position) {
            //这是一开始的状态和做完切换之后保留的状态
            if(position<-1||position>1){
                //不可见区域
               // page.setAlpha(MAX_ALPHA);
                page.setScaleX(MAX_SCALE_X);
                page.setScaleY(MAX_SCALE_Y);
            }else {
                //可见区域，透明度效果
                if(position<=0){
                    //pos区域[-1,0)
                    page.setAlpha(MAX_ALPHA+MAX_ALPHA*(1+position));
                }else{
                    //pos区域[0,1]
                    page.setAlpha(MAX_ALPHA+MAX_ALPHA*(1-position));
                }
                //可见区域，缩放效果
                float scale=Math.max(MAX_SCALE_Y,1-Math.abs(position));
                float scaleX = Math.max(MAX_SCALE_X,1-Math.abs(position));
                page.setScaleX(scaleX);
                page.setScaleY(scale);
            }
        }

/*
    private static final float MIN_SCALE = 0.75f;

    public void transformPage(View page, float position) {
        int pageWidth = page.getWidth();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setAlpha(0);

        } else if (position <= 0) { // [-1,0]
            // Use the default slide transition when moving to the left page
            page.setAlpha(1);
            page.setTranslationX(0);
            page.setScaleX(1);
            page.setScaleY(1);

        } else if (position <= 1) { // (0,1]
            // Fade the page out.
            page.setAlpha(1 - position);

            // Counteract the default slide transition
            page.setTranslationX(pageWidth * -position);

            // Scale the page down (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            page.setAlpha(0);
        }
    }*/


}
