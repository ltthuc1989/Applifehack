package com.ltthuc.habit.ui.activity

import android.transition.Transition
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.transition.addListener
import androidx.core.transition.doOnCancel
import androidx.core.transition.doOnEnd
import androidx.core.transition.doOnStart
import androidx.databinding.ViewDataBinding
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.util.extension.gotoActivity
import com.ezyplanet.thousandhands.shipper.data.preferences.AppPreferenceHelper
import com.ltthuc.habit.ui.fragment.category.CategoryFrag
import com.ltthuc.habit.ui.fragment.feed.FeedFrag
import com.ltthuc.habit.ui.activity.setting.SettingActivity
import com.ltthuc.habit.ui.widget.listener.NavListener
import com.ltthuc.habit.util.CustomTabHelper
import com.ltthuc.habit.util.extension.openLink
import com.ltthuc.habit.util.extension.shareMessage

abstract class BaseActivity<B : ViewDataBinding, V : BaseViewModel<*, *>> : MvvmActivity<B,V>(),NavListener{

    protected var isTransitionEnd = false
    override fun onViewInitialized(binding: B) {
        super.onViewInitialized(binding)
    }

 protected val customTabHelper: CustomTabHelper by lazy{
        CustomTabHelper()
    }

    fun openBrowser(url:String?){
        openLink(url,customTabHelper)
    }
    fun shareMss(message:String){
        shareMessage(message)
    }

    protected fun doTransition(action:()->Unit){
       window?.sharedElementEnterTransition?.addListener(
                object : Transition.TransitionListener {

                    override fun onTransitionStart(transition: Transition) {}

                    override fun onTransitionEnd(transition: Transition) {
                        isTransitionEnd = true
                        action.invoke()
                    }

                    override fun onTransitionCancel(transition: Transition) {}

                    override fun onTransitionPause(transition: Transition) {}

                    override fun onTransitionResume(transition: Transition) {}
                })
    }

    protected fun useNightMode(isNight: Boolean) {
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO)
        }
        recreate()
    }



}