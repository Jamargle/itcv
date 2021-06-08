package com.jmlb0003.itcv.domain.exception

import com.jmlb0003.itcv.core.exception.Failure

class NoReposException(
    error: Exception = IllegalStateException("No information provided")
) : Failure.FeatureFailure(
    IllegalStateException("There are no repos because of: ${error.message}").apply {
        addSuppressed(error)
    })
