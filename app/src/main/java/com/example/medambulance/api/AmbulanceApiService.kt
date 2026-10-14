package com.example.medambulance.api

import com.example.medambulance.model.AmbulanceBooking
import com.example.medambulance.model.AmbulanceCategory
import com.example.medambulance.model.BookingStatus
import com.example.medambulance.model.Driver
import com.example.medambulance.model.EmergencyContact
import com.example.medambulance.model.Hospital
import com.example.medambulance.model.RideHistoryItem
import com.example.medambulance.model.BloodGroup
import com.example.medambulance.model.EmergencyContactPerson
import com.example.medambulance.model.UserMedicalProfile
import kotlinx.coroutines.delay

/**
 * AmbulanceApiService
 *
 * Frontend service interface with well-commented placeholder endpoints.
 * Replace the simulated responses with real Retrofit / Fetch / Axios calls
 * when connecting to your live backend.
 */
object AmbulanceApiService {

    val sampleDriver = Driver(
        id = "drv_001",
        name = "Bharat Khatri Chhetri",
        phone = "+977 9765228814",
        vehicleModel = "Nepal Sprinter Ambulance",
        vehicleNumber = "AMB 2234",
        hospitalAffiliation = "Bhaktapur Hospital",
        rating = 4.95,
        licenseNumber = "123456789",
        etaMinutes = 8,
        distanceKm = 2.3
    )

    val sampleHospitals = listOf(
        Hospital(
            id = "hosp_1",
            name = "Kathmandu Medical Center",
            distanceKm = 1.2,
            distanceText = "1.2 km away",
            availableAmbulances = 1,
            totalAmbulances = 2,
            address = "Bhaisepati, Kathmandu",
            phone = "+977 1-4476152",
            rating = 4.6,
            reviewsCount = 310,
            tags = listOf("24/7 Open", "Emergency Room")
        ),
        Hospital(
            id = "hosp_2",
            name = "Nepal Medical College Hospital",
            distanceKm = 2.5,
            distanceText = "2.5 km away",
            availableAmbulances = 0,
            totalAmbulances = 2,
            address = "Tripureshwor, Kathmandu",
            phone = "+977 1-4911000",
            rating = 4.3,
            reviewsCount = 185,
            tags = listOf("24/7 Open", "Emergency Room")
        ),
        Hospital(
            id = "hosp_3",
            name = "National Hospital",
            distanceKm = 3.1,
            distanceText = "3.1 km away",
            availableAmbulances = 2,
            totalAmbulances = 2,
            address = "Teku, Kathmandu",
            phone = "+977 1-4240805",
            rating = 4.5,
            reviewsCount = 240,
            tags = listOf("24/7 Open", "Emergency Room")
        ),
        Hospital(
            id = "hosp_4",
            name = "Kathmandu Model Hospital",
            distanceKm = 3.8,
            distanceText = "3.8 km away",
            availableAmbulances = 1,
            totalAmbulances = 2,
            address = "Kalimati, Kathmandu",
            phone = "+977 1-4240805",
            rating = 4.4,
            reviewsCount = 190,
            tags = listOf("24/7 Open")
        ),
        Hospital(
            id = "hosp_5",
            name = "Bhaktapur Hospital",
            distanceKm = 4.2,
            distanceText = "4.2 km away",
            availableAmbulances = 0,
            totalAmbulances = 2,
            address = "44600 Radheradhe, Bhaktapur",
            phone = "+977 9765228814",
            rating = 4.8,
            reviewsCount = 420,
            tags = listOf("Emergency Room", "ICU Support")
        )
    )

    val sampleHistory = listOf(
        RideHistoryItem(
            id = "#MED82947",
            date = "Today",
            time = "2:30 PM",
            pickup = "putalisadak, Kathmandu",
            dropOff = "Bir Hospital",
            driverName = "Ramhari Bhandari",
            rating = 4.8,
            fare = 30.0,
            categoryName = "Standard",
            isCompleted = true
        ),
        RideHistoryItem(
            id = "#MED82946",
            date = "Yesterday",
            time = "9:15 AM",
            pickup = "Kalanki, Kathmandu",
            dropOff = "Tu Teaching Hospital",
            driverName = "Ramesh Khadka",
            rating = 4.9,
            fare = 18.75,
            categoryName = "Standard",
            isCompleted = true
        ),
        RideHistoryItem(
            id = "#MED82945",
            date = "Jan 15",
            time = "4:45 PM",
            pickup = "Airport Terminal TIA",
            dropOff = "Manmohan Hospital",
            driverName = "Nishant Kc",
            rating = 4.7,
            fare = 45.0,
            categoryName = "Premium",
            isCompleted = true
        ),
        RideHistoryItem(
            id = "#MED82944",
            date = "Jan 14",
            time = "11:20 AM",
            pickup = "Central Park, Kathmandu",
            dropOff = "Bhaktapur Hospital",
            driverName = "Neelam Bhandari",
            rating = 4.6,
            fare = 50.0,
            categoryName = "Standard",
            isCompleted = false
        )
    )

    val sampleEmergencyNumbers = listOf(
        EmergencyContact(
            title = "Universal Emergency Number",
            number = "911",
            subtitle = "Universal Emergency / Dial 102 for Ambulance Nepal",
            category = "Universal",
            isPrimary = true
        ),
        EmergencyContact(
            title = "Nepal Ambulance Helpline",
            number = "102",
            subtitle = "Direct Government & Red Cross Dispatch",
            category = "Medical",
            isPrimary = true
        ),
        EmergencyContact(
            title = "Emergency Room",
            number = "911 / 102",
            subtitle = "Hospital Immediate Triage Center",
            category = "Medical"
        ),
        EmergencyContact(
            title = "Poison Control Center",
            number = "1-800-222-1222",
            subtitle = "Toxic exposure & drug interaction help",
            category = "Medical"
        ),
        EmergencyContact(
            title = "Mental Health Crisis Hotline",
            number = "988",
            subtitle = "24/7 Suicide & crisis lifeline",
            category = "Medical"
        ),
        EmergencyContact(
            title = "Nepal Police Emergency",
            number = "100",
            subtitle = "Local law enforcement dispatch",
            category = "Law"
        ),
        EmergencyContact(
            title = "Traffic / Highway Patrol",
            number = "103",
            subtitle = "Highway incident and road blockage",
            category = "Law"
        ),
        EmergencyContact(
            title = "Fire & Rescue Department",
            number = "101",
            subtitle = "Fire brigade and urban rescue unit",
            category = "Fire"
        )
    )

    /**
     * API Placeholder: handleLogin
     * POST /api/v1/auth/login
     */
    suspend fun handleLogin(phone: String): Result<String> {
        delay(600)
        return if (phone.length >= 7) {
            Result.success("OTP sent to $phone")
        } else {
            Result.failure(Exception("Please enter a valid phone number"))
        }
    }

    /**
     * API Placeholder: verifyOtp
     * POST /api/v1/auth/verify-otp
     */
    suspend fun verifyOtp(phone: String, otp: String): Result<Boolean> {
        delay(600)
        return if (otp.length == 4 || otp == "1234" || otp.isNotEmpty()) {
            Result.success(true)
        } else {
            Result.failure(Exception("Invalid OTP code"))
        }
    }

    /**
     * API Placeholder: handleSignUp
     * POST /api/v1/auth/signup
     */
    suspend fun handleSignUp(name: String, phone: String, bloodGroup: String): Result<Boolean> {
        delay(700)
        return Result.success(true)
    }

    /**
     * API Placeholder: requestAmbulance
     * POST /api/v1/bookings/request
     */
    suspend fun requestAmbulance(
        pickup: String,
        drop: String,
        category: AmbulanceCategory
    ): Result<AmbulanceBooking> {
        delay(800)
        val booking = AmbulanceBooking(
            bookingId = "#MED" + (10000..99999).random(),
            pickupAddress = pickup.ifBlank { "Baneshwor, Kathmandu" },
            dropAddress = drop.ifBlank { "Bhaktapur Hospital" },
            category = category,
            driver = sampleDriver,
            hospital = sampleHospitals.last(),
            baseFare = category.baseFare,
            distanceFare = 25.0,
            equipmentFare = 75.0,
            totalAmount = category.baseFare + 25.0 + 75.0,
            paymentMethod = "Esewa / COD",
            status = BookingStatus.ASSIGNED,
            patientName = "Bharat Khatri Chhetri",
            patientCondition = "Chest Pain (Stable)",
            emergencyNote = "Birami ekdam serious hunuhuncha sakdo chitto aunu hola.",
            timestamp = "Just now"
        )
        return Result.success(booking)
    }

    /**
     * API Placeholder: getNearbyHospitals
     * GET /api/v1/hospitals/nearby?lat=...&lng=...
     */
    suspend fun getNearbyHospitals(): List<Hospital> {
        delay(300)
        return sampleHospitals
    }

    /**
     * User Medical Profile with Emergency Contacts, Blood Group, and Allergies
     */
    var currentUserProfile = UserMedicalProfile(
        fullName = "Manjil Bhandari",
        phoneNumber = "+977 9841234567",
        email = "manjilbhandari2059@gmail.com",
        address = "Baneshwor, Kathmandu, Nepal",
        age = 26,
        gender = "Male",
        bloodGroup = BloodGroup.O_POS,
        allergies = listOf("Penicillin", "Sulfa Drugs", "Aspirin"),
        chronicConditions = listOf("Mild Asthma"),
        emergencyContacts = listOf(
            EmergencyContactPerson(
                id = "c1",
                name = "Aayush Sharma",
                relationship = "Brother",
                phoneNumber = "+977 9812345678",
                isPrimary = true
            ),
            EmergencyContactPerson(
                id = "c2",
                name = "Dr. Sunil Adhikari",
                relationship = "Family Doctor",
                phoneNumber = "+977 9801234567",
                isPrimary = false
            ),
            EmergencyContactPerson(
                id = "c3",
                name = "Sita Bhandari",
                relationship = "Mother",
                phoneNumber = "+977 9865432109",
                isPrimary = false
            )
        ),
        isOrganDonor = true,
        emergencyNotes = "Epipen carried in emergency pouch. Preferred transport to Bir Hospital Trauma Center.",
        preferredHospital = "Bir Hospital, Kathmandu"
    )

    /**
     * API Placeholder: getUserProfile
     * GET /api/v1/users/me/medical-profile
     */
    suspend fun getUserProfile(): UserMedicalProfile {
        delay(200)
        return currentUserProfile
    }

    /**
     * API Placeholder: updateUserProfile
     * PUT /api/v1/users/me/medical-profile
     */
    suspend fun updateUserProfile(updated: UserMedicalProfile): Result<UserMedicalProfile> {
        delay(400)
        currentUserProfile = updated
        return Result.success(currentUserProfile)
    }
}

