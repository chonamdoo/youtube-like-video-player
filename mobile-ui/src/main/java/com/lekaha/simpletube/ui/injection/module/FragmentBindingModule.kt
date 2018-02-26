package com.lekaha.simpletube.ui.injection.module

import com.lekaha.simpletube.ui.browse.BrowseFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

    @ContributesAndroidInjector(modules =  arrayOf(
            BrowseActivityModule::class,
            BrowseModule::class))
    abstract fun provideBrowseFragment(): BrowseFragment
}