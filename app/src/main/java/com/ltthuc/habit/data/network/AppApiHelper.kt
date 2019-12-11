package com.ltthuc.habit.data.network

import android.provider.MediaStore
import com.androidhuman.rxfirebase2.database.RxFirebaseDatabase
import com.androidhuman.rxfirebase2.firestore.RxFirebaseFirestore
import com.androidhuman.rxfirebase2.firestore.model.Value
import com.google.android.gms.tasks.Tasks
import com.google.api.client.http.HttpRequest


import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.*
import com.ltthuc.habit.BuildConfig
import com.ltthuc.habit.data.entity.Post
import com.ltthuc.habit.data.entity.PostType
import com.ltthuc.habit.data.network.response.youtube.YoutubeResp
import com.ltthuc.habit.ui.activity.listpost.PostContent
import com.prof.rssparser.Parser
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.HashMap
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.Video
import android.content.pm.PackageManager
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.api.client.util.DateTime
import com.google.common.io.BaseEncoding
import com.google.firebase.Timestamp
import com.google.protobuf.WireFormat
import com.ltthuc.habit.util.extension.await
import io.reactivex.Flowable


/**
 * Created by jyotidubey on 04/01/18.
 */
class AppApiHelper @Inject constructor(private val apiHeader: ApiHeader) : ApiHelper {

    val limitPage = 20L
    private var youtube: YouTube? = null

    val firStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private fun generatePostId(): String = firStore.collection(ApiEndPoint.POST_DB_KEY).document().id

    override fun getApiHeader(): ApiHeader {
        return apiHeader
    }

    override fun getRssCat(): Single<Value<QuerySnapshot>> {
        return RxFirebaseFirestore.data(firStore.collection(ApiEndPoint.GET_RSS_CATEGORY))
    }

    override fun createPost(post: Post): Completable {
        val postValues = post.toMap()

        return RxFirebaseFirestore.set(firStore.collection(ApiEndPoint.POST_DB_KEY).document(generatePostId()), postValues)

    }

    override fun getPost(loadMore:Boolean?,lastItem:DocumentSnapshot?): Task<QuerySnapshot> {
        if(loadMore==true) {
            val createdAt= lastItem!!["createdDateText"] as Timestamp
            return firStore.collection(ApiEndPoint.POST_DB_KEY)
                    .orderBy("createdDateText",Query.Direction.DESCENDING)?.whereLessThanOrEqualTo("createdDateText",createdAt)
                    .startAfter(lastItem).limit(10).get()
        }else{

            return firStore.collection(ApiEndPoint.POST_DB_KEY)
                    .orderBy("createdDateText",Query.Direction.DESCENDING)
                    .limit(10).get()
        }


    }

    override fun getCatgories(): Single<Value<QuerySnapshot>> {
        return RxFirebaseFirestore.data(firStore.collection(ApiEndPoint.GET_CATEGORIES))
    }

    override fun getPostByCat(catId: String?): Single<Value<QuerySnapshot>> {
        val query = firStore.collection(ApiEndPoint.POST_DB_KEY).whereEqualTo(DatabasePath.CAT_ID, catId).whereEqualTo(DatabasePath.TYPE, PostType.ARTICLE.type)

        return RxFirebaseFirestore.data(query)

    }

    override fun getVideoPostByCat(catId: String?): Single<Value<QuerySnapshot>> {
        val query = firStore.collection(ApiEndPoint.POST_DB_KEY).whereEqualTo(DatabasePath.CAT_ID, catId).whereEqualTo(DatabasePath.TYPE, PostType.VIDEO.type)

        return RxFirebaseFirestore.data(query)
    }

    override fun getYtDetail(youtubeId: String?): Single<YoutubeResp> {

        val request = "items/snippet/title,items/snippet/publishedAt,items/snippet/description,items/snippet/thumbnails/default/url,items/snippet/thumbnails/high/url,items/statistics"


        return Rx2AndroidNetworking.get(ApiEndPoint.API_YOUTUBE)
                .addQueryParameter("id", youtubeId)
                .addQueryParameter("key", BuildConfig.API_YOUTUBE)
                .addQueryParameter("part", "snippet,statistics")
                .addQueryParameter("fields", request)
                .build()
                .getObjectSingle(YoutubeResp::class.java)
    }

    override  fun updateViewCount(postId: String?): Task<Transaction> {


        val query = firStore.collection(ApiEndPoint.POST_DB_KEY).document(postId!!)
       return firStore.runTransaction {
            val snapshot = it.get(query)
            val newViewCount = snapshot.getDouble("viewCount")!! + 1
            it.update(query, "viewCount", newViewCount)
        }




    }


}