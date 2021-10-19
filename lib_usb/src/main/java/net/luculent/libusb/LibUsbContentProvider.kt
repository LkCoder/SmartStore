package net.luculent.libusb

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import net.luculent.libusb.lifecycle.UsbLifeCycleCallBacksImpl

/**
 *
 * @Description:     lib模块初始化类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/19 10:22
 */
class LibUsbContentProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        init(context?.applicationContext as Application?)
        return true
    }

    private fun init(app: Application?) {
        app?.registerActivityLifecycleCallbacks(UsbLifeCycleCallBacksImpl())
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }
}