package com.example.rentalapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.rentalapp.model.RentalStatus
import com.example.rentalapp.ui.designsystem.AppTheme
import com.example.rentalapp.ui.designsystem.components.Text
import com.example.rentalapp.util.asDeadlineDisplay

@Composable
fun RentalStatusCard(
    modifier: Modifier = Modifier,
    status: RentalStatus
) {
    Column(
        modifier = modifier
            .background(
                color = Color.White,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = status.asDisplayString(),
            style = AppTheme.typography.h1
        )

        when (status) {
            RentalStatus.Unrented -> Unit
            is RentalStatus.Renting -> {
                Text(
                    text = status.machineName,
                    style = AppTheme.typography.label2
                )
                Text(
                    text = "~" + status.deadline.asDeadlineDisplay(),
                    style = AppTheme.typography.label3
                )
            }
        }
    }
}

@Composable
fun RentalStatus.asDisplayString(): String = when (this) {
    is RentalStatus.Renting -> "レンタル中"
    RentalStatus.Unrented -> "未レンタル"
}