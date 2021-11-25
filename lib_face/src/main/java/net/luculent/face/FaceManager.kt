package net.luculent.face

import android.content.Context
import android.content.Intent
import com.baidu.idl.main.facesdk.FaceAuth
import com.baidu.idl.main.facesdk.paymentlibrary.activity.FaceRGBPaymentActivity
import com.baidu.idl.main.facesdk.paymentlibrary.activity.UserManagerActivity
import com.baidu.idl.main.facesdk.paymentlibrary.listener.SdkInitListener
import com.baidu.idl.main.facesdk.paymentlibrary.manager.FaceSDKManager
import com.baidu.idl.main.facesdk.paymentlibrary.utils.ToastUtils
import net.luculent.face.api.FaceException
import net.luculent.face.api.response.AccessToken
import net.luculent.face.api.response.FaceUser
import net.luculent.face.config.FaceConfig
import net.luculent.face.logger.ILogger

/**
 *
 * @Description:     人脸识别管理类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/11/18 10:22
 */
object FaceManager {

    private lateinit var faceConfig: FaceConfig
    private var mIsInitSuccess = false

    private val faceVerifyCallBacks = mutableListOf<FaceCallBack<FaceUser>>()

    private val faceVerifyCallBack by lazy {
        object : FaceCallBack<FaceUser> {
            override fun onSuccess(result: FaceUser) {
                faceVerifyCallBacks.forEach { it.onSuccess(result) }
            }

            override fun onError(exception: FaceException) {
                faceVerifyCallBacks.forEach { it.onError(exception) }
            }
        }
    }

    fun init(context: Context, faceConfig: FaceConfig, logger: ILogger? = null) {
        if (mIsInitSuccess) {
            return
        }
        faceVerifyCallBacks.clear()
        FaceLogger.init(logger)
        this.faceConfig = faceConfig
        val license = faceConfig.license
        val auth = FaceAuth()
        auth.initLicenseBatchLine(
            context, license.licenseID
        ) { code, response ->
            FaceLogger.i("face init result= [code= $code, response= $response]")
            mIsInitSuccess = code == 0
            initFaceModel(context)
        }
    }

    private fun initFaceModel(context: Context) {
        if (FaceSDKManager.initStatus != FaceSDKManager.SDK_MODEL_LOAD_SUCCESS) {
            FaceSDKManager.getInstance().initModel(context, object : SdkInitListener {
                override fun initStart() {}
                override fun initLicenseSuccess() {}
                override fun initLicenseFail(errorCode: Int, msg: String) {}
                override fun initModelSuccess() {
                    FaceSDKManager.initModelSuccess = true
                    onInitSuccess(context)
                }

                override fun initModelFail(errorCode: Int, msg: String) {
                    FaceLogger.e("initModelFail: $errorCode, $msg")
                    FaceSDKManager.initModelSuccess = false
                    if (errorCode != -12) {
                        ToastUtils.toast(context, "人脸识别模型加载失败，请尝试重启应用")
                    }
                }
            })
        } else {
            onInitSuccess(context)
        }
    }

    private fun onInitSuccess(context: Context) {
        FaceSDKManager.getInstance().initDataBases(context)
        FaceSDKManager.getInstance().setFaceResultReceiver {
            faceVerifyCallBack.onSuccess(FaceUser(it.groupId, 99f, it.userName, it.userInfo))
        }
    }

    fun startFaceVerify(context: Context, live: Boolean = true) {
        val intent = Intent(context, FaceRGBPaymentActivity::class.java)
        context.startActivity(intent)
    }

    /**
     * 用户导入
     */
    fun goUserCenter(context: Context) {
        val intent = Intent(context, UserManagerActivity::class.java)
        context.startActivity(intent)
    }

    @JvmStatic
    fun getFaceConfig(): FaceConfig {
        return faceConfig
    }

    @JvmStatic
    fun registerFaceVerifyCallBack(callBack: FaceCallBack<FaceUser>) {
        if (!faceVerifyCallBacks.contains(callBack)) {
            faceVerifyCallBacks.add(callBack)
        }
    }

    @JvmStatic
    fun unregisterFaceVerifyCallBack(callBack: FaceCallBack<FaceUser>) {
        faceVerifyCallBacks.remove(callBack)
    }

    /**-------------百度face api访问 start-------------*/
    @JvmStatic
    fun auth(callBack: FaceCallBack<AccessToken>? = null) {
        FaceService.get().auth(callBack)
    }

    @JvmStatic
    fun verify(imageInBase64: String) {
        FaceService.get().verify(imageInBase64, faceVerifyCallBack)
    }
    /**-------------百度face api访问 end-------------*/
}