package com.reactlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactContext;
import com.reactlibrary.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.reactlibrary.camera.CameraManager;
import com.reactlibrary.decoding.CaptureActivityHandler;
import com.reactlibrary.decoding.InactivityTimer;
import com.reactlibrary.util.Constant;

import java.io.IOException;
import java.util.Vector;


/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class CaptureActivity extends FrameLayout implements Callback {

    private static final String TAG = CaptureActivity.class.getCanonicalName();

    private CaptureActivityHandler handler;
    private Activity activity;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;

    public CaptureActivity(Activity activity,  @NonNull ReactContext context) {
        super(context);
        this.activity = activity;
        LayoutInflater.from(context).inflate(R.layout.activity_scanner,this);
        CameraManager.init(activity.getApplication());
        hasSurface = false;
        inactivityTimer = new InactivityTimer(context.getCurrentActivity());
    }

    protected void init() {
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.scanner_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
//        this.activity.addContentView(surfaceView);
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
        }
        decodeFormats = null;
        characterSet = null;
    }

    // 生命周期-onResume
    @Override
    protected void onAttachedToWindow() {
        init();
        super.onAttachedToWindow();
    }

    // 生命周期-onDestroy
    @Override
    protected void onDetachedFromWindow() {
        inactivityTimer.shutdown();
        super.onDetachedFromWindow();
    }

    // 生命周期-暂停, 暴露给主activity
    public void capture_onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        String resultString = result.getText();
        //FIXME
        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(activity, "Scan failed!", Toast.LENGTH_SHORT).show();
        } else {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(Constant.INTENT_EXTRA_KEY_QR_SCAN, resultString);
            System.out.println("sssssssssssssssss scan 0 = " + resultString);
            // 不能使用Intent传递大于40kb的bitmap，可以使用一个单例对象存储这个bitmap
//            bundle.putParcelable("bitmap", barcode);
//            Logger.d("saomiao",resultString);
            resultIntent.putExtras(bundle);
//            this.setResult(RESULT_OK, resultIntent);
        }
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

    public void setBarCodeTypes() {

    }

//    /**
//     *  闪光灯开关按钮
//     */
//    private View.OnClickListener flashListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            try {
//                boolean isSuccess = CameraManager.get().setFlashLight(!isFlashOn);
//                if(!isSuccess){
//                    Toast.makeText(CaptureActivity.this, "暂时无法开启闪光灯", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (isFlashOn) {
//                    // 关闭闪光灯
//                    btnFlash.setImageResource(R.drawable.flash_off);
//                    isFlashOn = false;
//                } else {
//                    // 开启闪光灯
//                    btnFlash.setImageResource(R.drawable.flash_on);
//                    isFlashOn = true;
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    };
}