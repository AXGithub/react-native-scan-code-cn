import React from 'react'
import {
    View,
    requireNativeComponent,
    NativeModules,
    Platform,
    Dimensions,
    StatusBar,
    findNodeHandle
} from 'react-native'

type EventCallbackArgumentsType = {
    nativeEvent: Object,
};

const NativeBarCode = requireNativeComponent('RNScanCode', RNScanCode)

export const ScanCodeModule = NativeModules.ScanCodeModule

const EventThrottleMs = 500;


export class RNScanCode extends React.Component {
    constructor(props) {
        super(props)
        this._lastEvents = {};
        this._lastEventsTimes = {};
    }

    static Constants = {
        CodeType: ScanCodeModule.CodeType
    };

    _lastEvents: { [string]: string };
    _lastEventsTimes: { [string]: Date };
    // 存储组件实例的tag值
    _scancodeHandle: ?number;

    _onObjectDetected = (callback: ?Function) => ({ nativeEvent }: EventCallbackArgumentsType) => {
        // console.log('nativeEvent====',nativeEvent)
        const { type } = nativeEvent;
        if (
            this._lastEvents[type] &&
            this._lastEventsTimes[type] &&
            JSON.stringify(nativeEvent) === this._lastEvents[type] &&
            new Date() - this._lastEventsTimes[type] < EventThrottleMs
        ) {
            return;
        }
        if (callback) {
            callback(nativeEvent);
            this._lastEventsTimes[type] = new Date();
            this._lastEvents[type] = JSON.stringify(nativeEvent);
        }
    };

    /** 查找对应组件实例的tag值 */
    _setReference = (ref: ?Object) => {
        if (ref) {
          this._scancodeHandle = findNodeHandle(ref);
        } else {
          this._scancodeHandle = null;
        }
    }

    /** 设置手电筒 */
    setFlashlight(isOpen: Boolean) {
        if (Platform.OS === 'ios') {
            ScanCodeManager.setFlashlight(this._scancodeHandle, isOpen)
        } else {
            ScanCodeManager.setFlashlight(isOpen)
        }
    }

    render() {
        const {
            children,
            style,
            onLightBright,
            onBarCodeRead,
            codeTypes,
            ...otherProps
        } = this.props
        const { width, height } = Dimensions.get('window')
        let _height = height + (Platform.OS === 'ios' ? 0 : StatusBar.currentHeight)

        return (
            <View style={{ flex: 1, backgroundColor: 'green' }}>
                <NativeBarCode
                    style={{ width, height: _height }}
                    {...otherProps}
                    codeTypes={codeTypes}
                    onBarCodeRead={this._onObjectDetected(onBarCodeRead)}
                    onLightBright={this._onObjectDetected(onLightBright)}
                    ref={this._setReference}
                />
                <View style={[{ position: 'absolute', top: 0, left: 0 }, style]}>
                    {children}
                </View>
            </View>
        )
    }
}


