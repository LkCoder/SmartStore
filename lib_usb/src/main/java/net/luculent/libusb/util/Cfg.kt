package net.luculent.libusb.util

object Cfg {
    const val TAG = "Cfg"
    const val DEBUG = true
    val RGB_LIST = arrayListOf(1568, 1569)
    val IR_LIST = arrayListOf(1312, 1313)
    const val OB_DEVICE_VID = 11205
    const val MSG_PRE_FAIL = -101
    const val MSG_TEST_FAIL = -100
    private const val NAME_I1S = 1312
    private const val NAME_I2 = 1313
    var fwVersion: String? = null
    var cameraSn: String? = null
    var fwVersion_ir: String? = null
    var cameraName: String? = null
    var isI1I2Mode = 2

    fun getCameraName(colorProductId: Int): String {
        if (NAME_I1S == colorProductId) {
            return "I1S"
        } else if (NAME_I2 == colorProductId) {
            return "I2"
        }
        return "unknown"
    }
}