package com.reactlibrary;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.google.zxing.BarcodeFormat;
import com.reactlibrary.events.CodeReadEvent;

import java.util.Map;

public class RCTScanCodeManager extends SimpleViewManager<CaptureActivity> {

    public enum Events {
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
    CaptureActivity cap;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @Override
    protected CaptureActivity createViewInstance(@NonNull ThemedReactContext context) {
        Activity activity = context.getCurrentActivity();
        cap = new CaptureActivity(activity, context);
        return cap;
    }

    @Override
    @Nullable
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        MapBuilder.Builder<String, Object> builder = MapBuilder.builder();
        for (Events event : Events.values()) {
            builder.put(event.toString(), MapBuilder.of("registrationName", event.toString()));
        }
        return builder.build();
    }

//    @Override
//    protected void addEventEmitters(@NonNull final ThemedReactContext reactContext, @NonNull final CaptureActivity view) {
//        super.addEventEmitters(reactContext, view);
//        view.setOnEvChangeListener(new CaptureActivity.OnEvChangeListener() {
//            @Override
//            public void getScanResult(String result, BarcodeFormat format) {
//                Log.d(" ----- ", "getScanResult: " + result + " type: " + format);
//                CodeReadEvent event = new CodeReadEvent(view.getId(), result, format);
//                reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher()
//                        .dispatchEvent(event);
//            }
//        });
//    }

//    @ReactProp(name = "barCodeTypes")
//    public void setBarCodeTypes(CaptureActivity captureActivity) {
//        captureActivity.setBarCodeTypes();
//    }

}
