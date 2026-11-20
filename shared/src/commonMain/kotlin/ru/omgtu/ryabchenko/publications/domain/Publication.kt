package ru.omgtu.ryabchenko.publications.domain

data class Publication(
    val id: String,
    val mainTitle: String,
    val authors: List<String>,
    val publicationDate: String,
    val description: String,
    val subjects: List<String>,
    val type: PublicationType,
    val publisher: String,
)
