package com.lekaha.simpletube.data.source

import com.lekaha.simpletube.data.model.BufferooEntity
import com.lekaha.simpletube.data.repository.BufferooDataStore
import com.lekaha.simpletube.data.repository.BufferooRemote
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Implementation of the [BufferooDataStore] interface to provide a means of communicating
 * with the remote data source
 */
open class BufferooRemoteDataStore constructor(private val bufferooRemote: BufferooRemote) :
    BufferooDataStore {

    override fun clearBufferoos(): Completable {
        throw UnsupportedOperationException()
    }

    override fun saveBufferoos(bufferoos: List<BufferooEntity>): Completable {
        throw UnsupportedOperationException()
    }

    /**
     * Retrieve a list of [BufferooEntity] instances from the API
     */
    override fun getBufferoos(): Single<List<BufferooEntity>> {
        return bufferooRemote.getBufferoos()
    }

}