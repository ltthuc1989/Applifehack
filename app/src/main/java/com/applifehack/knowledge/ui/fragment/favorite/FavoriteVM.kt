package com.applifehack.knowledge.ui.fragment.favorite

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.widget.RelativeLayout
import com.applifehack.knowledge.BuildConfig
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.entity.PostType
import com.applifehack.knowledge.data.firebase.FirebaseAnalyticsHelper
import com.applifehack.knowledge.ui.fragment.feed.FeedNav
import com.applifehack.knowledge.ui.fragment.feed.FeedVM
import com.applifehack.knowledge.ui.widget.QuoteView
import com.applifehack.knowledge.util.extension.await
import com.applifehack.knowledge.util.extension.shareImage
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.supercab.data.local.db.DbHelper
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ezyplanet.thousandhands.util.livedata.NonNullLiveData
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.ktx.Firebase
import com.makeramen.roundedimageview.RoundedDrawable
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.sourcei.kowts.utils.functions.F
import org.sourcei.kowts.utils.pojo.QuoteResp
import javax.inject.Inject

class FavoriteVM @Inject constructor(appDataManager: AppDataManager, schedulerProvider: SchedulerProvider,  dbHelper: DbHelper, connectionManager: BaseConnectionManager
) : FeedVM(appDataManager,schedulerProvider,dbHelper, connectionManager) {







    @Override
    override fun getPost(nextPage: Boolean?) {

        if (nextPage == false) navigator?.showProgress()
        resetLoadingState = true
        uiScope?.launch {
            try {
                val data = async(Dispatchers.IO){
                    if(nextPage==false){
                        dbHelper.loadFavoritePosts(null)
                    }else{
                        dbHelper.loadFavoritePosts(mData[mData.size-1].likedDate)
                    }
                }

                data?.await().let {
                    if (!it.isNullOrEmpty()) {

                        mData.addAll(it)
                        hasMore = (it.size > visibleThreshold - 1)




                        currentPage += 1
                        showEmptyView.value = false
                    }else{
                        showEmptyView.value = true
                    }
                    results.value = mData
                    resetLoadingState = false
                    navigator?.hideProgress()


                }

            } catch (ex: Exception) {
                navigator?.hideProgress()
                resetLoadingState = false
            }

        }


    }

    override fun myFavoritePost(position: Int) {

    }


}