package com.reactlibrary;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

import java.util.Map;

public class RCTScanCodeManager extends SimpleViewManager<CaptureView> {

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
    CaptureView cap;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @Override
    protected CaptureView createViewInstance(@NonNull ThemedReactContext context) {
        Activity activity = context.getCurrentActivity();
        cap = new CaptureView(activity, context);
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

//    @ReactProp(name = "barCodeTypes")
//    public void setBarCodeTypes(CaptureActivity captureActivity) {
//        captureActivity.setBarCodeTypes();
//    }

}
