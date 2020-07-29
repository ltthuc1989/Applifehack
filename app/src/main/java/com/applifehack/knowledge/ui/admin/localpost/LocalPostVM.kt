package com.applifehack.knowledge.ui.admin.localpost

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.entity.PostType
import com.applifehack.knowledge.data.firebase.FirebaseAnalyticsHelper
import com.applifehack.knowledge.data.network.response.RssCatResp
import com.applifehack.knowledge.util.AlertDialogUtils
import com.applifehack.knowledge.util.PostStatus
import com.applifehack.knowledge.util.extension.await
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.AlertUtils
import com.ezyplanet.core.util.CoreConstants
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.supercab.data.local.db.DbHelper
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ezyplanet.thousandhands.util.livedata.NonNullLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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



      fun setPosts(){
            mData.clear()
            navigator?.showProgress()
            uiScope.launch {

            mData.addAll(async(Dispatchers.IO) {
                dbHelper.randomPost()
            }.await())
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
          var ids = mutableListOf<String>()
          val temp = results.value.apply {
              forEach {
                  it.post_status = PostStatus.PUBLISH.type
                  ids.add(it.id)
              }
          }
           appDataManager.createMultiplePost(temp).addOnFailureListener {
               navigator?.hideProgress()
               navigator?.showAlert(it.message)

           }.addOnSuccessListener {

               uiScope?.launch {
                   dbHelper.updatePosts(ids)
                   mData.clear()
                   navigator?.hideProgress()
                   bindToUI()
               }

           }


    }

    fun postNow(item: Post){


        uiScope?.launch {
            navigator?.showProgress()
            appDataManager.createPostLive(item.id, item).addOnFailureListener {
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
    fun skip(item: Post){
        uiScope.launch {

            dbHelper.updatePost(item.id)
            mData.remove(item)
            bindToUI()
            navigator?.hideProgress()
        }
    }

    private fun bindToUI(){
        results.value = mData
        postSize.value = "${mData.size} Posts"
    }



    fun exportDatabse(){
        if(mData?.isNullOrEmpty()) return
        navigator?.showProgress()
        val context = appDataManager.context
        val file = context.getDatabasePath(CoreConstants.APP_DB_NAME)
        appDataManager.uploadDatabase(file).addOnSuccessListener {
            navigator?.hideProgress()
        }.addOnFailureListener{
            navigator?.showAlert(it.message)
            navigator?.hideProgress()
        }
    }


}