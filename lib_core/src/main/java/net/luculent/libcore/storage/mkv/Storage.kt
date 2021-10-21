package net.luculent.libcore.storage.mkv

import android.content.Context
import com.blankj.utilcode.util.GsonUtils
import com.tencent.mmkv.MMKV
import java.util.concurrent.ConcurrentHashMap


/**
 *
 * @Description:     存储类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/13 10:36
 */
class Storage private constructor(uniqueId: String?) {

    val storage by lazy {
        if (uniqueId.isNullOrEmpty()) {
            MMKV.defaultMMKV()
        } else {
            MMKV.mmkvWithID(uniqueId)
        }
    }

    inline fun <reified T> get(key: String): T? {
        val json = storage.decodeString(key)
        return try {
            val type = GsonUtils.getType(InnerStorageBean::class.java, T::class.java)
            val bean = GsonUtils.fromJson<InnerStorageBean<T>>(json, type)
            bean.value
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun <T> put(key: String, value: T? = null) {
        val json = GsonUtils.toJson(InnerStorageBean(value))
        storage.encode(key, json)
    }

    fun remove(key: String) {
        storage.removeValueForKey(key)
    }

    companion object {

        private val storageMap = ConcurrentHashMap<String, Storage>()

        @JvmStatic
        fun init(context: Context) {
            MMKV.initialize(context)
        }

        @JvmOverloads
        fun getInstance(uniqueId: String? = null): Storage {
            val key = uniqueId ?: ""
            if (key.isEmpty()) {
                return Storage(uniqueId)
            }
            return storageMap[key] ?: Storage(uniqueId).apply {
                storageMap[key] = this
            }
        }
    }
}