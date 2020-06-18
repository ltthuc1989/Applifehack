package com.applifehack.knowledge.ui.admin.localpost

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.entity.PostType
import com.applifehack.knowledge.data.firebase.FirebaseAnalyticsHelper
import com.applifehack.knowledge.data.network.response.RssCatResp
import com.applifehack.knowledge.util.extension.await
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.supercab.data.local.db.DbHelper
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ezyplanet.thousandhands.util.livedata.NonNullLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocalPostVM @Inject constructor(val appDataManager: AppDataManager, val dbHelper: DbHelper,schedulerProvider: SchedulerProvider,connectionManager: BaseConnectionManager
) : BaseViewModel<LocalPostNav, String>(schedulerProvider, connectionManager) {

    val results = NonNullLiveData<List<Post>>(emptyList())
    private var mData = ArrayList<Post>()
    val postSize = MutableLiveData<String>()


    private var authors  : ArrayList<String>? = appDataManager.getAuthors()
    val isRandom  = MutableLiveData<Boolean>()


    @Inject lateinit var fbAnalytics: FirebaseAnalyticsHelper

    fun getPost() {


        navigator?.showProgress()
        uiScope?.launch {
            try {

                val data = async(Dispatchers.IO){
                   dbHelper.loadAllPost()
                }
                val temp = data.await()
                mData.addAll(temp)
                bindToUI()
                navigator?.hideProgress()




            } catch (ex: Exception) {
                navigator?.hideProgress()
                resetLoadingState = false
            }

        }


    }

    fun getRssCat() {
        navigator?.showProgress()
        compositeDisposable.add(appDataManager.getRssCat().compose(schedulerProvider?.ioToMainSingleScheduler())
            .map {
                val temp= it.value().toObjects(RssCatResp::class.java)
                val author = ArrayList<String>()
                temp.forEach {
                    author.add(it.author_name!!)
                }
                author

            }.subscribe({
                authors = it
                appDataManager.saveAuthors(authors!!)

               setPosts()



            }, {
                navigator?.hideProgress()
                navigator?.showAlert(it.message)
            }))

    }
    fun randomPost(){
        if(authors.isNullOrEmpty()){
            getRssCat()
        }else{
            uiScope.launch {
                navigator?.showProgress()
                setPosts()

            }
        }

    }
    private fun setPosts(){
         mData.clear()
        val temp  = authors?.clone() as ArrayList<String>
        uiScope.launch {
            for (i in 1..authors!!.size){
                mData.addAll(async(Dispatchers.IO){
                    val random = temp?.random()
                    Log.d("randomPost",random)
                    temp?.remove(random)
                    dbHelper.getPostByAuthor(random)
                }.await())

            }
            bindToUI()
            isRandom.value = true
            navigator?.hideProgress()

        }
    }

    fun openPostDetail(data: Post, shareView: View) {
        uiScope?.launch {
            try {

                if(data?.getPostType()== PostType.VIDEO){
                    navigator?.gotoYoutubeDetail(data,shareView)

                }else{
                    navigator?.gotoFeedDetail(data)
                }


                val resul = appDataManager.updateViewCount(data.id)
                resul.await()

            } catch (ex: Exception) {

            }


        }

    }
   fun publish(){
       if (isRandom.value!=true) return

           navigator?.showProgress()
           appDataManager.createMultiplePost(results.value).addOnFailureListener {
               navigator?.hideProgress()
               navigator?.showAlert(it.message)

           }.addOnSuccessListener {
               navigator?.hideProgress()
               mData.clear()
               bindToUI()
           }


    }

    fun postNow(item: Post){

        uiScope?.launch {
            navigator?.showProgress()
            appDataManager.createPost(item.id, item).addOnFailureListener {
                navigator?.hideProgress()
                navigator?.showAlert(it.message)

            }.addOnSuccessListener {
                uiScope.launch {

                    dbHelper.updatePost(item.id)
                    mData.remove(item)
                    bindToUI()
                    navigator?.hideProgress()
                }

            }



        }

    }
    private fun bindToUI(){
        results.value = mData
        postSize.value = "${mData.size} Posts"
    }






}