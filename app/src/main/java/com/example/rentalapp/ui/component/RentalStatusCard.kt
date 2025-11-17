package com.example.rentalapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.rentalapp.ui.designsystem.AppTheme
import com.example.rentalapp.ui.designsystem.components.Text
import com.example.rentalapp.ui.screen.home.logic.DisplayStatus
import com.example.rentalapp.ui.screen.home.logic.RentalStatus
import com.example.rentalapp.util.asDeadlineDisplay
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun RentalStatusCard(
    modifier: Modifier = Modifier,
    status: RentalStatus
) {
    Column(
        modifier = modifier
            .background(
                color = status.toColor(),
                shape = RoundedCornerShape(10.dp)
            )
            .size(width = 400.dp, height = 220.dp)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = status.asDisplayString(),
            style = AppTheme.typography.h1
        )

        when (status) {
            is RentalStatus.NoRental -> Unit
            is RentalStatus.Active -> {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // PC画像
                    status.request.imageUrl?.let { imageUrl ->
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(AppTheme.colors.background),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Column {
                        // PC情報
                        Text(
                            text = status.request.modelName,
                            style = AppTheme.typography.h2
                        )

                        Text(
                            text = "PC番号: ${status.request.pcNumber}",
                            style = AppTheme.typography.label1
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 貸出期間
                RentalPeriodInfo(
                    startTime = status.request.startTime,
                    endTime = status.request.endTime
                )
            }
        }
    }
}

@Composable
fun RentalStatus.asDisplayString(): String = when (this) {
    is RentalStatus.NoRental -> "未レンタル"
    is RentalStatus.Active -> when (this.request.status) {
        DisplayStatus.PENDING -> "受取待ち"
        DisplayStatus.CHECKED_OUT -> "レンタル中"
        DisplayStatus.OVERDUE -> "延滞中"
    }
}

fun RentalStatus.toColor(): Color = when (this) {
    is RentalStatus.NoRental -> Color(0xFFE0E0E0) // グレー
    is RentalStatus.Active -> when (this.request.status) {
        DisplayStatus.PENDING -> Color(0xFFFFF9C4)
        DisplayStatus.CHECKED_OUT -> Color(0xFFC8E6C9)
        DisplayStatus.OVERDUE -> Color(0xFFFFCDD2)
    }
}

@Composable
private fun RentalPeriodInfo(
    startTime: Instant,
    endTime: Instant
) {
    val formatter = remember {
        DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
            .withZone(ZoneId.systemDefault())
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "貸出期間",
            style = AppTheme.typography.label3,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = formatter.format(startTime),
                style = AppTheme.typography.body2
            )
            Text(
                modifier = Modifier.padding(horizontal = 5.dp),
                text = "―",
                style = AppTheme.typography.body2
            )
            Text(
                text = formatter.format(endTime),
                style = AppTheme.typography.body2
            )
        }
    }
}