import React, { PropTypes } from 'react'
import {
    View,
    requireNativeComponent,
    NativeModules,
    Platform,
} from 'react-native'

const ScanCodeManager = Platform.OS == 'ios' ? NativeModules.RCTScanCodeManager : NativeModules.RNScanCode

export default class RNScanCode extends React.Component {
    static defaultProps = {
        barCodeTypes: Object.values(ScanCodeManager.barCodeTypes)
    }

    static propTypes = {
        ...View.propTypes,
        onBarCodeRead: PropTypes.func.isRequired,
        barCodeTypes: PropTypes.arrayOf(PropTypes.string)
    }

    render() {
        return (
            <NativeBarCode
                {...this.props}
            >
                {this.props.children}
            </NativeBarCode>
        )
    }
}

const NativeBarCode = requireNativeComponent(Platform.OS == 'ios' ? 'ScanCode' : 'RNScanCode', RNScanCode)

