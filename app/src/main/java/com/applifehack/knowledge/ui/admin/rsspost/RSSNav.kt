package com.applifehack.knowledge.ui.activity

import com.ezyplanet.core.ui.base.MvvmNav
import com.applifehack.knowledge.data.network.response.RssCatResp
import com.applifehack.knowledge.ui.admin.rssposts.RssListModel


interface RSSNav : MvvmNav{

    fun gotoListPostScreen(resp: RssListModel)

}