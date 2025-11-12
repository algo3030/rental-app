package com.example.rentalapp.ui.screen.home.logic

import com.example.rentalapp.model.RentalStatus

data class HomeScreenUiState(
    val rentalStatus: RentalStatus = RentalStatus.Unrented
)