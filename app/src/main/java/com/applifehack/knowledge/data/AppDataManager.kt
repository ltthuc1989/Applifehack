package com.applifehack.knowledge.data

import android.content.Context
import com.ezyplanet.thousandhands.shipper.data.preferences.AppPreferenceHelper
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Transaction

import com.irmansyah.catalogmoviekotlin.data.DataManager
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.network.ApiHeader
import com.applifehack.knowledge.data.network.ApiHelper
import com.applifehack.knowledge.data.network.response.youtube.YoutubeResp
import com.applifehack.knowledge.util.SortBy
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppDataManager @Inject constructor(val context: Context, val appPreferenceHelper: AppPreferenceHelper, private val apiHelper: ApiHelper) : DataManager {


    override fun updateApiHeader(userId: Long?, accessToken: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setUserAsLoggedOut() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getApiHeader(): ApiHeader {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }




    override fun getPost(loadMore:Boolean?, lastItem:DocumentSnapshot?): Task<QuerySnapshot> {
        return apiHelper.getPost(loadMore,lastItem)
    }

    override fun getPopularPost(): Task<QuerySnapshot> {
        return apiHelper.getPopularPost()
    }

    override fun getCatgories(): Task<QuerySnapshot> {
        return apiHelper.getCatgories()
    }

    override fun getPostByCat(catId: String?, sortBy: SortBy?, loadMore: Boolean?, lastItem: DocumentSnapshot?): Task<QuerySnapshot> {
       return  apiHelper.getPostByCat(catId,sortBy,loadMore,lastItem)
    }

    override fun getVideoPostByCat(catId: String?, sortBy: SortBy?, loadMore: Boolean?, lastItem: DocumentSnapshot?): Task<QuerySnapshot> {
        return  apiHelper.getVideoPostByCat(catId,sortBy,loadMore,lastItem)
    }

    override fun getPostByQuote(typeQuote: String?, loadMore: Boolean?, lastItem: DocumentSnapshot?): Task<QuerySnapshot> {
        return  apiHelper.getPostByQuote(typeQuote,loadMore,lastItem)
    }

    override  fun getYtDetail(youtubeId: String?): Single<YoutubeResp> {
        return apiHelper.getYtDetail(youtubeId)
    }

    override   fun updateViewCount(postId: String?): Task<Transaction> {
        return  apiHelper.updateViewCount(postId)
    }

    override   fun updateLikeCount(postId: String?): Task<Transaction> {
        return  apiHelper.updateLikeCount(postId)
    }

    override fun getPostDetail(postId: String): Task<DocumentSnapshot> {

        return apiHelper.getPostDetail(postId)
    }
}