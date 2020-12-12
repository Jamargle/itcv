package com.jmlb0003.itcv.core.interactor

import com.jmlb0003.itcv.core.coroutines.Dispatchers
import com.jmlb0003.itcv.core.coroutines.DispatchersImp
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.core.Either
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class UseCase<out Type, in Params> where Type : Any {

    abstract suspend fun run(params: Params): Either<Failure, Type>

    operator fun invoke(
        coroutineScope: CoroutineScope,
        dispatchers: Dispatchers = DispatchersImp,
        runDispatcher: CoroutineDispatcher = dispatchers.backgroundThread,
        resultDispatcher: CoroutineDispatcher = dispatchers.mainThread,
        params: Params,
        onResult: (Either<Failure, Type>) -> Unit = {}
    ) {
        coroutineScope.launch(runDispatcher) {
            val result = run(params)
            withContext(resultDispatcher) {
                onResult(result)
            }
        }
    }

    /**
     * When no input parameters needed set [None] as input
     */
    class None
}
