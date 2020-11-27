package com.reactlibrary;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

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
}
