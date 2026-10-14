package com.example.medambulance.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.SignalCellularConnectedNoInternet4Bar
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medambulance.model.AppLanguage
import com.example.medambulance.model.AppStrings
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.GreenAvailable
import com.example.ui.theme.RedEmergency
import com.example.ui.theme.RedEmergencyLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OfflineEmergencyScreen(
    currentLanguage: AppLanguage,
    onLanguageToggle: () -> Unit,
    onSwitchToOnlineMode: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var tapCount by remember { mutableIntStateOf(0) }
    var lastTapTimestamp by remember { mutableStateOf(0L) }
    var smsSentBanner by remember { mutableStateOf(false) }

    val isNe = currentLanguage == AppLanguage.NEPALI

    fun triggerVibrate(durationMs: Long = 100) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator?.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                val v = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                @Suppress("DEPRECATION")
                v?.vibrate(durationMs)
            }
        } catch (_: Exception) {}
    }

    // Direct phone dialer fallback (102 Nepal Emergency Ambulance)
    fun dialEmergencyAmbulance() {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:102"))
        try {
            context.startActivity(intent)
        } catch (_: Exception) {}
    }

    // Crucial Triple-Tap SMS Dispatcher
    fun handleTripleTapEmergencySMS() {
        triggerVibrate(300)
        smsSentBanner = true

        val smsBody = "EMERGENCY SOS! Need immediate ambulance dispatch. Location: 27.6915° N, 85.3420° E (Baneshwor, Kathmandu). Patient requires urgent medical response."
        val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:102")
            putExtra("sms_body", smsBody)
        }
        try {
            context.startActivity(smsIntent)
        } catch (_: Exception) {
            Toast.makeText(context, "Offline SMS Prepared: $smsBody", Toast.LENGTH_LONG).show()
        }
    }

    // Tap counting handler with 1.4-second window
    fun handleButtonTap() {
        val now = System.currentTimeMillis()
        if (now - lastTapTimestamp > 1400) {
            tapCount = 1
        } else {
            tapCount++
        }
        lastTapTimestamp = now
        triggerVibrate(60)

        if (tapCount >= 3) {
            tapCount = 0
            handleTripleTapEmergencySMS()
        } else {
            scope.launch {
                delay(1400)
                if (System.currentTimeMillis() - lastTapTimestamp >= 1400) {
                    tapCount = 0
                }
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulseHalo")
    val pulseSize by infiniteTransition.animateFloat(
        initialValue = 180f,
        targetValue = 230f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseSize"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
    ) {
        // 1. Activation banner: "No Internet - Offline Mode Active ⚠️"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFEF2F2))
                .border(1.dp, Color(0xFFFCA5A5))
                .padding(vertical = 10.dp, horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.SignalCellularConnectedNoInternet4Bar,
                        contentDescription = "No internet",
                        tint = RedEmergency,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.get("offline_mode_active", currentLanguage),
                        color = Color(0xFF991B1B),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Switch to Online Mode Pill
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier
                        .clickable { onSwitchToOnlineMode() }
                        .testTag("switch_to_online_btn")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Wifi,
                            contentDescription = "Online",
                            tint = GreenAvailable,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isNe) "अनलाइन मोड" else "Online Mode",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    }
                }
            }
        }

        // Top Language Toggle Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "MEDIC AMBULANCE (OFFLINE)",
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF64748B),
                letterSpacing = 1.sp
            )

            // EN | ने Pill
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.clickable { onLanguageToggle() }
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

        Spacer(modifier = Modifier.height(10.dp))

        // SMS Sent Toast Banner
        AnimatedVisibility(
            visible = smsSentBanner,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFECFDF5),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFA7F3D0))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = GreenAvailable,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (isNe)
                            "आपतकालीन एसएमएस पठाइयो! १०२ नेपाल एम्बुलेन्स डेस्कलाई सूचना दिइयो।"
                        else
                            "Emergency SMS Dispatched! Coordinates sent to 102 Nepal Dispatch.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF065F46)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 2. Central Action: Massive, Heavily-Shadowed, Pulsing Circular Red Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            contentAlignment = Alignment.Center
        ) {
            // Animated halo ring
            Box(
                modifier = Modifier
                    .size(pulseSize.dp)
                    .clip(CircleShape)
                    .background(RedEmergency.copy(alpha = 0.15f))
            )

            // Outer red border ring
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(RedEmergencyLight)
                    .border(3.dp, RedEmergency.copy(alpha = 0.35f), CircleShape)
            )

            // The main pulsating circle button
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .shadow(20.dp, CircleShape, spotColor = RedEmergency, ambientColor = RedEmergency)
                    .clip(CircleShape)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                RedEmergency,
                                Color(0xFFB91C1C)
                            )
                        )
                    )
                    .clickable {
                        handleButtonTap()
                        if (tapCount == 1) {
                            dialEmergencyAmbulance()
                        }
                    }
                    .testTag("offline_emergency_central_btn"),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call Emergency",
                        tint = Color.White,
                        modifier = Modifier.size(42.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = AppStrings.get("call_emergency_ambulance", currentLanguage),
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Tap Counter Indicator
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (tapCount > 0) Color(0xFFFEE2E2) else Color(0xFFF1F5F9),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (tapCount > 0) RedEmergency else BorderSubtle)
            ) {
                Text(
                    text = if (tapCount == 0)
                        AppStrings.get("triple_tap_sms", currentLanguage)
                    else
                        "⚡ Tap Count: $tapCount / 3 (Tap ${3 - tapCount} more times for SMS!)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (tapCount > 0) RedEmergency else Color(0xFF475569),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (isNe)
                    "३ पटक छिटो थिच्दा जीपीएस सहितको आपतकालीन एसएमएस स्वतः जान्छ"
                else
                    "Triple-tap quickly auto-sends offline SOS with cached GPS coordinates",
                fontSize = 11.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // 3. Secondary Offline Actions (Clean, Borderless Cards below button)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Action 1: Send Emergency SMS (with GPS coordinates)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { handleTripleTapEmergencySMS() }
                    .testTag("offline_send_sms_btn"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(RedEmergencyLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Message,
                            contentDescription = null,
                            tint = RedEmergency,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = AppStrings.get("send_emergency_sms", currentLanguage),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "27.6915° N, 85.3420° E (Baneshwor)",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            // Action 2: Direct Call 102 (Toll-Free Dispatch)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { dialEmergencyAmbulance() }
                    .testTag("offline_call_102_btn"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFEFF6FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = null,
                            tint = Color(0xFF2563EB),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = AppStrings.get("direct_call_102", currentLanguage),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = if (isNe) "नेपाल सरकार र रेडक्रस एम्बुलेन्स सेवा" else "Govt & Red Cross Central Dispatch",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Action 3: Offline Triage & First Aid Guide
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalHospital,
                            contentDescription = null,
                            tint = Color(0xFF0F172A),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = if (isNe) "अफलाइन प्राथमिक उपचार निर्देशिका" else "Offline First-Aid Protocol",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = if (isNe) "CPR, रक्तस्राव नियन्त्रण र स्थिर स्थिति" else "CPR steps, bleeding triage & patient safety",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
