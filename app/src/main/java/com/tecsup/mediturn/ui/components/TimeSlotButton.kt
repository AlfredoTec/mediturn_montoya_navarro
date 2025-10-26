package com.tecsup.mediturn.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mediturn.ui.theme.MediTurnTheme
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TimeSlotButton(
    modifier: Modifier = Modifier,
    date: Date,
    isSelected: Boolean,
    isAvailable: Boolean = true,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale("es", "ES"))
    val timeFormatter = SimpleDateFormat("hh:mm a", Locale("es", "ES"))

    val containerColor = when {
        !isAvailable -> colors.surfaceVariant
        isSelected -> colors.primary
        else -> colors.surface
    }

    val borderColor = when {
        !isAvailable -> colors.outlineVariant
        else -> colors.outline
    }

    val dateTextColor = when {
        !isAvailable -> colors.outlineVariant
        isSelected -> colors.onPrimary.copy(alpha = 0.9f)
        else -> colors.onSurfaceVariant
    }

    val timeTextColor = when {
        !isAvailable -> colors.outlineVariant
        isSelected -> colors.onPrimary
        else -> colors.onSurface
    }

    Card(
        modifier = modifier
            .width(120.dp)
            .clickable(enabled = isAvailable, onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = if (!isSelected && isAvailable) BorderStroke(1.dp, borderColor) else null,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = dateFormatter.format(date),
                style = typography.labelSmall.copy(color = dateTextColor)
            )

            Text(
                text = timeFormatter.format(date),
                style = typography.titleMedium.copy(color = timeTextColor)
            )
        }
    }
}