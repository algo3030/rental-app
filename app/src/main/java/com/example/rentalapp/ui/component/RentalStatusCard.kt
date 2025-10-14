package com.example.rentalapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentalapp.model.fake.RentalStatusFake
import com.example.rentalapp.model.RentalStatus
import com.example.rentalapp.util.asDeadlineDisplay

@Composable
fun RentalStatusCard(
    modifier: Modifier = Modifier,
    status: RentalStatus
) {

    Column(
        modifier = modifier
            .background(Color.White)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = status.asDisplayString(),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        when(status){
            RentalStatus.Unrented -> Unit
            is RentalStatus.Renting -> {
                Text(
                    text = status.machineName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "~" + status.deadline.asDeadlineDisplay(),
                    style = MaterialTheme.typography.titleMedium
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


@Composable
@Preview
fun RentalStatusCardPreview() {
    RentalStatusCard(
        status = RentalStatusFake.rented
    )
}