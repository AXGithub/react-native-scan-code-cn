package com.reactlibrary;

import android.app.Activity;
import android.app.Application;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactContext;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.reactlibrary.camera.CameraManager;
import com.reactlibrary.decoding.CaptureActivityHandler;
import com.reactlibrary.decoding.InactivityTimer;
import com.reactlibrary.util.RNScanCodeHelper;
import com.reactlibrary.util.TouchEventUtil;

import java.io.IOException;
import java.util.Vector;


/**
 * 扫码主页
 */
public class CaptureView extends FrameLayout implements Callback {
    private static final String TAG = "CaptureView";
    private CaptureActivityHandler handler;
    private Activity activity;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    // 识别码线程
    private InactivityTimer inactivityTimer;
    // 暂停事件
    private Application.ActivityLifecycleCallbacks cb;
    // 扫码类型
    private BarcodeFormat type;
    // 两指距离
    private float mOldDist = 1f;

    public CaptureView(Activity activity, @NonNull ReactContext context) {
        super(context);
        this.activity = activity;
        LayoutInflater.from(context).inflate(R.layout.activity_scanner, this);
        CameraManager.init(activity.getApplication());
        hasSurface = false;
        inactivityTimer = new InactivityTimer(context.getCurrentActivity());
        type = BarcodeFormat.QR_CODE;
        RNScanCodeHelper.setView(this);
        cb = new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                // 扫码期间暂停,应当停止扫码进程和视频流
                capture_onPause();
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        };

    }

    // 生命周期-onResume
    @Override
    protected void onAttachedToWindow() {
        init();
        if (activity != null) {
            activity.getApplication().registerActivityLifecycleCallbacks(cb);
        }
        super.onAttachedToWindow();
    }

    // 生命周期-onDestroy
    @Override
    protected void onDetachedFromWindow() {
        inactivityTimer.shutdown();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
        if (activity != null) {
            activity.getApplication().unregisterActivityLifecycleCallbacks(cb);
        }
        super.onDetachedFromWindow();

    }

    // 生命周期-onPause
    public void capture_onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    // 初始化视图
    protected void init() {
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.scanner_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
        }
        decodeFormats = null;
        characterSet = null;
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    /**
     * 扫描结果
     * @param result
     */
    public void handleDecode(Result result) {
        inactivityTimer.onActivity();
        String resultString = result.getText();
        if (!TextUtils.isEmpty(resultString)) {
//            System.out.println("sssssssssssssssss scan 0 = " + resultString);
            RNScanCodeHelper.emitScanCodeResultEvent(resultString, type);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }


    public Handler getHandler() {
        return handler;
    }

    @Override
    public void requestLayout() {
        super.requestLayout();
        post(measureAndLayout);
    }

    private final Runnable measureAndLayout = new Runnable() {
        @Override
        public void run() {
            measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
            layout(getLeft(), getTop(), getRight(), getBottom());
        }
    };

    public static void setFlashlight(boolean isFlash) {
        CameraManager.get().setFlashLight(isFlash);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_POINTER_DOWN:
                    mOldDist = TouchEventUtil.calculateFingerSpacing(event);
//                    Log.d(TAG, "onTouchEvent: ACTION_POINTER_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:
                    float newDist = TouchEventUtil.calculateFingerSpacing(event);
//                    Log.d(TAG, "onTouchEvent: newDist = " + newDist + " : mOldDist = " + mOldDist);
                    if (Math.abs(newDist - mOldDist) > 3) {
                        if (newDist > mOldDist) {
//                        Log.d(TAG, "放大");
                            handleZoom(true, CameraManager.get().getCamera());
                        } else{
//                        Log.d(TAG, "缩小");
                            handleZoom(false, CameraManager.get().getCamera());
                        }
                        mOldDist = newDist;
                    }
                    break;
            }
        }
        return true;
    }

    private static void handleZoom(boolean isZoomIn, Camera camera) {
        Camera.Parameters params = camera.getParameters();
        if (params.isZoomSupported()) {
            int zoom = params.getZoom();
//            Log.d(TAG, "handleZoom: zoom = " + zoom + " : " + params.getMaxZoom());
            if (isZoomIn && zoom < params.getMaxZoom()) {
//                Log.d(TAG, "handleZoom: 放大" + zoom);
                zoom++;
            } else if (!isZoomIn && zoom >= 2) {
//                Log.d(TAG, "handleZoom: 缩小" + zoom);
                zoom-=2;
            }
            params.setZoom(zoom);
            camera.setParameters(params);
        }
    }
}