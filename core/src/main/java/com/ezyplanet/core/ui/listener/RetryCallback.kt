package com.ezyplanet.core.ui.listener

interface RetryCallback<T> {
   // val request: T
    //fun onSendEvent()
    fun onRetry()
    fun onCancel()


}
