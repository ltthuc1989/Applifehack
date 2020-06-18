package com.applifehack.knowledge.ui.admin.manualpost

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.applifehack.knowledge.R
import com.applifehack.knowledge.R.string.please_enter_image_url
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.entity.PostType
import com.applifehack.knowledge.data.network.response.json.CategoryItem
import com.applifehack.knowledge.data.network.response.json.PostMD
import com.applifehack.knowledge.data.network.response.json.PostTypeItem
import com.applifehack.knowledge.data.network.response.json.QuoteTypeItem
import com.applifehack.knowledge.ui.widget.TextObserVable
import com.applifehack.knowledge.util.AlertDialogUtils
import com.applifehack.knowledge.util.JsonHelper
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.core.util.extension.fromJson
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class ManualPostVM @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseViewModel<ManualPostNav, String>(schedulerProvider, connectionManager) {

    val textCat = TextObserVable()
    val textTitle = TextObserVable()
    val textContentUrl = TextObserVable()
    val textAuthorName = TextObserVable()
    val textAuthorUrl = TextObserVable()
    val textImageUrl = TextObserVable()
    val textPostType = TextObserVable()
    val textQuoteType = TextObserVable()
    val textVideoType = TextObserVable()
   // val textMediaType = TextObserVable()

    var postMD:PostMD?=null
    private var listCat= mutableListOf<String>()
    private var listPostType = mutableListOf<String>()
    private var listQuoteType = mutableListOf<String>()
    private var catSelected :CategoryItem?=null
     var postTypeSelected =MutableLiveData<PostTypeItem>()
    private var quoteTypeSelected :QuoteTypeItem?=null


    fun initData(context: Context){
        postMD = Gson().fromJson(JsonHelper.getJsonDataFromAsset(context,"data_json"))
        postMD?.postType?.forEach {
            listPostType.add(it.name)
        }
        postMD?.category?.forEach {
            listCat.add(it.name)
        }
        postMD?.quoteType?.forEach {
            listQuoteType.add(it.name)
        }

    }

    fun pickCat(context: Context){
        AlertDialogUtils.showSingleChoice(context, R.string.pick_cat, listCat.toTypedArray()){
         catSelected = postMD?.category!![it]
            textCat.text = catSelected?.name!!

        }

    }
    fun pickPostType(context: Context){
        AlertDialogUtils.showSingleChoice(context, R.string.pick_post_type, listPostType.toTypedArray()){
            postTypeSelected.value = postMD?.postType!![it]
            textPostType.text = postMD?.postType!![it].name
        }
    }
    fun pickMediaType(context: Context){
        AlertDialogUtils.showSingleChoice(context, R.string.pick_media_type, listQuoteType.toTypedArray()){
            quoteTypeSelected = postMD?.quoteType!![it]
            textQuoteType.text = quoteTypeSelected?.name!!
        }
    }

    fun post(isLive:Boolean=false){
        if(!validPost()) return
//        uiScope?.launch {
//            val generateId = async(Dispatchers.IO) {
//                appDataManager.getGeneratePostId()
//            }
//            generateId?.await()?.let {
//                val post = getPostData(it)
//                navigator?.showProgress()
//                val postApi = if(!isLive) appDataManager.createPost(it,post) else appDataManager.createPostLive(it,post)
//                postApi.addOnSuccessListener {
//                    navigator?.hideProgress()
//                    resetData()
//                }.addOnFailureListener {
//                    navigator?.showAlert(it.message)
//                }
//
//
//            }
//        }
    }
    private fun validPost():Boolean{
        var isQuoteType=false
        if(textCat.text.isEmpty()){
            navigator?.showAlert(R.string.please_pick_cat)
            return false
        }
        if(textPostType.text.isEmpty()){
            navigator?.showAlert(R.string.please_pick_post_type)
            return false
        }
        if(textPostType.text?.equals(PostType.QUOTE.type)){
            isQuoteType = true
            if(textQuoteType.text.isEmpty()){
                navigator?.showAlert(R.string.please_pick_quote_type)
                return false
            }
        }
        if(textTitle.text?.isEmpty()){
            navigator?.showAlert(R.string.please_enter_title)
            return false
        }
        if(textAuthorName.text.isEmpty()){
            navigator?.showAlert(R.string.please_enter_author_name)
            return false
        }
        if(textAuthorName.text.isEmpty()){
            navigator?.showAlert(R.string.please_enter_author_name)
            return false
        }
        if(isQuoteType) return true

        if(textContentUrl.text.isEmpty()){
            navigator?.showAlert(R.string.please_enter_content_url)
            return false
        }
        if(textImageUrl.text.isEmpty()){
            navigator?.showAlert(please_enter_image_url)
            return false
        }
        return true

    }

    private fun getPostData(postId:String): Post {
       return Post().apply {
           title = textTitle.text

           author = textAuthorName.text
           authorUrl = textAuthorUrl.text
           imageUrl = textImageUrl.text
           type = postTypeSelected?.value?.name
           when(type){
               PostType.VIDEO.type-> {
                   video_url = textContentUrl.text
               }
               PostType.ARTICLE.type->{
                   redirect_link = textContentUrl.text
               }
               PostType.QUOTE.type->{
                   quote_type = textQuoteType.text.toLowerCase()
               }
           }
           id =postId

            catId = catSelected?.id
           catName = catSelected?.name

        }
    }

    private fun resetData(){
        textCat.text=""
        textPostType.text=""
        textQuoteType.text=""
        textTitle.text=""
        textAuthorName.text=""
        textAuthorUrl.text=""
        textVideoType.text=""
        textImageUrl.text=""
        textQuoteType.text=""
        textContentUrl.text=""
    }





}