package com.example.ravenhackernews.core.extesions


import android.net.http.NetworkException
import com.example.ravenhackernews.core.definitions.Constants.CODE_HTTP_ERROR
import com.example.ravenhackernews.core.definitions.Constants.CODE_MALFORMED_JSON
import com.example.ravenhackernews.core.definitions.Constants.CODE_NETWORK_ERROR
import com.example.ravenhackernews.core.definitions.Constants.CODE_NO_INTERNET
import com.example.ravenhackernews.core.definitions.Constants.CODE_TIMEOUT
import com.example.ravenhackernews.core.definitions.Constants.COMMON_ERROR_PARSER_JSON
import com.example.ravenhackernews.core.definitions.Constants.COMMON_GENERIC_ERROR_TEXT_ES
import com.example.ravenhackernews.core.definitions.Constants.COMMON_NOT_NETWORK_AVAILABLE_ES

import com.example.ravenhackernews.core.exceptions.UseCaseException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.MalformedJsonException
import io.reactivex.rxjava3.core.Single
import retrofit2.HttpException

val defaultGenericNetworkException =
    UseCaseException.GenericException(CODE_NETWORK_ERROR, COMMON_GENERIC_ERROR_TEXT_ES, "")
fun <T : Any> Single<T>.parseError(
    otherWise: (Throwable) -> Throwable = { defaultGenericNetworkException }
): Single<T> {
    return onErrorResumeNext {
        Single.error(
            when (it) {
                is NetworkException -> UseCaseException.GenericException(
                    CODE_NO_INTERNET,
                    COMMON_NOT_NETWORK_AVAILABLE_ES
                )
                is HttpException -> UseCaseException.GenericException(
                    CODE_HTTP_ERROR,
                    COMMON_GENERIC_ERROR_TEXT_ES
                )
                is TimeoutException, is SocketTimeoutException -> UseCaseException.GenericException(CODE_TIMEOUT)
                is JsonSyntaxException, is MalformedJsonException -> UseCaseException.GenericException(
                    CODE_NETWORK_ERROR,
                    COMMON_ERROR_PARSER_JSON,
                    CODE_MALFORMED_JSON
                )
                else -> otherWise(it)
            }
        )
    }
}

fun String?.value(default: String = ""): String = this ?: default
fun Boolean?.value(default: Boolean = false) = this ?: default
