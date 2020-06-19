package com.applifehack.knowledge.ui.activity

import android.renderscript.ScriptGroup
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ezyplanet.thousandhands.util.livedata.NonNullLiveData
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.data.local.db.AppDatabase
import com.applifehack.knowledge.data.network.response.RssCatResp
import com.applifehack.knowledge.ui.admin.rsspost.URLWraper
import com.applifehack.knowledge.util.AppConstans
import com.ezyplanet.core.util.CoreConstants
import com.ezyplanet.core.util.FileUtils
import com.ezyplanet.supercab.data.local.db.DbHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.ArrayList
import javax.inject.Inject

class RSSVM @Inject constructor(val appDataManager: AppDataManager, val dbHelper: DbHelper,schedulerProvider: SchedulerProvider,connectionManager: BaseConnectionManager
) : BaseViewModel<RSSNav, String>(schedulerProvider, connectionManager) {

    val results = NonNullLiveData<List<RssCatResp>>(emptyList())
    private val mData = ArrayList<RssCatResp>()
    private val urlWrapers = ArrayList<URLWraper>()
    private var currentPage = 1
    private var isExport = false




    fun getRssCat() {
        navigator?.showProgress()
        compositeDisposable.add(appDataManager.getRssCat().compose(schedulerProvider?.ioToMainSingleScheduler())
                .map {
                    it.value().toObjects(RssCatResp::class.java)
                }.subscribe({

                    mData.addAll(it)
                    results.value = mData
                    setUrlWrapers(it)
                    navigator?.hideProgress()


                }, {
                    navigator?.hideProgress()
                    navigator?.showAlert(it.message)
                }))

    }


    fun onItemClicked(item:RssCatResp){
       // scrapData(item)

       navigator?.gotoListPostScreen(item)

    }

    private fun setUrlWrapers(data:List<RssCatResp>){
        data.forEach {
            urlWrapers.add(URLWraper(it,1))
        }


    }



   fun fetchAndSaveDatabase(){
       navigator?.showProgress()
       uiScope.launch {
           urlWrapers.forEach {
               val data = async(Dispatchers.IO) {
                   Log.d("updateDatabase","async")
                   var doc = Jsoup.connect(it.pageUrl()).get()
                   it.cssQuery(doc)

               }
               withContext(Dispatchers.IO) {
                   Log.d("updateDatabase", "updating")
                   isExport = true
                   dbHelper.insertPost(data.await())
                   Log.d("updateDatabase", "update")

               }
           }
           navigator?.hideProgress()
           Log.d("updateDatabase","updated")

       }

   }








}

