package com.cognota.core.networking

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.cognota.core.data.ApiEmptyResponse
import com.cognota.core.data.ApiErrorResponse
import com.cognota.core.data.ApiResponse
import com.cognota.core.data.ApiSuccessResponse
import com.cognota.core.vo.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import retrofit2.Response

@FlowPreview
@ExperimentalCoroutinesApi
abstract class NetworkBoundResource<ResultType, RequestType> {

    fun asFlow(): Flow<Resource<ResultType>> = flow {
        emit(Resource.loading(null))

        val dbValue = loadFromDb().first()
        if (shouldFetch(dbValue)) {
            emit(Resource.loading(dbValue))

            when (val apiResponse = try {
                val fetchFromNetwork = fetchFromNetwork()
                ApiResponse.create(fetchFromNetwork)
            } catch (e: Throwable) {
                ApiResponse.create<RequestType>(e)
            }) {
                is ApiSuccessResponse -> {
                    saveNetworkResult(processResponse(apiResponse))
                    emitAll(loadFromDb().map { Resource.success(it) })
                }
                is ApiEmptyResponse -> {
                    emitAll(loadFromDb().map { Resource.success(it) })
                }
                is ApiErrorResponse -> {
                    onFetchFailed()
                    emitAll(loadFromDb().map { Resource.error(apiResponse.error, it) })
                }
            }
        } else {
            emitAll(loadFromDb().map { Resource.success(it) })
        }
    }

    protected open fun onFetchFailed() {
        // Implement in sub-classes to handle errors
    }

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    @WorkerThread
    protected abstract suspend fun saveNetworkResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): Flow<ResultType>

    @MainThread
    protected abstract suspend fun fetchFromNetwork(): Response<RequestType>
}