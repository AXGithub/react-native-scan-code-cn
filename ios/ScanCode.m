#import "ScanCode.h"

@interface ScanCode ()

/** 扫码回调 */
@property(nonatomic, copy) RCTDirectEventBlock onBarCodeRead;

@end

@implementation ScanCode

#pragma mark - 初始化
- (id)initWithBridge:(RCTBridge *)bridge{
    if (self = [super init]) {
        self.sessionQueue = dispatch_queue_create("cameraQueue", DISPATCH_QUEUE_SERIAL);
        [self initQrCodeScanning];
    }
    return self;
}

#pragma mark - 视图移除时，释放资源
- (void)removeFromSuperview{
    [super removeFromSuperview];
    [self stopSession];
}

#pragma mark - 初始化扫码
/**
 *  扫描二维码 大概的流程应该是：
 *  1.打开设备的摄像头
 *  2.进行二维码图像捕获
 *  3.获取捕获的图像进行解析
 *  4.取得解析结果进行后续的处理
 *  这些流程需要用到AVFoundation这个库，要完成一次扫描的过程，需要用到AVCaptureSession这个类
 *  这个session类把一次扫描看做一次会话，会话开始后才是正在的'扫描'开始
 */
- (void)initQrCodeScanning {
    // 代表不在模拟器上使用
#if !(TARGET_IPHONE_SIMULATOR)
    self.session = [[AVCaptureSession alloc] init];
    // 采集高质量
    [self.session setSessionPreset:AVCaptureSessionPresetHigh];
    // 获取摄像头设备
    self.device = [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeVideo];
    if (self.device == nil) {
        return;
    }
    NSError *error = nil;
    //创建输入流
    self.input = [AVCaptureDeviceInput deviceInputWithDevice:self.device error:&error];
    if ([self.session canAddInput:self.input]) {
        [self.session addInput:self.input];
    }
    // 设置相机取景器大小，要不然会黑屏
    self.previewLayer =
    [AVCaptureVideoPreviewLayer layerWithSession:self.session];
    self.previewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill;
    self.previewLayer.needsDisplayOnBoundsChange = YES;
    self.previewLayer.frame = self.bounds;
    [self setBackgroundColor:[UIColor blackColor]];
    [self.layer insertSublayer:self.previewLayer atIndex:0];
    // 开始扫码
    [self startSession];
    
#endif
    return;
}

#pragma mark - 开启扫码
- (void)startSession{
#if !(TARGET_IPHONE_SIMULATOR)
    dispatch_async(self.sessionQueue, ^{
        if (self.metadataOutput == nil) {
            // 拍完照片以后，需要一个AVCaptureMetadataOutput对象将获取的'图像'输出，以便进行对其解析
            AVCaptureMetadataOutput * output = [[AVCaptureMetadataOutput alloc] init];
            self.metadataOutput = output;
            if ([self.session canAddOutput:self.metadataOutput]) {
                [self.metadataOutput setMetadataObjectsDelegate:self queue:self.sessionQueue];
                [self.session addOutput:self.metadataOutput];
                // 设置输出类型 有二维码 条形码等
//                [self.metadataOutput setMetadataObjectTypes:self.barCodeTypes];
                [self.metadataOutput setMetadataObjectTypes:@[AVMetadataObjectTypeQRCode]];
                // 设置全屏扫描
                output.rectOfInterest = CGRectMake(0, 0, 1.0, 1.0);
            }
        }
        [self.session startRunning];
    });
#endif
    return;
}

#pragma mark - 停止扫码
- (void)stopSession{
#if !(TARGET_IPHONE_SIMULATOR)
    dispatch_async(self.sessionQueue, ^{
        [self.previewLayer removeFromSuperlayer];
        [self.session commitConfiguration];
        [self.session stopRunning];
        for (AVCaptureInput *input in self.session.inputs) {
            [self.session removeInput:input];
        }
        for (AVCaptureOutput *output in self.session.outputs) {
            [self.session removeOutput:output];
        }
        self.metadataOutput = nil;
    });
#endif
}

// 扫描回调方法
- (void)captureOutput:(AVCaptureOutput *)output didOutputMetadataObjects:(NSArray<__kindof AVMetadataObject *> *)metadataObjects fromConnection:(AVCaptureConnection *)connection{
    
    for (AVMetadataMachineReadableCodeObject *metadata in metadataObjects) {
        for (id barcodeType in self.barCodeTypes) {
            if ([metadata.type isEqualToString:barcodeType]) {
                if (self.onBarCodeRead) {
                    // 这就是扫描的结果
                    self.onBarCodeRead(@{
                        @"data": @{
                                @"type": metadata.type,
                                @"code": metadata.stringValue
                        }});
                }
            }
        }
    }
}
@end
