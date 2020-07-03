package com.applifehack.knowledge.ui.fragment.feed

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.applifehack.knowledge.BuildConfig
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.entity.PostType
import com.applifehack.knowledge.data.firebase.FirebaseAnalyticsHelper
import com.applifehack.knowledge.util.AppConstants
import com.applifehack.knowledge.util.MediaUtil
import com.applifehack.knowledge.util.ShareType
import com.applifehack.knowledge.util.extension.await
import com.applifehack.knowledge.util.extension.shareImage
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.supercab.data.local.db.DbHelper
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ezyplanet.thousandhands.util.livedata.NonNullLiveData
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.sourcei.kowts.utils.functions.F
import java.util.*
import javax.inject.Inject


open class FeedVM @Inject constructor(
    val appDataManager: AppDataManager,
    schedulerProvider: SchedulerProvider,
    var dbHelper: DbHelper,
    connectionManager: BaseConnectionManager
) : BaseViewModel<FeedNav, String>(schedulerProvider, connectionManager) {

    val results = NonNullLiveData<List<Post>>(emptyList())
    protected val mData = ArrayList<Post>()

    private var lastItem: DocumentSnapshot? = null
    var currentPage = 0
    val shareType = MutableLiveData<ShareType>()

    init {
        visibleThreshold = 10
        shareType.value = ShareType.NONE
    }

    protected var hasMore = false
    protected var currentItem = 0
    private var currentLoadMorePosition = 0
    var onStop = false
    @Inject
    lateinit var fbAnalytics: FirebaseAnalyticsHelper

    fun getDynamicLinkInfo(post: Post?) {
        mData.add(post!!)
        results.value = mData
        resetLoadingState = false


    }

    open fun getPost(nextPage: Boolean? = false) {

        if (nextPage == false) navigator?.showProgress()
        resetLoadingState = true
        uiScope?.launch {
            try {
                val data = appDataManager.getPost(nextPage, lastItem)

                data?.await().let {
                    if (!it.isEmpty) {
                        val snapshot = async(Dispatchers.Default) {
                            it.toObjects(Post::class.java)
                        }


                        lastItem = it.documents[it.size() - 1]
                        val rs = snapshot.await()
                        mData.addAll(rs)
                        hasMore = (rs.size > visibleThreshold - 1)




                        currentPage += 1
                    } else hasMore = false
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

    fun openPostDetail(data: Post, shareView: View) {
        uiScope?.launch {
            try {

                if (data?.getPostType() == PostType.VIDEO) {
                    navigator?.gotoYoutubeDetail(data, shareView)

                } else {
                    navigator?.gotoFeedDetail(data)
                }
                logEvent(data?.id, "readmore")

                val resul = appDataManager.updateViewCount(data.id)
                resul.await()

            } catch (ex: Exception) {

            }


        }

    }

    fun openCatDetail(data: Post) {
        navigator?.gotoCatDetail(data?.catId)
    }

    fun openPageUrl(data: Post) {
        navigator?.gotoPageUrl(data)
    }

    fun shareClick(view: View, data: Post) {

        generateArticle(view, data.id)
        logEvent(data?.id, "share")


    }

    fun likeClick(data: Post) {
        uiScope?.launch {
            results.value = updateRow(currentItem)
            try {
                appDataManager.updateLikeCount(data.id)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            withContext(Dispatchers.IO) {
                dbHelper.insertFavoritePost(data.apply {
                    likedDate = Date()
                    liked = true
                })
                logEvent(data?.id, "like")
            }
        }
    }


    fun onLoadMore(position: Int) {

        currentItem = position
        if (hasMore && position + 5 > mData.size) {
            if (position>currentLoadMorePosition) {
                currentLoadMorePosition = position+5

                getPost(true)
            }
        }


    }


    fun generateArticle(view: View, id: String?) {
        uiScope?.launch {
            try {
                navigator?.showProgress()

                val parentView = view.parent.parent as RelativeLayout
                val vBottom = parentView.findViewById<View>(R.id.vBottom)
                val linBottom = parentView.findViewById<ConstraintLayout>(R.id.linBottom)
                val linShare = parentView.findViewById<LinearLayout>(R.id.shareLayout)
                parentView.run {

                    linBottom.visibility = View.GONE
                    linShare.visibility = View.VISIBLE
                    (vBottom.layoutParams as RelativeLayout.LayoutParams).apply {
                        height = 0
                    }
                    (parentView.layoutParams as RecyclerView.LayoutParams).apply {
                        setMargins(8, 8, 8, 8)
                    }

                    postDelayed({
                        MediaUtil.getOutputMediaFile(view.context, F.viewToBitmap(parentView))
                        postDelayed({


                            linBottom.visibility = View.VISIBLE
                            linShare.visibility = View.GONE
                            (vBottom.layoutParams as RelativeLayout.LayoutParams).apply {
                                height = view.resources.getDimension(R.dimen.dp_16).toInt()
                            }
                            (parentView.layoutParams as RecyclerView.LayoutParams).apply {
                                setMargins(0, 0, 0, 0)
                            }
                            createDynamicLink(view.context, id)
                        }, 100)

                    }, 100)


                }


            } catch (ex: Exception) {
                ex.printStackTrace()
            }


        }
    }

    private fun updateRow(position: Int): List<Post> {

        mData[position]?.apply {
            likesCount = likesCount!! + 1
        }

        return mData
    }

    private fun logEvent(id: String?, action: String) {
        val event = "$${id}_$action"
        val likeButton = "card_$action"
        val str = "app_attribute"
        fbAnalytics.logEvent(event, event, str)
        fbAnalytics.logEvent(likeButton, likeButton, str)


    }

    private fun createDynamicLink(context: Context, postId: String?) {
        Firebase.dynamicLinks.shortLinkAsync(ShortDynamicLink.Suffix.SHORT) {
            link =
                Uri.parse("${AppConstants.Google.PLAY_URL_DETAIL}?id=${BuildConfig.DYNAMIC_PACKAGE_NAME}&postId=$postId")
            domainUriPrefix = "${BuildConfig.URL_DYNAMIC_LINK}"
            androidParameters(BuildConfig.DYNAMIC_PACKAGE_NAME) {

            }

        }.addOnSuccessListener {
            navigator?.hideProgress()
            (context as MvvmActivity<*, *>).shareImage(it.shortLink.toString())
        }.addOnFailureListener {
            navigator?.hideProgress()
            it.printStackTrace()
        }
    }


    open fun myFavoritePost(position: Int) {

        uiScope?.launch {
            val temp = async(Dispatchers.IO) {
                dbHelper.getPostById(mData[position].id)
            }.await()
            if (temp != null) {
                results.value = mData?.apply {
                    get(position).liked = true
                }
            }

        }
    }

    override fun onStop() {
        super.onStop()
        onStop = true
    }

    fun onPageChange(position: Int) {
        if (!onStop) {
            myFavoritePost(position)
            onLoadMore(position)

        } else {
            onStop = false
        }
    }


}