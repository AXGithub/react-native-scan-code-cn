import React, {
    PropTypes,
    Component,
} from 'react'
import {
    View,
    requireNativeComponent,
    NativeModules,
    AppState,
    Platform,
} from 'react-native'

const BarcodeManager = Platform.OS == 'ios' ? NativeModules.RCTScanCodeManager : NativeModules.RNScanCode


export default class RNScanCode extends Component {

    static defaultProps = {
        barCodeTypes: Object.values(BarcodeManager.barCodeTypes)
    }

    static propTypes = {
        ...View.propTypes,
        onBarCodeRead: PropTypes.func.isRequired,
        barCodeTypes: PropTypes.array
    }

    render() {
        return (
            <NativeBarCode
                {...this.props}
            />
        )
    }
}

const NativeBarCode = requireNativeComponent(Platform.OS == 'ios' ? 'ScanCode' : 'RNScanCode', RNScanCode)

