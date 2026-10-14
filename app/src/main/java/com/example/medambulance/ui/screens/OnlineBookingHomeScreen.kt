package com.example.medambulance.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WifiOff
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medambulance.model.AppLanguage
import com.example.medambulance.model.AppStrings
import com.example.medambulance.ui.components.Isometric3DMapView
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.RedEmergency
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

enum class AmbulanceSelection(val code: String, val defaultFare: Int, val eta: String) {
    BLS("BLS", 1500, "5 mins"),
    ALS("ALS", 2500, "5 mins"),
    CARDIAC("CARDIAC", 3500, "5 mins")
}

@Composable
fun OnlineBookingHomeScreen(
    currentLanguage: AppLanguage,
    onLanguageToggle: () -> Unit,
    onSwitchToOfflineMode: () -> Unit,
    onRequestAmbulanceNow: (AmbulanceSelection, Int) -> Unit,
    onMenuClick: () -> Unit,
    onProfileClick: () -> Unit = {}
) {
    var selectedType by remember { mutableStateOf(AmbulanceSelection.BLS) }
    var offeredFare by remember { mutableIntStateOf(1500) }
    var destinationQuery by remember { mutableStateOf("") }

    val isNe = currentLanguage == AppLanguage.NEPALI

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Fullscreen 3D Isometric Map
        Isometric3DMapView(
            modifier = Modifier.fillMaxSize()
        )

        // 2. Top Glassmorphic Navigation Bar (Hamburger Left + Language Toggle Right)
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                color = Color.White.copy(alpha = 0.94f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: Top-Left Hamburger Menu (Opens Drawer)
                    IconButton(
                        onClick = onMenuClick,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5F9))
                            .testTag("hamburger_menu_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Open Drawer Menu",
                            tint = Color(0xFF0F172A),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Brand Label
                    Text(
                        text = if (isNe) "मेडिक एम्बुलेन्स" else "MEDIC AMBULANCE",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = RedEmergency,
                        letterSpacing = 0.4.sp
                    )

                    // Right: Language Toggle & Offline Chip
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Offline Mode Switcher Chip
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFFEF3C7),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
                            modifier = Modifier
                                .clickable { onSwitchToOfflineMode() }
                                .testTag("offline_mode_switcher_pill")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WifiOff,
                                    contentDescription = "Offline Mode",
                                    tint = Color(0xFFD97706),
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isNe) "अफलाइन" else "Offline",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF92400E)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Glassmorphic EN | ने toggle
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White,
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                            modifier = Modifier
                                .clickable { onLanguageToggle() }
                                .testTag("lang_toggle_pill")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "EN",
                                    fontSize = 11.sp,
                                    fontWeight = if (!isNe) FontWeight.Bold else FontWeight.Normal,
                                    color = if (!isNe) RedEmergency else TextSecondary
                                )
                                Text(text = " | ", fontSize = 11.sp, color = BorderSubtle)
                                Text(
                                    text = "ने",
                                    fontSize = 11.sp,
                                    fontWeight = if (isNe) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isNe) RedEmergency else TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3. Floating Search Bar ("Where is the emergency?")
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(22.dp)),
                shape = RoundedCornerShape(22.dp),
                color = Color.White.copy(alpha = 0.96f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (destinationQuery.isBlank())
                                AppStrings.get("where_is_emergency", currentLanguage)
                            else destinationQuery,
                            color = if (destinationQuery.isBlank()) Color(0xFF94A3B8) else TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = "Scan",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // 4. inDrive / Pathao Style Floating Bottom Sheet (Matching Mockup)
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 14.dp)
            ) {
                // Grab handle
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(42.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFFCBD5E1))
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Title: "Select Ambulance Type"
                Text(
                    text = AppStrings.get("select_ambulance_type", currentLanguage),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Horizontal Carousel of Ambulance Type Cards ("Basic", "Advanced", "Ventilator")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AmbulanceCarouselCard(
                        title = if (isNe) "आधारभूत (Basic)" else "Basic",
                        subLabel = if (isNe) "अक्सिजन र प्राथमिक किट" else "Oxygen & EMT Kit",
                        eta = "4 mins",
                        price = "NPR 1,500",
                        isSelected = selectedType == AmbulanceSelection.BLS,
                        onClick = {
                            selectedType = AmbulanceSelection.BLS
                            offeredFare = 1500
                        }
                    )

                    AmbulanceCarouselCard(
                        title = if (isNe) "उन्नत (Advanced)" else "Advanced",
                        subLabel = if (isNe) "ICU र प्यारामेडिक टोली" else "ICU & Paramedic",
                        eta = "6 mins",
                        price = "NPR 2,500",
                        isSelected = selectedType == AmbulanceSelection.ALS,
                        onClick = {
                            selectedType = AmbulanceSelection.ALS
                            offeredFare = 2500
                        }
                    )

                    AmbulanceCarouselCard(
                        title = if (isNe) "भेन्टिलेटर (Ventilator)" else "Ventilator",
                        subLabel = if (isNe) "भेन्टिलेटर र मुटु विशेषज्ञ" else "Ventilator + Doctor",
                        eta = "5 mins",
                        price = "NPR 3,500",
                        isSelected = selectedType == AmbulanceSelection.CARDIAC,
                        onClick = {
                            selectedType = AmbulanceSelection.CARDIAC
                            offeredFare = 3500
                        }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Government Base Rate Information: NPR 30 per 1 KM
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF1F5F9),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 7.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isNe) "🇳🇵 सरकारी आधार दर: ने.रु. ३० / १ कि.मी." else "🇳🇵 Govt Base Rate: NPR 30 / 1 KM",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF475569)
                        )
                        Text(
                            text = if (isNe) "अनुमानित: ५ कि.मी. (ने.रु. १५०)" else "Est: 5 KM (NPR 150)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = RedEmergency
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // inDrive Fare Bidding Row: "Offer Your Fare" with [-] and [+] controls
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFF8FAFC),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = AppStrings.get("offer_your_fare", currentLanguage),
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = "NPR ${"%,d".format(offeredFare)}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Suggest a fare tag
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFEFF6FF),
                                modifier = Modifier
                                    .padding(end = 12.dp)
                                    .clickable { offeredFare = selectedType.defaultFare }
                            ) {
                                Text(
                                    text = AppStrings.get("suggest_fare", currentLanguage),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = BluePrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            // Minus button
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .border(1.dp, Color(0xFFCBD5E1), CircleShape)
                                    .clickable {
                                        if (offeredFare > 500) offeredFare -= 100
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = TextPrimary, modifier = Modifier.size(16.dp))
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Plus button
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .border(1.dp, Color(0xFFCBD5E1), CircleShape)
                                    .clickable {
                                        offeredFare += 100
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase", tint = TextPrimary, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Massive Glowing Red CTA Button (Matching Mockup)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 14.dp,
                            shape = RoundedCornerShape(28.dp),
                            spotColor = RedEmergency,
                            ambientColor = RedEmergency
                        )
                ) {
                    Button(
                        onClick = { onRequestAmbulanceNow(selectedType, offeredFare) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("request_ambulance_now_glow_btn"),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RedEmergency)
                    ) {
                        Text(
                            text = AppStrings.get("request_ambulance_now", currentLanguage),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

@Composable
fun AmbulanceCarouselCard(
    title: String,
    subLabel: String,
    eta: String,
    price: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(135.dp)
            .clickable { onClick() }
            .testTag("carousel_card_${title.take(3)}"),
        shape = RoundedCornerShape(18.dp),
        color = if (isSelected) Color(0xFFFFEBEE) else Color.White,
        border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            if (isSelected) RedEmergency else Color(0xFFE2E8F0)
        ),
        shadowElevation = if (isSelected) 2.dp else 1.dp
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Ambulance Model Graphic simulation
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) Color.White else Color(0xFFF1F5F9)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.MedicalServices,
                        contentDescription = title,
                        tint = if (isSelected) RedEmergency else Color(0xFF1E293B),
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "102 VIP",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) RedEmergency else Color(0xFF64748B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = 2,
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "🕒 $eta",
                fontSize = 11.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = price,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (isSelected) RedEmergency else Color(0xFF0F172A)
            )
        }
    }
}
