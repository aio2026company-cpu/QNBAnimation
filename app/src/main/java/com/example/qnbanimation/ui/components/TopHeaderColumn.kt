package com.example.qnbanimation.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qnbanimation.ui.model.QnbCard3DModel
import com.example.qnbanimation.ui.model.Sample3DCards

@Composable
fun TopHeaderColumn(
    currentCard: QnbCard3DModel,
    isFrozen: Boolean,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        backgroundColor.copy(alpha = 0.98f),
                        backgroundColor.copy(alpha = 0.90f),
                        Color.Transparent
                    )
                )
            )
            .padding(top = 36.dp, start = 24.dp, end = 24.dp, bottom = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = currentCard.title,
                        color = Color.White,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isFrozen) Color(0xFFE55353).copy(alpha = 0.25f)
                                else Color(0xFF22C55E).copy(alpha = 0.25f)
                            )
                            .border(
                                1.dp,
                                if (isFrozen) Color(0xFFE55353) else Color(0xFF22C55E),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (isFrozen) "FROZEN" else "Active",
                            color = if (isFrozen) Color(0xFFFF8A8A) else Color(0xFF4ADE80),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Text(
                    text = currentCard.subtitle,
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 1.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopHeaderColumnPreview() {
    Box(modifier = Modifier.background(Color(0xFF0B2161))) {
        TopHeaderColumn(
            currentCard = Sample3DCards[0],
            isFrozen = false,
            backgroundColor = Color(0xFF0B2161),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
