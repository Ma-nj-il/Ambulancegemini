package com.example.medambulance.model

enum class AppLanguage {
    NEPALI, ENGLISH
}

enum class UserRole {
    CITIZEN, DRIVER, HOSPITAL_ADMIN
}

enum class AmbulanceCategory(
    val titleEn: String,
    val titleNe: String,
    val subtitleEn: String,
    val subtitleNe: String,
    val baseFare: Double,
    val etaMinutes: String
) {
    BASIC(
        titleEn = "Basic Ambulance",
        titleNe = "सामान्य एम्बुलेन्स",
        subtitleEn = "Standard transport",
        subtitleNe = "आधारभूत आपतकालीन सेवा",
        baseFare = 100.0,
        etaMinutes = "10-15"
    ),
    ICU(
        titleEn = "ICU / Advanced",
        titleNe = "आईसीयू एम्बुलेन्स",
        subtitleEn = "Provider-equipped with oxygen & paramedic",
        subtitleNe = "अक्सिजन र स्वास्थ्यकर्मीसहित",
        baseFare = 200.0,
        etaMinutes = "15-20"
    ),
    VENTILATOR(
        titleEn = "Ventilator Ambulance",
        titleNe = "भेन्टिलेटर एम्बुलेन्स",
        subtitleEn = "Where available - Life support unit",
        subtitleNe = "पूर्ण जीवन रक्षा उपकरणसहित",
        baseFare = 350.0,
        etaMinutes = "20-25"
    ),
    MORTUARY(
        titleEn = "Mortuary Van",
        titleNe = "शव वाहन",
        subtitleEn = "Respectful transport service",
        subtitleNe = "सम्मानपूर्वक सेवा",
        baseFare = 150.0,
        etaMinutes = "25-30"
    )
}

data class Hospital(
    val id: String,
    val name: String,
    val distanceKm: Double,
    val distanceText: String,
    val availableAmbulances: Int,
    val totalAmbulances: Int,
    val address: String,
    val phone: String,
    val rating: Double = 4.5,
    val reviewsCount: Int = 120,
    val isEmergency247: Boolean = true,
    val hasEmergencyRoom: Boolean = true,
    val hasTraumaCenter: Boolean = false,
    val tags: List<String> = listOf("24/7 Open", "Emergency Room")
)

data class Driver(
    val id: String,
    val name: String,
    val photoUrl: String = "",
    val phone: String,
    val vehicleModel: String,
    val vehicleNumber: String,
    val hospitalAffiliation: String,
    val rating: Double,
    val licenseNumber: String,
    val etaMinutes: Int,
    val distanceKm: Double
)

data class AmbulanceBooking(
    val bookingId: String,
    val pickupAddress: String,
    val dropAddress: String,
    val category: AmbulanceCategory,
    val driver: Driver,
    val hospital: Hospital,
    val baseFare: Double,
    val distanceFare: Double,
    val equipmentFare: Double,
    val totalAmount: Double,
    val paymentMethod: String,
    val status: BookingStatus,
    val patientName: String,
    val patientCondition: String,
    val emergencyNote: String,
    val timestamp: String
)

enum class BookingStatus {
    REQUESTED, ASSIGNED, ARRIVING, IN_TRANSIT, COMPLETED, CANCELLED
}

data class RideHistoryItem(
    val id: String,
    val date: String,
    val time: String,
    val pickup: String,
    val dropOff: String,
    val driverName: String,
    val rating: Double,
    val fare: Double,
    val categoryName: String,
    val isCompleted: Boolean
)

data class EmergencyContact(
    val title: String,
    val number: String,
    val subtitle: String,
    val category: String,
    val isPrimary: Boolean = false
)

enum class BloodGroup(val display: String, val info: String, val isRare: Boolean = false) {
    O_POS("O+", "Universal Red Cell Receiver: O+, O-"),
    O_NEG("O-", "Universal Red Cell Donor", isRare = true),
    A_POS("A+", "Compatible with A+, A-, O+, O-"),
    A_NEG("A-", "Compatible with A-, O-", isRare = true),
    B_POS("B+", "Compatible with B+, B-, O+, O-"),
    B_NEG("B-", "Compatible with B-, O-", isRare = true),
    AB_POS("AB+", "Universal Plasma Donor / Recipient"),
    AB_NEG("AB-", "Compatible with AB-, A-, B-, O-", isRare = true)
}

data class EmergencyContactPerson(
    val id: String,
    val name: String,
    val relationship: String,
    val phoneNumber: String,
    val isPrimary: Boolean = false
)

data class UserMedicalProfile(
    val fullName: String,
    val phoneNumber: String,
    val email: String,
    val address: String,
    val age: Int,
    val gender: String,
    val bloodGroup: BloodGroup,
    val allergies: List<String>,
    val chronicConditions: List<String>,
    val emergencyContacts: List<EmergencyContactPerson>,
    val isOrganDonor: Boolean,
    val emergencyNotes: String,
    val preferredHospital: String
)

