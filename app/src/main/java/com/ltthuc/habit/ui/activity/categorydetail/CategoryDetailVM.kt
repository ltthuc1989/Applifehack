package com.ltthuc.habit.ui.activity.categorydetail

import androidx.lifecycle.MutableLiveData
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ltthuc.habit.data.AppDataManager
import com.ltthuc.habit.data.network.response.CatResp
import com.ltthuc.habit.ui.activity.HomeActivityNav
import javax.inject.Inject

class CategoryDetailVM @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseViewModel<CategoryDetailNav, CatResp>(schedulerProvider, connectionManager) {





}