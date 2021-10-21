package net.luculent.smartstore.goods

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils.dp2px
import com.yanzhenjie.recyclerview.SwipeMenuCreator
import com.yanzhenjie.recyclerview.SwipeMenuItem
import kotlinx.android.synthetic.main.activity_goods_list.*
import kotlinx.android.synthetic.main.goods_list_sub_title_content.*
import net.luculent.libcore.base.BaseActivity
import net.luculent.libcore.base.WindowConfiguration
import net.luculent.libcore.mvvm.BindViewModel
import net.luculent.libcore.popup.DialogCallBack
import net.luculent.libcore.popup.DialogConfiguration
import net.luculent.libcore.recyclerview.SimpleRvEmptyView
import net.luculent.libcore.showConfirmDialog
import net.luculent.libcore.showXDialog
import net.luculent.libusb.scan.ICodeScan
import net.luculent.smartstore.R
import net.luculent.smartstore.api.response.Goods
import net.luculent.smartstore.api.response.PickDetailResp
import net.luculent.smartstore.api.response.UserInfo
import net.luculent.smartstore.dialog.InputDialog
import net.luculent.smartstore.dialog.LoginDialog

/**
 *
 * @Description:     物资列表
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/9 10:50
 */
class GoodsListActivity : BaseActivity(), ICodeScan {

    @BindViewModel
    lateinit var goodsViewModel: GoodsViewModel

    private var goodsListAdapter: GoodsListAdapter? = null

    private var codeDialog: InputDialog? = null

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
        goodsListAdapter = GoodsListAdapter()
        val swipeCreator = SwipeMenuCreator { leftMenu, rightMenu, position ->
            val addItem: SwipeMenuItem =
                SwipeMenuItem(this@GoodsListActivity)
                    .setBackgroundColor(resources.getColor(R.color.menu_delete_color))
                    .setWidth(dp2px(105f))
                    .setText(getString(R.string.store_delete))
                    .setTextSize(19)
                    .setTextColorResource(R.color.white)
                    .setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
            rightMenu?.addMenuItem(addItem) // 添加菜单到左侧。
        }
        goods_recyclerview.apply {
            setLayoutManager(LinearLayoutManager(context))
            setSwipeMenuCreator(swipeCreator)
            setOnItemMenuClickListener { menuBridge, adapterPosition ->
                doDelGoods(adapterPosition)
            }
            setAdapter(goodsListAdapter)
            val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            divider.setDrawable(resources.getDrawable(R.drawable.goods_item_divider))
            addItemDecoration(divider)
            setEmptyView(SimpleRvEmptyView(context, "请扫物资二维码"))
        }
    }

    override fun initListener() {
        super.initListener()
        goods_code_entry_btn.setOnClickListener {
            doEnterCode()
        }
        goods_outing_btn.setOnClickListener {
            doOutStore()
        }
    }

    override fun initObserver() {
        super.initObserver()
        goodsViewModel.pickDetailResp.observe(this, Observer {
            updateView(it)
        })
        goodsViewModel.scanResultData.observe(this, Observer {
            onGoodsScanned(it)
        })
    }

    override fun initData() {
        super.initData()
        goodsViewModel.initPick(intent.getStringExtra(PICK_NO) ?: "")
        goodsViewModel.getGoodsList()
    }

    override fun onScanResult(value: String) {
        goodsViewModel.scanGoodsCode(value)
    }

    private fun doEnterCode() {
        codeDialog = InputDialog().apply {
            callBack = object : InputDialog.CodeInputCallBack {
                override fun onConfirm(code: String) {
                    goodsViewModel.scanGoodsCode(code)
                }
            }
        }
        showXDialog(codeDialog!!)
    }

    private fun doDelGoods(adapterPosition: Int) {
        val goods = goodsListAdapter?.getItem(adapterPosition)
        goods ?: return
        LoginDialog.start(
            this,
            getString(R.string.goods_delete_auth_title),
            object : LoginDialog.LoginCallBack {
                override fun onLogin(user: UserInfo?) {
                    if (user != null) {
                        goodsListAdapter?.removeAt(adapterPosition)
                    }
                }
            })
    }

    private fun doOutStore() {
        val configuration = DialogConfiguration().apply {
            content = getString(R.string.store_out_title_tip)
            contentSize = 24f
        }
        showConfirmDialog(configuration, object : DialogCallBack {
            override fun onConfirm() {
                goodsViewModel.outStore()
            }
        })
    }

    private fun updateView(pickDetailResp: PickDetailResp) {
        ticket_id_tv.text = pickDetailResp.pickId
        ticket_pick_state_tv.text = pickDetailResp.statusNam
        ticket_user_name_tv.text = pickDetailResp.userNam
        ticket_user_dept_tv.text = pickDetailResp.deptNam
        ticket_date_tv.text = pickDetailResp.date
        goodsListAdapter?.notifyDataSetChanged()
        goods_outing_btn.visibility = View.VISIBLE
        goods_code_entry_btn.visibility = View.VISIBLE
    }

    private fun onGoodsScanned(goods: Goods?) {
        if (goods == null) {//物资不在当前领料单内
            inValidGoods()
        } else {
            goodsListAdapter?.addOrReplace(goods)
        }
    }

    private fun inValidGoods() {
        val configuration = DialogConfiguration().apply {
            title = getString(R.string.store_prompt_title)
            content = getString(R.string.store_goods_invalid_tip)
            contentColor = resources.getColor(R.color.color_gray_888)
        }
        showConfirmDialog(configuration)
    }

    companion object {

        private const val PICK_NO = "pickNo"

        fun start(context: Context, pickNo: String) {
            val intent = Intent(context, GoodsListActivity::class.java)
            intent.putExtra(PICK_NO, pickNo)
            context.startActivity(intent)
        }
    }
}