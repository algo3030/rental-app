package com.example.rentalapp.ui.screen.rental

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.rentalapp.ui.designsystem.AppTheme
import com.example.rentalapp.ui.designsystem.components.Button
import com.example.rentalapp.ui.designsystem.components.Text
import com.example.rentalapp.ui.designsystem.components.progressindicators.CircularProgressIndicator
import com.example.rentalapp.ui.screen.rental.logic.RentalScreenUiState
import com.example.rentalapp.ui.screen.rental.logic.RentalScreenViewModel
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
object RentalScreen

@Composable
fun RentalScreen(
    state: RentalScreenUiState,
    onNavigateBack: () -> Unit,
    onSelectPc: (id: String) -> Unit,
    onStartTimeSelected: (Instant) -> Unit,
    onEndTimeSelected: (Instant) -> Unit,
    onSubmit: () -> Unit
){
    LaunchedEffect(state.isRequestCompleted) {
        if (state.isRequestCompleted) onNavigateBack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // PC選択セクション
        Text(
            text = "PCを選択",
            style = AppTheme.typography.h1
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.availablePcs) { pc ->
                PcCard(
                    pc = pc,
                    isSelected = pc.id == state.selectedPcId,
                    onClick = {
                        onSelectPc(pc.id)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 日時選択セクション
        Text(
            text = "貸出期間を選択",
            style = AppTheme.typography.h2
        )
        Spacer(modifier = Modifier.height(8.dp))

        DateTimeSelector(
            startTime = state.startTime,
            endTime = state.endTime,
            onStartTimeSelected = onStartTimeSelected,
            onEndTimeSelected = onEndTimeSelected
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 申請ボタン
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.8f),
            enabled = state.canSubmit
        ) {
            if (state.isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = AppTheme.colors.onPrimary
                )
            } else {
                Text("申請する")
            }
        }
    }
}