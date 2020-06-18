package com.applifehack.knowledge.ui.activity

import com.ezyplanet.core.ui.base.MvvmNav
import com.applifehack.knowledge.data.network.response.RssCatResp


interface RSSNav : MvvmNav{

    fun gotoListPostScreen(resp: RssCatResp)

}