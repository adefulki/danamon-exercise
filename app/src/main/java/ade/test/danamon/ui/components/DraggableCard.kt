package ade.test.danamon.ui.components

import ade.test.danamon.domain.model.User
import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

const val ANIMATION_DURATION = 500
const val MIN_DRAG_AMOUNT = 6

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun DraggableCardComplex(
    user: User,
    cardHeight: Dp,
    isRevealed: Boolean,
    cardOffset: Float,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val transitionState = remember {
        MutableTransitionState(isRevealed).apply {
            targetState = !isRevealed
        }
    }
    val transition = updateTransition(transitionState, "cardTransition")
    val offsetTransition by transition.animateFloat(
        label = "cardOffsetTransition",
        transitionSpec = { tween(durationMillis = ANIMATION_DURATION) },
        targetValueByState = { if (isRevealed) cardOffset - offsetX else -offsetX },

        )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(cardHeight)
            .offset { IntOffset((offsetX + offsetTransition).roundToInt(), 0) }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    val original = Offset(offsetX, 0f)
                    val summed = original + Offset(x = dragAmount, y = 0f)
                    val newValue = Offset(x = summed.x.coerceIn(0f, cardOffset), y = 0f)
                    if (newValue.x >= 10) {
                        onExpand()
                        return@detectHorizontalDragGestures
                    } else if (newValue.x <= 0) {
                        onCollapse()
                        return@detectHorizontalDragGestures
                    }
                    if (change.positionChange() != Offset.Zero) change.consume()
                    offsetX = newValue.x
                }
            },
        shape = remember {
            RoundedCornerShape(0.dp)
        },
        content = { CardTitle(user) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun DraggableCard(
    user: User,
    cardHeight: Dp,
    isRevealed: Boolean,
    cardOffset: Float,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
) {
    val transitionState = remember {
        MutableTransitionState(isRevealed).apply {
            targetState = !isRevealed
        }
    }
    val transition = updateTransition(transitionState, "cardTransition")
    val offsetTransition by transition.animateFloat(
        label = "cardOffsetTransition",
        transitionSpec = { tween(durationMillis = ANIMATION_DURATION) },
        targetValueByState = { if (isRevealed) cardOffset else 30f },

        )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(cardHeight)
            .offset { IntOffset(offsetTransition.roundToInt(), 0) }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    when {
                        dragAmount >= MIN_DRAG_AMOUNT -> onExpand()
                        dragAmount < -MIN_DRAG_AMOUNT -> onCollapse()
                    }
                }
            },
        shape = remember {
            RoundedCornerShape(0.dp)
        },
        onClick = { if (isRevealed) onCollapse() else onExpand() },
        content = { CardTitle(user) }
    )
}