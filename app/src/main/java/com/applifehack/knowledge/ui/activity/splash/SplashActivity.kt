package com.applifehack.knowledge.ui.activity.splash

import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.util.extension.gotoActivity
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.firebase.FirebaseAnalyticsHelper
import com.applifehack.knowledge.data.firebase.ParamContentType
import com.applifehack.knowledge.data.firebase.ParamItemName
import com.applifehack.knowledge.data.firebase.PayloadResult
import com.applifehack.knowledge.databinding.ActivitySplashBinding
import com.applifehack.knowledge.ui.activity.home.HomeActivity
import com.applifehack.knowledge.util.AppBundleKey.KEY_NOTIFICATION
import javax.inject.Inject

class SplashActivity : MvvmActivity<ActivitySplashBinding,SplashVM>(),SplashNav{

    override val viewModel: SplashVM by getLazyViewModel()
    override val layoutId: Int = R.layout.activity_splash
    @Inject lateinit var fbAnalytics: FirebaseAnalyticsHelper

    override fun onViewInitialized(binding: ActivitySplashBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        viewModel.navigator = this

        val param = ParamItemName.OPEN_APP_COUNT
        fbAnalytics.logEvent(param,param,ParamContentType.OPEN_APP_BY_USER)
        var data = intent?.getParcelableExtra<PayloadResult>(KEY_NOTIFICATION)

      viewModel.setData(this,data)

    }

    override fun gotoHomeScreen(data: PayloadResult?) {
        var event= "notification_open"
        if(data!=null){
            fbAnalytics.logEvent(event,event,"notification")
            gotoActivity(HomeActivity::class, mapOf(KEY_NOTIFICATION to data),true)
        }else{
            val bundle = intent.extras
            var payload: PayloadResult? = null
            if(bundle!=null&&bundle?.containsKey("postId")){
                payload = PayloadResult()
                bundle.keySet().forEach { it1 ->

                    if(it1.equals("postId")) payload.postId = bundle.getString("postId")


                }
                fbAnalytics.logEvent(event,event,"notification")
                gotoActivity(HomeActivity::class, mapOf(KEY_NOTIFICATION to payload),true)
            }else{
                    gotoActivity(HomeActivity::class,true)
            }

        }

    }


}