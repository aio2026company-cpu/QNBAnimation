package com.example.qnbanimation.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import com.example.qnbanimation.ui.components.BlurredCardBackground
import com.example.qnbanimation.ui.components.FlippableCard
import com.example.qnbanimation.ui.components.TopHeaderColumn
import com.example.qnbanimation.ui.components.CardContentSection
import com.example.qnbanimation.ui.model.QnbCard3DModel
import com.example.qnbanimation.ui.model.Sample3DCards
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun QnbWalletScreen(
    modifier: Modifier = Modifier,
    cards: List<QnbCard3DModel> = Sample3DCards
) {
    val density = LocalDensity.current

    val cardWidth = 186.dp
    val cardHeight = 265.dp
    val cardGap = 20.dp
    val sideCardScaleX = 0.88f

    val sideOffsetDp = (cardWidth / 2f) + ((cardWidth * sideCardScaleX) / 2f) + cardGap

    var selectedIndex by remember { mutableIntStateOf(0) }
    val focusIndex = remember { Animatable(selectedIndex.toFloat()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(selectedIndex) {
        focusIndex.animateTo(
            targetValue = selectedIndex.toFloat(),
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        )
    }

    // Phase 1: Rise up from bottom in exact center (X = 0 for ALL cards)
    val riseAnim = remember { Animatable(0f) }
    // Phase 2: Split out horizontally into left and right positions
    val splitAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Step 1: Rise up to center as one stacked unit (X = 0 for all cards)
        riseAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 750,
                easing = FastOutSlowInEasing
            )
        )
        // Step 2: Split out horizontally into left/right positions
        splitAnim.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        )
    }

    var frozenCardIds by remember { mutableStateOf(setOf<String>()) }
    val safeIndex = selectedIndex.coerceIn(0, cards.lastIndex)
    val currentCard = cards[safeIndex]
    val isFrozen = currentCard.id in frozenCardIds

    // Bottom Sheet Scroll Over Cards logic
    val headerHeight = 90.dp
    val carouselHeight = 295.dp
    val maxSheetOffsetPx = with(density) { 255.dp.toPx() }
    var sheetOffsetPx by remember { mutableFloatStateOf(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                if (delta < 0 && sheetOffsetPx < maxSheetOffsetPx) {
                    val newOffset = (sheetOffsetPx - delta).coerceAtMost(maxSheetOffsetPx)
                    val consumedY = newOffset - sheetOffsetPx
                    sheetOffsetPx = newOffset
                    return Offset(0f, -consumedY)
                }
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y
                if (delta > 0 && sheetOffsetPx > 0) {
                    val newOffset = (sheetOffsetPx - delta).coerceAtLeast(0f)
                    val consumedY = sheetOffsetPx - newOffset
                    sheetOffsetPx = newOffset
                    return Offset(0f, consumedY)
                }
                return Offset.Zero
            }
        }
    }

    val backgroundBg by animateColorAsState(
        targetValue = currentCard.primaryGradient.first(),
        animationSpec = tween(600),
        label = "bg_color"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        BlurredCardBackground(
            card = currentCard,
            modifier = Modifier.fillMaxSize()
        )

        // LAYER 1: Custom Drag-Gesture Card Stage with 2-Phase Rise & Split Entrance
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = headerHeight)
                .height(carouselHeight)
                .graphicsLayer {
                    clip = false
                    val progress = (sheetOffsetPx / maxSheetOffsetPx).coerceIn(0f, 1f)
                    alpha = (1f - progress * 0.45f)
                    scaleX = (1f - progress * 0.06f)
                    scaleY = (1f - progress * 0.06f)
                }
                .pointerInput(cards.size) {
                    if (cards.size <= 1) return@pointerInput
                    val dragThresholdPx = 48.dp.toPx()
                    var accumulatedDragX = 0f

                    detectHorizontalDragGestures(
                        onDragStart = { accumulatedDragX = 0f },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            accumulatedDragX += dragAmount

                            val maxIndex = (cards.lastIndex).toFloat()
                            val rawTarget = focusIndex.value - (dragAmount / with(density) { sideOffsetDp.toPx() })
                            val clampedTarget = rawTarget.coerceIn(-0.25f, maxIndex + 0.25f)

                            coroutineScope.launch {
                                focusIndex.snapTo(clampedTarget)
                            }
                        },
                        onDragEnd = {
                            val targetPage = when {
                                accumulatedDragX < -dragThresholdPx -> (selectedIndex + 1).coerceAtMost(cards.lastIndex)
                                accumulatedDragX > dragThresholdPx -> (selectedIndex - 1).coerceAtLeast(0)
                                else -> focusIndex.value.roundToInt().coerceIn(0, cards.lastIndex)
                            }
                            selectedIndex = targetPage
                            coroutineScope.launch {
                                focusIndex.animateTo(
                                    targetValue = targetPage.toFloat(),
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioNoBouncy,
                                        stiffness = Spring.StiffnessMediumLow
                                    )
                                )
                            }
                        },
                        onDragCancel = {
                            coroutineScope.launch {
                                focusIndex.animateTo(
                                    targetValue = selectedIndex.toFloat(),
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioNoBouncy,
                                        stiffness = Spring.StiffnessMediumLow
                                    )
                                )
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            val riseProgress = riseAnim.value.coerceIn(0f, 1f)
            val splitProgress = splitAnim.value.coerceIn(0f, 1f)
            val currentFocus = focusIndex.value

            cards.forEachIndexed { index, card ->
                val rel = currentFocus - index.toFloat()
                val absOffset = abs(rel)

                if (absOffset <= 1.6f) {
                    val sideFactor = absOffset.coerceIn(0f, 1f)

                    // 1. Stacked & Off-screen Bottom starting state (riseProgress = 0f)
                    val stackIndex = (index - selectedIndex).toFloat()
                    val startY = with(density) { 380.dp.toPx() + (stackIndex * 12.dp.toPx()) }
                    val startScaleY = 0.80f
                    val startScaleX = 0.80f

                    // 2. Rise to center & position cards towards bottom (centerTargetY includes +22.dp offset)
                    val centerTargetY = with(density) { 22.dp.toPx() + (sideFactor * 10.dp.toPx()) }
                    val currentY = lerp(startY, centerTargetY, riseProgress)

                    // 3. Split phase: X animates from 0f out to left/right side offset
                    val targetX = -rel * with(density) { sideOffsetDp.toPx() }
                    val finalX = lerp(0f, targetX, splitProgress)

                    val targetScaleY = lerp(1.0f, 0.8f, sideFactor)
                    val targetScaleX = lerp(1.0f, sideCardScaleX, sideFactor)

                    val finalScaleY = lerp(lerp(startScaleY, 1.0f, riseProgress), targetScaleY, splitProgress)
                    val finalScaleX = lerp(lerp(startScaleX, 1.0f, riseProgress), targetScaleX, splitProgress)

                    val edgeAlpha = (1f - ((absOffset - 1f).coerceAtLeast(0f) / 0.6f)).coerceIn(0f, 1f)
                    val finalAlpha = riseProgress * edgeAlpha

                    val cardIsFrozen = card.id in frozenCardIds
                    val isCentered = abs(currentFocus.roundToInt() - index) == 0 && absOffset < 0.35f

                    Box(
                        modifier = Modifier
                            .zIndex(100f - absOffset * 20f)
                            .graphicsLayer {
                                scaleX = finalScaleX
                                scaleY = finalScaleY
                                translationX = finalX
                                translationY = currentY
                                rotationZ = 0f
                                cameraDistance = 14f * density.density
                                alpha = finalAlpha
                            }
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                selectedIndex = index
                            },
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        FlippableCard(
                            card = card,
                            isSelected = isCentered,
                            isFrozen = cardIsFrozen,
                            modifier = Modifier
                                .width(cardWidth)
                                .height(cardHeight)
                        )
                    }
                }
            }
        }

        // LAYER 2: White Bottom Sheet (Scrolls/Slides UP OVER Cards right up to the Header)
        val currentSheetTop = headerHeight + carouselHeight - with(density) { sheetOffsetPx.toDp() }

        CardContentSection(
            currentCard = currentCard,
            cardsCount = cards.size,
            currentIndex = safeIndex,
            onCardSelect = { selectedIndex = it },
            isFrozen = isFrozen,
            onFreezeClick = {
                frozenCardIds = if (isFrozen) {
                    frozenCardIds - currentCard.id
                } else {
                    frozenCardIds + currentCard.id
                }
            },
            nestedScrollConnection = nestedScrollConnection,
            modifier = Modifier.padding(top = currentSheetTop)
        )

        // LAYER 3 (TOPMOST, zIndex = 100): Fixed Top Card Info Header Column (Always visible at top)
        TopHeaderColumn(
            currentCard = currentCard,
            isFrozen = isFrozen,
            backgroundColor = backgroundBg,
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
                .zIndex(100f)
                .align(Alignment.TopCenter)
        )
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun QnbWalletScreenPreview() {
    QnbWalletScreen()
}
