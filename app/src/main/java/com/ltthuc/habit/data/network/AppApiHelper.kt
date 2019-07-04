package com.ltthuc.habit.data.network

import com.androidhuman.rxfirebase2.database.RxFirebaseDatabase
import com.androidhuman.rxfirebase2.firestore.RxFirebaseFirestore
import com.androidhuman.rxfirebase2.firestore.model.Value
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.ltthuc.habit.ui.activity.listpost.PostContent
import com.prof.rssparser.Parser
import io.reactivex.Single
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jyotidubey on 04/01/18.
 */
class AppApiHelper @Inject constructor(private val apiHeader: ApiHeader) : ApiHelper {

    val limitPage: Int = 20

    val firStore: FirebaseFirestore = FirebaseFirestore.getInstance()



    override fun getApiHeader(): ApiHeader {
        return apiHeader
    }

    override fun getRssCat(): Single<Value<QuerySnapshot>> {
       return RxFirebaseFirestore.data(firStore.collection(ApiEndPoint.GET_RSS_CATEGORY))
    }


}