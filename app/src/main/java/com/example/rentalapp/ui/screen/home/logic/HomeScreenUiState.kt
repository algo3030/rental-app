package com.example.rentalapp.ui.screen.home.logic

import com.example.rentalapp.model.RentalRequest
import java.time.Instant


data class HomeScreenUiState(
    val rentalStatus: RentalStatus = RentalStatus.NoRental,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface RentalStatus {
    data object NoRental : RentalStatus

    data class Active(
        val request: ActiveRentalDisplay
    ) : RentalStatus
}

// 画面表示用のデータクラス
data class ActiveRentalDisplay(
    val id: String,
    val pcNumber: String,
    val modelName: String,
    val manufacturer: String?,
    val imageUrl: String?,
    val startTime: Instant,
    val endTime: Instant,
    val status: DisplayStatus,
    val requestedAt: Instant
)

enum class DisplayStatus {
    PENDING,      // 受取待ち
    CHECKED_OUT,  // 貸出中
    OVERDUE       // 延滞中
}