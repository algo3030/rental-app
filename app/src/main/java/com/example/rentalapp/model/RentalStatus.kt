package com.example.rentalapp.model

import java.time.Instant

sealed interface RentalStatus{
    data object Unrented: RentalStatus
    data class Renting(
        val machineName: String,
        val deadline: Instant
    ): RentalStatus
}