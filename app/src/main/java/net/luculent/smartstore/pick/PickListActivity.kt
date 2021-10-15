package net.luculent.smartstore.pick

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_pick_list.*
import kotlinx.android.synthetic.main.store_pick_list_content.*
import kotlinx.android.synthetic.main.store_pick_sheet_empty.*
import net.luculent.libcore.base.BaseActivity
import net.luculent.libcore.mvvm.BindViewModel
import net.luculent.smartstore.R
import net.luculent.smartstore.goods.GoodsListActivity

/**
 *
 * @Description:     领料单列表
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/13 16:19
 */
class PickListActivity : BaseActivity() {

    private var pickListAdapter: PickListAdapter? = null

    @BindViewModel
    lateinit var pickViewModel: PickViewModel

    override fun getLayoutId(): Int {
        return R.layout.activity_pick_list
    }

    override fun initView() {
        super.initView()
        pickListAdapter = PickListAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                GoodsListActivity.start(view.context, getItem(position).pickNo)
            }
        }
        pick_recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            divider.setDrawable(resources.getDrawable(R.drawable.goods_item_divider))
            addItemDecoration(divider)
            adapter = pickListAdapter
        }
    }

    override fun initObserver() {
        super.initObserver()
        pickViewModel.pickListLiveData.observe(this, Observer {
            if (it == null || it.rows.isNullOrEmpty()) {
                pick_content.visibility = View.GONE
                pick_empty.visibility = View.VISIBLE
            } else {
                pickListAdapter?.setNewData(it.rows.toMutableList())
            }
        })
    }

    override fun initListener() {
        super.initListener()
        pick_cancel_btn.setOnClickListener {
            finish()
        }
        pick_back_btn.setOnClickListener {
            finish()
        }
    }

    override fun initData() {
        super.initData()
        pickViewModel.peekPickList()
    }
}