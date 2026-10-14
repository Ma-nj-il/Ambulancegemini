package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DriveEta
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.medambulance.api.AmbulanceApiService
import com.example.medambulance.model.AmbulanceCategory
import com.example.medambulance.model.AppLanguage
import com.example.medambulance.model.UserRole
import com.example.medambulance.ui.screens.ActiveTrackingScreen
import com.example.medambulance.ui.screens.AmbulanceSelection
import com.example.medambulance.ui.screens.BookAmbulanceScreen
import com.example.medambulance.ui.screens.BookForOthersScreen
import com.example.medambulance.ui.screens.DriverOrderScreen
import com.example.medambulance.ui.screens.EmergencyNumbersScreen
import com.example.medambulance.ui.screens.HomeScreen
import com.example.medambulance.ui.screens.MedicLoginScreen
import com.example.medambulance.ui.screens.NearbyHospitalsScreen
import com.example.medambulance.ui.screens.OfflineEmergencyScreen
import com.example.medambulance.ui.screens.OnlineBookingHomeScreen
import com.example.medambulance.ui.screens.ReactNativeExportScreen
import com.example.medambulance.ui.screens.RequestAmbulanceScreen
import com.example.medambulance.ui.screens.RideHistoryScreen
import com.example.medambulance.ui.screens.SignUpScreen
import com.example.medambulance.ui.screens.TripCompletedScreen
import com.example.medambulance.ui.screens.UserProfileScreen
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RedEmergency

enum class Screen {
    ONLINE_MAP_HOME,
    OFFLINE_EMERGENCY,
    LOGIN,
    SIGN_UP,
    DASHBOARD_HOME,
    PROFILE,
    NEARBY_HOSPITALS,
    REQUEST_AMBULANCE,
    BOOK_AMBULANCE,
    ACTIVE_TRACKING,
    DRIVER_ORDER,
    TRIP_COMPLETED,
    EMERGENCY_NUMBERS,
    BOOK_FOR_OTHERS,
    RIDE_HISTORY,
    REACT_NATIVE_EXPORT
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MedAmbulanceApp()
            }
        }
    }
}

@Composable
fun MedAmbulanceApp() {
    // Default open to Online inDrive-style Map Screen (logged in user flow per user brief)
    var currentScreen by remember { mutableStateOf(Screen.ONLINE_MAP_HOME) }
    var currentLanguage by remember { mutableStateOf(AppLanguage.ENGLISH) }
    var currentRole by remember { mutableStateOf(UserRole.CITIZEN) }
    var selectedCategory by remember { mutableStateOf(AmbulanceCategory.BASIC) }
    var selectedHospital by remember {
        mutableStateOf(AmbulanceApiService.sampleHospitals.first())
    }
    var activePickupLocation by remember { mutableStateOf("Baneshwor, Kathmandu") }
    var activeDropLocation by remember { mutableStateOf("Bhaktapur Hospital") }
    var currentBiddedFare by remember { mutableIntStateOf(1500) }

    val isNe = currentLanguage == AppLanguage.NEPALI

    // Sub-screen BackHandler routing
    if (currentScreen != Screen.ONLINE_MAP_HOME && currentScreen != Screen.LOGIN) {
        BackHandler {
            currentScreen = when (currentScreen) {
                Screen.SIGN_UP -> Screen.LOGIN
                Screen.PROFILE -> Screen.ONLINE_MAP_HOME
                Screen.OFFLINE_EMERGENCY -> Screen.ONLINE_MAP_HOME
                Screen.BOOK_AMBULANCE -> Screen.ONLINE_MAP_HOME
                Screen.ACTIVE_TRACKING -> Screen.ONLINE_MAP_HOME
                Screen.TRIP_COMPLETED -> Screen.ONLINE_MAP_HOME
                Screen.DRIVER_ORDER -> Screen.ONLINE_MAP_HOME
                else -> Screen.ONLINE_MAP_HOME
            }
        }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showSecurityDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(310.dp),
                drawerContainerColor = Color.White
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Drawer Header with User Medical Profile
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF8FAFC))
                            .padding(horizontal = 20.dp, vertical = 22.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(RedEmergency),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "MB",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "Manjil Bhandari",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = "+977 9841234567",
                                    fontSize = 12.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Medical ID Quick Tag
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFFEBEE),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFCDD2))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isNe) "🩸 रक्त समूह: O+ • मेडिकल ID सक्रिय" else "🩸 Blood: O+ • Medical ID Active",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RedEmergency
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 1. Profile Navigation Item
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = if (isNe) "मेरो प्रोफाइल र मेडिकल ID" else "Profile & Medical ID",
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        icon = { Icon(Icons.Default.Person, contentDescription = null, tint = RedEmergency) },
                        selected = currentScreen == Screen.PROFILE,
                        onClick = {
                            currentScreen = Screen.PROFILE
                            coroutineScope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    // 2. DISTINCT "SWITCH TO DRIVER MODE" BUTTON (Prominently placed below Profile)
                    Card(
                        onClick = {
                            currentScreen = Screen.DRIVER_ORDER
                            coroutineScope.launch { drawerState.close() }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .testTag("drawer_switch_driver_mode_btn"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(RedEmergency),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DriveEta,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isNe) "चालक मोडमा जानुहोस्" else "Switch to Driver Mode",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = if (isNe) "एम्बुलेन्स चालक ड्यास / अर्डर" else "Accept Emergency Dispatches",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = Color(0xFFE2E8F0))

                    // 3. Settings Item
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = if (isNe) "सेटिङहरू" else "Settings",
                                fontWeight = FontWeight.Medium
                            )
                        },
                        icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                        selected = false,
                        onClick = {
                            showSettingsDialog = true
                            coroutineScope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    // 4. Security Item
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = if (isNe) "सुरक्षा र आपतकालीन लक" else "Security",
                                fontWeight = FontWeight.Medium
                            )
                        },
                        icon = { Icon(Icons.Default.Security, contentDescription = null) },
                        selected = false,
                        onClick = {
                            showSecurityDialog = true
                            coroutineScope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    // 5. About Us Item
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = if (isNe) "हाम्रोबारे र जानकारी" else "About Us",
                                fontWeight = FontWeight.Medium
                            )
                        },
                        icon = { Icon(Icons.Default.Info, contentDescription = null) },
                        selected = false,
                        onClick = {
                            showAboutDialog = true
                            coroutineScope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize()
            // NO BOTTOM NAVIGATION BAR per instructions
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "ScreenTransition"
                ) { screen ->
                    when (screen) {
                        // Default InDrive / Uber style Online Map Screen (Clean full screen, no bottom bar)
                        Screen.ONLINE_MAP_HOME -> OnlineBookingHomeScreen(
                            currentLanguage = currentLanguage,
                            onLanguageToggle = {
                                currentLanguage = if (currentLanguage == AppLanguage.NEPALI)
                                    AppLanguage.ENGLISH else AppLanguage.NEPALI
                            },
                            onSwitchToOfflineMode = { currentScreen = Screen.OFFLINE_EMERGENCY },
                            onRequestAmbulanceNow = { selection, fare ->
                                selectedCategory = when (selection) {
                                    AmbulanceSelection.BLS -> AmbulanceCategory.BASIC
                                    AmbulanceSelection.ALS -> AmbulanceCategory.ICU
                                    AmbulanceSelection.CARDIAC -> AmbulanceCategory.VENTILATOR
                                }
                                currentBiddedFare = fare
                                currentScreen = Screen.ACTIVE_TRACKING
                            },
                            onMenuClick = { coroutineScope.launch { drawerState.open() } },
                            onProfileClick = { currentScreen = Screen.PROFILE }
                        )

                    // Offline Mode Screen (Triple-Tap SMS & Direct Call 102)
                    Screen.OFFLINE_EMERGENCY -> OfflineEmergencyScreen(
                        currentLanguage = currentLanguage,
                        onLanguageToggle = {
                            currentLanguage = if (currentLanguage == AppLanguage.NEPALI)
                                AppLanguage.ENGLISH else AppLanguage.NEPALI
                        },
                        onSwitchToOnlineMode = { currentScreen = Screen.ONLINE_MAP_HOME }
                    )

                    // Ultra-Modern Login Screen (Matching Left Mockup)
                    Screen.LOGIN -> MedicLoginScreen(
                        currentLanguage = currentLanguage,
                        onLanguageToggle = {
                            currentLanguage = if (currentLanguage == AppLanguage.NEPALI)
                                AppLanguage.ENGLISH else AppLanguage.NEPALI
                        },
                        onLoginSuccess = { currentScreen = Screen.ONLINE_MAP_HOME },
                        onNavigateToSignUp = { currentScreen = Screen.SIGN_UP },
                        onDirectGuestEmergency = { currentScreen = Screen.ONLINE_MAP_HOME }
                    )

                    Screen.SIGN_UP -> SignUpScreen(
                        currentLanguage = currentLanguage,
                        onSignUpSuccess = { currentScreen = Screen.ONLINE_MAP_HOME },
                        onNavigateToLogin = { currentScreen = Screen.LOGIN }
                    )

                    Screen.DASHBOARD_HOME -> HomeScreen(
                        currentLanguage = currentLanguage,
                        onEmergencyCallClick = { currentScreen = Screen.ONLINE_MAP_HOME },
                        onNearbyHospitalsClick = { currentScreen = Screen.NEARBY_HOSPITALS },
                        onMyRidesClick = { currentScreen = Screen.RIDE_HISTORY },
                        onEmergencyContactsClick = { currentScreen = Screen.EMERGENCY_NUMBERS },
                        onBookForOthersClick = { currentScreen = Screen.BOOK_FOR_OTHERS },
                        onSelectService = { cat ->
                            selectedCategory = cat
                            currentScreen = Screen.ONLINE_MAP_HOME
                        },
                        onProfileClick = { currentScreen = Screen.PROFILE },
                        onLanguageToggle = {
                            currentLanguage = if (currentLanguage == AppLanguage.NEPALI)
                                AppLanguage.ENGLISH else AppLanguage.NEPALI
                        },
                        onSosDispatched = { currentScreen = Screen.ACTIVE_TRACKING }
                    )

                    // User Profile Screen with Emergency Contacts, Blood Type & Allergies
                    Screen.PROFILE -> UserProfileScreen(
                        currentLanguage = currentLanguage,
                        onLanguageToggle = {
                            currentLanguage = if (currentLanguage == AppLanguage.NEPALI)
                                AppLanguage.ENGLISH else AppLanguage.NEPALI
                        },
                        onBack = { currentScreen = Screen.ONLINE_MAP_HOME }
                    )

                    Screen.NEARBY_HOSPITALS -> NearbyHospitalsScreen(
                        onBack = { currentScreen = Screen.ONLINE_MAP_HOME },
                        onBookHospitalAmbulance = { hospital ->
                            selectedHospital = hospital
                            activeDropLocation = hospital.name
                            currentScreen = Screen.BOOK_AMBULANCE
                        },
                        onEmergencyCall = { currentScreen = Screen.EMERGENCY_NUMBERS }
                    )

                    Screen.REQUEST_AMBULANCE -> RequestAmbulanceScreen(
                        initialCategory = selectedCategory,
                        initialHospital = selectedHospital,
                        onBack = { currentScreen = Screen.ONLINE_MAP_HOME },
                        onSelectDestination = { currentScreen = Screen.NEARBY_HOSPITALS },
                        onSendRequest = { cat, pickup, dest ->
                            selectedCategory = cat
                            activePickupLocation = pickup
                            activeDropLocation = dest
                            currentScreen = Screen.BOOK_AMBULANCE
                        }
                    )

                    Screen.BOOK_AMBULANCE -> BookAmbulanceScreen(
                        driver = AmbulanceApiService.sampleDriver,
                        hospital = selectedHospital,
                        category = selectedCategory,
                        onBack = { currentScreen = Screen.ONLINE_MAP_HOME },
                        onCallDriver = { /* Dial */ },
                        onConfirmBooking = { currentScreen = Screen.ACTIVE_TRACKING }
                    )

                    Screen.DRIVER_ORDER -> DriverOrderScreen(
                        onAcceptOrder = { currentScreen = Screen.ACTIVE_TRACKING },
                        onDeclineOrder = { currentScreen = Screen.ONLINE_MAP_HOME }
                    )

                    Screen.ACTIVE_TRACKING -> ActiveTrackingScreen(
                        driver = AmbulanceApiService.sampleDriver,
                        hospital = selectedHospital,
                        onBack = { currentScreen = Screen.ONLINE_MAP_HOME },
                        onCompleteTrip = { currentScreen = Screen.TRIP_COMPLETED },
                        onCancelTrip = { currentScreen = Screen.ONLINE_MAP_HOME }
                    )

                    Screen.TRIP_COMPLETED -> TripCompletedScreen(
                        onCompleteAndReturn = { currentScreen = Screen.ONLINE_MAP_HOME },
                        onViewReport = { currentScreen = Screen.RIDE_HISTORY }
                    )

                    Screen.EMERGENCY_NUMBERS -> EmergencyNumbersScreen(
                        onBack = { currentScreen = Screen.ONLINE_MAP_HOME }
                    )

                    Screen.BOOK_FOR_OTHERS -> BookForOthersScreen(
                        onBack = { currentScreen = Screen.ONLINE_MAP_HOME },
                        onBookSuccess = { currentScreen = Screen.ACTIVE_TRACKING }
                    )

                    Screen.RIDE_HISTORY -> RideHistoryScreen(
                        onBack = { currentScreen = Screen.ONLINE_MAP_HOME }
                    )

                    Screen.REACT_NATIVE_EXPORT -> ReactNativeExportScreen(
                        onBack = { currentScreen = Screen.ONLINE_MAP_HOME }
                    )
                }
            }
        }
    }
    }

    // Settings Dialog
    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = {
                Text(
                    text = if (isNe) "एप सेटिङहरू" else "Application Settings",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = if (isNe) "भाषा / Language:" else "Language:",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isNe) "नेपाली (सक्रिय)" else "English (Active)",
                        color = RedEmergency,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isNe) "आपतकालीन कम्पन (Haptic Feedback): सक्रिय" else "Emergency Haptic Feedback: Enabled",
                        fontSize = 13.sp,
                        color = Color(0xFF475569)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isNe) "अटो-अफलाइन पत्ता लगाउने: सक्रिय" else "Auto-Offline Fallback: Enabled",
                        fontSize = 13.sp,
                        color = Color(0xFF475569)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showSettingsDialog = false }) {
                    Text(text = if (isNe) "ठीक छ" else "Close", color = RedEmergency, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Security Dialog
    if (showSecurityDialog) {
        AlertDialog(
            onDismissRequest = { showSecurityDialog = false },
            title = {
                Text(
                    text = if (isNe) "सुरक्षा र PIN लक" else "Security & Privacy",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = if (isNe) "🔐 बायोमेट्रिक SOS प्रमाणीकरण: सक्रिय" else "🔐 Biometric SOS Verification: Enabled",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isNe) "📍 प्रत्यक्ष GPS इनक्रिप्सन: २६५-बिट AES इन्क्रिप्टेड" else "📍 Live GPS Telemetry: 256-bit AES Encrypted",
                        fontSize = 13.sp,
                        color = Color(0xFF475569)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isNe) "📞 आपतकालीन हटलाइन: १०२ (शुल्क-रहित)" else "📞 Dispatch Control: Toll-Free 102",
                        fontSize = 13.sp,
                        color = Color(0xFF475569)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showSecurityDialog = false }) {
                    Text(text = if (isNe) "ठीक छ" else "Close", color = RedEmergency, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // About Us Dialog
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = {
                Text(
                    text = if (isNe) "हाम्रोबारे - मेडिक एम्बुलेन्स" else "About Medic Ambulance",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "🇳🇵 MEDIC AMBULANCE NEPAL",
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        color = RedEmergency
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isNe)
                            "नेपाल सरकार स्वास्थ्य मन्त्रालयद्वारा प्रमाणित आपतकालीन एम्बुलेन्स नेटवर्क।\nलाइसेन्स: NP-MOHP-102-AMB\nसंस्करण: v2.4 (12K Ultra HD)"
                        else
                            "Certified by Ministry of Health & Population, Nepal.\nLicense: NP-MOHP-102-AMB\nVersion: v2.4 (12K Standard)",
                        fontSize = 13.sp,
                        color = Color(0xFF334155)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text(text = if (isNe) "ठीक छ" else "Close", color = RedEmergency, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
