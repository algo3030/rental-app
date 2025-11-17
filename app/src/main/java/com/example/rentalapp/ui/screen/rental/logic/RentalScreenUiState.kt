package com.example.rentalapp.ui.screen.rental.logic

import java.time.Instant

data class RentalScreenUiState(
    val availablePcs: List<PcDisplay> = emptyList(),
    val selectedPcId: String? = null,
    val startTime: Instant? = null,
    val endTime: Instant? = null,
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val isRequestCompleted: Boolean = false,
    val error: String? = null
) {
    // 申請ボタンを有効にする条件
    val canSubmit: Boolean
        get() = selectedPcId != null
                && startTime != null
                && endTime != null
                && !isSubmitting
                && !isLoading
}

// 画面表示用のPCデータ
data class PcDisplay(
    val id: String,
    val pcNumber: String,
    val modelName: String,
    val manufacturer: String?,
    val imageUrl: String?
)