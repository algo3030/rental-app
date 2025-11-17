package com.example.rentalapp.ui.screen.rental

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.rentalapp.ui.screen.rental.logic.RentalScreenUiState
import kotlinx.serialization.Serializable

@Composable
@Preview
private fun Default(){
    RentalScreen(
        state = RentalScreenUiState(),
        onNavigateBack ={},
        onSubmit = {},
        onSelectPc = {},
        onStartTimeSelected = {},
        onEndTimeSelected = {}
    )
}