package ru.omgtu.ryabchenko.publications.data.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ru.omgtu.ryabchenko.publications.BuildKonfig

fun publicationHttpClient(): HttpClient = HttpClient {
    expectSuccess = true
    defaultRequest {
        url(BuildKonfig.BASE_URL)
    }
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 20_000
    }
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) = println(message)
        }
        level = LogLevel.INFO
    }
}
