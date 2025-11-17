package com.example.rentalapp.ui.screen.rental

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.rentalapp.ui.designsystem.AppTheme
import com.example.rentalapp.ui.designsystem.components.Text
import com.example.rentalapp.ui.designsystem.components.card.Card
import com.example.rentalapp.ui.designsystem.components.card.CardDefaults
import com.example.rentalapp.ui.screen.rental.logic.PcDisplay

@Composable
fun PcCard(
    pc: PcDisplay,
    isSelected: Boolean,
    onClick: () -> Unit
){
    Card(
        onClick = onClick,
        border = if (isSelected) {
            BorderStroke(4.dp, AppTheme.colors.onSurface)
        } else null,
        colors = CardDefaults.cardColors(
            containerColor = if(isSelected) AppTheme.colors.surface
            else AppTheme.colors.disabled,
        ),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AsyncImage(
                model = pc.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp),
                contentScale = ContentScale.Crop
            )

            Column {
                Text(
                    text = pc.modelName,
                    style = AppTheme.typography.h2
                )
                pc.manufacturer?.let {
                    Text(
                        text = it,
                        style = AppTheme.typography.h3,
                        color = AppTheme.colors.textSecondary
                    )
                }
                Text(
                    text = pc.pcNumber,
                    style = AppTheme.typography.h3,
                    color = AppTheme.colors.textSecondary
                )
            }
        }
    }
}