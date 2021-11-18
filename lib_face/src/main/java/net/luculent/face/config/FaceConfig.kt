package net.luculent.face.config

/**
 *
 * @Description:     人脸认证配置
 * @Author:         yanlei.xia
 * @CreateDate:     2021/11/18 11:12
 */
data class FaceConfig(
    val apiConfig: ApiConfig,
    val license: FaceLicense,
    val qualityConfig: QualityConfig
)