package com.applifehack.knowledge.ui.activity.quotes

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.applifehack.knowledge.BuildConfig
import com.applifehack.knowledge.R
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ezyplanet.thousandhands.util.livedata.NonNullLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.entity.PostType
import com.applifehack.knowledge.data.firebase.FirebaseAnalyticsHelper
import com.applifehack.knowledge.data.network.response.FactResp
import com.applifehack.knowledge.data.network.response.HackResp
import com.applifehack.knowledge.data.network.response.MeaningPhotoResp

import com.applifehack.knowledge.data.network.response.QuoteResp
import com.applifehack.knowledge.ui.activity.BaseBottomVM
import com.applifehack.knowledge.util.AppConstants
import com.applifehack.knowledge.util.CategoryType
import com.applifehack.knowledge.util.MediaUtil
import com.applifehack.knowledge.util.ShareType
import com.applifehack.knowledge.util.extension.await
import com.applifehack.knowledge.util.extension.shareImage
import com.applifehack.knowledge.util.extension.shareMessage
import com.applifehack.knowledge.util.extension.toArray
import com.ezyplanet.supercab.data.local.db.DbHelper
import com.google.android.gms.tasks.Task
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.sourcei.kowts.utils.functions.F
import java.util.*
import javax.inject.Inject

class QuotesVM @Inject constructor(
    appDataManager: AppDataManager,
    schedulerProvider: SchedulerProvider,
    connectionManager: BaseConnectionManager,val dbHelper: DbHelper
) : BaseBottomVM<QuotesNav, String>(appDataManager, schedulerProvider, connectionManager) {

    val results = NonNullLiveData<List<Post>>(emptyList())
    private val mData = ArrayList<Post>()

    val quotes = NonNullLiveData<Array<String>>(emptyArray())

    private var lastItem: DocumentSnapshot? = null
    private var currentPage = 0
    private var quoteType: String? = null
    private var currentItem = 0
    private var hasMore = false
    val shareType = MutableLiveData<ShareType>()
    private var currentLoadMorePosition = 0
    var onStop = false
    private var catType : String? = null

    @Inject
    lateinit var fbAnalytics: FirebaseAnalyticsHelper

    init {
        visibleThreshold = 10
        shareType.value = ShareType.NONE
    }

    fun initData(catType:String){
        this.catType = catType
        getPosts(false)
        getPostCat()
    }

    fun getPosts(nextPage: Boolean? = false, quoteType: String? = null) {
        if (quoteType != null) this.quoteType = quoteType

        if (nextPage == false) {
            navigator?.showProgress()
            mData?.clear()
            lastItem = null
            currentLoadMorePosition = 0
            (navigator as QuotesNav).scrollToTop()
        }
        resetLoadingState = true
        uiScope?.launch {
            try {
                val data = getApiByCat(nextPage,quoteType)

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
                    }else{
                        hasMore = false
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
        // val view = view.rootView.findViewById<QuoteView>(R.id.quoteView)
        // val quote = view.getQuote()
        //generataQuote(view.context, quote, data.id)
        generateArticle(view,data.id)
        logEvent(data?.id, "share")


    }

    fun likeClick(data: Post) {
        uiScope?.launch {
            results.value = updateRow(currentItem)
            try {
                appDataManager.updateLikeCount(data.id)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            withContext(Dispatchers.IO) {
                dbHelper.insertFavoritePost(data.apply {
                    likedDate = Date()
                    liked = true
                })
                logEvent(data?.id, "like")
            }
        }
    }

    override fun onLoadMore(position: Int) {
        currentItem = position
        if (position>currentLoadMorePosition) {
            currentLoadMorePosition = position+5

            getPosts(true)
        }

    }



    fun generateArticle(view: View,id:String?){
        uiScope?.launch {
            try {
                navigator?.showProgress()

                val parentView = view.parent.parent as RelativeLayout
                val vBottom  = parentView.findViewById<View>(R.id.vBottom)
                val linBottom = parentView.findViewById<ConstraintLayout>(R.id.linBottom)
                val linShare = parentView.findViewById<LinearLayout>(R.id.shareLayout)
                parentView.run {

                    linBottom.visibility = View.GONE
                    linShare.visibility = View.VISIBLE
                    (vBottom.layoutParams as RelativeLayout.LayoutParams ).apply {
                        height = 0
                    }
                    (parentView.layoutParams as RecyclerView.LayoutParams).apply {
                        setMargins(8,8,8,8)
                    }
                    postDelayed({
                        MediaUtil.getOutputMediaFile(view.context, F.viewToBitmap(parentView))
                        postDelayed({


                            linBottom.visibility = View.VISIBLE
                            linShare.visibility = View.GONE
                            (vBottom.layoutParams as RelativeLayout.LayoutParams).apply {
                                height = view.resources.getDimension(R.dimen.dp_16).toInt()
                            }
                            (parentView.layoutParams as RecyclerView.LayoutParams).apply {
                                setMargins(0, 0, 0, 0)
                            }
                            createDynamicLink(view.context, Post(id!!).apply {
                                type = PostType.QUOTE.type
                            })
                        }, 100)

                    }, 100)

                }




            }catch (ex:Exception){
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

    private fun createDynamicLink(context: Context, post: Post?) {
        Firebase.dynamicLinks.shortLinkAsync(ShortDynamicLink.Suffix.SHORT) {
            link =
                Uri.parse("${AppConstants.Google.PLAY_URL_DETAIL}?id=${BuildConfig.DYNAMIC_PACKAGE_NAME}&postId=${post?.id}")
            domainUriPrefix = "${BuildConfig.URL_DYNAMIC_LINK}"
            androidParameters(BuildConfig.DYNAMIC_PACKAGE_NAME) {

            }

        }.addOnSuccessListener {
            navigator?.hideProgress()
            if(post?.getPostType()!=PostType.ARTICLE) {
                (context as MvvmActivity<*, *>).shareImage(it.shortLink.toString())
            }else{
                val message = String.format(context.getString(R.string.share_info_text," '${post?.title}'",it.shortLink.toString()))
                (context as MvvmActivity<*, *>).shareMessage(message)
            }
        }.addOnFailureListener {
            navigator?.hideProgress()
            it.printStackTrace()
        }
    }

    fun onPageChange(position: Int) {
        if (!onStop) {
            myFavoritePost(position)
            onLoadMore(position)

        } else {
            onStop = false
        }
    }

    override fun onStop() {
        super.onStop()
        onStop = true
    }

    fun myFavoritePost(position: Int) {
        if(mData.isEmpty()) return
        uiScope?.launch {
            val temp = async(Dispatchers.IO) {
                dbHelper.getPostById(mData[position].id)
            }.await()
            if (temp != null) {
                results.value = mData?.apply {
                    get(position).liked = true
                }
            }

        }
    }

    fun getPostCat() {

        uiScope.launch {
            val data = getCats()
            try {
                data?.await().let {
                    if (!it.isEmpty) {
                        val snapshot = async(Dispatchers.Default) {
                            when(catType){
                                CategoryType.HACK.type->{
                                    it.toObjects(HackResp::class.java)
                                }
                                CategoryType.FACTS.type->{
                                    it.toObjects(FactResp::class.java)
                                }

                                CategoryType.MEANING_PICTURE.type->{
                                    it.toObjects(MeaningPhotoResp::class.java)
                                }

                                else->{
                                    it.toObjects(QuoteResp::class.java)
                                }
                            }

                        }

                        val temp = snapshot.await()
                        var data = ArrayList<String>()
                        temp.forEach {
                            data.add(it.toString())
                        }
                        quotes.value= toArray(data)



                    }


                }
            }catch (ex:Exception){
                ex.printStackTrace()
            }
        }


    }

    private fun getApiByCat(nextPage: Boolean? = false, quoteType: String? = null): Task<QuerySnapshot> {
        return  when(catType){
            CategoryType.HACK.type -> {
                appDataManager.getPostByHack(quoteType, nextPage, lastItem)
            }
            PostType.FACT.type -> {
                appDataManager.getPostByFact(quoteType, nextPage, lastItem)
            }
            PostType.PICTURE.type -> {
                appDataManager.getPostByPhoto(quoteType, nextPage, lastItem)
            }
            else -> {
                appDataManager.getPostByQuote(quoteType, nextPage, lastItem)
            }

        }
    }

    private fun getCats():Task<QuerySnapshot>{
        return  when(catType){
            CategoryType.HACK.type -> {
                appDataManager.getHackCat()
            }
            PostType.FACT.type -> {
                appDataManager.getFactCat()
            }
            PostType.PICTURE.type -> {
                appDataManager.getMeaningCat()
            }
            else -> {
                appDataManager.getQuoteCat()
            }

        }
    }



    fun openPostDetail(isAuthor:Boolean,data: Post) {
        uiScope?.launch {
            try {


                (navigator as QuotesNav).gotoPageUrl(isAuthor,data)

                logEvent(data?.id, "readmore")

                val resul = appDataManager.updateViewCount(data.id)
                resul.await()

            } catch (ex: Exception) {

            }


        }


    }





}