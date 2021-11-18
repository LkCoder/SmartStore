package net.luculent.face.config

/**
 *
 * @Description:     face-api注册信息
 * @Author:         yanlei.xia
 * @CreateDate:     2021/11/18 14:49
 */
data class ApiConfig(
    val appId: String,
    val apiKey: String,
    val secretKey: String,
    val groupIds: List<String>,
) {
    fun getGroupIds(): String {
        var sb = StringBuilder()
        for (groupId in groupIds) {
            sb.append(groupId).append(",")
        }
        if (sb.isNotEmpty()) {
            sb = sb.deleteCharAt(sb.length - 1)
        }
        return sb.toString()
    }
}