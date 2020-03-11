package com.ltthuc.habit.ui.activity.splash

import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.util.extension.getExtraParcel
import com.ezyplanet.core.util.extension.gotoActivity
import com.ltthuc.habit.R
import com.ltthuc.habit.data.firebase.FirebaseAnalyticsHelper
import com.ltthuc.habit.data.firebase.ParamContentType
import com.ltthuc.habit.data.firebase.ParamItemName
import com.ltthuc.habit.data.firebase.PayloadResult
import com.ltthuc.habit.databinding.ActivitySplashBinding
import com.ltthuc.habit.ui.activity.home.HomeActivity
import com.ltthuc.habit.ui.fragment.feed.FeedFrag
import com.ltthuc.habit.util.AppBundleKey
import com.ltthuc.habit.util.AppBundleKey.KEY_NOTIFICATION
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

      viewModel.setData(data)

    }

    override fun gotoHomeScreen(data: PayloadResult?) {
        var event= "notification_open"
        if(data!=null){
            fbAnalytics.logEvent(event,event,"notification")
            gotoActivity(HomeActivity::class, mapOf(KEY_NOTIFICATION to data),true)
        }else{
            val bundle = intent.extras
            var payload: PayloadResult? = null
            if(bundle!=null&&bundle?.containsKey("postType")){
                payload = PayloadResult()
                bundle.keySet().forEach { it1 ->
                    if(it1.equals("postType")){
                        payload.postType = bundle.getString("postType")
                    }
                    if(it1.equals("link")) payload.link = bundle.getString("link")


                }
                fbAnalytics.logEvent(event,event,"notification")
                gotoActivity(HomeActivity::class, mapOf(KEY_NOTIFICATION to payload))
            }else{
                    gotoActivity(HomeActivity::class,true)
            }

        }

    }


}