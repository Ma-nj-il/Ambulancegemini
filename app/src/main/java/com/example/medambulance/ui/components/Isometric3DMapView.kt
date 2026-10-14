package com.example.medambulance.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.RedEmergency

@Composable
fun Isometric3DMapView(
    modifier: Modifier = Modifier,
    onRecenterClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mapMotion")

    // Pulsing aura for user location
    val auraScale by infiniteTransition.animateFloat(
        initialValue = 18f,
        targetValue = 44f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "auraScale"
    )

    val auraAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "auraAlpha"
    )

    // Moving ambulance positions along the grid
    val ambMoveProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ambMove"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE5E7EB))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. Draw 3D Isometric / Angled Grid Blocks
            val rows = 9
            val cols = 7
            val blockW = w / cols
            val blockH = h / rows

            for (r in -2 until rows + 3) {
                for (c in -2 until cols + 3) {
                    // Isometric coordinate shift
                    val isoX = c * blockW - (r * 0.25f * blockW)
                    val isoY = r * blockH + (c * 0.15f * blockH)

                    drawRoundRect(
                        color = Color(0xFFF1F5F9),
                        topLeft = Offset(isoX, isoY),
                        size = Size(blockW * 0.72f, blockH * 0.65f),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                }
            }

            // 2. Angled Roads & Avenues (Crisp White with soft borders)
            val roadPath1 = Path().apply {
                moveTo(0f, h * 0.35f)
                lineTo(w, h * 0.82f)
            }
            drawPath(
                path = roadPath1,
                color = Color.White,
                style = Stroke(width = 38f, cap = StrokeCap.Round)
            )

            val roadPath2 = Path().apply {
                moveTo(w * 0.15f, 0f)
                lineTo(w * 0.88f, h)
            }
            drawPath(
                path = roadPath2,
                color = Color.White,
                style = Stroke(width = 38f, cap = StrokeCap.Round)
            )

            // Cross streets
            drawLine(
                color = Color.White,
                start = Offset(0f, h * 0.65f),
                end = Offset(w, h * 0.25f),
                strokeWidth = 28f
            )

            drawLine(
                color = Color.White,
                start = Offset(w * 0.7f, 0f),
                end = Offset(w * 0.2f, h),
                strokeWidth = 28f
            )

            // 3. Highlighted Golden Navigation Route (Matching Screenshot)
            val routePath = Path().apply {
                moveTo(w * 0.15f, h * 0.95f)
                lineTo(w * 0.48f, h * 0.48f)
                lineTo(w * 0.78f, h * 0.18f)
            }

            // Glow casing
            drawPath(
                path = routePath,
                color = Color(0x66F59E0B),
                style = Stroke(width = 16f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
            // Golden center route line
            drawPath(
                path = routePath,
                color = Color(0xFFF59E0B),
                style = Stroke(width = 8f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // 4. User Location Marker (Blue Dot with Pulsing Aura)
            val userPos = Offset(w * 0.48f, h * 0.48f)

            // Pulsing translucent blue aura
            drawCircle(
                color = BluePrimary.copy(alpha = auraAlpha),
                radius = auraScale,
                center = userPos
            )
            // Soft inner ring
            drawCircle(
                color = Color(0x442563EB),
                radius = 18f,
                center = userPos
            )
            // White core border
            drawCircle(
                color = Color.White,
                radius = 11f,
                center = userPos
            )
            // Deep blue center
            drawCircle(
                color = BluePrimary,
                radius = 8f,
                center = userPos
            )

            // 5. 3D Miniature Ambulance Markers on the Roads (Matching Screenshot)
            // Ambulance 1 (Moving along top avenue)
            val amb1X = w * (0.2f + 0.5f * ambMoveProgress)
            val amb1Y = h * (0.28f + 0.15f * ambMoveProgress)
            drawMiniAmbulance(amb1X, amb1Y, angleDeg = 35f)

            // Ambulance 2 (Upper right road)
            val amb2X = w * (0.75f - 0.2f * ambMoveProgress)
            val amb2Y = h * (0.22f + 0.1f * ambMoveProgress)
            drawMiniAmbulance(amb2X, amb2Y, angleDeg = -30f)

            // Ambulance 3 (Lower left road)
            val amb3X = w * (0.25f + 0.15f * ambMoveProgress)
            val amb3Y = h * (0.72f - 0.15f * ambMoveProgress)
            drawMiniAmbulance(amb3X, amb3Y, angleDeg = 45f)
        }

        // Floating Recenter Crosshair Button (Matching Screenshot)
        Surface(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 18.dp, bottom = 260.dp)
                .size(46.dp)
                .shadow(6.dp, CircleShape)
                .clickable { onRecenterClick() },
            shape = CircleShape,
            color = Color.White
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Recenter",
                    tint = Color(0xFF1E293B),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

/**
 * Draws a crisp 3D-styled miniature ambulance vehicle with flasher lightbar
 */
private fun DrawScope.drawMiniAmbulance(x: Float, y: Float, angleDeg: Float) {
    val sizeW = 34f
    val sizeH = 20f

    // Drop shadow
    drawRoundRect(
        color = Color(0x33000000),
        topLeft = Offset(x - sizeW / 2 + 3f, y - sizeH / 2 + 4f),
        size = Size(sizeW, sizeH),
        cornerRadius = CornerRadius(6f, 6f)
    )

    // Vehicle white body
    drawRoundRect(
        color = Color.White,
        topLeft = Offset(x - sizeW / 2, y - sizeH / 2),
        size = Size(sizeW, sizeH),
        cornerRadius = CornerRadius(6f, 6f)
    )

    // Red emergency side stripe
    drawRoundRect(
        color = RedEmergency,
        topLeft = Offset(x - sizeW / 2 + 4f, y - 2f),
        size = Size(sizeW - 8f, 4f),
        cornerRadius = CornerRadius(2f, 2f)
    )

    // Tiny red cross
    drawLine(
        color = RedEmergency,
        start = Offset(x - 4f, y - 4f),
        end = Offset(x - 4f, y + 4f),
        strokeWidth = 2f
    )
    drawLine(
        color = RedEmergency,
        start = Offset(x - 7f, y),
        end = Offset(x - 1f, y),
        strokeWidth = 2f
    )

    // Roof lightbar (Blue & Red flasher)
    drawRoundRect(
        color = Color(0xFF1D4ED8),
        topLeft = Offset(x + 2f, y - 6f),
        size = Size(4f, 12f),
        cornerRadius = CornerRadius(2f, 2f)
    )
    drawRoundRect(
        color = RedEmergency,
        topLeft = Offset(x + 6f, y - 6f),
        size = Size(4f, 12f),
        cornerRadius = CornerRadius(2f, 2f)
    )
}
