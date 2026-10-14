package com.example.medambulance.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BluePrimaryDark
import com.example.ui.theme.RedEmergency

@Composable
fun MedicAmbulanceLogo(
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    showText: Boolean = true
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // High-fidelity stylized ambulance emblem matching the uploaded design
        Box(
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(22.dp))
                .background(Color(0xFFEFF6FF)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(6.dp)
            ) {
                // Motion speed dashes in red
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(10.dp)
                            .height(3.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(RedEmergency)
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Box(
                        modifier = Modifier
                            .width(16.dp)
                            .height(3.5.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(RedEmergency)
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Box(
                        modifier = Modifier
                            .width(8.dp)
                            .height(2.5.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(RedEmergency)
                    )
                }

                // Ambulance body container
                Box(
                    modifier = Modifier
                        .size((size.value * 0.65f).dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(RedEmergency),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Roof flasher
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFEB3B))
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))

                        // Medical Cross
                        Box(
                            modifier = Modifier.size(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(16.dp)
                                    .height(5.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(Color.White)
                            )
                            Box(
                                modifier = Modifier
                                    .width(5.dp)
                                    .height(16.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(Color.White)
                            )
                        }

                        Spacer(modifier = Modifier.height(3.dp))

                        // Wheels
                        Row {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0F172A))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0F172A))
                            )
                        }
                    }
                }
            }
        }

        if (showText) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "MEDIC",
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF1E3A8A),
                letterSpacing = 2.sp,
                fontFamily = FontFamily.SansSerif
            )
            Text(
                text = "AMBULANCE",
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = RedEmergency,
                letterSpacing = 4.sp,
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}
