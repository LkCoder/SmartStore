package net.luculent.smartstore.goods

import net.luculent.libcore.base.BaseActivity
import net.luculent.libcore.base.WindowConfiguration
import net.luculent.smartstore.R

/**
 *
 * @Description:     物资列表
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/9 10:50
 */
class GoodsListActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_goods_list
    }

    override fun getWindowConfiguration(): WindowConfiguration {
        return super.getWindowConfiguration().apply {
            enableImmersionBar = false
        }
    }

    override fun initView() {
        super.initView()
    }
}