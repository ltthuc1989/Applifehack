package com.ezyplanet.core.ui.fragment.tab



import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.ui.base.MvvmNav
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import javax.inject.Inject

class TabVM @Inject constructor(
        schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseViewModel<MvvmNav,Any>(schedulerProvider, connectionManager) {




}