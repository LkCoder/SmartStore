package net.luculent.smartstore.starter

import android.content.Context
import com.baidu.idl.face.platform.LivenessTypeEnum
import com.blankj.utilcode.util.LogUtils
import net.luculent.face.FaceLogger
import net.luculent.face.FaceManager
import net.luculent.face.config.ApiConfig
import net.luculent.face.config.FaceConfig
import net.luculent.face.config.FaceLicense
import net.luculent.face.config.QualityConfig
import net.luculent.face.logger.ILogger

/**
 *
 * @Description:     百度相关SDK的初始化类（第三方类库SDK的初始化使用单独的类维护）
 * @Author:         yanlei.xia
 * @CreateDate:     2021/11/18 16:53
 */
object BdSDKStarter {

    private const val FACE_APPID = "25187743"
    private const val FACE_API_KEY = "zcl7jHO164NDYGBHi6EV9eKx"
    private const val FACE_SECRET_KEY = "tAGMrblKGiyTTPCb6Wc0yzDKHph0e5zD"
    private const val FACE_LICENSE_ID = "luculent-jst-face-android"
    private const val FACE_LICENSE_NAME = "idl-license.face-android"

    fun initialize(context: Context) {
        try {
            initFaceSdk(context)
        } catch (e: Exception) {
            e.printStackTrace()
            FaceLogger.e("sdk init error= ${e.message}")
        }
    }

    private fun initFaceSdk(context: Context) {
        val apiConfig = ApiConfig(
            FACE_APPID, FACE_API_KEY, FACE_SECRET_KEY, arrayListOf("test")
        )
        val license = FaceLicense(FACE_LICENSE_ID, FACE_LICENSE_NAME)
        FaceManager.init(context, FaceConfig(apiConfig, license, QualityConfig().apply {
            livenessList = arrayListOf(LivenessTypeEnum.Eye)
        }), object : ILogger {
            override fun d(tag: String, msg: String?) {
                LogUtils.dTag(tag, msg)
            }

            override fun i(tag: String, msg: String?) {
                LogUtils.iTag(tag, msg)
            }

            override fun w(tag: String, msg: String?) {
                LogUtils.wTag(tag, msg)
            }

            override fun e(tag: String, msg: String?) {
                LogUtils.eTag(tag, msg)
            }
        })
        FaceManager.auth()
    }
}