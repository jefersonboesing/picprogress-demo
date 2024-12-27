package com.picprogress.usecase

import com.picprogress.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class UseCase<PARAM, RESULT> {
    suspend fun invoke(param: PARAM): Result<RESULT> = withContext(Dispatchers.Default) {
        return@withContext try {
            Result.Success(data = execute(param))
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }

    protected abstract suspend fun execute(param: PARAM): RESULT

}


