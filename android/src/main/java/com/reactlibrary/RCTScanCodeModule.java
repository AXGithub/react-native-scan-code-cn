package com.reactlibrary;

import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class RCTScanCodeModule extends ReactContextBaseJavaModule {
    private static ReactApplicationContext _reactContext;

    @NonNull
    @Override
    public String getName() {
        return "RCTScanCodeModule";
    }

    public RCTScanCodeModule(ReactApplicationContext reactContext) {
        super(reactContext);
        _reactContext = reactContext;
    }

    public static ReactApplicationContext getReactContextSingleton() {
        return _reactContext;
    }

    /**
     * 设置闪光灯
     * @param onFlash
     */
    @ReactMethod
    public void setFlashlight(boolean onFlash) {
        CaptureView.setFlashlight(onFlash);
    }
}
