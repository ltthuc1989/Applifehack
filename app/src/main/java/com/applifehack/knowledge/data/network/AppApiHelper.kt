package com.applifehack.knowledge.data.network




import com.google.firebase.firestore.*
import com.applifehack.knowledge.BuildConfig
import com.applifehack.knowledge.data.entity.PostType
import com.applifehack.knowledge.data.network.response.youtube.YoutubeResp
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Single
import javax.inject.Inject
import com.google.api.services.youtube.YouTube
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.applifehack.knowledge.util.SortBy
import com.applifehack.knowledge.util.AppConstants.DatabasePath
import com.applifehack.knowledge.util.TimeUtil


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


    override fun getPost(loadMore: Boolean?, lastItem: DocumentSnapshot?): Task<QuerySnapshot> {
        if (loadMore == true) {
            val createdAt = lastItem!![DatabasePath.CREATED_DATE_TEXT] as Timestamp
            return firStore.collection(ApiEndPoint.POST_DB_KEY)
                .orderBy(DatabasePath.CREATED_DATE_TEXT, Query.Direction.DESCENDING)
                ?.whereLessThan(DatabasePath.CREATED_DATE_TEXT, createdAt)
                ?.startAfter(lastItem)?.limit(10)!!.get()
        } else {

            return firStore.collection(ApiEndPoint.POST_DB_KEY)
                .orderBy(DatabasePath.CREATED_DATE_TEXT, Query.Direction.DESCENDING)
                .limit(10).get()
        }


    }

    override fun getCatgories(): Task<QuerySnapshot> {
       return firStore.collection(ApiEndPoint.GET_CATEGORIES).
       whereEqualTo(DatabasePath.EDITING_DATABASE,false).
       orderBy(DatabasePath.CAT_CREATED_DATE, Query.Direction.DESCENDING).get()
    }

    override fun getQuoteCat(): Task<QuerySnapshot> {
        return firStore.collection(ApiEndPoint.GET_QUOTES).
        orderBy(DatabasePath.QUOTE_NAME,
            Query.Direction.ASCENDING).
        whereEqualTo(DatabasePath.EDITING_DATABASE,false).get()
    }

    override fun getPopularPost(): Task<QuerySnapshot> {
        val dates = TimeUtil.getFirsAndLastDateOfLastWeek()
        val startAt = Timestamp(dates[0])
        val endAt = Timestamp(dates[1])

        val query = firStore.collection(ApiEndPoint.POST_DB_KEY)


            .orderBy(DatabasePath.CREATED_DATE_TEXT,Query.Direction.ASCENDING)
           .startAt(startAt).endAt(endAt)



        return query.get()
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
                    temp?.whereLessThan(DatabasePath.CREATED_DATE_TEXT, createdAt)
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
                    temp?.whereLessThan(DatabasePath.VIEW_COUNT, viewCount)
                } else temp
            }
            else -> {
                val createdAt = lastItem!![DatabasePath.CREATED_DATE_TEXT] as Timestamp
                val temp = firStore.collection(ApiEndPoint.POST_DB_KEY)
                    .whereEqualTo(DatabasePath.TYPE, PostType.ARTICLE.type)
                    .whereEqualTo(DatabasePath.CAT_ID, catId)
                    .orderBy(DatabasePath.CREATED_DATE_TEXT, Query.Direction.ASCENDING)
                if (lastItem != null) {
                    temp?.whereLessThan(DatabasePath.CREATED_DATE_TEXT, createdAt)
                } else {
                    temp
                }
            }
        }
        return if (loadMore == true) {
            query.startAfter(lastItem).limit(20).get()
        } else query.limit(20).get()


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
                    temp?.whereLessThan(DatabasePath.CREATED_DATE_TEXT, createdAt)

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
        return if (loadMore == true) query.startAfter(lastItem).limit(20).get() else query.limit(20)
            .get()

    }

    override fun getPostByQuote(
        typeQuote: String?,
        loadMore: Boolean?,
        lastItem: DocumentSnapshot?
    ): Task<QuerySnapshot> {

        var supQuery = firStore.collection(ApiEndPoint.POST_DB_KEY).whereEqualTo(DatabasePath.TYPE, PostType.QUOTE.type)
        if (typeQuote != null&&!typeQuote!!.equals("All",true)){

           supQuery=supQuery.whereEqualTo(DatabasePath.QUOTE_TYPE, typeQuote)

        }
        supQuery=supQuery.orderBy(DatabasePath.CREATED_DATE_TEXT, Query.Direction.DESCENDING)


        return if (loadMore == true) {
            val createdAt = lastItem!![DatabasePath.CREATED_DATE_TEXT] as Timestamp

            supQuery?.whereLessThanOrEqualTo(DatabasePath.CREATED_DATE_TEXT, createdAt)
                ?.startAfter(lastItem)!!.limit(10).get()


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


    override fun getPostDetail(postId: String): Task<DocumentSnapshot> {
        return firStore.collection(ApiEndPoint.POST_DB_KEY).document(postId!!).get()
    }

    override fun getFactCat(): Task<QuerySnapshot> {
        return firStore.collection(ApiEndPoint.GET_FACTS).
        orderBy(DatabasePath.FACT_NAME,
            Query.Direction.ASCENDING).
        whereEqualTo(DatabasePath.EDITING_DATABASE,false).get()
    }

    override fun getPostByFact(
        typeFact: String?,
        loadMore: Boolean?,
        lastItem: DocumentSnapshot?
    ): Task<QuerySnapshot> {
        var supQuery = firStore.collection(ApiEndPoint.POST_DB_KEY).whereEqualTo(DatabasePath.TYPE, PostType.FACT.type)
        if (typeFact != null&&!typeFact!!.equals("All",true)){

            supQuery=supQuery.whereEqualTo(DatabasePath.FACT_TYPE, typeFact)

        }
        supQuery=supQuery.orderBy(DatabasePath.CREATED_DATE_TEXT, Query.Direction.DESCENDING)


        return if (loadMore == true) {
            val createdAt = lastItem!![DatabasePath.CREATED_DATE_TEXT] as Timestamp

            supQuery?.whereLessThanOrEqualTo(DatabasePath.CREATED_DATE_TEXT, createdAt)
                ?.startAfter(lastItem)!!.limit(10).get()


        } else {
            supQuery.limit(10).get()
        }
    }

    override fun getHackCat(): Task<QuerySnapshot> {
        return firStore.collection(ApiEndPoint.GET_HACKS).
        orderBy(DatabasePath.HACK_NAME,
            Query.Direction.ASCENDING).
        whereEqualTo(DatabasePath.EDITING_DATABASE,false).get()
    }

    override fun getPostByHack(
        typeHack: String?,
        loadMore: Boolean?,
        lastItem: DocumentSnapshot?
    ): Task<QuerySnapshot> {
        var supQuery = firStore.collection(ApiEndPoint.POST_DB_KEY).whereEqualTo(DatabasePath.TYPE, PostType.HACK.type)
        if (typeHack != null&&!typeHack!!.equals("All",true)){

            supQuery=supQuery.whereEqualTo(DatabasePath.HACK_TYPE, typeHack)

        }
        supQuery=supQuery.orderBy(DatabasePath.CREATED_DATE_TEXT, Query.Direction.DESCENDING)


        return if (loadMore == true) {
            val createdAt = lastItem!![DatabasePath.CREATED_DATE_TEXT] as Timestamp

            supQuery?.whereLessThanOrEqualTo(DatabasePath.CREATED_DATE_TEXT, createdAt)
                ?.startAfter(lastItem)!!.limit(10).get()


        } else {
            supQuery.limit(10).get()
        }
    }

    override fun getMeaningCat(): Task<QuerySnapshot> {
        return firStore.collection(ApiEndPoint.GET_MEANING_PICTURE).
        orderBy(DatabasePath.PHOTO_TYPE,
            Query.Direction.ASCENDING).
        whereEqualTo(DatabasePath.EDITING_DATABASE,false).get()
    }

    override fun getPostByPhoto(
        typePhoto: String?,
        loadMore: Boolean?,
        lastItem: DocumentSnapshot?
    ): Task<QuerySnapshot> {
        var supQuery = firStore.collection(ApiEndPoint.POST_DB_KEY).whereEqualTo(DatabasePath.TYPE, PostType.PICTURE.type)
        if (typePhoto != null&&!typePhoto!!.equals("All",true)){

            supQuery=supQuery.whereEqualTo(DatabasePath.PHOTO_TYPE, typePhoto)

        }
        supQuery=supQuery.orderBy(DatabasePath.CREATED_DATE_TEXT, Query.Direction.DESCENDING)


        return if (loadMore == true) {
            val createdAt = lastItem!![DatabasePath.CREATED_DATE_TEXT] as Timestamp

            supQuery?.whereLessThanOrEqualTo(DatabasePath.CREATED_DATE_TEXT, createdAt)
                ?.startAfter(lastItem)!!.limit(10).get()


        } else {
            supQuery.limit(10).get()
        }
    }
}