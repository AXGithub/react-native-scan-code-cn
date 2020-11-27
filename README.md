# react-native-scan-code-cn

## Getting started
### 1. Installation
Using Npm

`$ npm install react-native-scan-code-cn --save`

Using Yarn

`yarn add react-native-scan-code-cn`

Linking (for React Native <= 0.59 only)

`react-native link react-native-linear-gradient`


### 2. Configuration
- android 
1. run `npx jetify`
2. In `android/app/src/main/AndroidManifest.xml` add:
```
<uses-permission android:name="android.permission.CAMERA" />
```

- ios 
1. run `npx pod-install`

## Usage
```javaScript
import { RNScanCode } from 'react-native-scan-code-cn'

<RNScanCode
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
