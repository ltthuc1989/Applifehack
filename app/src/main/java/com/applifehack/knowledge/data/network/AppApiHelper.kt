package com.applifehack.knowledge.data.network

import com.androidhuman.rxfirebase2.firestore.RxFirebaseFirestore
import com.androidhuman.rxfirebase2.firestore.model.Value


import com.google.firebase.firestore.*
import com.applifehack.knowledge.BuildConfig
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.entity.PostType
import com.applifehack.knowledge.data.network.response.youtube.YoutubeResp
import com.applifehack.knowledge.util.AppConstants
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import com.google.api.services.youtube.YouTube
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.applifehack.knowledge.util.SortBy
import com.applifehack.knowledge.util.AppConstants.DatabasePath


/**
 * Created by jyotidubey on 04/01/18.
 */
class AppApiHelper @Inject constructor(private val apiHeader: ApiHeader) : ApiHelper {

    val limitPage = 20L
    private var youtube: YouTube? = null

    val firStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private fun generatePostId(): String =
        firStore.collection(ApiEndPoint.POST_DB_KEY).document().id

    override fun getApiHeader(): ApiHeader {
        return apiHeader
    }

    override fun getRssCat(): Single<Value<QuerySnapshot>> {
        return RxFirebaseFirestore.data(firStore.collection(ApiEndPoint.GET_RSS_CATEGORY))
    }

    override fun createPost(post: Post): Completable {
        val postValues = post.toMap()

        return RxFirebaseFirestore.set(
            firStore.collection(ApiEndPoint.POST_DB_KEY).document(generatePostId()), postValues
        )

    }

    override fun getPost(loadMore: Boolean?, lastItem: DocumentSnapshot?): Task<QuerySnapshot> {
        if (loadMore == true) {
            val createdAt = lastItem!![DatabasePath.CREATED_DATE_TEXT] as Timestamp
            return firStore.collection(ApiEndPoint.POST_DB_KEY)
                .orderBy(DatabasePath.CREATED_DATE_TEXT, Query.Direction.DESCENDING)
                ?.whereLessThanOrEqualTo(DatabasePath.CREATED_DATE_TEXT, createdAt)
                .startAfter(lastItem).limit(10).get()
        } else {

            return firStore.collection(ApiEndPoint.POST_DB_KEY)
                .orderBy(DatabasePath.CREATED_DATE_TEXT, Query.Direction.DESCENDING)
                .limit(10).get()
        }


    }

    override fun getCatgories(): Single<Value<QuerySnapshot>> {
        return RxFirebaseFirestore.data(firStore.collection(ApiEndPoint.GET_CATEGORIES))
    }

    override fun getPopularPost(): Task<QuerySnapshot> {
        val query = firStore.collection(ApiEndPoint.POST_DB_KEY)
            .whereIn(DatabasePath.TYPE, mutableListOf(PostType.ARTICLE.type,PostType.VIDEO.type))
           // .orderBy(DatabasePath.VIEW_COUNT, Query.Direction.DESCENDING)

        return query.limit(8).get()
    }

    override fun getPostByCat(
        catId: String?,
        sortBy: SortBy?,
        loadMore: Boolean?,
        lastItem: DocumentSnapshot?
    ): Task<QuerySnapshot> {

        val query = when (sortBy) {
            SortBy.NEWEST -> {
                var temp = firStore.collection(ApiEndPoint.POST_DB_KEY)
                    .whereEqualTo(DatabasePath.TYPE, PostType.ARTICLE.type)
                    .whereEqualTo(DatabasePath.CAT_ID, catId)
                    .orderBy(DatabasePath.CREATED_DATE_TEXT, Query.Direction.DESCENDING)
                if (lastItem != null) {
                    val createdAt = lastItem!![DatabasePath.CREATED_DATE_TEXT] as Timestamp
                    temp?.whereLessThanOrEqualTo(DatabasePath.CREATED_DATE_TEXT, createdAt)
                } else {
                    temp
                }

            }

            SortBy.MOST_POPUPLAR -> {

                val temp = firStore.collection(ApiEndPoint.POST_DB_KEY)
                    .whereEqualTo(DatabasePath.TYPE, PostType.ARTICLE.type)
                    .whereEqualTo(DatabasePath.CAT_ID, catId)
                    .orderBy(DatabasePath.VIEW_COUNT, Query.Direction.DESCENDING)
                if (lastItem != null) {
                    val viewCount = lastItem!![DatabasePath.VIEW_COUNT] as Int
                    temp?.whereLessThanOrEqualTo(DatabasePath.VIEW_COUNT, viewCount)
                } else temp
            }
            else -> {
                val createdAt = lastItem!![DatabasePath.CREATED_DATE_TEXT] as Timestamp
                val temp = firStore.collection(ApiEndPoint.POST_DB_KEY)
                    .whereEqualTo(DatabasePath.TYPE, PostType.ARTICLE.type)
                    .whereEqualTo(DatabasePath.CAT_ID, catId)
                    .orderBy(DatabasePath.CREATED_DATE_TEXT, Query.Direction.ASCENDING)
                if (lastItem != null) {
                    temp?.whereLessThanOrEqualTo(DatabasePath.CREATED_DATE_TEXT, createdAt)
                } else {
                    temp
                }
            }
        }
        return if (loadMore == true) {
            query.startAfter(lastItem).limit(10).get()
        } else query.limit(10).get()


    }

    override fun getVideoPostByCat(
        catId: String?,
        sortBy: SortBy?,
        loadMore: Boolean?,
        lastItem: DocumentSnapshot?
    ): Task<QuerySnapshot> {

        val query = when (sortBy) {
            SortBy.NEWEST -> {

                val temp = firStore.collection(ApiEndPoint.POST_DB_KEY)
                    .whereEqualTo(DatabasePath.TYPE, PostType.VIDEO.type)
                    .whereEqualTo(DatabasePath.CAT_ID, catId)
                    .orderBy(DatabasePath.CREATED_DATE_TEXT, Query.Direction.DESCENDING)
                if (lastItem != null) {
                    val createdAt = lastItem!![DatabasePath.CREATED_DATE_TEXT] as Timestamp
                    temp?.whereLessThanOrEqualTo(DatabasePath.CREATED_DATE_TEXT, createdAt)

                } else temp


            }

            SortBy.MOST_POPUPLAR -> {

                val temp = firStore.collection(ApiEndPoint.POST_DB_KEY)
                    .whereEqualTo(DatabasePath.TYPE, PostType.VIDEO.type)
                    .whereEqualTo(DatabasePath.CAT_ID, catId)
                    .orderBy(DatabasePath.VIEW_COUNT, Query.Direction.DESCENDING)
                if (lastItem != null) {
                    val viewCount = lastItem!![DatabasePath.VIEW_COUNT] as Int
                    temp?.whereLessThanOrEqualTo(DatabasePath.VIEW_COUNT, viewCount)
                } else temp
            }
            else -> {

                val temp = firStore.collection(ApiEndPoint.POST_DB_KEY)
                    .whereEqualTo(DatabasePath.TYPE, PostType.VIDEO.type)
                    .whereEqualTo(DatabasePath.CAT_ID, catId)
                    .orderBy(DatabasePath.CREATED_DATE_TEXT, Query.Direction.ASCENDING)
                if (lastItem != null) {
                    val createdAt = lastItem!![DatabasePath.CREATED_DATE_TEXT] as Timestamp
                    temp?.whereLessThanOrEqualTo(DatabasePath.CREATED_DATE_TEXT, createdAt)
                } else temp


            }
        }
        return if (loadMore == true) query.startAfter(lastItem).limit(10).get() else query.limit(10)
            .get()

    }

    override fun getPostByQuote(
        typeQuote: String?,
        loadMore: Boolean?,
        lastItem: DocumentSnapshot?
    ): Task<QuerySnapshot> {

        var supQuery = firStore.collection(ApiEndPoint.POST_DB_KEY).whereEqualTo(DatabasePath.TYPE, PostType.QUOTE.type)
        if (typeQuote != null&&!typeQuote?.equals("All",true)){

           supQuery=supQuery.whereEqualTo(DatabasePath.QUOTE_TYPE, typeQuote)

        }
        supQuery=supQuery.orderBy(DatabasePath.CREATED_DATE_TEXT, Query.Direction.DESCENDING)


        return if (loadMore == true) {
            val createdAt = lastItem!![DatabasePath.CREATED_DATE_TEXT] as Timestamp

            supQuery?.whereLessThanOrEqualTo(DatabasePath.CREATED_DATE_TEXT, createdAt)
                ?.startAfter(lastItem).limit(10).get()


        } else {
            supQuery.limit(10).get()
        }

    }

    override fun getYtDetail(youtubeId: String?): Single<YoutubeResp> {

        val request =
            "items/snippet/title,items/snippet/publishedAt,items/snippet/description,items/snippet/thumbnails/default/url,items/snippet/thumbnails/high/url,items/statistics"


        return Rx2AndroidNetworking.get(ApiEndPoint.API_YOUTUBE)
            .addQueryParameter("id", youtubeId)
            .addQueryParameter("key", BuildConfig.API_YOUTUBE)
            .addQueryParameter("part", "snippet,statistics")
            .addQueryParameter("fields", request)
            .build()
            .getObjectSingle(YoutubeResp::class.java)
    }

    override fun updateViewCount(postId: String?): Task<Transaction> {


        val query = firStore.collection(ApiEndPoint.POST_DB_KEY).document(postId!!)
        return firStore.runTransaction {
            val snapshot = it.get(query)
            var count = snapshot.getDouble(DatabasePath.VIEW_COUNT)
            if (count == null) {
                count = 1.0
            } else {
                count += 1.0
            }
            it.update(query, DatabasePath.VIEW_COUNT, count)
        }


    }

    override fun updateLikeCount(postId: String?): Task<Transaction> {


        val query = firStore.collection(ApiEndPoint.POST_DB_KEY).document(postId!!)
        return firStore.runTransaction {
            val snapshot = it.get(query)
            var count = snapshot.getDouble(DatabasePath.LIKES_COUNT)
            if (count == null) {
                count = 1.0
            } else {
                count += 1.0
            }

            it.update(query, DatabasePath.LIKES_COUNT, count)
        }


    }


}