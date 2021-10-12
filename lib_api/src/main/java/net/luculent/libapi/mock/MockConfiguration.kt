package net.luculent.libapi.mock

/**
 *
 * @Description:     mock配置
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/12 15:38
 */
data class MockConfiguration(
    val mockEnabled: Boolean = true,
    val assetPaths: List<String>,
) {
    val mockPaths: List<String>? = null//用于开启mock，但是只想支持部分path的情况，如果为空，则表示全部mock
}