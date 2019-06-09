package com.ezyplanet.thousandhands.util.livedata


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.ezyplanet.core.util.livedata.SingleEventLiveData

typealias ActivityAction = (FragmentActivity) -> Unit

/**
 * A custom wrapper for [SingleEventLiveData] that only works with [ActivityAction]
 */
class ActivityActionLiveData: SingleEventLiveData<ActivityAction>() {

    /**
     * invoke operator function to save [action] to value of [SingleEventLiveData] instance.
     *
     * <br></br>
     * For Example:
     *
     * val activityActionLiveData = ActivityActionLiveData()
     *
     * activityActionLiveData { activity -> // do something with given activity }
     *
     * @param action initView lambda function that receives initView [FragmentActivity].
     *
     */
    operator fun invoke(action: ActivityAction) {
        this.value = action
    }
}

/**
 * A lambda function that receives initView [Fragment]
 */
typealias FragmentAction = (Fragment) -> Unit

/**
 * A custom wrapper for [SingleEventLiveData] that only works with [FragmentAction]
 */
class FragmentActionLiveData: SingleEventLiveData<FragmentAction>() {

    /**
     * invoke operator function to save [action] to value of [SingleEventLiveData] instance.
     *
     * <br></br>
     * For Example:
     *
     * val fragmentActionLiveData = FragmentActionLiveData()
     *
     * fragmentActionLiveData { fragment -> // do something with the given fragment }
     *
     * @param action initView lambda function that receives initView [FragmentAction].
     *
     */
    operator fun invoke(action: FragmentAction) {
        this.value = action
    }
}