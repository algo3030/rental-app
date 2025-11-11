package com.example.rentalapp.ui.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.rentalapp.model.RentalStatus
import com.example.rentalapp.ui.component.RentalStatusPPP

@Composable
@Preview
private fun HomeScreenPreview(
    @PreviewParameter(RentalStatusPPP::class) status: RentalStatus
) {
    HomeScreen(
        rentalStatus = status
    )
}