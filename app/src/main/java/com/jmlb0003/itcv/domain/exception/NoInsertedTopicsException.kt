package com.jmlb0003.itcv.domain.exception

import com.jmlb0003.itcv.core.exception.Failure

class NoInsertedTopicsException(
    error: Exception = IllegalStateException("No information provided")
) : Failure.FeatureFailure(
    IllegalStateException("Not possible to insert topics because of: ${error.message}").apply {
        addSuppressed(error)
    })
