package com.ltthuc.habit.ui.fragment.slidepost

import androidx.lifecycle.ViewModel
import com.ezyplanet.core.di.qualifier.ViewModelKey
import com.ltthuc.habit.ui.activity.webview.WebViewVM
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SlidePostVMBuilder{
    @Binds
    @IntoMap
    @ViewModelKey(SlidePostVM::class)
    abstract fun bindSlidePostVM(slidePostVM: SlidePostVM): ViewModel
}