package net.luculent.face

import android.content.Context
import android.content.Intent
import com.baidu.idl.face.platform.FaceEnvironment
import com.baidu.idl.face.platform.FaceSDKManager
import com.baidu.idl.face.platform.listener.IInitCallback
import net.luculent.face.api.FaceException
import net.luculent.face.api.response.AccessToken
import net.luculent.face.api.response.FaceUser
import net.luculent.face.config.FaceConfig
import net.luculent.face.config.QualityConfig
import net.luculent.face.logger.ILogger
import net.luculent.face.ui.FaceLivenessExpActivity

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
        initQuality(faceConfig.qualityConfig)
        val license = faceConfig.license
        FaceSDKManager.getInstance().initialize(
            context,
            license.licenseID,
            license.licenseFileName,
            object : IInitCallback {
                override fun initSuccess() {
                    FaceLogger.i("initSuccess")
                    mIsInitSuccess = true
                }

                override fun initFailure(errCode: Int, errMsg: String?) {
                    FaceLogger.e("initFailure: errorCode= $errCode, errMsg= $errMsg")
                    mIsInitSuccess = false
                }
            })
    }

    fun initQuality(qualityConfig: QualityConfig) {
        val config = FaceSDKManager.getInstance().faceConfig
        // 设置模糊度阈值
        config.blurnessValue = qualityConfig.blur
        // 设置最小光照阈值（范围0-255）
        config.brightnessValue = qualityConfig.minIllum
        // 设置最大光照阈值（范围0-255）
        config.brightnessMaxValue = qualityConfig.maxIllum
        // 设置左眼遮挡阈值
        config.occlusionLeftEyeValue = qualityConfig.leftEyeOcclusion
        // 设置右眼遮挡阈值
        config.occlusionRightEyeValue = qualityConfig.rightEyeOcclusion
        // 设置鼻子遮挡阈值
        config.occlusionNoseValue = qualityConfig.noseOcclusion
        // 设置嘴巴遮挡阈值
        config.occlusionMouthValue = qualityConfig.mouseOcclusion
        // 设置左脸颊遮挡阈值
        config.occlusionLeftContourValue = qualityConfig.leftContourOcclusion
        // 设置右脸颊遮挡阈值
        config.occlusionRightContourValue = qualityConfig.rightContourOcclusion
        // 设置下巴遮挡阈值
        config.occlusionChinValue = qualityConfig.chinOcclusion
        // 设置人脸姿态角阈值
        config.headPitchValue = qualityConfig.pitch
        config.headYawValue = qualityConfig.yaw
        config.headRollValue = qualityConfig.roll
        // 设置可检测的最小人脸阈值
        config.minFaceSize = FaceEnvironment.VALUE_MIN_FACE_SIZE
        // 设置可检测到人脸的阈值
        config.notFaceValue = FaceEnvironment.VALUE_NOT_FACE_THRESHOLD
        // 设置闭眼阈值
        config.eyeClosedValue = FaceEnvironment.VALUE_CLOSE_EYES
        // 设置图片缓存数量
        config.cacheImageNum = FaceEnvironment.VALUE_CACHE_IMAGE_NUM
        // 设置活体动作，通过设置list，LivenessTypeEunm.Eye, LivenessTypeEunm.Mouth,
        // LivenessTypeEunm.HeadUp, LivenessTypeEunm.HeadDown, LivenessTypeEunm.HeadLeft,
        // LivenessTypeEunm.HeadRight
        config.livenessTypeList = qualityConfig.livenessList
        // 设置动作活体是否随机
        config.isLivenessRandom = qualityConfig.livenessRandom
        // 设置开启提示音
        config.isSound = true
        FaceSDKManager.getInstance().faceConfig = config
    }

    fun startFaceVerify(context: Context) {
        val intent = Intent(context, FaceLivenessExpActivity::class.java)
        context.startActivity(intent)
    }

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