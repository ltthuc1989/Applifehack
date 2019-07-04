package com.ltthuc.habit.data.network

import com.androidhuman.rxfirebase2.firestore.model.Value
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.ltthuc.habit.ui.activity.listpost.PostContent
import io.reactivex.Single
import kotlinx.coroutines.Deferred


interface ApiHelper {

    fun getApiHeader(): ApiHeader
    fun getRssCat():Single<Value<QuerySnapshot>>


}