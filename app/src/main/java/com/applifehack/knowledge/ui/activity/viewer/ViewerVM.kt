package com.applifehack.knowledge.ui.activity.viewer

import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.applifehack.knowledge.data.AppDataManager
import javax.inject.Inject

class ViewerVM @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseViewModel<ViewerNav, String>(schedulerProvider, connectionManager) {


}