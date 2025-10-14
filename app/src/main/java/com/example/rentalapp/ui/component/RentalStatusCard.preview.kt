package com.example.rentalapp.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.example.rentalapp.model.RentalStatus
import java.time.LocalDateTime
import java.time.ZoneId

class RentalPPP : PreviewParameterProvider<RentalStatus> {
    override val values: Sequence<RentalStatus>
            = sequenceOf(
        RentalStatus.Unrented,
        RentalStatus.Renting(
            machineName = "Thinkpad p14s",
            deadline = LocalDateTime.of(2025, 10, 14, 21, 0)
                .atZone(ZoneId.of("Asia/Tokyo"))
                .toInstant()
        )
    )

}

@Composable
@Preview
fun RentalStatusCardPreview(
    @PreviewParameter(RentalPPP::class) status: RentalStatus
) {
    RentalStatusCard(
        modifier = Modifier.size(300.dp, 160.dp),
        status = status
    )
}