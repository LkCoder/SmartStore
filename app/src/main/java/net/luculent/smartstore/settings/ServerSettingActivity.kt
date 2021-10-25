package net.luculent.smartstore.settings

import kotlinx.android.synthetic.main.activity_server_setting.*
import net.luculent.libapi.http.HttpFactory
import net.luculent.libcore.base.BaseActivity
import net.luculent.libcore.base.WindowConfiguration
import net.luculent.libcore.utils.ServerManager
import net.luculent.smartstore.R

/**
 *
 * @Description:     服务器设置页面
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/25 13:39
 */
class ServerSettingActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_server_setting
    }

    override fun initView() {
        super.initView()
        server_ip_et.setText(ServerManager.getHost())
        server_port_et.setText(ServerManager.getPort())
    }

    override fun initListener() {
        super.initListener()
        server_save_btn.setOnClickListener {
            saveServer()
        }
    }

    private fun saveServer() {
        val host = server_ip_et.text.toString()
        val port = server_port_et.text.toString()
        if (host.isEmpty() || port.isEmpty()) {
            return
        }
        ServerManager.init("http://$host:$port")
        HttpFactory.clearCache()
        finish()
    }

    override fun getWindowConfiguration(): WindowConfiguration {
        return super.getWindowConfiguration().apply {
            enableImmersionBar = false
        }
    }
}