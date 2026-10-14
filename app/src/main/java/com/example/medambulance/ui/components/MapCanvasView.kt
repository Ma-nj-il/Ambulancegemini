package com.example.medambulance.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.RedEmergency

@Composable
fun MapCanvasView(
    modifier: Modifier = Modifier,
    height: Dp = 220.dp,
    showRoute: Boolean = true,
    showAmbulancePin: Boolean = true,
    etaText: String? = "8 mins to arrive • 2.5 km away"
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 12f,
        targetValue = 28f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseRadius"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(Color(0xFFF1F5F9))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. Draw city blocks
            val blockCols = 6
            val blockRows = 4
            val cellW = w / blockCols
            val cellH = h / blockRows
            val roadMargin = 10f

            for (r in 0 until blockRows) {
                for (c in 0 until blockCols) {
                    val left = c * cellW + roadMargin / 2
                    val top = r * cellH + roadMargin / 2
                    val blockWidth = cellW - roadMargin
                    val blockHeight = cellH - roadMargin
                    drawRoundRect(
                        color = Color(0xFFE2E8F0).copy(alpha = 0.5f),
                        topLeft = Offset(left, top),
                        size = Size(blockWidth, blockHeight),
                        cornerRadius = CornerRadius(6f, 6f)
                    )
                }
            }

            // 2. Draw Bagmati River curve
            val riverPath = Path().apply {
                moveTo(0f, h * 0.75f)
                cubicTo(
                    w * 0.25f, h * 0.65f,
                    w * 0.6f, h * 0.88f,
                    w, h * 0.70f
                )
            }
            drawPath(
                path = riverPath,
                color = Color(0xFFBAE6FD).copy(alpha = 0.8f),
                style = Stroke(width = 24f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // 3. Green park area
            drawCircle(
                color = Color(0xFFDCFCE7).copy(alpha = 0.85f),
                radius = minOf(w, h) * 0.22f,
                center = Offset(w * 0.2f, h * 0.78f)
            )

            // 4. Secondary park/zone
            drawRoundRect(
                color = Color(0xFFDCFCE7).copy(alpha = 0.75f),
                topLeft = Offset(w * 0.55f, h * 0.32f),
                size = Size(w * 0.16f, h * 0.2f),
                cornerRadius = CornerRadius(12f, 12f)
            )

            // 5. Grid Road Lines
            for (r in 1 until blockRows) {
                val y = r * cellH
                drawLine(
                    color = Color.White,
                    start = Offset(0f, y),
                    end = Offset(w, y),
                    strokeWidth = 14f
                )
            }
            for (c in 1 until blockCols) {
                val x = c * cellW
                drawLine(
                    color = Color.White,
                    start = Offset(x, 0f),
                    end = Offset(x, h),
                    strokeWidth = 14f
                )
            }

            // Diagonal major avenue
            drawLine(
                color = Color.White,
                start = Offset(0f, h * 0.4f),
                end = Offset(w * 0.8f, 0f),
                strokeWidth = 12f
            )

            // 6. Stepped Emergency Route (Matching Figma Design in Screenshot 14)
            if (showRoute) {
                val startX = w * 0.25f
                val startY = h * 0.82f
                val corner1X = w * 0.28f
                val corner1Y = h * 0.55f
                val corner2X = w * 0.42f
                val corner2Y = h * 0.55f
                val corner3X = w * 0.42f
                val corner3Y = h * 0.35f
                val corner4X = w * 0.68f
                val corner4Y = h * 0.35f
                val endX = w * 0.76f
                val endY = h * 0.2f

                val routePath = Path().apply {
                    moveTo(startX, startY)
                    lineTo(corner1X, corner1Y)
                    lineTo(corner2X, corner2Y)
                    lineTo(corner3X, corner3Y)
                    lineTo(corner4X, corner4Y)
                    lineTo(endX, endY)
                }

                // Route shadow/casing
                drawPath(
                    path = routePath,
                    color = Color(0x33E53935),
                    style = Stroke(width = 10f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )

                // Route main red line
                drawPath(
                    path = routePath,
                    color = RedEmergency,
                    style = Stroke(width = 5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )

                // Pickup Pin: Black outer circle, white inner, black center dot
                drawCircle(
                    color = Color(0xFF0F172A),
                    radius = 9f,
                    center = Offset(startX, startY)
                )
                drawCircle(
                    color = Color.White,
                    radius = 5f,
                    center = Offset(startX, startY)
                )

                // Destination / Hospital Pin: Red circle with white medical cross
                drawCircle(
                    color = RedEmergency,
                    radius = 14f,
                    center = Offset(endX, endY)
                )
                // White cross
                drawLine(
                    color = Color.White,
                    start = Offset(endX - 6f, endY),
                    end = Offset(endX + 6f, endY),
                    strokeWidth = 3f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = Color.White,
                    start = Offset(endX, endY - 6f),
                    end = Offset(endX, endY + 6f),
                    strokeWidth = 3f,
                    cap = StrokeCap.Round
                )
            }

            // 7. Moving Ambulance Indicator
            if (showAmbulancePin) {
                val ambX = w * 0.42f
                val ambY = h * 0.45f

                // Pulse ring
                drawCircle(
                    color = RedEmergency.copy(alpha = pulseAlpha),
                    radius = pulseRadius,
                    center = Offset(ambX, ambY)
                )
                // Vehicle circle
                drawCircle(
                    color = Color.White,
                    radius = 12f,
                    center = Offset(ambX, ambY)
                )
                drawCircle(
                    color = RedEmergency,
                    radius = 9f,
                    center = Offset(ambX, ambY)
                )
                // Flasher dot
                drawCircle(
                    color = Color(0xFFFFEB3B),
                    radius = 3f,
                    center = Offset(ambX, ambY)
                )
            }
        }

        // Floating ETA Badge (Screenshots 3, 4, 8)
        if (etaText != null) {
            Card(
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.BottomStart),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "⏰ $etaText",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF0F172A),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                )
            }
        }
    }
}
