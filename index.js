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

const NativeBarCode = requireNativeComponent('RNScanCode', RNScanCode)

export default class RNScanCode extends React.Component {
    static defaultProps = {
        barCodeTypes: Object.values(ScanCodeManager.barCodeTypes)
    }

    static propTypes = {
        ...ViewPropTypes,
        onBarCodeRead: PropTypes.func,
        barCodeTypes: PropTypes.arrayOf(PropTypes.string)
    }
    
    render() {
        const { children, style, ...otherProps } = this.props
        const { width, height } =  Dimensions.get('window')
        let _height = height + (Platform.OS === 'ios' ? 0 : StatusBar.currentHeight)
        return (
            <View style={{flex: 1, backgroundColor: 'green'}}>
                <NativeBarCode
                    style={{width, height: _height}}
                    {...otherProps}
                />
                <View style={[{ position: 'absolute', top: 0, left: 0 }, style]}>
                    {children}
                </View>
            </View>
        )
    }
}


