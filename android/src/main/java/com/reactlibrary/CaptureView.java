package com.reactlibrary;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.reactlibrary.camera.CameraManager;
import com.reactlibrary.decoding.CaptureActivityHandler;
import com.reactlibrary.decoding.InactivityTimer;

import java.util.Vector;

public class CaptureView  extends FrameLayout implements TextureView.SurfaceTextureListener {
    private Activity activity;
    private CaptureActivityHandler handler;
    private boolean isFlashOn = false;
    private boolean hasSurface;
//    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
//    private InactivityTimer inactivityTimer;

    public CaptureView(Activity activity, @NonNull Context context) {
        super(context);
        this.activity = activity;
        CameraManager.init(activity.getApplication());
        hasSurface = false;
//        inactivityTimer = new InactivityTimer(this);
    }

    /**
     * Activity onResume后调用view的onAttachedToWindow
     */
    @Override
    protected void onAttachedToWindow() {
//        init();
        super.onAttachedToWindow();
    }

//    private void init() {
//        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.scanner_view);
//        SurfaceHolder surfaceHolder = surfaceView.getHolder();
//        if (hasSurface) {
//            initCamera(surfaceHolder);
//        } else {
//            surfaceHolder.addCallback(this);
//        }
//    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }
}
