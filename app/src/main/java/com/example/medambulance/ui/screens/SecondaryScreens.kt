package com.example.medambulance.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.FireTruck
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medambulance.api.AmbulanceApiService
import com.example.medambulance.model.RideHistoryItem
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.GreenAvailable
import com.example.ui.theme.RedEmergency
import com.example.ui.theme.RedEmergencyLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * BookForOthersScreen (Screenshot Page 11)
 */
@Composable
fun BookForOthersScreen(
    onBack: () -> Unit,
    onBookSuccess: () -> Unit
) {
    var patientName by remember { mutableStateOf("") }
    var patientPhone by remember { mutableStateOf("") }
    var pickup by remember { mutableStateOf("Baneshwor, Kathmandu") }
    var drop by remember { mutableStateOf("Bir Hospital, Kathmandu") }
    var driverNote by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Top App Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Book for Others",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Passenger Details
        Text(
            text = "Passenger Details",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = patientName,
            onValueChange = { patientName = it },
            placeholder = { Text("Full Name") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = TextSecondary) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF8FAFC),
                unfocusedContainerColor = Color(0xFFF8FAFC),
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = BluePrimary
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = patientPhone,
            onValueChange = { patientPhone = it },
            placeholder = { Text("Phone Number") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = TextSecondary) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF8FAFC),
                unfocusedContainerColor = Color(0xFFF8FAFC),
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = BluePrimary
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Location Details
        Text(
            text = "Location Details",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Pickup Location
        OutlinedTextField(
            value = pickup,
            onValueChange = { pickup = it },
            label = { Text("Pickup Location") },
            leadingIcon = {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(GreenAvailable)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF8FAFC),
                unfocusedContainerColor = Color(0xFFF8FAFC),
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = BluePrimary
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Drop Location
        OutlinedTextField(
            value = drop,
            onValueChange = { drop = it },
            label = { Text("Drop Location") },
            leadingIcon = {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(RedEmergency)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF8FAFC),
                unfocusedContainerColor = Color(0xFFF8FAFC),
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = BluePrimary
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Additional Details
        Text(
            text = "Additional Details",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = driverNote,
            onValueChange = { driverNote = it },
            placeholder = { Text("Add note for driver (optional)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF8FAFC),
                unfocusedContainerColor = Color(0xFFF8FAFC),
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = BluePrimary
            ),
            minLines = 2
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Fare Estimate
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Fare Estimate",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Estimated fare", fontSize = 14.sp, color = TextSecondary)
                    Text(
                        text = "Rs 30-50",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Final fare may vary based on traffic and distance",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Book Ride Button (Blue Pill, Screenshot Page 11)
        Button(
            onClick = onBookSuccess,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("book_for_others_btn"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
        ) {
            Text(
                text = "Book Ride",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

/**
 * EmergencyNumbersScreen (Screenshot Page 10)
 */
@Composable
fun EmergencyNumbersScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current

    fun dialNumber(number: String) {
        val cleanNumber = number.replace(Regex("[^0-9+]"), "")
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$cleanNumber"))
        try {
            context.startActivity(intent)
        } catch (_: Exception) {}
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // App Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
                Column(modifier = Modifier.padding(start = 6.dp)) {
                    Text(
                        text = "Emergency Numbers",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Quick access to emergency services",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Hero Card 911 / 102 (Screenshot Page 10)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "102 / 911",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = RedEmergency
                        )
                        Text(
                            text = "Universal Emergency & Nepal Ambulance",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(RedEmergency)
                            .clickable { dialNumber("102") },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Medical Emergency Card (Screenshot Page 10)
            EmergencyCategoryCard(
                categoryTitle = "Medical Emergency",
                icon = Icons.Default.LocalHospital,
                iconColor = RedEmergency,
                items = listOf(
                    Triple("Ambulance Dispatch Nepal", "102", "102"),
                    Triple("Emergency Room", "102 / 911", "102"),
                    Triple("Red Cross Ambulance Kathmandu", "01-4288488", "014288488"),
                    Triple("Poison Control Hotline", "1-800-222-1222", "18002221222"),
                    Triple("Mental Health Crisis (Patan Hotline)", "988 / 1166", "1166")
                ),
                onDial = { dialNumber(it) }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Law Enforcement (Screenshot Page 10)
            EmergencyCategoryCard(
                categoryTitle = "Law Enforcement",
                icon = Icons.Default.Security,
                iconColor = BluePrimary,
                items = listOf(
                    Triple("Nepal Police Emergency", "100", "100"),
                    Triple("Traffic Police Hotline", "103", "103"),
                    Triple("Tourist Police Nepal", "1144", "1144"),
                    Triple("Highway Incident Control", "103", "103")
                ),
                onDial = { dialNumber(it) }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Fire & Rescue (Screenshot Page 10)
            EmergencyCategoryCard(
                categoryTitle = "Fire & Rescue",
                icon = Icons.Default.FireTruck,
                iconColor = Color(0xFFEA580C),
                items = listOf(
                    Triple("Fire Department (Damkal)", "101", "101"),
                    Triple("Armed Police Force Disaster Rescue", "1114", "1114")
                ),
                onDial = { dialNumber(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Warning Banner (Screenshot Page 10)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Always call 102 or 100 for immediate life-threatening emergencies. Do not wait to see if the situation improves.",
                        fontSize = 12.sp,
                        color = Color(0xFF92400E),
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun EmergencyCategoryCard(
    categoryTitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    items: List<Triple<String, String, String>>,
    onDial: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = categoryTitle,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            items.forEachIndexed { index, item ->
                if (index > 0) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFF1F5F9))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDial(item.third) }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = item.first, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                        Text(text = item.second, fontSize = 13.sp, color = iconColor, fontWeight = FontWeight.Bold)
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Call",
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

/**
 * RideHistoryScreen (Screenshot Page 13)
 */
@Composable
fun RideHistoryScreen(
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    val history = remember { AmbulanceApiService.sampleHistory }

    val filtered = history.filter {
        (selectedFilter == "All" || it.date.contains(selectedFilter, ignoreCase = true)) &&
            (searchQuery.isBlank() || it.dropOff.contains(searchQuery, ignoreCase = true) || it.pickup.contains(searchQuery, ignoreCase = true))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // App Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Ride History",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        }

        // Search Box (Screenshot Page 13)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search rides by destination") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedBorderColor = BorderSubtle,
                    focusedBorderColor = BluePrimary
                )
            )
        }

        // Filter Chips (Screenshot Page 13)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("All", "This Month", "Last Month", "This Year").forEach { filter ->
                val isSelected = selectedFilter == filter
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) BluePrimary else Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) BluePrimary else BorderSubtle),
                    modifier = Modifier.clickable { selectedFilter = filter }
                ) {
                    Text(
                        text = filter,
                        color = if (isSelected) Color.White else TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // History List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            items(filtered) { item ->
                HistoryItemCard(item = item)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun HistoryItemCard(item: RideHistoryItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🕒 ${item.date}, ${item.time}",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Text(
                    text = if (item.isCompleted) "Completed" else "Cancelled",
                    color = if (item.isCompleted) GreenAvailable else RedEmergency,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Pickup
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = BluePrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Pickup", fontSize = 11.sp, color = TextSecondary)
                    Text(item.pickup, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Drop
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = RedEmergency,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Drop-off", fontSize = 11.sp, color = TextSecondary)
                    Text(item.dropOff, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEFF6FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = BluePrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = item.driverName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(text = item.rating.toString(), fontSize = 11.sp, color = TextSecondary)
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Rs ${item.fare}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = item.categoryName,
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}
