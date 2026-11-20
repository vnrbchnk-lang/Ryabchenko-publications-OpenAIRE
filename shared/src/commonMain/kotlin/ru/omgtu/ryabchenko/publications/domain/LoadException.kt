package ru.omgtu.ryabchenko.publications.domain

sealed class LoadException(message: String) : Exception(message) {
    data object NoConnection : LoadException("no connection")
    data object Timeout : LoadException("timeout")
    data object NotFound : LoadException("not found")
    data class Server(val status: Int) : LoadException("server $status")
    data object BadResponse : LoadException("bad response")
}
