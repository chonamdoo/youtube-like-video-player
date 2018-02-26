package com.lekaha.simpletube.data

import com.lekaha.simpletube.data.mapper.BufferooMapper
import com.lekaha.simpletube.data.model.BufferooEntity
import com.lekaha.simpletube.data.source.BufferooDataStoreFactory
import com.lekaha.simpletube.data.source.BufferooRemoteDataStore
import com.lekaha.simpletube.domain.model.Bufferoo
import com.lekaha.simpletube.domain.repository.BufferooRepository
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Provides an implementation of the [BufferooRepository] interface for communicating to and from
 * data sources
 */
class BufferooDataRepository constructor(
    private val factory: BufferooDataStoreFactory,
    private val bufferooMapper: BufferooMapper
) :
    BufferooRepository {

    override fun clearBufferoos(): Completable {
        return factory.retrieveCacheDataStore().clearBufferoos()
    }

    override fun saveBufferoos(bufferoos: List<Bufferoo>): Completable {
        val bufferooEntities = bufferoos.map { bufferooMapper.mapToEntity(it) }
        return saveBufferooEntities(bufferooEntities)
    }

    private fun saveBufferooEntities(bufferoos: List<BufferooEntity>): Completable {
        return factory.retrieveCacheDataStore().saveBufferoos(bufferoos)
    }

    override fun getBufferoos(): Single<List<Bufferoo>> {
        val dataStore = factory.retrieveDataStore()
        return dataStore.getBufferoos()
            .flatMap {
                if (dataStore is BufferooRemoteDataStore) {
                    saveBufferooEntities(it).toSingle { it }
                } else {
                    Single.just(it)
                }
            }
            .map { list ->
                list.map { listItem ->
                    bufferooMapper.mapFromEntity(listItem)
                }
            }
    }

}