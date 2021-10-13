package net.luculent.libcore.image

import android.widget.ImageView
import com.blankj.utilcode.util.AppUtils
import com.bumptech.glide.Glide

/**
 *
 * @Description:     图片加载类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/13 17:50
 */
object ImageLoader {

    @JvmStatic
    fun load(url: String, target: ImageView) {
        Glide.with(target)
            .load(url)
            .error(AppUtils.getAppIconId())
            .into(target)
    }
}