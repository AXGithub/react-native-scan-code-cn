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

const AMBIENT_BRIGHTNESS_DARK = Platform.OS === 'ios' ? 0 : 60

// 是否打开闪光灯
let onFlash = false
let animation = new Animated.Value(0)

const ScanQrcodeScreen = ({ navigation, onclose }) => {
    // 是否显示手电筒按钮
    const [isFlashlight, setIsFlashlight] = useState(false)
    // 显示手电筒的什么按钮
    const [flashlightType, setFlashlightType] = useState('open')

    useEffect(() => {
        InteractionManager.runAfterInteractions(() => {
            Animated.loop(
                Animated.timing(animation, {
                    toValue: adaptW(275),
                    duration: 1500,
                    easing: Easing.linear,
                    useNativeDriver: true
                })
            ).start()
        })
    }, [])

    function barcodeReceived(e) {
        HapticFeedbackService.trigger()
        if (e.code) {
            if (e.code === 'pay1' || e.code === 'pay2') {
                let data = DataSource[e.code]
                paymentStore.setPaymentInfo(data)
            } else {
                let data = DataSource['pay1']
                paymentStore.setPaymentInfo(data)
            }
        } else {
            let data = DataSource['pay1']
            paymentStore.setPaymentInfo(data)
        }
        navigation.navigate('PaymentOptionsScreen')
        onclose()
    }

    function FlashView() {
        return flashlightType === 'open' ? (
            <TouchableOpacity
                style={{ width: 100, height: 100, backgroundColor: 'green' }}
                onPress={() => {
                    RNScanCode.setFlashlight(true)
                    onFlash = true
                    setFlashlightType('close')
                }}
            >
                <Text>打开</Text>
            </TouchableOpacity>
        ) : (
            <TouchableOpacity
                style={{ width: 100, height: 100, backgroundColor: 'yellow' }}
                onPress={() => {
                    RNScanCode.setFlashlight(false)
                    onFlash = false
                    setFlashlightType('open')
                }}
            >
                <Text>关闭</Text>
            </TouchableOpacity>
        )
    }

    return (
        <View style={{ flex: 1 }}>
            <RNScanCode
                style={style.preview}
                codeTypes={[RNScanCode.Constants.CodeType.qr]}
                onBarCodeRead={barcodeReceived}
                onLightBright={(data: any) => {
                    console.log('当前的光源= ', data.light)
                    if (!onFlash) {
                        if (Number(data.light) < AMBIENT_BRIGHTNESS_DARK) {
                            setIsFlashlight(true)
                        } else {
                            setIsFlashlight(false)
                        }
                    }
                }}
            >
                <View style={style.scanView} pointerEvents="none">
                    <Image source={IconAsset.scanFrame} style={style.scanImg} />
                    <View style={style.scanAnimateView}>
                        <Animated.View
                            style={{
                                transform: [
                                    {
                                        translateY: animation
                                    }
                                ]
                            }}
                        >
                            <ShadowView style={style.animatedShadow}>
                                <View style={style.animatedStyle} />
                            </ShadowView>
                        </Animated.View>
                    </View>
                </View>
                {isFlashlight && <FlashView />}
            </RNScanCode>
        </View>
    )
}
```
