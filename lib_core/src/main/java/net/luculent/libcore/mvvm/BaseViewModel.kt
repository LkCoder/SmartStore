package net.luculent.libcore.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

/**
 *
 * @Description:     viewmodel基类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/12 17:53
 */
abstract class BaseViewModel : ViewModel() {

    fun <T> launch(block: suspend CoroutineScope.() -> T, callback: ((T) -> Unit)? = null) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                block.invoke(this)
            }
            callback?.invoke(response)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}