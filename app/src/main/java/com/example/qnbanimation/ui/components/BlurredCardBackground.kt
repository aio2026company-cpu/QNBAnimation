package com.example.qnbanimation.ui.components

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qnbanimation.ui.model.QnbCard3DModel
import com.example.qnbanimation.ui.model.Sample3DCards

@Composable
fun BlurredCardBackground(
    card: QnbCard3DModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        // Base dark backdrop
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            card.primaryGradient.first(),
                            Color(0xFF070E24),
                            Color(0xFF03060F)
                        )
                    )
                )
        )

        // Scaled & Rotated Blurred Gradient Card Glow
        Box(
            modifier = Modifier
                .padding(top = 10.dp)
                .size(width = 340.dp, height = 480.dp)
                .graphicsLayer {
                    rotationZ = -14f
                    scaleX = 1.4f
                    scaleY = 1.4f
                    alpha = 0.35f
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        renderEffect = BlurEffect(60f, 60f, TileMode.Clamp)
                    }
                }
                .clip(RoundedCornerShape(32.dp))
                .background(
                    Brush.linearGradient(
                        colors = card.primaryGradient + listOf(card.accentColor.copy(alpha = 0.35f))
                    )
                )
                .border(2.dp, card.accentColor.copy(alpha = 0.35f), RoundedCornerShape(32.dp))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BlurredCardBackgroundPreview() {
    BlurredCardBackground(
        card = Sample3DCards[0],
        modifier = Modifier.size(width = 360.dp, height = 640.dp)
    )
}
