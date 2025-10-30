package com.example.ravenhackernews.core.exceptions

sealed class UseCaseException : Exception() {
    class GenericException(
        val code: Int = 0,
        val errorMessage: String = "",
        vararg val params: Any?
    ) : Exception(errorMessage) {
        var serverCode: String? = null
        var serverMessage: String? = null
    }
}
