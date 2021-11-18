package net.luculent.face.api.response

/**
 *
 * @Description:     人脸认证结果
 * @Author:         yanlei.xia
 * @CreateDate:     2021/11/18 15:56
 */
data class FaceVerifyRes(
    val face_token: String?, // fid
    val user_list: List<FaceUser>?
) : FaceBaseResp()

data class FaceUser(
    val group_id: String?, // test1
    val score: Float?, // 99.3
    val user_id: String?, // u333333
    val user_info: String? // Test User
)