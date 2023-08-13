package com.osamaaftab.busright.data.repository.base

import com.osamaaftab.busright.data.ApiErrorHandle
import com.osamaaftab.busright.domain.model.Resource
import kotlinx.coroutines.Deferred

open class BaseRepositoryImp(private val errorHandle: ApiErrorHandle) {
    protected suspend fun <T> Deferred<T>.awaitAndCatch(): Resource<T> {
        return try {
            Resource.Success(this.await())
        } catch (e: Exception) {
            return Resource.Error(errorModel = errorHandle.traceErrorException(e))
        }
    }
}