package com.example.rentalapp.ui.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.IntSize

@Composable
fun HomeScreenLayout(
    modifier: Modifier = Modifier,
    top: @Composable () -> Unit,
    center: @Composable () -> Unit,
){
    Layout(
        modifier = modifier,
        content = {
            Box(modifier = Modifier.layoutId("top")){
                top()
            }
            Box(modifier = Modifier.layoutId("center")){
                center()
            }
        },
        measurePolicy = { measurables, constraints ->
            val topBarMeasurable = measurables.find { it.layoutId == "top" } ?: error(Unit)
            val centerMeasurable = measurables.find { it.layoutId == "center" } ?: error(Unit)

            val topConstraints = constraints.copy(
                minHeight = 0,
                minWidth = 0
            )
            val topPlaceable = topBarMeasurable.measure(topConstraints)
            val centerConstraints = constraints.copy(
                maxHeight = maxOf(0, constraints.maxHeight - topPlaceable.height * 2),
                minHeight = 0,
                minWidth = 0
            )
            val centerPlaceable = centerMeasurable.measure(centerConstraints)

            val width = maxOf(
                constraints.minWidth,
                topPlaceable.width,
                centerPlaceable.width
            )
            val height = constraints.maxHeight

            layout(width, height){
                val space = IntSize(width, height)
                val topSize = IntSize(topPlaceable.width, topPlaceable.height)
                val centerSize = IntSize(centerPlaceable.width, centerPlaceable.height)
                val topOffset = Alignment.TopCenter.align(topSize, space, layoutDirection)
                val centerOffset = Alignment.Center.align(centerSize, space, layoutDirection)

                topPlaceable.place(topOffset)
                centerPlaceable.place(centerOffset)
            }
        }
    )
}