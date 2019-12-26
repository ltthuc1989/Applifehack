package com.ltthuc.habit.data

import android.content.Context
import android.provider.MediaStore
import android.telephony.TelephonyManager
import com.androidhuman.rxfirebase2.firestore.model.Value
import com.ezyplanet.thousandhands.shipper.data.preferences.AppPreferenceHelper
import com.google.android.gms.tasks.Task
import com.google.api.services.youtube.model.Video
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Transaction

import com.google.gson.Gson
import com.irmansyah.catalogmoviekotlin.data.DataManager
import com.ltthuc.habit.data.entity.Post
import com.ltthuc.habit.data.network.ApiHeader
import com.ltthuc.habit.data.network.ApiHelper
import com.ltthuc.habit.data.network.response.youtube.YoutubeResp
import com.ltthuc.habit.util.SortBy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import timber.log.Timber
import java.util.*
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

    override fun getRssCat(): Single<Value<QuerySnapshot>> {
        return apiHelper.getRssCat()
    }

    override fun createPost(postContent: Post): Completable {
        return apiHelper.createPost(postContent)
    }

    override fun getPost(loadMore:Boolean?,lastItem:DocumentSnapshot?): Task<QuerySnapshot> {
        return apiHelper.getPost(loadMore,lastItem)
    }

    override fun getCatgories(): Single<Value<QuerySnapshot>> {
        return apiHelper.getCatgories()
    }

    override fun getPostByCat(catId: String?, sortBy: SortBy?, loadMore: Boolean?, lastItem: DocumentSnapshot?): Task<QuerySnapshot> {
       return  apiHelper.getPostByCat(catId,sortBy,loadMore,lastItem)
    }

    override fun getVideoPostByCat(catId: String?, sortBy: SortBy?, loadMore: Boolean?, lastItem: DocumentSnapshot?): Task<QuerySnapshot> {
        return  apiHelper.getVideoPostByCat(catId,sortBy,loadMore,lastItem)
    }

    override fun getPostByQuote(typeQuote: String?, sortBy: SortBy?, loadMore: Boolean?, lastItem: DocumentSnapshot?): Task<QuerySnapshot> {
        return  apiHelper.getPostByQuote(typeQuote,sortBy,loadMore,lastItem)
    }

    override  fun getYtDetail(youtubeId: String?): Single<YoutubeResp> {
        return apiHelper.getYtDetail(youtubeId)
    }

    override   fun updateViewCount(postId: String?): Task<Transaction> {
        return  apiHelper.updateViewCount(postId)
    }
}