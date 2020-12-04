# react-native-scan-code-cn

## Getting started
### 1. Installation
Using Npm

`$ npm install react-native-scan-code-cn --save`

Using Yarn

`yarn add react-native-scan-code-cn`

Linking (for React Native < 0.60 only)

`react-native link react-native-scan-code-cn`


### 2. Configuration
- android 
1. run `npx jetify`
2. In `android/app/src/main/AndroidManifest.xml` add:
```
<uses-permission android:name="android.permission.CAMERA" />
```

- iOS 
1. run `npx pod-install`
2. On iOS, you must update Info.plist with a usage description for camera
```
<key>NSCameraUsageDescription</key>
<string>Your own description of the purpose</string>
```

## Usage

Make sure permissions are turned on before using

```javaScript
import { RNScanCode } from 'react-native-scan-code-cn'

<RNScanCode
    codeTypes={[RNScanCode.Constants.CodeType.qr]}
    onBarCodeRead={res => {
        console.log('扫码结果 = ', res)
    }}
    onLightBright={light => {
        console.log('当前的光源= ', light)
    }}
>
    
    {/* 其他UI */}
</RNScanCode>
```
