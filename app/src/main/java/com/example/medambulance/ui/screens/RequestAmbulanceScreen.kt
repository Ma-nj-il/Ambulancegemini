package com.example.medambulance.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medambulance.model.AmbulanceCategory
import com.example.medambulance.model.Hospital
import com.example.medambulance.ui.components.MapCanvasView
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.RedEmergency
import com.example.ui.theme.RedEmergencyLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun RequestAmbulanceScreen(
    initialCategory: AmbulanceCategory = AmbulanceCategory.BASIC,
    initialHospital: Hospital? = null,
    onBack: () -> Unit,
    onSelectDestination: () -> Unit,
    onSendRequest: (AmbulanceCategory, String, String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf(initialCategory) }
    var pickupLocation by remember { mutableStateOf("Baneshwor, Kathmandu") }
    var destinationLocation by remember {
        mutableStateOf(initialHospital?.name ?: "Bhaktapur Hospital")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // 1. Top Bar (Matching Screenshot 14)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(RedEmergency),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MedicalServices,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Request",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            // Light gray circular placeholder
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE2E8F0))
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Map View Container with Rounded Corners (Screenshot 14)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            MapCanvasView(
                height = 200.dp,
                showRoute = true,
                showAmbulancePin = true,
                etaText = null
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 3. Route Details Card (Screenshot 14)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Pickup row
                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0F172A))
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Pickup",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = pickupLocation,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }

                // Dotted connector line
                Column(
                    modifier = Modifier.padding(start = 4.dp, top = 2.dp, bottom = 2.dp)
                ) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 2.dp)
                                .size(2.dp)
                                .background(Color(0xFFCBD5E1))
                        )
                    }
                }

                // Destination row
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.clickable { onSelectDestination() }
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Destination",
                        tint = RedEmergency,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Destination",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = destinationLocation,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 4. Ambulance Type Selector (Screenshot 14)
        Text(
            text = "AMBULANCE TYPE",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF475569),
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AmbulanceTypeCard(
                modifier = Modifier.weight(1f),
                title = "Basic",
                subtitle = "Standard\ntransport",
                isSelected = selectedCategory == AmbulanceCategory.BASIC,
                onClick = { selectedCategory = AmbulanceCategory.BASIC }
            )

            AmbulanceTypeCard(
                modifier = Modifier.weight(1f),
                title = "Advanced",
                subtitle = "Provider-\nequipped",
                isSelected = selectedCategory == AmbulanceCategory.ICU,
                onClick = { selectedCategory = AmbulanceCategory.ICU }
            )

            AmbulanceTypeCard(
                modifier = Modifier.weight(1f),
                title = "Ventilator",
                subtitle = "Where\navailable",
                isSelected = selectedCategory == AmbulanceCategory.VENTILATOR,
                onClick = { selectedCategory = AmbulanceCategory.VENTILATOR }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 5. Send Request CTA Button (Red Pill with Arrow, Screenshot 14)
        Button(
            onClick = { onSendRequest(selectedCategory, pickupLocation, destinationLocation) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("send_request_button"),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RedEmergency),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Send request",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun AmbulanceTypeCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(130.dp)
            .clickable { onClick() }
            .testTag("type_${title.lowercase()}"),
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) RedEmergencyLight else Color.White,
        border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            if (isSelected) RedEmergency else Color(0xFFE2E8F0)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.MedicalServices,
                contentDescription = title,
                tint = if (isSelected) RedEmergency else Color(0xFF1E293B),
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 14.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
