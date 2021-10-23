package net.luculent.smartstore.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.dialog_login.*
import net.luculent.libcore.mvvm.ViewModelFactory
import net.luculent.libcore.popup.BaseXDialog
import net.luculent.libcore.popup.DesignPopSize
import net.luculent.libcore.showXDialog
import net.luculent.libcore.toast
import net.luculent.smartstore.R
import net.luculent.smartstore.api.response.UserInfo
import net.luculent.smartstore.verify.LoginViewModel

/**
 *
 * @Description:     登录弹框
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/18 15:20
 */
class LoginDialog(private val title: String) : BaseXDialog() {

    private val loginViewModel by lazy {
        ViewModelFactory.create(LoginViewModel::class.java, this)
    }

    var callBack: LoginCallBack? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel.loginLiveData.observe(this, Observer {
            if (it.isSuccess()) {
                callBack?.onLogin(it)
                dismiss()
            } else {
                callBack?.onLogin(null)
                context?.toast(getString(R.string.account_user_invalid_tip))
            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.dialog_login
    }

    override fun getDesignSize(): DesignPopSize {
        return DesignPopSize(540, 389)
    }

    override fun initView(view: View) {
        super.initView(view)
        login_dialog_title_tv.text = title
        login_dialog_close_iv.setOnClickListener {
            dismiss()
        }

        input_dialog_confirm_btn.setOnClickListener {
            doLogin()
        }
    }

    private fun doLogin() {
        val userid = login_dialog_account_et.text.toString()
        val password = login_dialog_password_et.text.toString()
        if (userid.isEmpty() || password.isEmpty()) {
            context?.toast(getString(R.string.account_user_empty_tip))
            return
        }
        loginViewModel.login(userid, password)
    }

    companion object {
        fun start(activity: FragmentActivity, title: String, callBack: LoginCallBack? = null) {
            val dialog = LoginDialog(title)
            dialog.callBack = callBack
            activity.showXDialog(dialog)
        }
    }

    interface LoginCallBack {
        fun onLogin(user: UserInfo?)
    }
}