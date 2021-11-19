package net.luculent.libcore.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.*

/**
 *
 * @Description:     viewmodel基类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/12 17:53
 */
abstract class BaseViewModel : ViewModel() {

    val launchLiveData by lazy { MutableLiveData<Boolean>() }

    fun <T> launch(
        block: suspend CoroutineScope.() -> T,
        onSuccess: ((T) -> Unit)? = null,
        onFailure: ((Exception) -> Unit)? = null
    ) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    block.invoke(this)
                }
                if (onSuccess != null) {
                    onSuccess.invoke(response)
                } else {
                    launchLiveData.postValue(true)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtils.e("launch exception= ${e.message}")
                if (onFailure != null) {
                    onFailure.invoke(e)
                } else {
                    launchLiveData.postValue(false)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}