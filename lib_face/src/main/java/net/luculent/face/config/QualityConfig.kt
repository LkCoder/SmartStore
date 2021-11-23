package net.luculent.face.config

import com.baidu.idl.face.platform.FaceEnvironment
import com.baidu.idl.face.platform.LivenessTypeEnum

/**
 *
 * @Description:     质量配置
 * @Author:         yanlei.xia
 * @CreateDate:     2021/11/18 10:57
 */
class QualityConfig {
    var blur: Float = 0.6f // 0.6
    var chinOcclusion: Float = 0.8f // 0.8
    var leftContourOcclusion: Float = 0.8f // 0.8
    var leftEyeOcclusion: Float = 0.8f // 0.8
    var maxIllum: Float = 220f // 220
    var minIllum: Float = 40f // 40
    var mouseOcclusion: Float = 0.8f // 0.8
    var noseOcclusion: Float = 0.8f // 0.8
    var pitch: Int = 20 // 20
    var rightContourOcclusion: Float = 0.8f // 0.8
    var rightEyeOcclusion: Float = 0.8f // 0.8
    var roll: Int = 20 // 20
    var yaw: Int = 18 // 18
    var livenessList: List<LivenessTypeEnum> = FaceEnvironment.livenessTypeDefaultList
    var livenessRandom: Boolean = false

    /**-------页面识别相关的配置---------*/
    var videoDirection: Int = -1//相机预览角度，如果是-1，预览角度是根据camera配置来的，否则根据设定的值
    var surfaceRatio: Float = 0.75f //相机预览站屏幕的宽度比
}