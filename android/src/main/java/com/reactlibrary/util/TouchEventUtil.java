package com.reactlibrary.util;

import android.view.MotionEvent;

public class TouchEventUtil {
    /**
     * 计算手指间距
     */
    public static float calculateFingerSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
}
