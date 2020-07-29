package com.applifehack.knowledge.ui.admin.mirgrate

import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.data.network.response.CatResp
import com.applifehack.knowledge.data.network.response.QuoteResp
import com.applifehack.knowledge.data.network.response.RssCatResp
import com.applifehack.knowledge.util.extension.await
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.ui.base.MvvmNav
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class MirgrateVM @Inject constructor(val appDataManager: AppDataManager,schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseViewModel<MvvmNav, String>(schedulerProvider, connectionManager) {



    fun transferCat(cats:List<CatResp>){
        appDataManager.transferCat(cats).addOnFailureListener {
            navigator?.hideProgress()
            navigator?.showAlert(it.message)

        }.addOnSuccessListener {
            navigator?.hideProgress()
           navigator?.showAlert(R.string.transfer_successfull)

        }
    }

    fun transferQuoteCat(quotes:List<QuoteResp>){
        appDataManager.transferQuoteCat(quotes).addOnFailureListener {
            navigator?.hideProgress()
            navigator?.showAlert(it.message)

        }.addOnSuccessListener {
            navigator?.hideProgress()
            navigator?.showAlert(R.string.transfer_successfull)

        }
    }

    fun transferCrawlData(quotes:List<RssCatResp>){
        appDataManager.transferCrawlData(quotes).addOnFailureListener {
            navigator?.hideProgress()
            navigator?.showAlert(it.message)

        }.addOnSuccessListener {
            navigator?.hideProgress()
            navigator?.showAlert(R.string.transfer_successfull)

        }
    }

    fun getCat() {
        uiScope.launch {
            val data = appDataManager.getCatgories()
            try {
                data?.await().let {
                    if (!it.isEmpty) {
                        val snapshot = async(Dispatchers.Default) {
                            it.toObjects(CatResp::class.java)
                        }

                        transferCat(snapshot.await())


                    }


                }
            }catch (ex:Exception){
                ex.printStackTrace()
            }
        }

    }

    fun getQuoteCat() {

        uiScope.launch {
            val data = appDataManager.getQuoteCat()
            try {
                data?.await().let {
                    if (!it.isEmpty) {
                        val snapshot = async(Dispatchers.Default) {
                            it.toObjects(QuoteResp::class.java)
                        }

                        transferQuoteCat(snapshot.await())


                    }


                }
            }catch (ex:Exception){
                ex.printStackTrace()
            }
        }


    }




    fun getRssCat() {

        navigator?.showProgress()
        compositeDisposable.add(appDataManager.getRssCat().compose(schedulerProvider?.ioToMainSingleScheduler())
            .map {
            it.value().toObjects(RssCatResp::class.java)

            }.subscribe({

                transferCrawlData(it)


            }, {
                navigator?.hideProgress()
                navigator?.showAlert(it.message)
            }))


    }

}