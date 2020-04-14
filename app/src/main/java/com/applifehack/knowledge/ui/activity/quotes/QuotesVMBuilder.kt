package com.applifehack.knowledge.ui.activity.quotes

import androidx.lifecycle.ViewModel
import com.ezyplanet.core.di.qualifier.ViewModelKey

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class QuotesVMBuilder{
    @Binds
    @IntoMap
    @ViewModelKey(QuotesVM::class)
    abstract fun bindQuoteVM(quotesVM: QuotesVM): ViewModel
}