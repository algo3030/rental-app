package com.example.rentalapp.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.rentalapp.ui.screen.home.logic.ActiveRentalDisplay
import com.example.rentalapp.ui.screen.home.logic.DisplayStatus
import com.example.rentalapp.ui.screen.home.logic.RentalStatus
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class RentalStatusPPP : PreviewParameterProvider<RentalStatus> {
    override val values: Sequence<RentalStatus>
            = sequenceOf(
        RentalStatus.NoRental,

        // 受取待ち
        RentalStatus.Active(
            request = ActiveRentalDisplay(
                id = "1",
                pcNumber = "PC-001",
                modelName = "Surface Laptop Go 2",
                manufacturer = "Microsoft",
                imageUrl = "",
                startTime = Instant.parse("2025-11-18T10:00:00Z"),
                endTime = Instant.parse("2025-11-18T15:00:00Z"),
                status = DisplayStatus.PENDING,
                requestedAt = Instant.parse("2025-11-17T14:00:00Z")
            )
        ),

        // レンタル中
        RentalStatus.Active(
            request = ActiveRentalDisplay(
                id = "2",
                pcNumber = "PC-002",
                modelName = "IdeaPad Slim 370i",
                manufacturer = "Lenovo",
                imageUrl = "",
                startTime = Instant.parse("2025-11-17T09:00:00Z"),
                endTime = Instant.parse("2025-11-20T17:00:00Z"),
                status = DisplayStatus.CHECKED_OUT,
                requestedAt = Instant.parse("2025-11-16T12:00:00Z")
            )
        ),

        // 延滞中
        RentalStatus.Active(
            request = ActiveRentalDisplay(
                id = "3",
                pcNumber = "PC-003",
                modelName = "Spectre x360",
                manufacturer = "HP",
                imageUrl = "",
                startTime = Instant.parse("2025-11-10T10:00:00Z"),
                endTime = Instant.parse("2025-11-15T18:00:00Z"),
                status = DisplayStatus.OVERDUE,
                requestedAt = Instant.parse("2025-11-09T16:00:00Z")
            )
        )
    )
}

@Composable
@Preview
private fun RentalStatusCardPreview(
    @PreviewParameter(RentalStatusPPP::class) status: RentalStatus
) {
    RentalStatusCard(
        status = status
    )
}