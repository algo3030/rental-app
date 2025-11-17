package com.example.rentalapp.ui.screen.rental

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.rentalapp.model.Pc
import com.example.rentalapp.ui.screen.rental.logic.PcDisplay

private val pc = PcDisplay(
    id = "",
    pcNumber = "PC-001",
    modelName = "Test PC",
    manufacturer = "Lenovo",
    imageUrl = ""
)

@Composable
@Preview
private fun Default(){
    PcCard(
        pc = pc,
        isSelected = false,
        onClick = {}
    )
}

@Composable
@Preview
private fun Selected(){
    PcCard(
        pc = pc,
        isSelected = true,
        onClick = {}
    )
}