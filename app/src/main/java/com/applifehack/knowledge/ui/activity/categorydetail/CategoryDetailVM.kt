package com.applifehack.knowledge.ui.activity.categorydetail

import androidx.lifecycle.LiveData
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.data.network.response.CatResp
import com.applifehack.knowledge.ui.activity.BaseBottomVM
import javax.inject.Inject

class CategoryDetailVM @Inject constructor( appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseBottomVM<CategoryDetailNav, CatResp>(appDataManager,schedulerProvider, connectionManager) {




}