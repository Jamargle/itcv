package com.jmlb0003.itcv.domain.exception

import com.jmlb0003.itcv.core.exception.Failure

class NoInsertedReposException(
    error: Exception = IllegalStateException("No information provided")
) : Failure.FeatureFailure(
    IllegalStateException("Not possible to insert repos because of: ${error.message}").apply {
        addSuppressed(error)
    })
