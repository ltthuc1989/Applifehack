package com.ltthuc.habit.ui.activity

import com.ezyplanet.core.ui.base.MvvmNav
import com.ltthuc.habit.data.network.response.RssCatResp
import com.ltthuc.habit.ui.activity.listpost.PostContent

interface HomeActivityNav : MvvmNav{

    fun gotoListPostScreen(resp: RssCatResp)
}