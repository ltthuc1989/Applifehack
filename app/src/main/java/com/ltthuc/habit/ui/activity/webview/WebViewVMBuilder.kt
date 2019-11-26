package com.ltthuc.habit.ui.activity.webview

import androidx.lifecycle.ViewModel
import com.ezyplanet.core.di.qualifier.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class WebViewVMBuilder{
    @Binds
    @IntoMap
    @ViewModelKey(WebViewVM::class)
    abstract fun bindWebViewVM(webViewVM: WebViewVM): ViewModel
}