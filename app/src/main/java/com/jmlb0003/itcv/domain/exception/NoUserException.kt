package com.jmlb0003.itcv.domain.exception

import com.jmlb0003.itcv.core.exception.Failure

class NoUserException(
    error: Exception = IllegalStateException("No information provided")
) : Failure.FeatureFailure(
    IllegalStateException("There is no user because of: ${error.message}").apply {
        addSuppressed(error)
    })
