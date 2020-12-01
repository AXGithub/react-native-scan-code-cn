package com.reactlibrary;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.google.zxing.BarcodeFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class RCTScanCodeManager extends SimpleViewManager<CaptureView> {

    private static final String TAG = "RCTScanCodeManager";

    // 事件名,这里写个enum方便循环
    public enum Events {
        //        EVENT_CODE_TYPES("codeTypes"),
        EVENT_ON_BAR_CODE_READ("onBarCodeRead"),
        EVENT_ON_LIGHT_BRIGHT("onLightBright");

        private final String mName;

        Events(final String name) {
            mName = name;
        }

        @Override
        public String toString() {
            return mName;
        }
    }

    public static final String REACT_CLASS = "RNScanCode";
    CaptureView cap;

    /**
     * 设置别名
     *
     * @return
     */
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    /**
     * 初始化入口
     *
     * @param context
     * @return
     */
    @NonNull
    @Override
    protected CaptureView createViewInstance(@NonNull ThemedReactContext context) {
        Activity activity = context.getCurrentActivity();
        cap = new CaptureView(activity, context);
        return cap;
    }

    /**
     * 注册事件
     *
     * @return
     */
    @Override
    @Nullable
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        MapBuilder.Builder<String, Object> builder = MapBuilder.builder();
        for (Events event : Events.values()) {
            builder.put(event.toString(), MapBuilder.of("registrationName", event.toString()));
        }
        return builder.build();
    }

    @ReactProp(name = "codeTypes")
    public void setCodeTypes(CaptureView captureView, @Nullable ReadableArray codeTypes) {
        captureView.setCodeTypes(codeTypes);
    }

}
