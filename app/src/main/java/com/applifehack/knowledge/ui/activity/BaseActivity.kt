package com.applifehack.knowledge.ui.activity

import android.transition.Transition
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.ui.base.MvvmActivity
import com.applifehack.knowledge.KnowledgeApp
import com.applifehack.knowledge.data.firebase.FirebaseAnalyticsHelper
import com.applifehack.knowledge.util.CustomTabHelper
import com.applifehack.knowledge.util.extension.openLink
import com.applifehack.knowledge.util.extension.shareMessage
import javax.inject.Inject

abstract class BaseActivity<B : ViewDataBinding, V : BaseViewModel<*, *>> : MvvmActivity<B,V>(){

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
        (application as KnowledgeApp).isBackGround = false
    }

    override fun onStop() {
        super.onStop()
        (application as KnowledgeApp).isBackGround = true
    }



}