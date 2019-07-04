package com.ltthuc.habit.ui.activity.listpost

import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ezyplanet.thousandhands.util.livedata.NonNullLiveData
import com.ltthuc.habit.data.AppDataManager
import com.ltthuc.habit.data.network.response.RssCatResp
import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class ListPostVM @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseViewModel<ListPostNav, String>(schedulerProvider, connectionManager) {

    val results = NonNullLiveData<List<PostContent>>(emptyList())
    private val mData = ArrayList<PostContent>()



    fun onItemClicked(item: RssCatResp) {

    }

    fun getFeed(url: String) {
        uiScope.launch(Dispatchers.Main) {
            try {
                val parser = Parser()
                val articleList = parser.getArticles(url)
                val posts = toPostList(articleList)
                results.value = posts
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }

    private suspend fun toPostList(data: List<Article>) = withContext(Dispatchers.Default) {
        val result = mutableListOf<PostContent>()
        data.forEach {
            result.add(PostContent(it))

        }
        result
    }


}