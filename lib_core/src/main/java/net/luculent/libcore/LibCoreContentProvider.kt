package net.luculent.libcore

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import net.luculent.libcore.storage.file.FileManager
import net.luculent.libcore.storage.mkv.Storage
import xcrash.XCrash

/**
 *
 * @Description:     自启动初始化lib里面的东西
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/13 13:43
 */
class LibCoreContentProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        context?.apply {
            init(this)
        }
        return true
    }

    private fun init(context: Context) {
        FileManager.init(context)
        XCrash.init(context, XCrash.InitParameters().apply {
            setLogDir(FileManager.getAppNewDir("xCrash").absolutePath)
        })
        Storage.init(context)
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