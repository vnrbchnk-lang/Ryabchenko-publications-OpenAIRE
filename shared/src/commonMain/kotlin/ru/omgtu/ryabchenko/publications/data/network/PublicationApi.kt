package ru.omgtu.ryabchenko.publications.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.ContentConvertException
import kotlinx.serialization.SerializationException
import ru.omgtu.ryabchenko.publications.core.suspendRunCatching
import ru.omgtu.ryabchenko.publications.data.network.dto.PublicationDto
import ru.omgtu.ryabchenko.publications.data.network.dto.PublicationPageDto
import ru.omgtu.ryabchenko.publications.domain.LoadException

class PublicationApi(private val client: HttpClient) {

    suspend fun getPublicationPage(
        page: Int,
        pageSize: Int,
        title: String? = null,
        subject: String? = null,
    ): Result<PublicationPageDto> = suspendRunCatching {
        client.get("researchProducts") {
            parameter("type", TYPES)
            parameter("page", page)
            parameter("pageSize", pageSize)
            title?.let { parameter("mainTitle", it) }
            subject?.let { parameter("subjects", it) }
        }.body<PublicationPageDto>()
    }.mapFailure()

    suspend fun getPublication(id: String): Result<PublicationDto> = suspendRunCatching {
        client.get("researchProducts/$id").body<PublicationDto>()
    }.mapFailure()

    private fun <T> Result<T>.mapFailure(): Result<T> =
        if (isSuccess) this else Result.failure(exceptionOrNull()?.toLoadException() ?: LoadException.BadResponse)

    companion object {
        private const val TYPES = "publication,dataset,software"
    }
}

private fun Throwable.toLoadException(): LoadException = when (this) {
    is ResponseException -> when (response.status) {
        HttpStatusCode.NotFound -> LoadException.NotFound
        else -> LoadException.Server(response.status.value)
    }
    is HttpRequestTimeoutException,
    is ConnectTimeoutException,
    is SocketTimeoutException,
    -> LoadException.Timeout
    is ContentConvertException,
    is SerializationException,
    -> LoadException.BadResponse
    else -> LoadException.NoConnection
}
