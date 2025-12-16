package r.finance.hazino

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun <T> ReorderableList(
    items: List<T>,
    onMove: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
    key: ((T) -> Any)? = null,
    itemContent: @Composable LazyItemScope.(T) -> Unit
) {
    val listState = rememberLazyListState()
    var draggedItemIndex by remember { mutableStateOf<Int?>(null) }
    var dragOffset by remember { mutableStateOf(0f) }

    LazyColumn(
        state = listState,
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { offset ->
                        listState.layoutInfo.visibleItemsInfo
                            .firstOrNull { offset.y.toInt() in it.offset..it.offset + it.size }
                            ?.also {
                                draggedItemIndex = it.index
                            }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragOffset += dragAmount.y

                        draggedItemIndex?.let { index ->
                            val item = items[index]
                            val itemHeight = listState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }?.size ?: 0
                            val newIndex = (index + (dragOffset / itemHeight).toInt()).coerceIn(0, items.size - 1)
                            if (newIndex != index) {
                                onMove(index, newIndex)
                                draggedItemIndex = newIndex
                                dragOffset = 0f
                            }
                        }
                    },
                    onDragEnd = { draggedItemIndex = null },
                    onDragCancel = { draggedItemIndex = null }
                )
            }
    ) {
        items(items, key = key) { item ->
            val isBeingDragged = items.indexOf(item) == draggedItemIndex
            val elevation by animateDpAsState(if (isBeingDragged) 8.dp else 0.dp)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        translationY = if (isBeingDragged) dragOffset else 0f
                        shadowElevation = elevation.toPx()
                    }
            ) {
                itemContent(item)
            }
        }
    }
}
