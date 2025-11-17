package com.example.rentalapp.model

data class PcModel(
    val id: String,
    val modelName: String,
    val manufacturer: String?,
    val specs: Map<String, Any>?,
    val imagePath: String?
)