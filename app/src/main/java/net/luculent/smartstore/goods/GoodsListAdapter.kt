package net.luculent.smartstore.goods

import net.luculent.libcore.image.ImageLoader
import net.luculent.libcore.recyclerview.BaseRvAdapter
import net.luculent.libcore.recyclerview.BaseRvHolder
import net.luculent.libcore.utils.ServerManager
import net.luculent.smartstore.R
import net.luculent.smartstore.api.response.Goods

/**
 *
 * @Description:     java类作用描述
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/13 17:05
 */
class GoodsListAdapter : BaseRvAdapter<Goods, BaseRvHolder>(R.layout.store_goods_list_item) {

    override fun convert(holder: BaseRvHolder, item: Goods) {
        holder.itemView.setBackgroundResource(R.drawable.goods_item_bg)
        holder.setText(R.id.goods_name_tv, item.name)
            .setText(R.id.goods_id_tv, item.id)
            .setText(R.id.goods_num_tv, item.recQty)
        ImageLoader.load(
            item.photo.toString(),
            holder.getView(R.id.goods_photo_iv),
            R.drawable.store_default_goods_photo
        )
    }

    fun addOrReplace(goods: Goods) {
        var preIndex = -1
        run outside@{
            data.forEachIndexed { index, item ->
                if (item.no == goods.no) {
                    preIndex = index
                    return@outside
                }
            }
        }
        if (preIndex == -1) {
            addData(goods)
        } else {
            setData(preIndex, goods)
        }
    }
}