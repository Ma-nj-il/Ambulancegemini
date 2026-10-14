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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import com.example.medambulance.api.AmbulanceApiService
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medambulance.model.AmbulanceCategory
import com.example.medambulance.model.AppLanguage
import com.example.medambulance.ui.components.EmergencySosDialog
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.GreenAvailable
import com.example.ui.theme.GreenLight
import com.example.ui.theme.RedEmergency
import com.example.ui.theme.RedEmergencyLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    currentLanguage: AppLanguage,
    onEmergencyCallClick: () -> Unit,
    onNearbyHospitalsClick: () -> Unit,
    onMyRidesClick: () -> Unit,
    onEmergencyContactsClick: () -> Unit,
    onBookForOthersClick: () -> Unit,
    onSelectService: (AmbulanceCategory) -> Unit,
    onProfileClick: () -> Unit,
    onLanguageToggle: () -> Unit,
    onSosDispatched: () -> Unit = onEmergencyCallClick
) {
    val isNe = currentLanguage == AppLanguage.NEPALI
    var showSosCountdownDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9FAFB))
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
        // 1. Top Header (Screenshot Page 1)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MedicalServices,
                        contentDescription = "Logo",
                        tint = RedEmergency,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isNe) "मेडि-एम्बुलेन्स" else "Med-Ambulance",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = if (isNe) "तपाईंको वर्तमान स्थान (काठमाडौँ)" else "Your Current Location (Kathmandu)",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Language toggle pill
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.clickable { onLanguageToggle() }
                ) {
                    Text(
                        text = if (isNe) "🇳🇵 ने" else "🇺🇸 EN",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                IconButton(
                    onClick = onProfileClick,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, BorderSubtle, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Call Emergency Ambulance Hero Card (Exact replica from Screenshot Page 1)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .clickable { onEmergencyCallClick() }
                .testTag("emergency_hero_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Red circular emblem with ambulance cross icon
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(RedEmergency),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MedicalServices,
                        contentDescription = "Ambulance",
                        tint = Color.White,
                        modifier = Modifier.size(38.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = if (isNe) "आपतकालीन एम्बुलेन्स बोलाउनुहोस्" else "Call Emergency Ambulance",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = RedEmergency,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = if (isNe) "Call Emergency Ambulance" else "Instant 1-Tap Hospital Dispatch",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Green status chip: "५ एम्बुलेन्स उपलब्ध छन्"
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = GreenLight
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(GreenAvailable)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isNe) "५ एम्बुलेन्स उपलब्ध छन्" else "5 Ambulances Available",
                            color = Color(0xFF065F46),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 3. 2x2 Quick Action Grid (Screenshot Page 1)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.LocalHospital,
                title = if (isNe) "नजिकैको अस्पताल" else "Nearby Hospitals",
                subtitle = if (isNe) "Nearby Hospitals" else "5 Hospitals Close",
                onClick = onNearbyHospitalsClick
            )
            QuickActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.AccessTime,
                title = if (isNe) "मेरो यात्राहरू" else "My Rides",
                subtitle = if (isNe) "My Rides" else "Ride History",
                onClick = onMyRidesClick
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Phone,
                title = if (isNe) "आपतकालीन सम्पर्कहरू" else "Emergency Contacts",
                subtitle = if (isNe) "Emergency Contacts" else "911 / 102 Hotlines",
                onClick = onEmergencyContactsClick
            )
            QuickActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.HelpOutline,
                title = if (isNe) "अरुका लागि बुक गर्नुहोस" else "Book for Someone Else",
                subtitle = if (isNe) "Help" else "Family / Patient",
                onClick = onBookForOthersClick
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Medical ID & Profile Preview Card
        val userProfile = AmbulanceApiService.currentUserProfile
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .clickable { onProfileClick() }
                .testTag("dashboard_medical_id_card"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(RedEmergencyLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bloodtype,
                            contentDescription = "Medical ID",
                            tint = RedEmergency,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isNe) "मेरो मेडिकल प्रोफाइल" else "Medical Profile & ID",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = RedEmergencyLight
                            ) {
                                Text(
                                    text = userProfile.bloodGroup.display,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = RedEmergency,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "${userProfile.emergencyContacts.size} ${if (isNe) "सम्पर्क" else "Contacts"} • ${userProfile.allergies.size} ${if (isNe) "एलर्जी रेकर्ड" else "Allergies"}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFEFF6FF)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            tint = BluePrimary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isNe) "हेर्नुहोस्" else "View / Edit",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BluePrimary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 4. Ambulance Services Header
        Text(
            text = if (isNe) "एम्बुलेन्स सेवाहरू / Services" else "Ambulance Services / Types",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Basic Ambulance Card
        ServiceRowCard(
            category = AmbulanceCategory.BASIC,
            title = if (isNe) "सामान्य एम्बुलेन्स" else "Basic Ambulance",
            subtitle = if (isNe) "Basic Ambulance" else "Standard medical transit",
            fare = "रु. १००",
            time = if (isNe) "१०-१५ मिनेट" else "10-15 mins",
            onClick = { onSelectService(AmbulanceCategory.BASIC) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ICU Ambulance Card
        ServiceRowCard(
            category = AmbulanceCategory.ICU,
            title = if (isNe) "आईसीयू एम्बुलेन्स" else "ICU Ambulance",
            subtitle = if (isNe) "ICU Ambulance" else "Advanced life support",
            fare = "रु. २००",
            time = if (isNe) "१५-२० मिनेट" else "15-20 mins",
            onClick = { onSelectService(AmbulanceCategory.ICU) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Ventilator Ambulance Card
        ServiceRowCard(
            category = AmbulanceCategory.VENTILATOR,
            title = if (isNe) "भेन्टिलेटर एम्बुलेन्स" else "Ventilator Ambulance",
            subtitle = if (isNe) "Ventilator Life Support" else "Critical emergency care",
            fare = "रु. ३५०",
            time = if (isNe) "२०-२५ मिनेट" else "20-25 mins",
            onClick = { onSelectService(AmbulanceCategory.VENTILATOR) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 5. Recent Rides (Screenshot Page 1)
        Text(
            text = if (isNe) "पछिल्ला यात्राहरू / Recent Rides" else "Recent Rides",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Spacer(modifier = Modifier.height(10.dp))

        RecentRideCard(
            destination = if (isNe) "बिर अस्पताल" else "Bir Hospital",
            dateTime = if (isNe) "आज, ११:३० बिहान" else "Today, 11:30 AM",
            statusText = if (isNe) "पूरा भयो" else "Completed"
        )

        Spacer(modifier = Modifier.height(8.dp))

        RecentRideCard(
            destination = if (isNe) "पाटन अस्पताल" else "Patan Hospital",
            dateTime = if (isNe) "हिजो, ३:२० दिउँसो" else "Yesterday, 3:20 PM",
            statusText = if (isNe) "पूरा भयो" else "Completed"
        )

        Spacer(modifier = Modifier.height(80.dp))
    }

    // Emergency SOS Floating Action Button (Screenshot & User Brief)
    ExtendedFloatingActionButton(
        onClick = { showSosCountdownDialog = true },
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(end = 16.dp, bottom = 16.dp)
            .testTag("sos_emergency_fab"),
        containerColor = RedEmergency,
        contentColor = Color.White,
        shape = RoundedCornerShape(28.dp),
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Emergency,
            contentDescription = "Emergency SOS",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (isNe) "🚨 आपतकालीन SOS" else "🚨 SOS DISPATCH",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            letterSpacing = 0.5.sp
        )
    }

    // 3-Second Safety Countdown Modal
    if (showSosCountdownDialog) {
        EmergencySosDialog(
            currentLanguage = currentLanguage,
            onDismiss = { showSosCountdownDialog = false },
            onConfirmedDispatch = {
                showSosCountdownDialog = false
                onSosDispatched()
            }
        )
    }
}
}

@Composable
fun QuickActionCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(115.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = RedEmergency,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = 1
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = TextSecondary,
                maxLines = 1
            )
        }
    }
}

@Composable
fun ServiceRowCard(
    category: AmbulanceCategory,
    title: String,
    subtitle: String,
    fare: String,
    time: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(RedEmergencyLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MedicalServices,
                        contentDescription = title,
                        tint = RedEmergency,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "Time",
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = time,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            Text(
                text = fare,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = RedEmergency
            )
        }
    }
}

@Composable
fun RecentRideCard(
    destination: String,
    dateTime: String,
    statusText: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = destination,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = dateTime,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            Text(
                text = statusText,
                color = GreenAvailable,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            )
        }
    }
}
