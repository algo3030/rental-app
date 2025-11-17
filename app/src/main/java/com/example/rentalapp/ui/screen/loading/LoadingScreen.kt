package com.example.rentalapp.ui.screen.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.rentalapp.ui.designsystem.AppTheme
import com.example.rentalapp.ui.designsystem.components.progressindicators.CircularProgressIndicator
import kotlinx.serialization.Serializable

@Serializable
object LoadingScreen

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}