package com.example.rentalapp.model

data class Pc(
    val id: String,
    val model: PcModel,
    val pcNumber: String,
    val serialNumber: String?,
    val status: PcStatus
)

enum class PcStatus {
    AVAILABLE,
    MAINTENANCE,
    RETIRED
}