package com.applifehack.knowledge.ui.activity.quotes

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.View
import com.applifehack.knowledge.BuildConfig
import com.applifehack.knowledge.R
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ezyplanet.thousandhands.util.livedata.NonNullLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.firebase.FirebaseAnalyticsHelper
import com.applifehack.knowledge.ui.activity.BaseBottomVM
import com.applifehack.knowledge.ui.widget.QuoteView
import com.applifehack.knowledge.util.SortBy
import com.applifehack.knowledge.util.extension.await
import com.applifehack.knowledge.util.extension.shareImage
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.sourcei.kowts.utils.functions.F
import org.sourcei.kowts.utils.pojo.QuoteResp
import java.util.ArrayList
import javax.inject.Inject

class QuotesVM @Inject constructor(
    appDataManager: AppDataManager,
    schedulerProvider: SchedulerProvider,
    connectionManager: BaseConnectionManager
) : BaseBottomVM<QuotesNav, String>(appDataManager, schedulerProvider, connectionManager) {

    val results = NonNullLiveData<List<Post>>(emptyList())
    private val mData = ArrayList<Post>()

    private var lastItem: DocumentSnapshot? = null
    private var currentPage = 0
    private var quoteType: String? = null
    private var currentItem = 0
    private var hasMore = false

    @Inject
    lateinit var fbAnalytics: FirebaseAnalyticsHelper

    init {
        visibleThreshold = 10
    }

    fun getQuotes(nextPage: Boolean? = false, quoteType: String? = null) {
        if (quoteType != null) this.quoteType = quoteType

        if (nextPage == false) {
            navigator?.showProgress()
            mData?.clear()
            lastItem = null
        }
        resetLoadingState = true
        uiScope?.launch {
            try {
                val data = appDataManager.getPostByQuote(quoteType, nextPage, lastItem)

                data?.await().let {
                    if (!it.isEmpty) {
                        val snapshot = async(Dispatchers.Default) {
                            it.toObjects(Post::class.java)
                        }


                        lastItem = it.documents[it.size() - 1]
                        val rs = snapshot.await()
                        mData.addAll(rs)
                        hasMore = (rs.size > visibleThreshold - 1)


                        currentPage += 1
                    }
                    results.value = mData
                    resetLoadingState = false
                    navigator?.hideProgress()
                    showEmptyView.value = (mData?.size == 0)

                }

            } catch (ex: Exception) {
                navigator?.hideProgress()
                resetLoadingState = false
            }

        }


    }


    fun shareClick(data: Post, view: View) {
        val view = view.rootView.findViewById<QuoteView>(R.id.quoteView)
        val quote = view.getQuote()
        generataQuote(view.context, quote, data.id)
        logEvent(data?.id, "share")


    }

    fun likeClick(data: Post) {
        uiScope?.launch {

            results.value = updateRow(currentItem)
            appDataManager.updateViewCount(data.id)
            logEvent(data?.id, "like")
        }
    }

    override fun onLoadMore(position: Int) {
        currentItem = position
        if (hasMore && position + 5 > mData.size) {
            getQuotes(true)
        }

    }

    fun generataQuote(context: Context, quoteResp: QuoteResp, postId: String?) {
        uiScope?.launch {
            try {
                navigator?.showProgress()

                val result = F.generateBitmap(context, quoteResp)
                createDynamicLink(context, postId, result!!)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }


        }


    }

    private fun updateRow(position: Int): List<Post> {

        mData[position]?.apply {
            likesCount = likesCount!! + 1
        }


        return mData
    }

    private fun logEvent(id: String?, action: String) {
        val event = "$${id}_$action"
        val likeButton = "card_$action"
        val str = "app_attribute"
        fbAnalytics.logEvent(event, event, str)
        fbAnalytics.logEvent(likeButton, likeButton, str)


    }

    fun authorClick(url: String?) {
        (navigator as QuotesNav).openAuthorWiki(url)
    }

    private fun createDynamicLink(context: Context, postId: String?, bitmap: Bitmap) {
        Firebase.dynamicLinks.shortLinkAsync {
            link = Uri.parse("https://www.applifehack.com/$postId")
            domainUriPrefix = "${BuildConfig.URL_DYNAMIC_LINK}"
            androidParameters("com.applifehack.knowledge.staging") {

            }
            // Open links with this app on Android

        }.addOnSuccessListener {
            navigator?.hideProgress()
            (context as MvvmActivity<*, *>).shareImage(bitmap, it.shortLink.toString())
        }.addOnFailureListener {
            navigator?.hideProgress()
            it.printStackTrace()
        }
    }

}