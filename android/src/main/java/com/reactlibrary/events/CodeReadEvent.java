package com.reactlibrary.events;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.google.zxing.BarcodeFormat;
import com.reactlibrary.RCTScanCodeManager;

public class CodeReadEvent extends Event<CodeReadEvent> {
    private String mCodeRes;
    private BarcodeFormat mCodeType;


    public CodeReadEvent(int viewTag, String code, BarcodeFormat type) {
        super(viewTag);
        mCodeRes = code;
        mCodeType = type;
    }

    private String getCodeType() {return mCodeType == null ? "null" : mCodeType.toString(); }

    private String getCodeResult() {
        return mCodeRes;
    }

    @Override
    public String getEventName() {
        return RCTScanCodeManager.Events.EVENT_ON_BAR_CODE_READ.toString();
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
        eventData.putString("type", getCodeType());
        eventData.putString("code", getCodeResult());
        return eventData;
    }
}