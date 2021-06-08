package com.jmlb0003.itcv.domain.exception

import com.jmlb0003.itcv.core.exception.Failure

class NoReposRemovedException(
    error: Exception = IllegalStateException("No information provided")
) : Failure.FeatureFailure(
    IllegalStateException("Impossible to remove repos because of: ${error.message}").apply {
        addSuppressed(error)
    })
