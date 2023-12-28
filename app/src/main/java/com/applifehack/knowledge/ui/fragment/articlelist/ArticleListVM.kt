package com.applifehack.knowledge.ui.fragment.articlelist

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.applifehack.knowledge.BuildConfig
import com.applifehack.knowledge.R
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ezyplanet.thousandhands.util.livedata.NonNullLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.firebase.FirebaseAnalyticsHelper
import com.applifehack.knowledge.util.AppConstants
import com.applifehack.knowledge.util.SortBy
import com.applifehack.knowledge.util.extension.await
import com.applifehack.knowledge.util.extension.shareMessage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ezyplanet.core.GlideApp
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.supercab.data.local.db.DbHelper
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

class ArticleListVM @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider,val dbHelper: DbHelper, connectionManager: BaseConnectionManager
) : BaseViewModel<ArticleListNav, String>(schedulerProvider, connectionManager) {

    val results = NonNullLiveData<List<Post>>(emptyList())
    private val mData = ArrayList<Post>()
    private var lastItem: DocumentSnapshot?=null
    private var currentPage=0
    lateinit var catId:String
    @Inject lateinit var fbAnalytics:FirebaseAnalyticsHelper
    override fun updateModel(data: String?) {
        catId = data!!
        getPost(catId)

    }


    fun getPost(catId:String?,nextPage: Boolean? = false) {

        if (nextPage == false) navigator?.showProgress()
        resetLoadingState = true
        uiScope?.launch {
            try {
                val data = appDataManager.getPostByCat(catId, SortBy.NEWEST, nextPage,lastItem)

                data?.await().let {
                    if(!it!!.isEmpty) {
                        val snapshot = async(Dispatchers.Default) {
                            it.toObjects(Post::class.java)
                        }


                        lastItem = it.documents[it.size() - 1]
                        val rs = snapshot.await()
                        mData.addAll(rs)
                        rs.forEach {
                            Log.d("PostTitle",it.title!!)
                        }

                        isNoMoreDataLoad = false

                        currentPage +=1
                    }else{
                        isNoMoreDataLoad = true
                    }


                    results.value = myFavoritePost(mData)
                    resetLoadingState = false
                    navigator?.hideProgress()

                    Log.d("currentPage","$currentPage")
                    Log.d("Post size:","${mData.size}")
                }

            } catch (ex: Exception) {
                navigator?.hideProgress()
                resetLoadingState = false
            }

        }


    }

    fun onItemClicked(item: Post){
        navigator?.gotoPostDetail(item)
        tapEvent(item?.id)
    }

    fun onLoadMore(page: Int) {
        if(isNoMoreDataLoad) return
        getPost(catId,true)
    }

    fun shareClick(context: Context, data:Post){

//            val download = context.getString(R.string.download)
//            val applink = context.getString(R.string.app_link)
//            val messge = String.format(context.getString(R.string.share_info_message),
//                    context.getString(R.string.app_name))+"\n ${data.title}\n ${data.redirect_link} \n $download\n $applink"
//            navigator?.share(messge)
        logEvent(data?.id,"share")


        generateArticle(context,data)




    }
//    fun likeClick(data:Post){
//
//        uiScope?.launch {
//
//           results.value = updateRow(data)
//            appDataManager.updateViewCount(data.id)
//            logEvent(data?.id,"like")
//        }
//
//
//
//    }
    fun likeClick(data:Post){
        uiScope?.launch {
            results.value = updateRow(data)
            try {
                appDataManager.updateLikeCount(data.id)
            }catch (ex:Exception){
                ex.printStackTrace()
            }
            withContext(Dispatchers.IO) {
                dbHelper.insertFavoritePost(data.apply {
                    likedDate = Date()
                    liked = true })
                logEvent(data?.id, "like")
            }
        }
    }

    private fun updateRow(data: Post):List<Post>{
        val size = mData?.size
            for(i in 0..size!!){
                var p = mData.get(i)
                if(p.id==data.id){
                    p.likesCount=+1
                    mData[i] = p
                    return mData
                }
            }
        return mData
    }


    private fun logEvent(id:String?,action:String){
        val event = "$${id}_$action"
        val likeButton = "card_$action"
        val str = "app_attribute"
        fbAnalytics.logEvent(event,event,str)
        fbAnalytics.logEvent(likeButton,likeButton,str)


    }
    private fun tapEvent(id:String?){
        val event = "${id}_tap"
        fbAnalytics.logEvent(event,event,"app_attribute")
        fbAnalytics.logEvent("article_appview","article_appview","app_attribute")

    }


    fun generateArticle(context: Context,post: Post){
        uiScope?.launch {
            try {
                navigator?.showProgress()
               // val bitmap = getBitmap(context,post.imageUrl)
                //val result = F.createBitmapFromView(context,post,bitmap!!)

                createDynamicLink(context,post)

            }catch (ex:Exception){
                ex.printStackTrace()
            }


        }
    }
    private fun createDynamicLink(context: Context,post:Post){
        Firebase.dynamicLinks.shortLinkAsync(ShortDynamicLink.Suffix.SHORT) {
            link = Uri.parse("${AppConstants.Google.PLAY_URL_DETAIL}?id=${BuildConfig.DYNAMIC_PACKAGE_NAME}&postId=${post.id}")
            domainUriPrefix = "${BuildConfig.URL_DYNAMIC_LINK}"
            androidParameters(BuildConfig.DYNAMIC_PACKAGE_NAME){

            }

            // Open links with this app on Android

        }.addOnSuccessListener {
            navigator?.hideProgress()
            val message = String.format(context.getString(R.string.share_info_text," '${post?.title}'",it.shortLink.toString()))
            ( context as MvvmActivity<*, *>).shareMessage(message)
        }.addOnFailureListener{
            navigator?.hideProgress()
            it.printStackTrace()
        }
    }

  suspend fun  myFavoritePost(data:List<Post>) = withContext(Dispatchers.Default){

        data?.forEach {


            val temp =  async(Dispatchers.IO) {
                dbHelper.getPostById(it.id)
            }.await()
            if(temp!=null){

                it.liked = true
            }

        }
      data

    }
   suspend fun getBitmap( context: Context,url:String?) = withContext(Dispatchers.IO){

           var bitmap :Bitmap?=null
            val future = GlideApp.with(context)
                .asBitmap()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .submit()

            try {
               bitmap = future.get()

            } catch (e: Exception) {
                e.printStackTrace()

            }
       bitmap

    }

}
