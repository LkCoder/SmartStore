package net.luculent.libcore.mvvm

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import java.lang.reflect.Field

/**
 *
 * @Description:     viewmodel生产类
 * @Author:         yanlei.xia
 * @CreateDate:     2021/10/13 13:56
 */
object ViewModelFactory {

    fun inject(activity: FragmentActivity) {
        activity.apply {
            javaClass.fields.filter { field -> field.isAnnotationPresent(BindViewModel::class.java) }
                .forEach { field -> injectViewModel(field, this) }
        }
    }

    fun inject(fragment: Fragment) {
        fragment.apply {
            javaClass.fields
                .filter { field -> field.isAnnotationPresent(BindViewModel::class.java) }
                .forEach { field -> injectViewModel(field, this) }
        }
    }

    fun <T : ViewModel> create(modelClass: Class<T>, owner: ViewModelStoreOwner): T {
        return ViewModelProvider(owner).get(modelClass)
    }

    private fun injectViewModel(field: Field?, owner: ViewModelStoreOwner) {
        field?.apply {
            isAccessible = true
            val type = field.genericType as Class<ViewModel>
            val viewModel = create(type, owner)
            set(owner, viewModel)
        }
    }
}