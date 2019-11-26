package com.ltthuc.habit.data.network

import com.androidhuman.rxfirebase2.database.RxFirebaseDatabase
import com.androidhuman.rxfirebase2.firestore.RxFirebaseFirestore
import com.androidhuman.rxfirebase2.firestore.model.Value
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.*
import com.ltthuc.habit.data.entity.Post
import com.ltthuc.habit.ui.activity.listpost.PostContent
import com.prof.rssparser.Parser
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

/**
 * Created by jyotidubey on 04/01/18.
 */
class AppApiHelper @Inject constructor(private val apiHeader: ApiHeader) : ApiHelper {

    val limitPage: Int = 20

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

        return RxFirebaseFirestore.set(firStore.collection(ApiEndPoint.POST_DB_KEY).
            document(generatePostId()),postValues)

    }

    override fun getPost(): Single<Value<QuerySnapshot>> {
        return RxFirebaseFirestore.data(firStore.collection(ApiEndPoint.POST_DB_KEY))
    }

    override fun getCatgories(): Single<Value<QuerySnapshot>> {
        return RxFirebaseFirestore.data(firStore.collection(ApiEndPoint.GET_CATEGORIES))
    }
}