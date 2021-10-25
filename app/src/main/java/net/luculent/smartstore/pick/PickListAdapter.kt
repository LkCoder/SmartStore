package net.luculent.smartstore.pick

import net.luculent.libcore.image.ImageLoader
import net.luculent.libcore.recyclerview.BaseRvAdapter
import net.luculent.libcore.recyclerview.BaseRvHolder
import net.luculent.smartstore.R
import net.luculent.smartstore.api.response.PickSheet
import net.luculent.smartstore.utils.NetFileUtil
import net.luculent.smartstore.utils.StateColorUtils

/**
 *
 * @Description:     java类作用描述
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/14 14:30
 */
class PickListAdapter : BaseRvAdapter<PickSheet, BaseRvHolder>(R.layout.store_pick_list_item) {

    override fun convert(holder: BaseRvHolder, item: PickSheet) {
        holder.setText(R.id.pick_item_id_tv, item.userId)
            .setText(R.id.pick_item_state_tv, item.statusNam)
            .setText(R.id.pick_item_user_name_tv, item.userNam)
            .setText(R.id.pick_item_user_dept_tv, item.deptNam)
            .setText(R.id.pick_item_date_tv, item.date)
            .setTextColorRes(
                R.id.pick_item_state_tv,
                StateColorUtils.getTextColor(item.statusNo ?: "")
            )
            .setBackgroundResource(
                R.id.pick_item_state_tv,
                StateColorUtils.getStateBg(item.statusNo ?: "")
            )
        ImageLoader.load(
            NetFileUtil.getUrlForBreakpointDownload(item.photo),
            holder.getView(R.id.pick_item_user_photo_iv)
        )
    }

}