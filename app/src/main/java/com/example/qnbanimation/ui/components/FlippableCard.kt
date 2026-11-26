package com.example.qnbanimation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qnbanimation.ui.model.QnbCard3DModel
import com.example.qnbanimation.ui.model.Sample3DCards

@Composable
fun FlippableCard(
    card: QnbCard3DModel,
    isSelected: Boolean,
    isFrozen: Boolean,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val shape = RoundedCornerShape(20.dp)
    val cardAlpha = if (isFrozen) 0.65f else 1f

    Box(
        modifier = modifier
            .shadow(
                elevation = if (isSelected) 20.dp else 8.dp,
                shape = shape,
                ambientColor = card.primaryGradient.first().copy(alpha = 0.6f),
                spotColor = card.accentColor.copy(alpha = 0.4f)
            )
            .graphicsLayer {
                cameraDistance = 14f * density.density
                alpha = cardAlpha
            }
            .clip(shape)
            .background(
                brush = Brush.linearGradient(
                    colors = card.primaryGradient,
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        card.accentColor.copy(alpha = 0.65f),
                        Color.White.copy(alpha = 0.15f),
                        card.accentColor.copy(alpha = 0.3f)
                    )
                ),
                shape = shape
            )
            .padding(16.dp)
    ) {
        // Top Card Badge and Contactless Icon
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = card.badgeLabel,
                color = card.accentColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.2.sp
            )

            ContactlessIcon(accentColor = card.accentColor)
        }

        // Center Area: QNB Logo & Metallic EMV Chip
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            QnbLogoView(accentColor = card.accentColor)

            EmvChipView(accentColor = card.accentColor)
        }

        // Bottom Area: Card Number & Cardholder Name
        Column(
            modifier = Modifier.align(Alignment.BottomStart),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = card.cardNumberMasked,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )
            Text(
                text = card.cardHolder,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.2.sp
            )
        }
    }
}

@Composable
private fun QnbLogoView(accentColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Canvas(modifier = Modifier.size(18.dp)) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val r = size.width / 2f

            val path = Path().apply {
                moveTo(center.x, center.y - r)
                lineTo(center.x + r * 0.3f, center.y - r * 0.3f)
                lineTo(center.x + r, center.y)
                lineTo(center.x + r * 0.3f, center.y + r * 0.3f)
                lineTo(center.x, center.y + r)
                lineTo(center.x - r * 0.3f, center.y + r * 0.3f)
                lineTo(center.x - r, center.y)
                lineTo(center.x - r * 0.3f, center.y - r * 0.3f)
                close()
            }
            drawPath(path = path, color = accentColor)
        }

        Text(
            text = "QNB",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.5.sp
        )
    }
}

@Composable
private fun EmvChipView(accentColor: Color) {
    Box(
        modifier = Modifier
            .width(34.dp)
            .size(width = 34.dp, height = 26.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFE2E8F0),
                        Color(0xFF94A3B8)
                    )
                )
            )
            .border(1.dp, accentColor.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            drawLine(Color(0xFF475569), Offset(w * 0.35f, 0f), Offset(w * 0.35f, h), strokeWidth = 1f)
            drawLine(Color(0xFF475569), Offset(w * 0.65f, 0f), Offset(w * 0.65f, h), strokeWidth = 1f)
            drawLine(Color(0xFF475569), Offset(0f, h * 0.5f), Offset(w, h * 0.5f), strokeWidth = 1f)
        }
    }
}

@Composable
private fun ContactlessIcon(accentColor: Color) {
    Canvas(modifier = Modifier.size(18.dp)) {
        val stroke = Stroke(width = 2f, cap = StrokeCap.Round)

        drawArc(
            color = accentColor,
            startAngle = -45f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(-size.width * 0.2f, 0f),
            size = Size(size.width * 0.6f, size.height),
            style = stroke
        )
        drawArc(
            color = accentColor,
            startAngle = -45f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(size.width * 0.1f, size.height * 0.15f),
            size = Size(size.width * 0.8f, size.height * 0.7f),
            style = stroke
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FlippableCardPreview() {
    Box(
        modifier = Modifier
            .background(Color(0xFF03060F))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        FlippableCard(
            card = Sample3DCards[0],
            isSelected = true,
            isFrozen = false,
            modifier = Modifier.width(200.dp).height(270.dp)
        )
    }
}
