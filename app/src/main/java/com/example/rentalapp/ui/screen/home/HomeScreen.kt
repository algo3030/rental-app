package com.example.rentalapp.ui.screen.home

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rentalapp.ui.component.RentalStatusCard
import com.example.rentalapp.ui.designsystem.AppTheme
import com.example.rentalapp.ui.designsystem.components.Button
import com.example.rentalapp.ui.designsystem.components.ButtonVariant
import com.example.rentalapp.ui.designsystem.components.Icon
import com.example.rentalapp.ui.designsystem.components.IconButton
import com.example.rentalapp.ui.designsystem.components.IconButtonVariant
import com.example.rentalapp.ui.designsystem.components.Text
import com.example.rentalapp.ui.screen.home.logic.HomeScreenUiState
import kotlinx.serialization.Serializable

@Serializable
object HomeScreen

@Composable
fun HomeScreen(
    onRefresh: () -> Unit,
    state: HomeScreenUiState
){
    HomeScreenLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.colors.background),
        top = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = "RentalApp",
                    style = AppTheme.typography.h1
                )
            }
        },
        center = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.align(Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "貸出状況",
                        style = AppTheme.typography.h3,
                        color = AppTheme.colors.textSecondary
                    )

                    IconButton(
                        variant = IconButtonVariant.Ghost,
                        onClick = onRefresh
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Refresh
                        )
                    }
                }

                RentalStatusCard(
                    modifier = Modifier.size(300.dp, 170.dp),
                    status = state.rentalStatus
                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    variant = ButtonVariant.Primary
                ) {
                    Text(
                        text = "貸出申請を行う",
                    )
                }
            }
        }
    )
}