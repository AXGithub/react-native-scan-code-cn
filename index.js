import React from 'react'
import PropTypes from 'prop-types';
import {
    View,
    requireNativeComponent,
    NativeModules,
    Platform,
    Dimensions,
    StatusBar,
    ViewPropTypes
} from 'react-native'

const ScanCodeManager = NativeModules.RNScanCode
type EventCallbackArgumentsType = {
    nativeEvent: Object,
};

const NativeBarCode = requireNativeComponent('RNScanCode')

const EventThrottleMs = 500;
export class RNScanCode extends React.Component {
    constructor(props) {
        super(props)
        this._lastEvents = {};
        this._lastEventsTimes = {};
    }
    static defaultProps = {
        // barCodeTypes: Object.values(ScanCodeManager.barCodeTypes)
    }

    // static propTypes = {
    //     ...View.propTypes,
    //     onBarCodeRead: PropTypes.func.isRequired,
    //     barCodeTypes: PropTypes.arrayOf(PropTypes.string)
    // }

    _lastEvents: { [string]: string };
    _lastEventsTimes: { [string]: Date };

    _onObjectDetected = (callback: ?Function) => ({ nativeEvent }: EventCallbackArgumentsType) => {
        // console.log('nativeEvent = ', nativeEvent);
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

    render() {
        const { children, style, onLightBright, onBarCodeRead, ...otherProps } = this.props
        const { width, height } = Dimensions.get('window')
        let _height = height + (Platform.OS === 'ios' ? 0 : StatusBar.currentHeight)

        return (
            <View style={{ flex: 1, backgroundColor: 'green' }}>
                <NativeBarCode
                    style={{ width, height: _height }}
                    {...otherProps}
                    onBarCodeRead={this._onObjectDetected(onBarCodeRead)}
                    onLightBright={this._onObjectDetected(onLightBright)}
                />
                <View style={[{ position: 'absolute', top: 0, left: 0 }, style]}>
                    {children}
                </View>
            </View>
        )
    }
}


