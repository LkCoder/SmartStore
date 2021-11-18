package net.luculent.face.api.request

/**
 *
 * @Description:     人脸搜索参数
 * @Author:         yanlei.xia
 * @CreateDate:     2021/11/18 15:16
 */
data class FaceSearchReq(
    val image: String,//图片信息(总数据大小应小于10M)，图片上传方式根据image_type来判断
    val image_type: String,//图片类型BASE64:图片的base64值，base64编码后的图片数据，编码后的图片大小不超过2M；URL:图片的 URL地址( 可能由于网络等原因导致下载图片时间过长)；FACE_TOKEN: 人脸图片的唯一标识，调用人脸检测接口时，会为每个人脸图片赋予一个唯一的FACE_TOKEN，同一张图片多次检测得到的FACE_TOKEN是同一个。
    val group_id_list: String,//从指定的group中进行查找 用逗号分隔，上限10个
    val quality_control: String? = null,//图片质量控制NONE: 不进行控制LOW:较低的质量要求NORMAL: 一般的质量要求HIGH: 较高的质量要求默认 NONE若图片质量不满足要求，则返回结果中会提示质量检测失败
    val liveness_control: String? = null,//活体检测控制NONE: 不进行控制LOW:较低的活体要求(高通过率 低攻击拒绝率)NORMAL: 一般的活体要求(平衡的攻击拒绝率, 通过率)HIGH: 较高的活体要求(高攻击拒绝率 低通过率)默认NONE若活体检测结果不满足要求，则返回结果中会提示活体检测失败
    val user_id: String? = null,//当需要对特定用户进行比对时，指定user_id进行比对。即人脸认证功能。
    val max_user_num: String? = null,//查找后返回的用户数量。返回相似度最高的几个用户，默认为1，最多返回50个。
    val face_sort_type: String? = null,//人脸检测排序类型0:代表检测出的人脸按照人脸面积从大到小排列1:代表检测出的人脸按照距离图片中心从近到远排列默认为0
)