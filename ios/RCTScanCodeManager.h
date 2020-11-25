//
//  RCTCameraManager.h
//  react-native-scan-code
//
//  Created by koren on 2020/11/24.
//

#import <React/RCTViewManager.h>
#import <React/RCTBridgeModule.h>
#import "ScanCode.h"


NS_ASSUME_NONNULL_BEGIN

@interface RCTScanCodeManager : RCTViewManager<RCTBridgeModule>

@property (nonatomic, strong) ScanCode *scanCode;

@end

NS_ASSUME_NONNULL_END
