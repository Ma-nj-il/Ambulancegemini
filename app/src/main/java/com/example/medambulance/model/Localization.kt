package com.example.medambulance.model

object AppStrings {
    fun get(key: String, lang: AppLanguage): String {
        val isNe = lang == AppLanguage.NEPALI
        return when (key) {
            "welcome_back" -> if (isNe) "पुनः स्वागत छ" else "Welcome Back"
            "access_fast" -> if (isNe) "द्रुत र भरपर्दो स्वास्थ्य यातायात सेवा" else "Access fast & reliable medical transit"
            "email_or_phone" -> if (isNe) "इमेल वा फोन नम्बर" else "Email or Phone Number"
            "password" -> if (isNe) "गोप्य पासवर्ड" else "Password"
            "login" -> if (isNe) "लगइन गर्नुहोस्" else "Login"
            "sign_up" -> if (isNe) "दर्ता गर्नुहोस्" else "Sign Up"
            "social_login" -> if (isNe) "सामाजिक सञ्जालबाट लगइन" else "Social login"
            "current_user" -> if (isNe) "हालको प्रयोगकर्ता" else "Current user"
            "users_location" -> if (isNe) "बानेश्वर, काठमाडौँ" else "Baneshwor, Kathmandu"
            "where_is_emergency" -> if (isNe) "आपतकालीन अवस्था कहाँ छ?" else "Where is the emergency?"
            "select_ambulance_type" -> if (isNe) "एम्बुलेन्स प्रकार छान्नुहोस्" else "Select Ambulance Type"
            "request_ambulance_now" -> if (isNe) "एम्बुलेन्स तुरुन्तै बोलाउनुहोस्" else "REQUEST AMBULANCE NOW"
            "offer_your_fare" -> if (isNe) "आफ्नो भाडा प्रस्ताव" else "Offer Your Fare"
            "suggest_fare" -> if (isNe) "सुझावित भाडा" else "Suggest a fare"
            "offline_mode_active" -> if (isNe) "इन्टरनेट छैन - अफलाइन मोड सक्रिय ⚠️" else "No Internet - Offline Mode Active ⚠️"
            "call_emergency_ambulance" -> if (isNe) "आपतकालीन एम्बुलेन्स कल" else "CALL EMERGENCY AMBULANCE"
            "triple_tap_sms" -> if (isNe) "आपतकालीन एसएमएसका लागि ३ पटक थिच्नुहोस्" else "Triple-Tap to Send Emergency SMS"
            "send_emergency_sms" -> if (isNe) "आपतकालीन एसएमएस (GPS सहित)" else "Send Emergency SMS (with GPS)"
            "direct_call_102" -> if (isNe) "सिधै १०२ मा डायल गर्नुहोस्" else "Direct Call 102 (Toll-Free)"
            "bls_title" -> if (isNe) "आधारभूत जीवन रक्षा (BLS)" else "Basic Life Support (BLS)"
            "als_title" -> if (isNe) "उन्नत जीवन रक्षा (ALS)" else "Advanced Life Support (ALS)"
            "cardiac_title" -> if (isNe) "कार्डियाक एम्बुलेन्स" else "Cardiac Ambulance"
            "mins" -> if (isNe) "मिनेट" else "mins"
            "profile_title" -> if (isNe) "मेरो मेडिकल प्रोफाइल" else "Medical ID & Profile"
            "edit_profile" -> if (isNe) "सम्पादन गर्नुहोस्" else "Edit Profile"
            "save_changes" -> if (isNe) "सुरक्षित गर्नुहोस्" else "Save Changes"
            "cancel" -> if (isNe) "रद्द गर्नुहोस्" else "Cancel"
            "blood_type" -> if (isNe) "रक्त समूह" else "Blood Group"
            "allergies" -> if (isNe) "औषधी तथा मेडिकल एलर्जी" else "Medical Allergies"
            "emergency_contacts" -> if (isNe) "आपतकालीन सम्पर्कहरू" else "Emergency Contacts"
            "add_contact" -> if (isNe) "सम्पर्क थप्नुहोस्" else "Add Contact"
            "paramedic_notes" -> if (isNe) "प्यारामेडिकका लागि निर्देशन" else "Paramedic Instructions"
            "chronic_conditions" -> if (isNe) "स्वास्थ्य अवस्था / दीर्घरोग" else "Chronic Conditions"
            "organ_donor" -> if (isNe) "अङ्ग दाता" else "Organ Donor"
            "primary_contact" -> if (isNe) "मुख्य आपतकालीन सम्पर्क" else "Primary Contact"
            "call_now" -> if (isNe) "कल गर्नुहोस्" else "Call Now"
            "emergency_ready" -> if (isNe) "आपतकालीन अवस्थाको लागि तयार" else "Emergency Ready"
            "first_responder_notice" -> if (isNe) "एम्बुलेन्स प्यारामेडिकले यो जानकारी तत्काल हेर्न सक्छन्" else "Accessible to paramedics during dispatch"
            "no_allergies" -> if (isNe) "कुनै ज्ञात एलर्जी छैन (NKDA)" else "No known drug allergies (NKDA)"
            "add_allergy_hint" -> if (isNe) "नयाँ एलर्जी लेख्नुहोस् (उदा: Penicillin)..." else "Enter allergy (e.g., Penicillin)..."
            "profile_saved_msg" -> if (isNe) "मेडिकल प्रोफाइल सफलतापूर्वक अद्यावधिक भयो!" else "Medical profile updated successfully!"
            else -> key
        }
    }
}
