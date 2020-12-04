//
//  RCTCameraManager.m
//  react-native-scan-code
//
//  Created by koren on 2020/11/24.
//

#import "RCTScanCodeManager.h"
#import "ScanCode.h"
#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>
#import <AVFoundation/AVFoundation.h>

@implementation RCTScanCodeManager

RCT_EXPORT_MODULE(RNScanCodeManager)

/** 视图初始化加载 */
- (UIView *)view{
    return [[ScanCode alloc] initWithBridge:self.bridge];
}

/** 线程队列创建 */
- (dispatch_queue_t)methodQueue{
    return self.bridge.uiManager.methodQueue;
}

/** 指定在主线程初始化 */
+ (BOOL)requiresMainQueueSetup{
    return YES;
}

/** 导出常量 */
- (NSDictionary *)constantsToExport{
    return @{
            @"CodeType": @{
                 @"upce": AVMetadataObjectTypeUPCECode,
                 @"code39": AVMetadataObjectTypeCode39Code,
                 @"code39mod43": AVMetadataObjectTypeCode39Mod43Code,
                 @"ean13": AVMetadataObjectTypeEAN13Code,
                 @"ean8":  AVMetadataObjectTypeEAN8Code,
                 @"code93": AVMetadataObjectTypeCode93Code,
                 @"code128": AVMetadataObjectTypeCode128Code,
                 @"pdf417": AVMetadataObjectTypePDF417Code,
                 @"qr": AVMetadataObjectTypeQRCode,
                 @"aztec": AVMetadataObjectTypeAztecCode,
                 @"interleaved2of5": AVMetadataObjectTypeInterleaved2of5Code,
                 @"itf14": AVMetadataObjectTypeITF14Code,
                 @"datamatrix": AVMetadataObjectTypeDataMatrixCode
                }
            };
}

/** 扫码回调 */
RCT_EXPORT_VIEW_PROPERTY(onBarCodeRead, RCTDirectEventBlock);
/** 光源感应回调 */
RCT_EXPORT_VIEW_PROPERTY(onLightBright, RCTDirectEventBlock);
/** 二维码类型 */
RCT_CUSTOM_VIEW_PROPERTY(codeTypes, NSArray, ScanCode) {
    [view setCodeTypes:[RCTConvert NSArray:json]];
}
/** 打开、关闭手电筒 */
RCT_EXPORT_METHOD(setFlashlight:(nonnull NSNumber *)reactTag isOpen:(BOOL)isOpen){
    [self.bridge.uiManager addUIBlock:^(RCTUIManager *uiManager, NSDictionary<NSNumber *,ScanCode *> *viewRegistry) {
        if (reactTag == nil) {
            RCTLogError(@"Invalid view returned from registry, expecting ScanCode");
        } else {
            ScanCode *view = viewRegistry[reactTag];
            if (![view isKindOfClass:[ScanCode class]]) {
                RCTLogError(@"Invalid view returned from registry, expecting ScanCode, got: %@", view);
            } else {
                [view setFlashlight:isOpen];
            }
        }
    }];
}

@end
