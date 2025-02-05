package com.example.extendablebox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.extendablebox.ui.theme.ExtendableBoxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExtendableBoxTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    val initialWidth = 300.dp  // Initial outer box width
    var boxWidth by remember { mutableStateOf(initialWidth) }
    val minWidth = initialWidth  // Prevent shrinking below initial width
    val maxWidth = 600.dp  // Max expansion limit
    val dragSpeedFactor = 0.5f  // Control drag speed

    val animatedWidth by animateDpAsState(targetValue = boxWidth, label = "animatedWidth")

    // Base sizes for inner elements
    val baseBlueWidth = 200.dp
    val baseGreenSize = 50.dp
    val baseYellowWidth = 250.dp

    // Dynamically scale sizes while keeping proportions
    val scaledBlueWidth = animatedWidth / initialWidth * baseBlueWidth
    val scaledGreenSize = animatedWidth / initialWidth * baseGreenSize
    val scaledYellowWidth = animatedWidth / initialWidth * baseYellowWidth

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
            .padding(end = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .height(300.dp)
                .width(animatedWidth)
                .background(Color.Red)
                .border(1.dp, Color.Black)
                .align(Alignment.End) // Keeps the right side fixed

        ) {
            // Left draggable handle
            Box(
                modifier = Modifier
                    .width(20.dp)
                    .fillMaxHeight()
                    .background(Color.DarkGray)
                    .align(Alignment.CenterStart)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            val adjustedDrag = dragAmount.x.dp * dragSpeedFactor  // Slow down drag speed
                            val newWidth = (boxWidth - adjustedDrag).coerceIn(minWidth, maxWidth)
                            boxWidth = newWidth
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Resize Handle",
                    tint = Color.White
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                // Blue Box (Scales based on fixed width formula)
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .width(scaledBlueWidth)
                        .background(Color.Blue)
                ) {
                    Text(
                        text = "My name is Harshul",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White
                    )

                    // Green Box (Inside Blue Box, also scales)
                    Box(
                        modifier = Modifier
                            .size(scaledGreenSize) // Fixed scaling instead of fraction
                            .align(Alignment.Center)
                            .background(Color.Green)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Yellow Box (Scales with outer box)
                Box(
                    modifier = Modifier
                        .height(200.dp)
                        .width(scaledYellowWidth)
                        .background(Color.Yellow)
                ) {
                    Text(
                        text = "My name is Harshul",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ExtendableBoxTheme {
        Greeting("Android")
    }
}
