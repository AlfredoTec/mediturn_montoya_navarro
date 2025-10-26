package com.tecsup.mediturn.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TimeSlotButton(
    modifier: Modifier = Modifier,
    date: Date,
    isSelected: Boolean,
    isAvailable: Boolean = true,
    onClick: () -> Unit
) {
    val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale("es", "ES"))
    val timeFormatter = SimpleDateFormat("hh:mm a", Locale("es", "ES"))
    Card(
        modifier = modifier
            .width(110.dp)
            .clickable(enabled = isAvailable, onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                !isAvailable -> Color(0xFFF3F4F6)
                isSelected -> Color(0xFF2563EB)
                else -> Color.White
            }
        ),
        border = if (!isSelected && isAvailable) {
            BorderStroke(1.dp, Color(0xFFE5E7EB))
        } else null,
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
            // Fecha
            Text(
                text = dateFormatter.format(date),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = when {
                    !isAvailable -> Color(0xFF9CA3AF)
                    isSelected -> Color.White.copy(alpha = 0.9f)
                    else -> Color(0xFF6B7280)
                }
            )

            // Hora
            Text(
                text = timeFormatter.format(date),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = when {
                    !isAvailable -> Color(0xFF9CA3AF)
                    isSelected -> Color.White
                    else -> Color(0xFF1F2937)
                }
            )
        }
    }
}
