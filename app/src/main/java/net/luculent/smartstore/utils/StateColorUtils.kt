package net.luculent.smartstore.utils

import net.luculent.smartstore.R

/**
 * Created by xiayanlei on 2021/10/23
 */
object StateColorUtils {

    fun getTextColor(state: String): Int {
        return when (state) {
            "03" -> R.color.red_pick
            "02" -> R.color.orange_pick
            else -> R.color.green_pick
        }
    }

    fun getStateBg(state: String): Int {
        return when (state) {
            "03" -> R.drawable.goods_pick_state_red_bg
            "02" -> R.drawable.goods_pick_state_orange_bg
            else -> R.drawable.goods_pick_state_green_bg
        }
    }
}