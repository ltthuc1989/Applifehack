package com.applifehack.knowledge.ui.fragment.category

import androidx.lifecycle.ViewModel
import com.applifehack.knowledge.data.entity.Post
import com.ezyplanet.core.util.livedata.SingleEventLiveData

class CategoryShareEvent :ViewModel() {
     val catClickEvent = SingleEventLiveData<String>()
     val postClickEvent = SingleEventLiveData<Post>()
}