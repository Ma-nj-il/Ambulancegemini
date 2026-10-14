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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medambulance.model.AppLanguage
import com.example.medambulance.model.AppStrings
import com.example.medambulance.ui.components.MedicAmbulanceLogo
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.RedEmergency
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun MedicLoginScreen(
    currentLanguage: AppLanguage,
    onLanguageToggle: () -> Unit,
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onDirectGuestEmergency: () -> Unit
) {
    var emailOrPhone by remember { mutableStateOf("9841234567") }
    var password by remember { mutableStateOf("••••••••") }
    var passwordVisible by remember { mutableStateOf(false) }

    val bgGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF8FAFC),
            Color(0xFFEFF6FF),
            Color(0xFFF8FAFC)
        )
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8FAFC)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgGradient)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Right: Language Switcher [ EN | ने ] matching mockup
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White.copy(alpha = 0.85f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier
                            .shadow(2.dp, RoundedCornerShape(20.dp))
                            .clickable { onLanguageToggle() }
                            .testTag("lang_toggle_pill")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "EN",
                                fontSize = 12.sp,
                                fontWeight = if (currentLanguage == AppLanguage.ENGLISH) FontWeight.Bold else FontWeight.Normal,
                                color = if (currentLanguage == AppLanguage.ENGLISH) RedEmergency else TextSecondary
                            )
                            Text(
                                text = " | ",
                                fontSize = 12.sp,
                                color = BorderSubtle
                            )
                            Text(
                                text = "ने",
                                fontSize = 12.sp,
                                fontWeight = if (currentLanguage == AppLanguage.NEPALI) FontWeight.Bold else FontWeight.Normal,
                                color = if (currentLanguage == AppLanguage.NEPALI) RedEmergency else TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Stylized MEDIC AMBULANCE Logo matching mockup
                MedicAmbulanceLogo(size = 86.dp, showText = true)

                Spacer(modifier = Modifier.height(28.dp))

                // Welcome Back Header
                Text(
                    text = AppStrings.get("welcome_back", currentLanguage),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = AppStrings.get("access_fast", currentLanguage),
                    fontSize = 13.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Input 1: Email or Phone Number with subtle envelope icon
                OutlinedTextField(
                    value = emailOrPhone,
                    onValueChange = { emailOrPhone = it },
                    placeholder = {
                        Text(
                            text = AppStrings.get("email_or_phone", currentLanguage),
                            color = Color(0xFF94A3B8),
                            fontSize = 14.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("login_email_phone_input"),
                    shape = RoundedCornerShape(27.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFFCBD5E1),
                        unfocusedBorderColor = Color(0xFFE2E8F0)
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Input 2: Password with lock icon
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = {
                        Text(
                            text = AppStrings.get("password", currentLanguage),
                            color = Color(0xFF94A3B8),
                            fontSize = 14.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle password",
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("login_password_input"),
                    shape = RoundedCornerShape(27.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFFCBD5E1),
                        unfocusedBorderColor = Color(0xFFE2E8F0)
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Primary Glowing Emergency Red Login Button (Matching Mockup)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(27.dp),
                            spotColor = RedEmergency,
                            ambientColor = RedEmergency
                        )
                ) {
                    Button(
                        onClick = onLoginSuccess,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("login_glow_btn"),
                        shape = RoundedCornerShape(27.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RedEmergency)
                    ) {
                        Text(
                            text = AppStrings.get("login", currentLanguage),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Outlined Clean Sign Up Button (Matching Mockup)
                OutlinedButton(
                    onClick = onNavigateToSignUp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("signup_outline_btn"),
                    shape = RoundedCornerShape(26.dp),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFF1E293B)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF0F172A))
                ) {
                    Text(
                        text = AppStrings.get("sign_up", currentLanguage),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Social Login Divider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE2E8F0))
                    Text(
                        text = "  ${AppStrings.get("social_login", currentLanguage)}  ",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE2E8F0))
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Social Circular Buttons: Facebook & Twitter / Google
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Facebook
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                            .clickable { onLoginSuccess() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "f",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1877F2)
                        )
                    }

                    // Twitter / Google
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                            .clickable { onLoginSuccess() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "G",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEA4335)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
