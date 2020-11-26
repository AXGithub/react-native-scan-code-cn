import React, { PropTypes } from 'react'
import {
    View,
    requireNativeComponent,
    NativeModules,
    Platform,
    Dimensions
} from 'react-native'

// const ScanCodeManager = Platform.OS == 'ios' ? NativeModules.RCTScanCodeManager : NativeModules.RNScanCode

const NativeBarCode = requireNativeComponent(Platform.OS == 'ios' ? 'ScanCode' : 'RNScanCode', RNScanCode)

export default class RNScanCode extends React.Component {
    // static defaultProps = {
    //     barCodeTypes: Object.values(ScanCodeManager.barCodeTypes)
    // }

    // static propTypes = {
    //     ...View.propTypes,
    //     onBarCodeRead: PropTypes.func.isRequired,
    //     barCodeTypes: PropTypes.arrayOf(PropTypes.string)
    // }
    
    render() {
        const { children, style, ...otherProps } = this.props
        const {width, height} =  Dimensions.get('window')
        return (
            <View style={{flex: 1, backgroundColor: 'green'}}>
                <NativeBarCode
                    style={{width, height}}
                    {...otherProps}
                />
                <View style={[{ position: 'absolute', top: 0, left: 0 }, style]}>
                    {children}
                </View>
            </View>
        )
    }
}


