package com.example.rentalapp.model

import java.time.Instant

data class RentalRequest(
    val id: String,
    val userId: String,
    val pc: Pc,
    val startTime: Instant,
    val endTime: Instant,
    val status: RentalRequestStatus,
    val requestedAt: Instant,
    val checkedOutAt: Instant? = null,
    val returnedAt: Instant? = null,
    val cancelledAt: Instant? = null
)

enum class RentalRequestStatus {
    PENDING,
    CHECKED_OUT,
    OVERDUE,
    RETURNED,
    CANCELLED
}