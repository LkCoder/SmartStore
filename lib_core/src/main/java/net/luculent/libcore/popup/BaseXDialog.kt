package net.luculent.libcore.popup

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.blankj.utilcode.util.AdaptScreenUtils
import com.blankj.utilcode.util.ScreenUtils
import net.luculent.libcore.widget.WindowViewContainer

/**
 * Created by xiayanlei on 2021/10/14
 */
abstract class BaseXDialog : DialogFragment() {

    override fun onStart() {
        super.onStart()
        val popSize = getDesignSize()
        val width = ScreenUtils.getAppScreenWidth() * popSize.popWidth / popSize.windowWidth
        AdaptScreenUtils.adaptWidth(super.getResources(), popSize.windowWidth)
        dialog?.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val windowViewContainer = WindowViewContainer(inflater.context)
        windowViewContainer.setContent(getLayoutId(), Gravity.CENTER)
        return windowViewContainer
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    open fun initView(view: View) {

    }

    abstract fun getLayoutId(): Int

    abstract fun getDesignSize(): DesignPopSize
}