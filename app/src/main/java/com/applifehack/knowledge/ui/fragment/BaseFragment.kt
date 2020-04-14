package com.applifehack.knowledge.ui.fragment

import android.app.Activity
import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.ui.base.MvvmFragment
import com.ezyplanet.core.util.extension.addExtra
import com.applifehack.knowledge.data.firebase.FirebaseAnalyticsHelper
import com.applifehack.knowledge.ui.activity.BaseActivity
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseFragment<B : ViewDataBinding, V : BaseViewModel<*, *>> :MvvmFragment<V,B>(){


    @Inject
    lateinit var fbAnalyticsHelper: FirebaseAnalyticsHelper

  fun gotoActivity(cls: KClass<out Activity>, finish: Boolean = false) {

        val intent = Intent(context, cls.java)
        startActivity(intent)
        if (finish) activity?.finish()
    }

    fun gotoActivityClearTask(cls: KClass<out Activity>, finish: Boolean = false) {
        val intent = Intent(context, cls.java)
        intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        if (finish) activity?.finish()
    }

    fun gotoActivity(cls: KClass<out Activity>,
                                        extras: Map<String, Any?>? = null, finish: Boolean = false) {
        val intent = Intent(context, cls.java)

        extras?.forEach { intent.addExtra(it.key, it.value) }
        startActivity(intent)
        if (finish) activity?.finish()
    }

    fun openLink(url:String?){
        (activity as BaseActivity<*,*>).openBrowser(url)
    }

}