/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.reactlibrary.camera;

import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.reactlibrary.CaptureActivity;
import com.reactlibrary.RCTScanCodeModule;
import com.reactlibrary.util.RNScanCodeHelper;


final class PreviewCallback implements Camera.PreviewCallback {

  private static final String TAG = PreviewCallback.class.getSimpleName();

  private final CameraConfigurationManager configManager;
  private final boolean useOneShotPreviewCallback;
  private Handler previewHandler;
  private int previewMessage;

  // 上次环境亮度记录的时间戳
  private long mLastAmbientBrightnessRecordTime = System.currentTimeMillis();
  // 上次环境亮度记录的索引
  private int mAmbientBrightnessDarkIndex = 0;
  // 环境亮度历史记录的数组，255 是代表亮度最大值
  private long[] AMBIENT_BRIGHTNESS_DARK_LIST = new long[]{255, 255, 255, 255};
  // 环境亮度扫描间隔
  private int AMBIENT_BRIGHTNESS_WAIT_SCAN_TIME = 150;

  CaptureActivity Cap;

  PreviewCallback(CameraConfigurationManager configManager, boolean useOneShotPreviewCallback) {
    this.configManager = configManager;
    this.useOneShotPreviewCallback = useOneShotPreviewCallback;
  }

  void setHandler(Handler previewHandler, int previewMessage) {
    this.previewHandler = previewHandler;
    this.previewMessage = previewMessage;
  }

  public void onPreviewFrame(byte[] data, Camera camera) {
    Point cameraResolution = configManager.getCameraResolution();
    if (!useOneShotPreviewCallback) {
      camera.setPreviewCallback(null);
    }
    if (previewHandler != null) {
      handleAmbientBrightness(data, camera);
      Message message = previewHandler.obtainMessage(previewMessage, cameraResolution.x,
          cameraResolution.y, data);
      message.sendToTarget();
      previewHandler = null;
    } else {
      Log.d(TAG, "Got preview callback, but no handler for it");
    }
  }

  private void handleAmbientBrightness(byte[] data, Camera camera) {
    long currentTime = System.currentTimeMillis();
    if (currentTime - mLastAmbientBrightnessRecordTime < AMBIENT_BRIGHTNESS_WAIT_SCAN_TIME) {
      return;
    }
    mLastAmbientBrightnessRecordTime = currentTime;

    int width = camera.getParameters().getPreviewSize().width;
    int height = camera.getParameters().getPreviewSize().height;
    // 像素点的总亮度
    long pixelLightCount = 0L;
    // 像素点的总数
    long pixelCount = width * height;
    // 采集步长，因为没有必要每个像素点都采集，可以跨一段采集一个，减少计算负担，必须大于等于1。
    int step = 10;
    // data.length - allCount * 1.5f 的目的是判断图像格式是不是 YUV420 格式，只有是这种格式才相等
    //因为 int 整形与 float 浮点直接比较会出问题，所以这么比
    if (Math.abs(data.length - pixelCount * 1.5f) < 0.00001f) {
      for (int i = 0; i < pixelCount; i += step) {
        // 如果直接加是不行的，因为 data[i] 记录的是色值并不是数值，byte 的范围是 +127 到 —128，
        // 而亮度 FFFFFF 是 11111111 是 -127，所以这里需要先转为无符号 unsigned long 参考 Byte.toUnsignedLong()
        pixelLightCount += ((long) data[i]) & 0xffL;
      }
      // 平均亮度
      long cameraLight = pixelLightCount / (pixelCount / step);
      // 更新历史记录
      int lightSize = AMBIENT_BRIGHTNESS_DARK_LIST.length;
      AMBIENT_BRIGHTNESS_DARK_LIST[mAmbientBrightnessDarkIndex = mAmbientBrightnessDarkIndex % lightSize] = cameraLight;
      mAmbientBrightnessDarkIndex++;

      Log.i("光源 ------ ", String.valueOf(cameraLight));
//      reactContext
//              .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
//              .emit("RNScanCodeLightBright", String.valueOf(cameraLight));
      RNScanCodeHelper.emitLightBrightEvent(String.valueOf(cameraLight));
    }
  }


}
