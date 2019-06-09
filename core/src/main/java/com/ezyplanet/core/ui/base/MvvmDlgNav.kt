package com.ezyplanet.core.ui.base

interface MvvmDlgNav :MvvmNav{
    fun onClose()
    fun onSumbit(data:Any?=null)
}