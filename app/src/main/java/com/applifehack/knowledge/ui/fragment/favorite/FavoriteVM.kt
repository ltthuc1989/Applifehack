package com.applifehack.knowledge.ui.fragment.favorite

import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.ui.fragment.feed.FeedVM
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.supercab.data.local.db.DbHelper
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteVM @Inject constructor(
    appDataManager: AppDataManager,
    schedulerProvider: SchedulerProvider,
    dbHelper: DbHelper,
    connectionManager: BaseConnectionManager
) : FeedVM(appDataManager, schedulerProvider, dbHelper, connectionManager) {


    @Override
    override fun getPost(nextPage: Boolean?) {

        if (nextPage == false) {
            mData.clear()
            navigator?.showProgress()
        }
        resetLoadingState = true
        uiScope?.launch {
            try {
                val data = async(Dispatchers.IO) {
                    if (nextPage == false) {
                        dbHelper.loadFavoritePosts(null)
                    } else {
                        dbHelper.loadFavoritePosts(mData[mData.size - 1].likedDate)
                    }
                }

                data?.await().let {
                    if (!it.isNullOrEmpty()) {

                            mData.addAll(it)
                            hasMore = (it.size > visibleThreshold - 1)



                            currentPage += 1


                        showEmptyView.value = false
                    } else {
                        showEmptyView.value = true
                    }
                    results.value = mData
                    resetLoadingState = false
                    navigator?.hideProgress()


                }

            } catch (ex: Exception) {
                navigator?.hideProgress()
                resetLoadingState = false
            }

        }


    }

    override fun myFavoritePost(position: Int) {

    }


}