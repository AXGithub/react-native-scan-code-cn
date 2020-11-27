package com.reactlibrary.util;

import android.view.ViewGroup;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.google.zxing.BarcodeFormat;
import com.reactlibrary.RCTScanCodeModule;
import com.reactlibrary.events.CodeReadEvent;
import com.reactlibrary.events.LightBrightEvent;

/**
 * 扫码回调类
 */
public class RNScanCodeHelper {
    private static ViewGroup _view;

    public static void setView(ViewGroup view) {
        _view = view;
    }
    /**
     * 扫码结果回调js端
     * @param code
     */
    public static void emitScanCodeResultEvent(final String code, final BarcodeFormat type) {
        final ReactContext reactContext = RCTScanCodeModule.getReactContextSingleton();
        reactContext.runOnNativeModulesQueueThread(new Runnable() {
            @Override
            public void run() {
                CodeReadEvent event = new CodeReadEvent(_view.getId(), code, type);
                reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher().dispatchEvent(event);
            }
        });
    }

    /**
     * 当前光源实时回调给js
     * @param light
     */
    public static void emitLightBrightEvent(final String light) {
        final ReactContext reactContext = RCTScanCodeModule.getReactContextSingleton();
        reactContext.runOnNativeModulesQueueThread(new Runnable() {
            @Override
            public void run() {
                LightBrightEvent event = new LightBrightEvent(_view.getId(), light);
                reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher().dispatchEvent(event);
            }
        });
    }
}
