package com.reactlibrary.events;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.reactlibrary.RCTScanCodeManager;

public class LightBrightEvent extends Event<LightBrightEvent> {
    private String mLight;

    public LightBrightEvent(int viewTag, String light) {
        super(viewTag);
        mLight = light;
    }

    private String getLight() {
        return mLight;
    }

    @Override
    public String getEventName() {
        return RCTScanCodeManager.Events.EVENT_ON_LIGHT_BRIGHT.toString();
    }

    @Override
    public short getCoalescingKey() {
        // All switch events for a given view can be coalesced.
        return 0;
    }

    @Override
    public void dispatch(RCTEventEmitter rctEventEmitter) {
        rctEventEmitter.receiveEvent(getViewTag(), getEventName(), serializeEventData());
    }

    private WritableMap serializeEventData() {
        WritableMap eventData = Arguments.createMap();
        eventData.putString("light", getLight());
        return eventData;
    }
}
