package com.applifehack.knowledge.di.component

import android.app.Application
import com.ezyplanet.supercab.driver.di.module.AppModule
import com.ezyplanet.supercab.driver.di.module.UtilModule
import com.applifehack.knowledge.KnowledgeApp
import com.applifehack.knowledge.di.buider.ActivityBuilder
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Main Application [Component] that included all of modules and sub components.
 */
@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    (AppModule::class),
    (UtilModule::class),
    (AndroidInjectionModule::class),
    (ActivityBuilder::class)
])
interface AppComponent {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent

    }

    fun inject(app: KnowledgeApp)
}
