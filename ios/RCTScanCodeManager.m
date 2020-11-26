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

- (UIView *)view{
    return [[ScanCode alloc] initWithBridge:self.bridge];
}

- (dispatch_queue_t)methodQueue{
    return self.bridge.uiManager.methodQueue;
}

/** 扫码回调 */
RCT_EXPORT_VIEW_PROPERTY(onBarCodeRead, RCTDirectEventBlock);
/** 二维码类型 */
RCT_CUSTOM_VIEW_PROPERTY(barCodeTypes, NSArray, ScanCode) {
    [view setBarCodeTypes:[RCTConvert NSArray:json]];
}

+ (BOOL)requiresMainQueueSetup{
    return YES;
}

- (NSArray<NSString *> *)supportedEvents{
    return @[@"onBarCodeRead"];
}

- (NSDictionary *)constantsToExport
{
    return @{
            @"barCodeTypes": @{
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

@end
