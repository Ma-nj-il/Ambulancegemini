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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medambulance.api.AmbulanceApiService
import com.example.medambulance.model.AppLanguage
import com.example.medambulance.model.UserRole
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.GreenAvailable
import com.example.ui.theme.RedEmergency
import com.example.ui.theme.RedEmergencyLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    currentLanguage: AppLanguage,
    onLanguageToggle: () -> Unit,
    onLoginSuccess: (UserRole) -> Unit,
    onNavigateToSignUp: () -> Unit,
    onGuestEmergencyAccess: () -> Unit
) {
    var phoneNumber by remember { mutableStateOf("9841234567") }
    var otpCode by remember { mutableStateOf("") }
    var isOtpSent by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedRole by remember { mutableStateOf(UserRole.CITIZEN) }
    val scope = rememberCoroutineScope()

    val isNe = currentLanguage == AppLanguage.NEPALI

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8FAFC)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Top Language Toggle & Emergency Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = RedEmergencyLight,
                    modifier = Modifier.clickable { onGuestEmergencyAccess() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Emergency,
                            contentDescription = "Emergency",
                            tint = RedEmergency,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isNe) "द्रुत आपतकालीन पहुँच" else "Instant Emergency Access",
                            color = RedEmergency,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.clickable { onLanguageToggle() }
                ) {
                    Text(
                        text = if (isNe) "🇳🇵 नेपाली (EN)" else "🇺🇸 English (ने)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App Brand Logo
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(RedEmergencyLight)
                    .border(2.dp, RedEmergency.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalHospital,
                    contentDescription = "Med-Ambulance",
                    tint = RedEmergency,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isNe) "मेडि-एम्बुलेन्स नेपाल" else "Med-Ambulance Nepal",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = if (isNe)
                    "नेपालभर आपतकालीन एम्बुलेन्स सेवा"
                else
                    "Fast, transparent & reliable ambulance service",
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Role Selector Chips
            Text(
                text = if (isNe) "तपाईंको भूमिका छान्नुहोस्:" else "Select Your Profile:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    UserRole.CITIZEN to (if (isNe) "नागरिक (Citizen)" else "Patient / Citizen"),
                    UserRole.DRIVER to (if (isNe) "चालक (Driver)" else "Driver"),
                    UserRole.HOSPITAL_ADMIN to (if (isNe) "अस्पताल (Admin)" else "Hospital")
                ).forEach { (role, label) ->
                    val isSelected = selectedRole == role
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) RedEmergency else Color.White,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) RedEmergency else BorderSubtle
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedRole = role }
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) Color.White else TextPrimary,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Phone Input
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text(if (isNe) "फोन नम्बर (Phone Number)" else "Phone Number") },
                leadingIcon = {
                    Text(
                        text = "🇳🇵 +977",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        modifier = Modifier.padding(start = 12.dp, end = 4.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("phone_input"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RedEmergency,
                    unfocusedBorderColor = BorderSubtle,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true
            )

            // OTP Input (if requested)
            if (isOtpSent) {
                Spacer(modifier = Modifier.height(14.dp))
                OutlinedTextField(
                    value = otpCode,
                    onValueChange = { otpCode = it },
                    label = { Text(if (isNe) "४-अङ्कको ओटीपी कोड (OTP: 1234)" else "4-Digit OTP (Use: 1234)") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = "OTP", tint = TextSecondary)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("otp_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RedEmergency,
                        unfocusedBorderColor = BorderSubtle,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    singleLine = true
                )
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage ?: "",
                    color = RedEmergency,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Primary Action Button
            Button(
                onClick = {
                    errorMessage = null
                    scope.launch {
                        isLoading = true
                        if (!isOtpSent) {
                            val res = AmbulanceApiService.handleLogin(phoneNumber)
                            res.onSuccess {
                                isOtpSent = true
                                otpCode = "1234"
                            }.onFailure {
                                errorMessage = it.message
                            }
                        } else {
                            val res = AmbulanceApiService.verifyOtp(phoneNumber, otpCode)
                            res.onSuccess {
                                onLoginSuccess(selectedRole)
                            }.onFailure {
                                errorMessage = it.message
                            }
                        }
                        isLoading = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("login_primary_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RedEmergency)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = if (!isOtpSent) {
                            if (isNe) "ओटीपी कोड पठाउनुहोस्" else "Send Verification Code"
                        } else {
                            if (isNe) "लगइन पुष्टि गर्नुहोस्" else "Verify & Enter"
                        },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Guest Emergency CTA
            OutlinedButton(
                onClick = onGuestEmergencyAccess,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("guest_emergency_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = RedEmergency
                ),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, RedEmergency)
            ) {
                Icon(
                    imageVector = Icons.Default.Emergency,
                    contentDescription = null,
                    tint = RedEmergency,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isNe) "दर्ता बिना सिधै आपतकालीन बुक" else "Continue as Guest for Emergency",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isNe) "खाता छैन? " else "Don't have an account? ",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
                TextButton(onClick = onNavigateToSignUp) {
                    Text(
                        text = if (isNe) "नयाँ दर्ता गर्नुहोस्" else "Sign Up",
                        color = RedEmergency,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SignUpScreen(
    currentLanguage: AppLanguage,
    onSignUpSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("Ramhari Khadka") }
    var phone by remember { mutableStateOf("9841234567") }
    var bloodGroup by remember { mutableStateOf("O+") }
    var emergencyContactName by remember { mutableStateOf("Sita Khadka") }
    var emergencyContactPhone by remember { mutableStateOf("9801234567") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val isNe = currentLanguage == AppLanguage.NEPALI

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8FAFC)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = if (isNe) "नयाँ खाता बनाउनुहोस्" else "Create Account",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = if (isNe)
                    "आपतकालीन अवस्थाका लागि आफ्नो विवरण सुरक्षित गर्नुहोस्"
                else
                    "Save emergency info for fast medical dispatch",
                fontSize = 13.sp,
                color = TextSecondary,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(if (isNe) "पूरा नाम (Full Name)" else "Full Name") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RedEmergency,
                    unfocusedBorderColor = BorderSubtle,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text(if (isNe) "मोबाइल नम्बर" else "Mobile Number") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RedEmergency,
                    unfocusedBorderColor = BorderSubtle,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Blood Group Selector
            Text(
                text = if (isNe) "रक्त समूह (Blood Group):" else "Blood Group:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("A+", "B+", "O+", "AB+", "O-").forEach { bg ->
                    val isSelected = bloodGroup == bg
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) RedEmergency else Color.White,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) RedEmergency else BorderSubtle
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { bloodGroup = bg }
                    ) {
                        Text(
                            text = bg,
                            color = if (isSelected) Color.White else TextPrimary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Emergency Contact Section
            Text(
                text = if (isNe) "आपतकालीन सम्पर्क व्यक्ति:" else "Primary Emergency Contact:",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = emergencyContactName,
                onValueChange = { emergencyContactName = it },
                label = { Text(if (isNe) "सम्पर्क व्यक्तिको नाम" else "Contact Person Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RedEmergency,
                    unfocusedBorderColor = BorderSubtle,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = emergencyContactPhone,
                onValueChange = { emergencyContactPhone = it },
                label = { Text(if (isNe) "सम्पर्क व्यक्तिको फोन" else "Emergency Phone Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RedEmergency,
                    unfocusedBorderColor = BorderSubtle,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        AmbulanceApiService.handleSignUp(name, phone, bloodGroup)
                        isLoading = false
                        onSignUpSuccess()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("signup_submit_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RedEmergency)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = if (isNe) "खाता सुरक्षित गर्नुहोस्" else "Complete Registration",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = onNavigateToLogin) {
                Text(
                    text = if (isNe) "पहिल्यै खाता छ? लगइन गर्नुहोस्" else "Already have an account? Sign In",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }
        }
    }
}
