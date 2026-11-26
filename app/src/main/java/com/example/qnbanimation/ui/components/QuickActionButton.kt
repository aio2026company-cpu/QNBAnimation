package com.example.qnbanimation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qnbanimation.ui.model.QuickActionIconType

@Composable
fun QuickActionButton(
    title: String,
    iconType: QuickActionIconType,
    isActive: Boolean = false,
    onClick: () -> Unit
) {
    val bgColor = if (isActive) Color(0xFFFEE2E2) else Color(0xFFEFF6FF)
    val iconColor = if (isActive) Color(0xFFEF4444) else Color(0xFF3B82F6)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(22.dp)) {
                val w = size.width
                val h = size.height

                when (iconType) {
                    QuickActionIconType.DETAILS -> {
                        drawArc(
                            color = iconColor,
                            startAngle = 0f,
                            sweepAngle = 180f,
                            useCenter = false,
                            topLeft = Offset(0f, h * 0.1f),
                            size = Size(w, h * 0.8f),
                            style = Stroke(2f)
                        )
                        drawArc(
                            color = iconColor,
                            startAngle = 180f,
                            sweepAngle = 180f,
                            useCenter = false,
                            topLeft = Offset(0f, h * 0.1f),
                            size = Size(w, h * 0.8f),
                            style = Stroke(2f)
                        )
                        drawCircle(color = iconColor, radius = w * 0.2f, center = Offset(w / 2f, h / 2f))
                    }
                    QuickActionIconType.PIN -> {
                        drawRoundRect(
                            color = iconColor,
                            topLeft = Offset(w * 0.2f, h * 0.4f),
                            size = Size(w * 0.6f, h * 0.55f),
                            cornerRadius = CornerRadius(4f),
                            style = Stroke(2f)
                        )
                        drawArc(
                            color = iconColor,
                            startAngle = 180f,
                            sweepAngle = 180f,
                            useCenter = false,
                            topLeft = Offset(w * 0.3f, h * 0.1f),
                            size = Size(w * 0.4f, h * 0.6f),
                            style = Stroke(2f)
                        )
                    }
                    QuickActionIconType.FREEZE -> {
                        val c = Offset(w / 2f, h / 2f)
                        drawLine(iconColor, Offset(c.x, 0f), Offset(c.x, h), strokeWidth = 2f)
                        drawLine(iconColor, Offset(0f, c.y), Offset(w, c.y), strokeWidth = 2f)
                        drawLine(iconColor, Offset(w * 0.15f, h * 0.15f), Offset(w * 0.85f, h * 0.85f), strokeWidth = 2f)
                        drawLine(iconColor, Offset(w * 0.85f, h * 0.15f), Offset(w * 0.15f, h * 0.85f), strokeWidth = 2f)
                    }
                    QuickActionIconType.SETTINGS -> {
                        drawLine(iconColor, Offset(w * 0.2f, h * 0.2f), Offset(w * 0.8f, h * 0.2f), strokeWidth = 2f)
                        drawCircle(iconColor, radius = 3f, center = Offset(w * 0.4f, h * 0.2f))

                        drawLine(iconColor, Offset(w * 0.2f, h * 0.5f), Offset(w * 0.8f, h * 0.5f), strokeWidth = 2f)
                        drawCircle(iconColor, radius = 3f, center = Offset(w * 0.65f, h * 0.5f))

                        drawLine(iconColor, Offset(w * 0.2f, h * 0.8f), Offset(w * 0.8f, h * 0.8f), strokeWidth = 2f)
                        drawCircle(iconColor, radius = 3f, center = Offset(w * 0.35f, h * 0.8f))
                    }
                }
            }
        }

        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF475569)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuickActionButtonPreview() {
    Row(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        QuickActionButton(
            title = "Details",
            iconType = QuickActionIconType.DETAILS,
            onClick = {}
        )
        QuickActionButton(
            title = "Freeze",
            iconType = QuickActionIconType.FREEZE,
            isActive = true,
            onClick = {}
        )
    }
}
