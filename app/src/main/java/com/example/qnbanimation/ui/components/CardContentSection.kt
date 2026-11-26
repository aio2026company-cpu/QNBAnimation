package com.example.qnbanimation.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qnbanimation.ui.model.CardOperationUiModel
import com.example.qnbanimation.ui.model.QnbCard3DModel
import com.example.qnbanimation.ui.model.QuickActionIconType
import com.example.qnbanimation.ui.model.Sample3DCards

@Composable
fun CardContentSection(
    currentCard: QnbCard3DModel,
    nestedScrollConnection: NestedScrollConnection,
    modifier: Modifier = Modifier,
    cardsCount: Int = 3,
    currentIndex: Int = 0,
    onCardSelect: (Int) -> Unit = {},
    isFrozen: Boolean = false,
    onFreezeClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .nestedScroll(nestedScrollConnection),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        color = Color(0xFFF8FAFC)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Horizontal Swipe & Drag Handle Header Area
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(cardsCount, currentIndex) {
                        if (cardsCount <= 1) return@pointerInput
                        var accumulatedDragX = 0f
                        val thresholdPx = 36.dp.toPx()

                        detectHorizontalDragGestures(
                            onDragStart = { accumulatedDragX = 0f },
                            onHorizontalDrag = { change, dragAmount ->
                                change.consume()
                                accumulatedDragX += dragAmount
                            },
                            onDragEnd = {
                                if (accumulatedDragX < -thresholdPx) {
                                    // Swiped left -> Switch to next card
                                    onCardSelect((currentIndex + 1).coerceAtMost(cardsCount - 1))
                                } else if (accumulatedDragX > thresholdPx) {
                                    // Swiped right -> Switch to previous card
                                    onCardSelect((currentIndex - 1).coerceAtLeast(0))
                                }
                            }
                        )
                    }
            ) {

                // Card Page Dot Indicator
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(cardsCount) { index ->
                        val isSelected = index == currentIndex
                        val indicatorWidth by animateDpAsState(
                            targetValue = if (isSelected) 18.dp else 6.dp,
                            label = "indicator_width"
                        )
                        val indicatorColor = if (isSelected) Color(0xFF2563EB) else Color(0xFFCBD5E1)

                        Box(
                            modifier = Modifier
                                .height(6.dp)
                                .width(indicatorWidth)
                                .clip(CircleShape)
                                .background(indicatorColor)
                                .clickable { onCardSelect(index) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                QuickActionButton(
                    title = "Card Details",
                    iconType = QuickActionIconType.DETAILS,
                    onClick = {}
                )
                QuickActionButton(
                    title = "PIN Code",
                    iconType = QuickActionIconType.PIN,
                    onClick = {}
                )
                QuickActionButton(
                    title = if (isFrozen) "Unfreeze" else "Freeze",
                    iconType = QuickActionIconType.FREEZE,
                    isActive = isFrozen,
                    onClick = onFreezeClick
                )
                QuickActionButton(
                    title = "Settings",
                    iconType = QuickActionIconType.SETTINGS,
                    onClick = {}
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Recent Operations Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Operations",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "View All",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2563EB),
                    modifier = Modifier.clickable { }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Operations List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                val displayedOps = if (isFrozen) {
                    listOf(
                        CardOperationUiModel(
                            id = "frozen_op",
                            title = "Card Frozen",
                            subtitle = "Contactless & payments paused",
                            amountFormatted = "LOCKED"
                        )
                    ) + currentCard.operations
                } else {
                    currentCard.operations
                }

                items(displayedOps, key = { it.id }) { op ->
                    OperationCardItem(
                        operation = op,
                        accentColor = currentCard.accentColor
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardContentSectionPreview() {
    CardContentSection(
        currentCard = Sample3DCards[0],
        nestedScrollConnection = object : NestedScrollConnection {},
        cardsCount = 3,
        currentIndex = 0,
        onCardSelect = {},
        isFrozen = false,
        onFreezeClick = {},
        modifier = Modifier.height(500.dp)
    )
}
