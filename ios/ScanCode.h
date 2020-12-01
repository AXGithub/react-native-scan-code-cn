#import <AVFoundation/AVFoundation.h>
#import <React/RCTBridge.h>
#import <React/RCTBridgeModule.h>
#import <React/RCTComponent.h>
#import <UIKit/UIKit.h>

@interface ScanCode : UIView <AVCaptureMetadataOutputObjectsDelegate, AVCaptureVideoDataOutputSampleBufferDelegate>

/** 线程队列 */
@property(nonatomic, strong) dispatch_queue_t sessionQueue;
/** 管理输入输出音视频流 */
@property(nonatomic, strong) AVCaptureSession *session;
/** 相机硬件的接口,用于控制硬件特性，诸如镜头的位置(前后摄像头)、曝光、闪光灯等 */
@property(nonatomic, strong) AVCaptureDevice *device;
/** 配置输入设备,提供来自设备的数据 */
@property(nonatomic, strong) AVCaptureInput *input;
/** 获取的'图像'输出，进行对其解析 */
@property (nonatomic, strong) AVCaptureMetadataOutput *metadataOutput;
/** 获取的'视频'输出，进行对其解析 */
@property (nonatomic, strong) AVCaptureVideoDataOutput *videodataOutput;
/** 对视频数据进行实时预览 */
@property(nonatomic, strong) AVCaptureVideoPreviewLayer *previewLayer;
/** 二维码类型数组 */
@property(nonatomic, strong) NSArray *barCodeTypes;
/** 扫码回调 */
@property(nonatomic, copy) RCTDirectEventBlock onBarCodeRead;
/** 光源感应回调 */
@property(nonatomic, copy) RCTDirectEventBlock onLightBright;

/** 初始化 */
- (id)initWithBridge:(RCTBridge *)bridge;
/** 打开、关闭手电筒 */
- (void)setFlashlight:(BOOL)isOpen;

@end
