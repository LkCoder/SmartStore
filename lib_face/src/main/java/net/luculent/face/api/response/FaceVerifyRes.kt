package net.luculent.face.api.response

/**
 *
 * @Description:     人脸认证结果
 * @Author:         yanlei.xia
 * @CreateDate:     2021/11/18 15:56
 */
data class FaceVerifyRes(
    val cached: Int?, // 0
    val error_code: Int?, // 0
    val error_msg: String?, // SUCCESS
    val log_id: Long?, // 9935101759920
    val result: Result?,
    val timestamp: Int? // 1637288570
)

data class Result(
    val face_token: String?, // fid
    val user_list: List<FaceUser>?
)

data class FaceUser(
    val group_id: String?, // test1
    val score: Float?, // 99.3
    val user_id: String?, // u333333
    val user_info: String? // Test User
)
