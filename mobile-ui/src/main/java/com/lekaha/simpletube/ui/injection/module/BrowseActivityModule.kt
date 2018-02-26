package com.lekaha.simpletube.ui.injection.module

import android.content.Context
import com.lekaha.simpletube.cache.BufferooCacheImpl
import com.lekaha.simpletube.cache.PreferencesHelper
import com.lekaha.simpletube.cache.db.DbOpenHelper
import com.lekaha.simpletube.cache.mapper.BufferooEntityMapper
import com.lekaha.simpletube.data.BufferooDataRepository
import com.lekaha.simpletube.data.repository.BufferooCache
import com.lekaha.simpletube.data.repository.BufferooRemote
import com.lekaha.simpletube.data.source.BufferooCacheDataStore
import com.lekaha.simpletube.data.source.BufferooDataStoreFactory
import com.lekaha.simpletube.data.source.BufferooRemoteDataStore
import com.lekaha.simpletube.domain.executor.PostExecutionThread
import com.lekaha.simpletube.domain.executor.ThreadExecutor
import com.lekaha.simpletube.domain.interactor.browse.GetBufferoos
import com.lekaha.simpletube.domain.repository.BufferooRepository
import com.lekaha.simpletube.presentation.browse.BrowseBufferoosContract
import com.lekaha.simpletube.presentation.browse.BrowseBufferoosPresenter
import com.lekaha.simpletube.presentation.mapper.BufferooMapper
import com.lekaha.simpletube.remote.BufferooRemoteImpl
import com.lekaha.simpletube.remote.BufferooService
import com.lekaha.simpletube.ui.browse.BrowseViewHolder
import com.lekaha.simpletube.ui.injection.qualifier.ActivityContext
import com.lekaha.simpletube.ui.model.BrowseViewModelFactory
import dagger.Module
import dagger.Provides


/**
 * Module used to provide dependencies at an activity-level.
 */
@Module
open class BrowseActivityModule {

    @Provides
    internal fun provideGetBufferoos(
        bufferooRepository: BufferooRepository,
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread
    ) = GetBufferoos(bufferooRepository, threadExecutor, postExecutionThread)

    @Provides
    internal fun provideBufferooEntityMapper() = BufferooEntityMapper()

    @Provides
    internal fun provideBufferooMapper() = com.lekaha.simpletube.presentation.mapper.BufferooMapper()

    @Provides
    internal fun provideDbBufferooMapper() = com.lekaha.simpletube.cache.db.mapper.BufferooMapper()

    @Provides
    internal fun provideRemoteBufferooMapper() =
        com.lekaha.simpletube.remote.mapper.BufferooEntityMapper()

    @Provides
    internal fun provideDataBufferooMapper() = com.lekaha.simpletube.data.mapper.BufferooMapper()

    @Provides
    internal fun provideUiBufferooMapper() = com.lekaha.simpletube.ui.mapper.BufferooMapper()

    @Provides
    internal fun provideBufferooCache(
        factory: DbOpenHelper,
        entityMapper: BufferooEntityMapper,
        mapper: com.lekaha.simpletube.cache.db.mapper.BufferooMapper,
        helper: PreferencesHelper
    ): BufferooCache = BufferooCacheImpl(factory, entityMapper, mapper, helper)

    @Provides
    internal fun provideBufferooRemote(
        service: BufferooService,
        factory: com.lekaha.simpletube.remote.mapper.BufferooEntityMapper
    ): BufferooRemote = BufferooRemoteImpl(service, factory)

    @Provides
    internal fun provideBufferooDataStoreFactory(
        bufferooCache: BufferooCache,
        bufferooCacheDataStore: BufferooCacheDataStore,
        bufferooRemoteDataStore: BufferooRemoteDataStore
    ): BufferooDataStoreFactory = BufferooDataStoreFactory(
        bufferooCache,
        bufferooCacheDataStore,
        bufferooRemoteDataStore
    )

    @Provides
    internal fun provideBufferooCacheDataStore(bufferooCache: BufferooCache)
            : BufferooCacheDataStore = BufferooCacheDataStore(bufferooCache)

    @Provides
    internal fun provideBufferooRemoteDataStore(bufferooRemote: BufferooRemote)
            : BufferooRemoteDataStore = BufferooRemoteDataStore(bufferooRemote)

    @Provides
    internal fun provideBufferooRepository(
        factory: BufferooDataStoreFactory,
        mapper: com.lekaha.simpletube.data.mapper.BufferooMapper
    ): BufferooRepository = BufferooDataRepository(factory, mapper)

    @Provides
    internal fun provideBrowseViewHolderFactory(@ActivityContext context: Context) =
        BrowseViewHolder.BrowseViewHolderFactory(context)

    @Provides
    internal fun provideBrowseViewHolderBinder() = BrowseViewHolder.BrowseViewHolderBinder()

    @Provides
    internal fun provideBrowsePresenter(getBufferoos: GetBufferoos, mapper: BufferooMapper)
            : BrowseBufferoosContract.Presenter = BrowseBufferoosPresenter(getBufferoos, mapper)

    @Provides
    internal fun provideBrowseViewModelFactory(presenter: BrowseBufferoosContract.Presenter) =
        BrowseViewModelFactory(presenter)
}