package com.ezyplanet.core.ui.base


import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.androidnetworking.common.ANConstants
import com.androidnetworking.error.ANError


import com.ezyplanet.core.ui.listener.RetryCallback
import com.ezyplanet.core.util.CoreConstants
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ezyplanet.thousandhands.util.livedata.ActivityActionLiveData
import com.ezyplanet.thousandhands.util.livedata.FragmentActionLiveData

import com.google.gson.reflect.TypeToken
import io.reactivex.disposables.CompositeDisposable

import timber.log.Timber
import com.ezyplanet.core.R
import com.ezyplanet.core.data.network.ApiError
import com.ezyplanet.core.data.network.DataClassError
import com.ezyplanet.core.ui.listener.LoginNavigator
import com.ezyplanet.core.ui.listener.PasswordNavigator
import com.ezyplanet.core.util.CoreConstants.API_STATUS_CODE_LOCAL_ERROR
import com.ezyplanet.core.util.extension.toObjectRespone
import com.ezyplanet.core.util.livedata.SingleEventLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import io.reactivex.Single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.lang.IllegalArgumentException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.net.ssl.HttpsURLConnection


abstract class BaseViewModel<n : MvvmNav, m : Any?>(private val connectionManager: BaseConnectionManager)
    : ViewModel(), LifecycleObserver {

    protected val TAG = this.javaClass.simpleName
    var schedulerProvider: SchedulerProvider? = null

    constructor(schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager) : this(connectionManager) {

        this.schedulerProvider = schedulerProvider
    }


    var navigator: n? = null
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val activityAction = ActivityActionLiveData()
    val fragmentAction = FragmentActionLiveData()
    //for ListViewModel
    var resetLoadingState: Boolean = false
    var visibleThreshold = CoreConstants.VISIBLE_THRESHOLD
    val toolbarTitle: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    //liveData event for Ui save action(optional)
    val event = SingleEventLiveData<Any>()
    val _model = MutableLiveData<m>()
    val model: LiveData<m> = _model
    protected var isInBackGround = false


    var isLogIn = MutableLiveData<Boolean>()
    private val _isUiStateChange = MutableLiveData<Boolean>()
    val isUiStateChange: LiveData<Boolean>
        get() = _isUiStateChange

    fun setIsUiStageChange(isUiStateChange: Boolean) {
        if (_isUiStateChange.value != isUiStateChange) {
            _isUiStateChange.value = isUiStateChange
            // reLoadData()


        }
    }
    private val viewModelJob = SupervisorJob()
    protected val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    open fun updateModel(data: m?) {
        _model.value=data
        setIsUiStageChange(true)
    }

    open fun handleIntent(intent: Intent?, isOnNewIntent: Boolean? = false) {

    }

    fun updateLoginSatus(boolean: Boolean) {
        isLogIn.value = boolean
    }


    open fun reLoadData() {
    }

    open fun customAction(data: Any) {

    }

    open fun getEmptyString(context: Context): String? {

        return context.getString(R.string.no_data)
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable?.clear()
        navigator?.hideProgress()
        viewModelJob?.cancel()
    }


    fun onTokenExpired() {
        // todo : what should i do if token expired ?!
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() {
    }

    /**
     * We can use lifeCycle in inherited classes if we need
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop() {
        isInBackGround = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume() {
        isInBackGround = false

    }


    fun openActivityOnTokenExpire() {}

    fun showAlert(@StringRes resId: Int, code: Int) {}
    fun showCancelRetryAlert(@StringRes resId: Int, callback: RetryCallback<*>) {}
    fun showCancelRetryAlert(message: String?, callback: RetryCallback<*>) {}
    fun showNetworkFail(callback: RetryCallback<*>) {}


    fun showAlert(message: String) {}

    fun onErrorSnackBar(@StringRes resId: Int) {}
    //
    fun onErrorSnackBar(message: String) {}

    fun isNetworkConnected(): Boolean? {
        return connectionManager?.isNetworkConnected()
    }

    fun hideKeyboard() {

    }

    fun onLoadmore(page: Int) {

    }

    protected fun <T> apiSingleString(single: Single<T>, isShowLoading: Boolean = false, action: (T) -> Unit = {},
                                      retryCallback: RetryCallback<*>? = null) {

        if (isShowLoading) navigator?.showProgress()
        compositeDisposable.add(single
                .compose(schedulerProvider?.ioToMainSingleScheduler()).map {
                    if (it is String) {
                        toObjectRespone<T>(it)
                    }else {
                        it
                    }
                }
                .subscribe({it1->
                    action(it1 as T)
                    navigator?.hideProgress()


                }, {

                    if (retryCallback != null) {
                        apiErrorObservable(it, retryCallback)
                    } else {
                        apiErrorObservable(it)
                    }

                }))
    }
    protected fun <T> apiSingle(single: Single<T>, action: (T) -> Unit = {},
                                isShowLoading: Boolean = false,retryCallback: RetryCallback<*>? = null) {

        if (isShowLoading) navigator?.showProgress()
        compositeDisposable.add(single
                .compose(schedulerProvider?.ioToMainSingleScheduler())
                .subscribe({
                    navigator?.hideProgress()
                    action(it)



                }, {

                    if (retryCallback != null) {
                        apiErrorObservable(it, retryCallback)
                    } else {
                        apiErrorObservable(it)
                    }

                }))
    }





    fun apiErrorObservable(throwable: Throwable, retryCallback: RetryCallback<*>) {
        if (throwable != null) {
            val anError = throwable as ANError?
            navigator?.hideProgress()
            if (anError == null || anError != null && anError.errorCode == 0) {
                if (anError != null && anError.errorCode == 0) {
                    if (anError.errorDetail != ANConstants.CONNECTION_ERROR) {
                        navigator?.showAlert(anError?.errorDetail, throwable.message)
                    } else {
                        if (throwable.cause is UnknownHostException || throwable.cause is SocketTimeoutException || throwable.cause is SocketException) {
                            navigator?.connectionFail(R.string.can_not_connect_to_server, retryCallback)
                        } else {
                            navigator?.showAlert(throwable.message)

                        }
                    }

                } else {

                    // navigator?.showAlert(throwable.message)
                }

                return
            }

            if (anError.errorCode == HttpsURLConnection.HTTP_NOT_FOUND) {
                navigator?.connectionFail(R.string.url_not_found)
                return
            }

            if (anError.errorCode == API_STATUS_CODE_LOCAL_ERROR && anError.errorDetail == ANConstants.CONNECTION_ERROR) {

                navigator?.connectionFail(R.string.can_not_connect_to_server, retryCallback)
                return

            }

            if (anError.errorCode == API_STATUS_CODE_LOCAL_ERROR && anError.errorDetail == ANConstants.REQUEST_CANCELLED_ERROR) {

                navigator?.connectionFail(R.string.can_not_connect_to_server, retryCallback)

                return
            }
            if (anError.errorCode == HttpsURLConnection.HTTP_UNAUTHORIZED || anError.errorCode == HttpsURLConnection.HTTP_FORBIDDEN) {
                if (navigator !is LoginNavigator && navigator !is PasswordNavigator) {
                    navigator?.openActivityOnTokenExpire()
                    return
                }
            }
            val type = object : TypeToken<List<DataClassError.ErrorResp>>() {

            }.type
            val builder = GsonBuilder().excludeFieldsWithoutExposeAnnotation()
            val gson = builder.create()

            var apiError: List<DataClassError.ErrorResp>
            try {
                apiError = gson.fromJson(anError.errorBody, type)

                if ((apiError == null || apiError.get(0) == null) && anError.errorCode != HttpsURLConnection.HTTP_UNAUTHORIZED) {

                    //  navigator?.showAlert(R.string.something_wrong)
                    navigator?.showAlert(anError.errorCode?.toString())

                    return

                }
                var msg = ""
                for (i in apiError.indices) {
                    msg = if (i != apiError.size - 1)
                        msg + apiError[i].full_messages[0] + ","
                    else msg + apiError[i].full_messages[0]
                }
                Timber.d("error $msg")
                when (anError.errorCode) {
                    HttpsURLConnection.HTTP_UNAUTHORIZED, HttpsURLConnection.HTTP_FORBIDDEN -> {

                        if (navigator !is LoginNavigator && navigator !is PasswordNavigator) {

                            navigator?.openActivityOnTokenExpire()
                            return
                        }

                        if (apiError != null && msg != null) {
                            navigator?.showAlert(msg)
                            return
                        } else {
                            var error = gson.fromJson(anError.errorBody, ApiError::class.java)
                            navigator?.showAlert(error.message)
                            return
                        }
                    }
                    HttpsURLConnection.HTTP_INTERNAL_ERROR, HttpsURLConnection.HTTP_NOT_FOUND -> if (apiError != null) {

                        navigator?.showAlert(R.string.general_error, anError.errorCode)
                        return
                    }
                    else -> if (apiError != null) {
                        navigator?.showAlert(anError.errorCode?.toString(), msg)
                        return
                    }
                }


            } catch (err: JsonSyntaxException) {


                val apiError = gson.fromJson<DataClassError.ErrorResp>(anError.errorBody, DataClassError.ErrorResp::class.java)

                when (anError.errorCode) {
                    HttpsURLConnection.HTTP_UNAUTHORIZED, HttpsURLConnection.HTTP_FORBIDDEN -> {
                        if (navigator !is LoginNavigator && navigator !is PasswordNavigator) {

                            navigator?.openActivityOnTokenExpire()
                            return
                        }

                        if (apiError != null && !apiError?.full_messages.isNullOrEmpty()) {

                            navigator?.showAlert(apiError.full_messages[0])
                            return
                        } else {

                            var error = gson.fromJson(anError.errorBody, ApiError::class.java)
                            navigator?.hideProgress()
                            navigator?.showAlert(error.message)
                            return
                        }
                    }

                    HttpsURLConnection.HTTP_INTERNAL_ERROR, HttpsURLConnection.HTTP_NOT_FOUND -> if (apiError != null) {
                        navigator?.hideProgress()

                        navigator?.showAlert(R.string.general_error, anError.errorCode)

                        return
                    }

                    else -> if (apiError != null) {
                        navigator?.showAlert(apiError.full_messages[0])

                        return
                    }
                }
            }

        } else {

        }

    }

    fun apiErrorObservable(throwable: Throwable) {
        val anError = throwable as ANError?
        navigator?.hideProgress()
        if (anError == null || anError != null && anError.errorCode == 0) {
            if (anError != null && anError.errorCode == 0) {
                if (anError.errorDetail != ANConstants.CONNECTION_ERROR) {
                    navigator?.showAlert(anError?.errorDetail, throwable.message)
                } else {
                    if (throwable.cause is UnknownHostException || throwable.cause is SocketTimeoutException || throwable.cause is SocketException) {
                        navigator?.connectionFail(R.string.can_not_connect_to_server)
                    } else {
                        navigator?.showAlert(throwable.message)

                    }
                }

            } else {

                // navigator?.showAlert(throwable.message)
            }

            return
        }

        if (anError.errorCode == HttpsURLConnection.HTTP_NOT_FOUND) {
            navigator?.connectionFail(R.string.url_not_found)
            return
        }
        if (anError.errorCode == API_STATUS_CODE_LOCAL_ERROR && anError.errorDetail == ANConstants.CONNECTION_ERROR) {

            navigator?.showAlert(R.string.can_not_connect_to_server)
            return

        }

        if (anError.errorCode == API_STATUS_CODE_LOCAL_ERROR && anError.errorDetail == ANConstants.REQUEST_CANCELLED_ERROR) {

            //  navigator?.showAlert(R.string.can_not_connect_to_server)
            navigator?.showAlert(throwable.message)

            return
        }

        if (anError.errorCode == HttpsURLConnection.HTTP_UNAUTHORIZED || anError.errorCode == HttpsURLConnection.HTTP_FORBIDDEN) {
            val builder = GsonBuilder().excludeFieldsWithoutExposeAnnotation()
            val gson = builder.create()
            var apiError: Any
            try {
                apiError = gson.fromJson<Array<DataClassError.ErrorResp>>(anError.errorBody, Array<DataClassError.ErrorResp>::class.java)
                if (anError.errorCode == HttpsURLConnection.HTTP_FORBIDDEN) {
                    navigator?.showAlert(apiError[0].full_messages[0])
                } else {
                    navigator?.openActivityOnTokenExpire()

                }
            } catch (e: Exception) {


                try {
                    apiError = gson.fromJson<DataClassError.ErrorResp2>(anError.errorBody, DataClassError.ErrorResp2::class.java)
                    navigator?.showAlert(apiError?.error_description)
                    if (navigator !is LoginNavigator) {
                        navigator?.openActivityOnTokenExpire()
                    }

                } catch (e: java.lang.Exception) {
                    //  apiError = gson.fromJson<DataClassError.ErrorResp>(anError.errorBody, DataClassError.ErrorResp::class.java)
                    if (anError.errorCode == HttpsURLConnection.HTTP_FORBIDDEN) {
                        //navigator?.showAlert(apiError.full_messages[0])
                    } else {
                        navigator?.openActivityOnTokenExpire()

                    }
                }

            }

            return
        }

        val type = object : TypeToken<List<DataClassError.ErrorResp>>() {

        }.type
        val builder = GsonBuilder().excludeFieldsWithoutExposeAnnotation()
        val gson = builder.create()

        var apiError: List<DataClassError.ErrorResp>
        try {
            apiError = gson.fromJson(anError.errorBody, type)

            if ((apiError == null) && anError.errorCode != HttpsURLConnection.HTTP_UNAUTHORIZED) {

                // navigator?.showAlert(R.string.something_wrong)
                navigator?.showAlert(anError.errorCode)

                return

            }
            var msg = ""
            for (i in apiError.indices) {
                msg = if (i != apiError.size - 1)
                    msg + apiError[i].full_messages[0] + ","
                else msg + apiError[i].full_messages[0]
            }
            Timber.d("error $msg")
            when (anError.errorCode) {
                HttpsURLConnection.HTTP_UNAUTHORIZED, HttpsURLConnection.HTTP_FORBIDDEN -> {

                    if (navigator !is LoginNavigator && navigator !is PasswordNavigator) {

                        navigator?.openActivityOnTokenExpire()

                        return
                    }

                    if (apiError != null) {

                        navigator?.showAlert(msg)
                        return
                    } else {
                        val error = gson.fromJson(anError.errorBody, ApiError::class.java)
                        navigator?.hideProgress()
                        navigator?.showAlert(error.message)
                        return
                    }
                }
                HttpsURLConnection.HTTP_INTERNAL_ERROR, HttpsURLConnection.HTTP_NOT_FOUND -> if (apiError != null) {
                    navigator?.hideProgress()
                    navigator?.showAlert(R.string.general_error, anError.errorCode)
                    return
                }
                else -> if (apiError != null) {

                    navigator?.showAlert(msg)
                    return
                }
            }


        } catch (err: JsonSyntaxException) {

            var apiError: DataClassError.ErrorResp? = null
            apiError = gson.fromJson<DataClassError.ErrorResp>(anError.errorBody, DataClassError.ErrorResp::class.java)

            when (anError.errorCode) {
                HttpsURLConnection.HTTP_UNAUTHORIZED, HttpsURLConnection.HTTP_FORBIDDEN -> {
                    navigator?.hideProgress()
                    if (navigator !is LoginNavigator && navigator !is PasswordNavigator) {
                        navigator?.openActivityOnTokenExpire()
                        return
                    }

                    if (apiError != null && !apiError?.full_messages.isNullOrEmpty()) {
                        navigator?.showAlert(apiError.full_messages[0])
                        return
                    } else {
                        val error = gson.fromJson(anError.errorBody, ApiError::class.java)
                        navigator?.showAlert(error.message)
                        return
                    }
                }

                HttpsURLConnection.HTTP_INTERNAL_ERROR, HttpsURLConnection.HTTP_NOT_FOUND -> if (apiError != null) {
                    navigator?.showAlert(R.string.general_error, anError.errorCode)

                    return
                }

                else -> if (apiError != null) {
                    if (apiError?.full_messages.isNullOrEmpty()) {
                        val error = gson.fromJson(anError.errorBody, ApiError::class.java)
                        navigator?.showAlert(error.message)
                    } else {
                        navigator?.showAlert(apiError.full_messages[0])
                    }

                    return
                }
            }
        }


    }

    open fun receiveNewTrip(data: Any) {

    }

    private inline fun <T> toObjectRespone(result: String?): Any? {
        return if (result.isNullOrEmpty() || result == "[]") result
        else return Gson().fromJson(result, object : com.google.gson.reflect.TypeToken<T>() {}.type)
    }

}