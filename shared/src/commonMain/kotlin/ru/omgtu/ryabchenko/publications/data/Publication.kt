package ru.omgtu.ryabchenko.publications.data

data class Publication(
    val id: String,
    val mainTitle: String,
    val authors: List<String>,
    val publicationDate: String,
    val description: String,
    val subjects: List<String>,
    val type: String,
    val publisher: String,
)
