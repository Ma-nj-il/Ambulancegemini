package com.example.medambulance.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medambulance.api.AmbulanceApiService
import com.example.medambulance.model.AppLanguage
import com.example.medambulance.model.AppStrings
import com.example.medambulance.model.BloodGroup
import com.example.medambulance.model.EmergencyContactPerson
import com.example.medambulance.model.UserMedicalProfile
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.GreenAvailable
import com.example.ui.theme.RedEmergency
import com.example.ui.theme.RedEmergencyLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    currentLanguage: AppLanguage,
    onLanguageToggle: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val isNe = currentLanguage == AppLanguage.NEPALI

    // Persisted profile state
    var profile by remember { mutableStateOf(AmbulanceApiService.currentUserProfile) }

    // Edit mode toggle
    var isEditMode by remember { mutableStateOf(false) }

    // Draft state for edit mode
    var draftName by remember { mutableStateOf(profile.fullName) }
    var draftPhone by remember { mutableStateOf(profile.phoneNumber) }
    var draftEmail by remember { mutableStateOf(profile.email) }
    var draftAddress by remember { mutableStateOf(profile.address) }
    var draftAge by remember { mutableStateOf(profile.age.toString()) }
    var draftBloodGroup by remember { mutableStateOf(profile.bloodGroup) }
    var draftAllergies by remember { mutableStateOf(profile.allergies.toMutableList()) }
    var draftConditions by remember { mutableStateOf(profile.chronicConditions.toMutableList()) }
    var draftContacts by remember { mutableStateOf(profile.emergencyContacts.toMutableList()) }
    var draftOrganDonor by remember { mutableStateOf(profile.isOrganDonor) }
    var draftNotes by remember { mutableStateOf(profile.emergencyNotes) }
    var draftHospital by remember { mutableStateOf(profile.preferredHospital) }

    // Input state for adding custom allergy
    var newAllergyInput by remember { mutableStateOf("") }
    var newConditionInput by remember { mutableStateOf("") }

    // Dialog state for adding a new emergency contact
    var showAddContactDialog by remember { mutableStateOf(false) }
    var newContactName by remember { mutableStateOf("") }
    var newContactPhone by remember { mutableStateOf("") }
    var newContactRelation by remember { mutableStateOf("Family") }
    var newContactIsPrimary by remember { mutableStateOf(false) }

    val commonAllergies = listOf("Penicillin", "Sulfa Drugs", "Aspirin", "Ibuprofen", "Latex", "Peanuts", "Contrast Dye", "Codeine")

    fun resetDrafts() {
        draftName = profile.fullName
        draftPhone = profile.phoneNumber
        draftEmail = profile.email
        draftAddress = profile.address
        draftAge = profile.age.toString()
        draftBloodGroup = profile.bloodGroup
        draftAllergies = profile.allergies.toMutableList()
        draftConditions = profile.chronicConditions.toMutableList()
        draftContacts = profile.emergencyContacts.toMutableList()
        draftOrganDonor = profile.isOrganDonor
        draftNotes = profile.emergencyNotes
        draftHospital = profile.preferredHospital
        newAllergyInput = ""
        newConditionInput = ""
    }

    fun saveDrafts() {
        val ageInt = draftAge.toIntOrNull() ?: profile.age
        val updated = profile.copy(
            fullName = draftName.trim().ifEmpty { profile.fullName },
            phoneNumber = draftPhone.trim().ifEmpty { profile.phoneNumber },
            email = draftEmail.trim(),
            address = draftAddress.trim(),
            age = ageInt,
            bloodGroup = draftBloodGroup,
            allergies = draftAllergies.toList(),
            chronicConditions = draftConditions.toList(),
            emergencyContacts = draftContacts.toList(),
            isOrganDonor = draftOrganDonor,
            emergencyNotes = draftNotes.trim(),
            preferredHospital = draftHospital.trim()
        )
        profile = updated
        coroutineScope.launch {
            AmbulanceApiService.updateUserProfile(updated)
        }
        isEditMode = false
        Toast.makeText(
            context,
            if (isNe) "मेडिकल प्रोफाइल सफलतापूर्वक सुरक्षित गरियो!" else "Medical Profile saved successfully!",
            Toast.LENGTH_SHORT
        ).show()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = if (isEditMode) 90.dp else 24.dp)
        ) {
            // ==========================================
            // TOP HEADER BAR
            // ==========================================
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF1F5F9))
                                .testTag("profile_back_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = TextPrimary
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isNe) "मेरो मेडिकल प्रोफाइल" else "Medical ID & Profile",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = if (isEditMode) {
                                    if (isNe) "सम्पादन मोड सक्रिय छ" else "Edit Mode Active"
                                } else {
                                    if (isNe) "आपतकालीन स्वास्थ्य परिचय" else "Emergency Health Identity"
                                },
                                fontSize = 12.sp,
                                color = if (isEditMode) RedEmergency else TextSecondary
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Language Toggle EN | ने
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFFF1F5F9),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier
                                .clickable { onLanguageToggle() }
                                .testTag("profile_lang_toggle")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isNe) "नेपाली" else "EN",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RedEmergency
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = if (isNe) "EN" else "ने",
                                    fontSize = 10.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Edit / Cancel Toggle Action
                        if (!isEditMode) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color(0xFFEFF6FF),
                                border = BorderStroke(1.dp, BluePrimary.copy(alpha = 0.3f)),
                                modifier = Modifier
                                    .clickable {
                                        resetDrafts()
                                        isEditMode = true
                                    }
                                    .testTag("edit_profile_btn")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit Profile",
                                        tint = BluePrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isNe) "सम्पादन" else "Edit",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BluePrimary
                                    )
                                }
                            }
                        } else {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color(0xFFFEE2E2),
                                border = BorderStroke(1.dp, RedEmergency.copy(alpha = 0.3f)),
                                modifier = Modifier
                                    .clickable {
                                        resetDrafts()
                                        isEditMode = false
                                    }
                                    .testTag("cancel_edit_btn")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Cancel Edit",
                                        tint = RedEmergency,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isNe) "रद्द" else "Cancel",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RedEmergency
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ==========================================
            // EDIT MODE BANNER (When editing)
            // ==========================================
            AnimatedVisibility(visible = isEditMode) {
                Surface(
                    color = Color(0xFFFEF3C7),
                    border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editing",
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isNe)
                                "तपाईं मेडिकल कार्ड सम्पादन गर्दै हुनुहुन्छ। तलको विवरण परिवर्तन गरि 'Save' थिच्नुहोस्।"
                            else
                                "You are editing your emergency medical ID. Changes update ambulance paramedic records.",
                            fontSize = 12.sp,
                            color = Color(0xFF92400E)
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {

                // ==========================================
                // 1. DIGITAL MEDICAL ID HERO CARD
                // ==========================================
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF0F172A),
                                        Color(0xFF1E293B)
                                    )
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column {
                            // Top Row: Emergency Logo & Verified Badge
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(RedEmergency),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.MedicalServices,
                                            contentDescription = "Medical Cross",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "MED-AMBULANCE ID",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.sp,
                                        color = Color.White
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = GreenAvailable.copy(alpha = 0.2f),
                                    border = BorderStroke(1.dp, GreenAvailable)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Verified,
                                            contentDescription = "Verified",
                                            tint = GreenAvailable,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (isNe) "प्रमाणित मेडिकल कार्ड" else "VERIFIED ID",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = GreenAvailable
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            // Main User Details Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Avatar with Red Emergency Glow
                                Box(
                                    modifier = Modifier
                                        .size(68.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.15f))
                                        .border(2.dp, RedEmergency, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "User Avatar",
                                        tint = Color.White,
                                        modifier = Modifier.size(38.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (isEditMode) draftName else profile.fullName,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Phone,
                                            contentDescription = "Phone",
                                            tint = Color(0xFF94A3B8),
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (isEditMode) draftPhone else profile.phoneNumber,
                                            fontSize = 13.sp,
                                            color = Color(0xFFCBD5E1)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = "Address",
                                            tint = Color(0xFF94A3B8),
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (isEditMode) draftAddress else profile.address,
                                            fontSize = 12.sp,
                                            color = Color(0xFF94A3B8)
                                        )
                                    }
                                }

                                // Prominent Blood Group Badge inside Hero Card
                                val currentBg = if (isEditMode) draftBloodGroup else profile.bloodGroup
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = RedEmergency,
                                    shadowElevation = 4.dp
                                ) {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "BLOOD",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White.copy(alpha = 0.8f)
                                        )
                                        Text(
                                            text = currentBg.display,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = Color.White.copy(alpha = 0.15f))
                            Spacer(modifier = Modifier.height(12.dp))

                            // Bottom Strip: Age, Gender, Donor Status
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${if (isEditMode) draftAge else profile.age} Yrs • ${profile.gender}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFFE2E8F0)
                                    )
                                }

                                val isOrganDonorActive = if (isEditMode) draftOrganDonor else profile.isOrganDonor
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = "Organ Donor",
                                        tint = if (isOrganDonorActive) Color(0xFFF43F5E) else Color(0xFF64748B),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isOrganDonorActive) {
                                            if (isNe) "अङ्ग दाता (Organ Donor)" else "Organ Donor"
                                        } else {
                                            if (isNe) "अङ्ग दाता होइन" else "Not Registered"
                                        },
                                        fontSize = 11.sp,
                                        color = if (isOrganDonorActive) Color(0xFFFECDD3) else Color(0xFF94A3B8),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }

                // ==========================================
                // EDITABLE PERSONAL FIELDS (IN EDIT MODE)
                // ==========================================
                AnimatedVisibility(visible = isEditMode) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, BorderSubtle)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (isNe) "व्यक्तिगत विवरण सम्पादन" else "Personal Details",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = draftName,
                                onValueChange = { draftName = it },
                                label = { Text(if (isNe) "पूरा नाम" else "Full Name") },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("edit_name_input"),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BluePrimary,
                                    unfocusedBorderColor = BorderSubtle
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = draftPhone,
                                    onValueChange = { draftPhone = it },
                                    label = { Text(if (isNe) "फोन नम्बर" else "Phone Number") },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    modifier = Modifier
                                        .weight(1.5f)
                                        .testTag("edit_phone_input"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = BluePrimary,
                                        unfocusedBorderColor = BorderSubtle
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                OutlinedTextField(
                                    value = draftAge,
                                    onValueChange = { draftAge = it },
                                    label = { Text(if (isNe) "उमेर" else "Age") },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier
                                        .weight(0.8f)
                                        .testTag("edit_age_input"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = BluePrimary,
                                        unfocusedBorderColor = BorderSubtle
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = draftAddress,
                                onValueChange = { draftAddress = it },
                                label = { Text(if (isNe) "ठेगाना (स्थान)" else "Home Address / Location") },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("edit_address_input"),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BluePrimary,
                                    unfocusedBorderColor = BorderSubtle
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ==========================================
                // 2. BLOOD TYPE SECTION (CRITICAL REQUIREMENT)
                // ==========================================
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BorderSubtle)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(RedEmergencyLight),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bloodtype,
                                        contentDescription = "Blood Group",
                                        tint = RedEmergency,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (isNe) "रक्त समूह (Blood Group)" else "Blood Group",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = if (isNe) "आपतकालीन रक्त संचार विवरण" else "Emergency transfusion profile",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            if (!isEditMode) {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = RedEmergencyLight,
                                    border = BorderStroke(1.dp, RedEmergency.copy(alpha = 0.4f))
                                ) {
                                    Text(
                                        text = profile.bloodGroup.display,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = RedEmergency,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (isEditMode) {
                            // Interactive Blood Group Selector Grid (8 types)
                            Text(
                                text = if (isNe) "आफ्नो रक्त समूह चयन गर्नुहोस्:" else "Select your blood group:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextSecondary,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            val bloodGroups = BloodGroup.values()
                            val chunked = bloodGroups.toList().chunked(4)

                            chunked.forEach { rowGroups ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    rowGroups.forEach { bg ->
                                        val isSelected = draftBloodGroup == bg
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = if (isSelected) RedEmergency else Color(0xFFF8FAFC),
                                            border = BorderStroke(
                                                1.dp,
                                                if (isSelected) RedEmergency else Color(0xFFE2E8F0)
                                            ),
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { draftBloodGroup = bg }
                                                .testTag("blood_type_${bg.display}")
                                        ) {
                                            Box(
                                                modifier = Modifier.padding(vertical = 12.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = bg.display,
                                                    fontSize = 15.sp,
                                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                                                    color = if (isSelected) Color.White else TextPrimary
                                                )
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "ℹ️ ${draftBloodGroup.info}",
                                fontSize = 11.sp,
                                color = Color(0xFF0284C7),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF0F9FF), RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            )
                        } else {
                            // View Mode Card
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFFFF1F2),
                                border = BorderStroke(1.dp, Color(0xFFFECDD3)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bloodtype,
                                        contentDescription = "Info",
                                        tint = RedEmergency,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "${profile.bloodGroup.display} Rh Positive",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = RedEmergency
                                        )
                                        Text(
                                            text = profile.bloodGroup.info,
                                            fontSize = 12.sp,
                                            color = Color(0xFF9F1239)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ==========================================
                // 3. MEDICAL ALLERGIES (CRITICAL REQUIREMENT)
                // ==========================================
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BorderSubtle)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFEF3C7)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = "Medical Allergies",
                                        tint = Color(0xFFD97706),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (isNe) "औषधी तथा मेडिकल एलर्जी" else "Medical Allergies",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = if (isNe) "प्यारामेडिकका लागि उच्च सतर्कता सूचना" else "High-alert for emergency medics",
                                        fontSize = 11.sp,
                                        color = Color(0xFFB45309)
                                    )
                                }
                            }

                            val count = if (isEditMode) draftAllergies.size else profile.allergies.size
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (count > 0) Color(0xFFFEF2F2) else Color(0xFFECFDF5),
                                border = BorderStroke(
                                    1.dp,
                                    if (count > 0) RedEmergency.copy(alpha = 0.5f) else GreenAvailable
                                )
                            ) {
                                Text(
                                    text = "$count ${if (isNe) "एलर्जी" else "Allergies"}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (count > 0) RedEmergency else GreenAvailable,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        if (isEditMode) {
                            // Active Allergies Chips with Delete (X) Icon
                            if (draftAllergies.isNotEmpty()) {
                                Text(
                                    text = if (isNe) "हाल सूचीकृत एलर्जी (हटाउन 'X' थिच्नुहोस्):" else "Active recorded allergies (tap X to remove):",
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )

                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    draftAllergies.forEach { allergy ->
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = Color(0xFFFEE2E2),
                                            border = BorderStroke(1.dp, Color(0xFFFCA5A5))
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(start = 10.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Vaccines,
                                                    contentDescription = null,
                                                    tint = RedEmergency,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = allergy,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Color(0xFF991B1B)
                                                )
                                                IconButton(
                                                    onClick = {
                                                        draftAllergies.remove(allergy)
                                                        draftAllergies = draftAllergies.toMutableList()
                                                    },
                                                    modifier = Modifier.size(24.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Close,
                                                        contentDescription = "Remove $allergy",
                                                        tint = RedEmergency,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            } else {
                                Text(
                                    text = if (isNe) "कुनै एलर्जी थपिएको छैन। तलबाट थप्नुहोस्।" else "No allergies recorded. Select below or add custom.",
                                    fontSize = 12.sp,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }

                            // Quick-add suggestions
                            Text(
                                text = if (isNe) "द्रुत थप (प्रचलित औषधी एलर्जी):" else "Quick-add common drug allergies:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextSecondary,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                commonAllergies.forEach { suggestion ->
                                    val alreadyAdded = draftAllergies.contains(suggestion)
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (alreadyAdded) Color(0xFFE2E8F0) else Color(0xFFF1F5F9),
                                        border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                                        modifier = Modifier
                                            .clickable(enabled = !alreadyAdded) {
                                                draftAllergies.add(suggestion)
                                                draftAllergies = draftAllergies.toMutableList()
                                            }
                                            .testTag("quick_allergy_$suggestion")
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = if (alreadyAdded) Icons.Default.Check else Icons.Default.Add,
                                                contentDescription = null,
                                                tint = if (alreadyAdded) GreenAvailable else BluePrimary,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = suggestion,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = if (alreadyAdded) Color(0xFF64748B) else TextPrimary
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Custom allergy text input
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = newAllergyInput,
                                    onValueChange = { newAllergyInput = it },
                                    placeholder = {
                                        Text(
                                            if (isNe) "अन्य एलर्जी लेख्नुहोस्..." else "Enter other allergy (e.g. Latex)...",
                                            fontSize = 12.sp
                                        )
                                    },
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("custom_allergy_input"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = BluePrimary,
                                        unfocusedBorderColor = BorderSubtle
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        val trimmed = newAllergyInput.trim()
                                        if (trimmed.isNotEmpty() && !draftAllergies.contains(trimmed)) {
                                            draftAllergies.add(trimmed)
                                            draftAllergies = draftAllergies.toMutableList()
                                            newAllergyInput = ""
                                        }
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                                    modifier = Modifier.testTag("add_allergy_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add",
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isNe) "थप्नुहोस्" else "Add",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        } else {
                            // View Mode: Display badges
                            if (profile.allergies.isNotEmpty()) {
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    profile.allergies.forEach { allergy ->
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = Color(0xFFFEE2E2),
                                            border = BorderStroke(1.dp, Color(0xFFFCA5A5))
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Vaccines,
                                                    contentDescription = null,
                                                    tint = RedEmergency,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = allergy,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Color(0xFF991B1B)
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFFECFDF5),
                                    border = BorderStroke(1.dp, Color(0xFFA7F3D0)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = GreenAvailable,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (isNe) "कुनै औषधी एलर्जी छैन (NKDA)" else "No known drug allergies (NKDA)",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF065F46)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ==========================================
                // 4. EMERGENCY CONTACTS (CRITICAL REQUIREMENT)
                // ==========================================
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BorderSubtle)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE0F2FE)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = "Emergency Contacts",
                                        tint = Color(0xFF0284C7),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (isNe) "आपतकालीन सम्पर्क व्यक्तिहरू" else "Emergency Contacts",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = if (isNe) "घटना वा आपतमा स्वतः सम्पर्क गरिनेछ" else "Notified during medical SOS dispatch",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            if (isEditMode) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = BluePrimary,
                                    modifier = Modifier
                                        .clickable { showAddContactDialog = true }
                                        .testTag("add_emergency_contact_btn")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PersonAdd,
                                            contentDescription = "Add Contact",
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (isNe) "थप्नुहोस्" else "Add",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        val contactList = if (isEditMode) draftContacts else profile.emergencyContacts

                        if (contactList.isEmpty()) {
                            Text(
                                text = if (isNe) "कुनै आपतकालीन सम्पर्क थपिएको छैन।" else "No emergency contacts registered yet.",
                                fontSize = 12.sp,
                                color = TextSecondary,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                contactList.forEachIndexed { index, contact ->
                                    EmergencyContactCard(
                                        contact = contact,
                                        isEditMode = isEditMode,
                                        isNe = isNe,
                                        onCallClick = {
                                            try {
                                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                                    data = Uri.parse("tel:${contact.phoneNumber.replace(" ", "")}")
                                                }
                                                context.startActivity(intent)
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "Dialing ${contact.phoneNumber}", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        onDeleteClick = {
                                            draftContacts.removeAt(index)
                                            draftContacts = draftContacts.toMutableList()
                                        },
                                        onTogglePrimary = {
                                            // Set this contact as primary and others as non-primary
                                            draftContacts = draftContacts.mapIndexed { i, c ->
                                                c.copy(isPrimary = i == index)
                                            }.toMutableList()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ==========================================
                // 5. CHRONIC CONDITIONS & PARAMEDIC NOTES
                // ==========================================
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BorderSubtle)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF3E8FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalHospital,
                                    contentDescription = "Medical Notes",
                                    tint = Color(0xFF7E22CE),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isNe) "दीर्घरोग तथा प्यारामेडिक निर्देशन" else "Conditions & Medic Instructions",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = if (isNe) "एम्बुलेन्स टोलीलाई देखाइने महत्वपूर्ण नोट" else "Displayed directly on driver/paramedic screen",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Chronic Conditions Section
                        Text(
                            text = if (isNe) "स्वास्थ्य अवस्था / दीर्घरोग:" else "Chronic Medical Conditions:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        if (isEditMode) {
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                draftConditions.forEach { cond ->
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFFF3E8FF),
                                        border = BorderStroke(1.dp, Color(0xFFD8B4FE))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = cond,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color(0xFF6B21A8)
                                            )
                                            IconButton(
                                                onClick = {
                                                    draftConditions.remove(cond)
                                                    draftConditions = draftConditions.toMutableList()
                                                },
                                                modifier = Modifier.size(20.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "Remove",
                                                    tint = Color(0xFF6B21A8),
                                                    modifier = Modifier.size(12.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = newConditionInput,
                                    onValueChange = { newConditionInput = it },
                                    placeholder = {
                                        Text(
                                            if (isNe) "नयाँ स्वास्थ्य अवस्था (उदा: Diabetes)..." else "Enter condition (e.g. Asthma, Diabetes)...",
                                            fontSize = 12.sp
                                        )
                                    },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF7E22CE),
                                        unfocusedBorderColor = BorderSubtle
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        val trimmed = newConditionInput.trim()
                                        if (trimmed.isNotEmpty() && !draftConditions.contains(trimmed)) {
                                            draftConditions.add(trimmed)
                                            draftConditions = draftConditions.toMutableList()
                                            newConditionInput = ""
                                        }
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E22CE))
                                ) {
                                    Text(text = if (isNe) "थप्नुहोस्" else "Add", fontSize = 12.sp)
                                }
                            }
                        } else {
                            if (profile.chronicConditions.isNotEmpty()) {
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    profile.chronicConditions.forEach { cond ->
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = Color(0xFFF3E8FF),
                                            border = BorderStroke(1.dp, Color(0xFFD8B4FE))
                                        ) {
                                            Text(
                                                text = cond,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color(0xFF6B21A8),
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                            )
                                        }
                                    }
                                }
                            } else {
                                Text(
                                    text = if (isNe) "कुनै दीर्घरोग विवरण छैन।" else "None recorded.",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = BorderSubtle)
                        Spacer(modifier = Modifier.height(14.dp))

                        // Paramedic Special Notes
                        Text(
                            text = if (isNe) "आपतकालीन प्यारामेडिक नोट:" else "Paramedic & First Responder Instructions:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        if (isEditMode) {
                            OutlinedTextField(
                                value = draftNotes,
                                onValueChange = { draftNotes = it },
                                placeholder = {
                                    Text(
                                        if (isNe)
                                            "उदा: Epipen ब्यागमा छ, वीर अस्पताल ट्रमा सेन्टर प्राथमिकता..."
                                        else
                                            "e.g., Epipen carried in bag, prefers Patan Hospital emergency..."
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("edit_paramedic_notes"),
                                shape = RoundedCornerShape(12.dp),
                                minLines = 3,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BluePrimary,
                                    unfocusedBorderColor = BorderSubtle
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Organ Donor Switch in Edit Mode
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = if (isNe) "अङ्ग दाता दर्ता (Organ Donor)" else "Organ Donor Registry",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = if (isNe) "जीवनदान सम्बन्धी सहमति" else "Consent for life-saving donation",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                                Switch(
                                    checked = draftOrganDonor,
                                    onCheckedChange = { draftOrganDonor = it },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = RedEmergency
                                    ),
                                    modifier = Modifier.testTag("organ_donor_switch")
                                )
                            }
                        } else {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        text = "📋",
                                        fontSize = 16.sp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = profile.emergencyNotes.ifBlank {
                                            if (isNe) "कुनै विशेष प्यारामेडिक निर्देशन छैन।" else "No special instructions provided."
                                        },
                                        fontSize = 13.sp,
                                        color = TextPrimary,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // ==========================================
        // FLOATING STICKY ACTION BAR IN EDIT MODE
        // ==========================================
        if (isEditMode) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                color = Color.White,
                shadowElevation = 16.dp,
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            resetDrafts()
                            isEditMode = false
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("discard_changes_btn"),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, Color(0xFF94A3B8))
                    ) {
                        Text(
                            text = if (isNe) "रद्द गर्नुहोस्" else "Cancel",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B)
                        )
                    }

                    Button(
                        onClick = { saveDrafts() },
                        modifier = Modifier
                            .weight(1.5f)
                            .height(50.dp)
                            .testTag("save_profile_changes_btn"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RedEmergency)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isNe) "सुरक्षित गर्नुहोस्" else "Save Changes",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // ==========================================
        // ADD EMERGENCY CONTACT MODAL DIALOG
        // ==========================================
        if (showAddContactDialog) {
            AlertDialog(
                onDismissRequest = { showAddContactDialog = false },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = null,
                            tint = BluePrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isNe) "नयाँ आपतकालीन सम्पर्क" else "Add Emergency Contact",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = newContactName,
                            onValueChange = { newContactName = it },
                            label = { Text(if (isNe) "सम्पर्क व्यक्तिको नाम" else "Full Name") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("modal_contact_name"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = newContactPhone,
                            onValueChange = { newContactPhone = it },
                            label = { Text(if (isNe) "फोन नम्बर" else "Phone Number") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("modal_contact_phone"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Text(
                            text = if (isNe) "नाता / सम्बन्ध:" else "Relationship:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary
                        )

                        val relations = listOf("Spouse", "Brother", "Mother", "Father", "Doctor", "Friend")
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            relations.forEach { rel ->
                                val isSelected = newContactRelation == rel
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { newContactRelation = rel },
                                    label = { Text(rel, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = BluePrimary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (isNe) "मुख्य सम्पर्क बनाउनुहोस्" else "Set as Primary Contact",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Switch(
                                checked = newContactIsPrimary,
                                onCheckedChange = { newContactIsPrimary = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = BluePrimary
                                )
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newContactName.isNotBlank() && newContactPhone.isNotBlank()) {
                                val newEntry = EmergencyContactPerson(
                                    id = "c_${System.currentTimeMillis()}",
                                    name = newContactName.trim(),
                                    relationship = newContactRelation,
                                    phoneNumber = newContactPhone.trim(),
                                    isPrimary = newContactIsPrimary
                                )
                                if (newContactIsPrimary) {
                                    // Demote others
                                    draftContacts = draftContacts.map { it.copy(isPrimary = false) }.toMutableList()
                                }
                                draftContacts.add(newEntry)
                                draftContacts = draftContacts.toMutableList()
                                newContactName = ""
                                newContactPhone = ""
                                newContactIsPrimary = false
                                showAddContactDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("confirm_add_contact_btn")
                    ) {
                        Text(if (isNe) "थप्नुहोस्" else "Add Contact")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddContactDialog = false }) {
                        Text(if (isNe) "रद्द" else "Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun EmergencyContactCard(
    contact: EmergencyContactPerson,
    isEditMode: Boolean,
    isNe: Boolean,
    onCallClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onTogglePrimary: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (contact.isPrimary) Color(0xFFF0FDF4) else Color(0xFFF8FAFC),
        border = BorderStroke(
            1.dp,
            if (contact.isPrimary) Color(0xFF86EFAC) else Color(0xFFE2E8F0)
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            if (contact.isPrimary) GreenAvailable.copy(alpha = 0.15f)
                            else Color(0xFFE2E8F0)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (contact.isPrimary) Icons.Default.Star else Icons.Default.Person,
                        contentDescription = null,
                        tint = if (contact.isPrimary) GreenAvailable else Color(0xFF64748B),
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = contact.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color.White,
                            border = BorderStroke(1.dp, Color(0xFFCBD5E1))
                        ) {
                            Text(
                                text = contact.relationship,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextSecondary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = contact.phoneNumber,
                        fontSize = 12.sp,
                        color = Color(0xFF475569),
                        fontWeight = FontWeight.Medium
                    )

                    if (contact.isPrimary) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isNe) "★ मुख्य सम्पर्क व्यक्ति" else "★ Primary Contact",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenAvailable
                        )
                    }
                }
            }

            if (isEditMode) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onTogglePrimary,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Toggle Primary",
                            tint = if (contact.isPrimary) GreenAvailable else Color(0xFFCBD5E1),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete Contact",
                            tint = RedEmergency,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            } else {
                Surface(
                    shape = CircleShape,
                    color = GreenAvailable,
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .clickable { onCallClick() }
                        .testTag("call_contact_${contact.name}")
                ) {
                    Box(
                        modifier = Modifier.size(38.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Call ${contact.name}",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
