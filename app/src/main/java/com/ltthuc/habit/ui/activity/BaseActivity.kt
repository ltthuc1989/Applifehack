package com.ltthuc.habit.ui.activity

import android.transition.Transition
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.transition.addListener
import androidx.core.transition.doOnCancel
import androidx.core.transition.doOnEnd
import androidx.core.transition.doOnStart
import androidx.databinding.ViewDataBinding
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.util.extension.gotoActivity
import com.ezyplanet.thousandhands.shipper.data.preferences.AppPreferenceHelper
import com.ltthuc.habit.HabitApp
import com.ltthuc.habit.data.firebase.FirebaseAnalyticsHelper
import com.ltthuc.habit.ui.fragment.category.CategoryFrag
import com.ltthuc.habit.ui.fragment.feed.FeedFrag
import com.ltthuc.habit.ui.activity.setting.SettingActivity
import com.ltthuc.habit.ui.widget.listener.NavListener
import com.ltthuc.habit.util.CustomTabHelper
import com.ltthuc.habit.util.extension.openLink
import com.ltthuc.habit.util.extension.shareMessage
import javax.inject.Inject

abstract class BaseActivity<B : ViewDataBinding, V : BaseViewModel<*, *>> : MvvmActivity<B,V>(),NavListener{

    protected var isTransitionEnd = false
    override fun onViewInitialized(binding: B) {
        super.onViewInitialized(binding)
    }
    @Inject
    lateinit var fbAnalytics: FirebaseAnalyticsHelper
   protected val icon = "Icons"
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

    protected fun setToolBar(toolbar: Toolbar, title: String) {
        toolbar.setTitle(title)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        (application as HabitApp).isBackGround = false
    }

    override fun onStop() {
        super.onStop()
        (application as HabitApp).isBackGround = true
    }

}