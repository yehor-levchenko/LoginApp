package com.yehorlevchenko.domain.utils

data class ApiResponse<T> (
    val result : T?,
    val error : ApiError?
)
