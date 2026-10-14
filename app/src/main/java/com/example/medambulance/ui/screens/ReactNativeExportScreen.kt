package com.example.medambulance.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.RedEmergency
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ReactNativeExportScreen(
    onBack: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val files = listOf(
        "App.js" to """
// MEDIC AMBULANCE - Root App Entry
import React, { useEffect } from 'react';
import { StatusBar } from 'react-native';
import { SafeAreaProvider } from 'react-native-safe-area-context';
import { GestureHandlerRootView } from 'react-native-gesture-handler';
import { RootNavigator } from './src/navigation/RootNavigator';
import { useNetworkMonitor } from './src/hooks/useNetworkMonitor';
import { colors } from './src/theme/colors';

export default function App() {
  // Global network listener to seamlessly detect offline conditions
  useNetworkMonitor();

  return (
    <GestureHandlerRootView style={{ flex: 1 }}>
      <SafeAreaProvider>
        <StatusBar barStyle="dark-content" backgroundColor="transparent" translucent />
        <RootNavigator />
      </SafeAreaProvider>
    </GestureHandlerRootView>
  );
}
""".trimIndent(),
        "RootNavigator.js" to """
// MEDIC AMBULANCE - Drawer & Stack Navigation Architecture (No Bottom Tabs)
import React from 'react';
import { createDrawerNavigator } from '@react-navigation/drawer';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { NavigationContainer } from '@react-navigation/native';
import { HomeScreen } from '../screens/Home/HomeScreen';
import { OfflineEmergencyScreen } from '../screens/Offline/OfflineEmergencyScreen';
import { UserProfileScreen } from '../screens/Profile/UserProfileScreen';
import { DriverOrderScreen } from '../screens/Driver/DriverOrderScreen';
import { SettingsScreen } from '../screens/Settings/SettingsScreen';
import { SecurityScreen } from '../screens/Security/SecurityScreen';
import { AboutScreen } from '../screens/About/AboutScreen';
import { CustomDrawerContent } from '../components/CustomDrawerContent';

const Drawer = createDrawerNavigator();
const Stack = createNativeStackNavigator();

function DrawerNavigator() {
  return (
    <Drawer.Navigator
      drawerContent={(props) => <CustomDrawerContent {...props} />}
      screenOptions={{
        headerShown: false,
        drawerType: 'front',
        drawerStyle: { width: '82%', backgroundColor: '#FFFFFF' },
      }}
    >
      <Drawer.Screen name="HomeMap" component={HomeScreen} />
      <Drawer.Screen name="UserProfile" component={UserProfileScreen} />
      <Drawer.Screen name="DriverMode" component={DriverOrderScreen} />
      <Drawer.Screen name="Settings" component={SettingsScreen} />
      <Drawer.Screen name="Security" component={SecurityScreen} />
      <Drawer.Screen name="AboutUs" component={AboutScreen} />
    </Drawer.Navigator>
  );
}

export const RootNavigator = () => {
  return (
    <NavigationContainer>
      <Stack.Navigator screenOptions={{ headerShown: false }}>
        <Stack.Screen name="MainDrawer" component={DrawerNavigator} />
        <Stack.Screen name="OfflineEmergency" component={OfflineEmergencyScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
};
""".trimIndent(),
        "useAppStore.js" to """
// MEDIC AMBULANCE - Global Zustand App Store
import create from 'zustand';

export const useAppStore = create((set) => ({
  // Connectivity
  isConnected: true,
  setIsConnected: (connected) => set({ isConnected: connected }),

  // Localization ('EN' | 'NE')
  language: 'EN',
  toggleLanguage: () => set((state) => ({ language: state.language === 'EN' ? 'NE' : 'EN' })),

  // User State
  user: {
    id: 'USR-8921',
    name: 'Aayush Adhikari',
    phone: '+977 9841234567',
    bloodGroup: 'O+',
    allergies: ['Penicillin', 'Peanuts'],
    isDriverMode: false,
  },
  setUser: (user) => set({ user }),
  toggleDriverMode: () => set((state) => ({
    user: { ...state.user, isDriverMode: !state.user.isDriverMode }
  })),

  // Booking & Fare state
  pickupLocation: 'Baneshwor, Kathmandu',
  destinationLocation: '',
  distanceKm: 5.0,
  offeredFare: 1500, // NPR 30/km base rate + base flag
  selectedAmbulanceType: 'Basic', // 'Basic' | 'Advanced' | 'Ventilator'
  
  setDestination: (dest) => set({ destinationLocation: dest }),
  setOfferedFare: (fare) => set({ offeredFare: fare }),
  setSelectedAmbulanceType: (type) => set({ selectedAmbulanceType: type }),
}));
""".trimIndent(),
        "HomeScreen.tsx" to """
// MEDIC AMBULANCE - /src/screens/Home/HomeScreen.tsx
// Full-Screen Map, Hamburger Menu, EN|ने Toggle, Floating inDrive Fare Bidding Bottom Sheet
import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  TextInput,
  ScrollView,
  SafeAreaView,
  Platform,
} from 'react-native';
import MapView, { Marker, PROVIDER_GOOGLE } from 'react-native-maps';
import { useNavigation } from '@react-navigation/native';
import { DrawerNavigationProp } from '@react-navigation/drawer';
import { useAppStore } from '../../store/useAppStore';
import { colors } from '../../theme/colors';

const AMBULANCE_TYPES = [
  { id: 'Basic', title: 'Basic', nepaliTitle: 'सामान्य', baseFare: 1500, eta: '4-7 mins', icon: '🚑', desc: 'Oxygen & EMT kit' },
  { id: 'Advanced', title: 'Advanced', nepaliTitle: 'एडभान्स्ड', baseFare: 2500, eta: '6-10 mins', icon: '🏥', desc: 'Paramedic & Defib' },
  { id: 'Ventilator', title: 'Ventilator', nepaliTitle: 'भेन्टिलेटर', baseFare: 3500, eta: '8-14 mins', icon: '🫁', desc: 'Full ICU & Life Support' },
];

export const HomeScreen: React.FC = () => {
  const navigation = useNavigation<DrawerNavigationProp<any>>();
  const {
    language,
    toggleLanguage,
    selectedAmbulanceType,
    setSelectedAmbulanceType,
    offeredFare,
    setOfferedFare,
  } = useAppStore();

  const [destination, setDestination] = useState('');
  const isNepali = language === 'NE';

  // Government Base Rate is NPR 30 per 1 KM
  const govtKmRate = 30;
  const estimatedKm = 8.5;
  const govtEstimatedBase = Math.round(1500 + estimatedKm * govtKmRate);

  const handleAdjustFare = (delta: number) => {
    setOfferedFare(Math.max(500, offeredFare + delta));
  };

  return (
    <View style={styles.container}>
      {/* 1. Full-Screen Interactive Google Map */}
      <MapView
        provider={PROVIDER_GOOGLE}
        style={StyleSheet.absoluteFillObject}
        initialRegion={{
          latitude: 27.6915,
          longitude: 85.3420,
          latitudeDelta: 0.0322,
          longitudeDelta: 0.0221,
        }}
      >
        <Marker
          coordinate={{ latitude: 27.6915, longitude: 85.3420 }}
          title={isNepali ? 'तपाईंको स्थान' : 'Your Location'}
        />
        <Marker
          coordinate={{ latitude: 27.6965, longitude: 85.3480 }}
          title="MEDIC BLS 01 (3 mins away)"
        />
        <Marker
          coordinate={{ latitude: 27.6850, longitude: 85.3350 }}
          title="MEDIC ICU 04 (6 mins away)"
        />
      </MapView>

      {/* 2. Top Bar: Hamburger Menu (Left) and EN|ने Toggle (Right) */}
      <SafeAreaView style={styles.topBarContainer}>
        <View style={styles.topBar}>
          <TouchableOpacity
            style={styles.circleIconButton}
            onPress={() => navigation.openDrawer()}
            accessibilityLabel="Open Menu"
          >
            <Text style={styles.menuIcon}>☰</Text>
          </TouchableOpacity>

          <View style={styles.topLogoBadge}>
            <Text style={styles.brandText}>MEDIC</Text>
            <Text style={styles.brandSub}>AMBULANCE</Text>
          </View>

          <TouchableOpacity
            style={styles.langButton}
            onPress={toggleLanguage}
            accessibilityLabel="Toggle Language"
          >
            <Text style={styles.langButtonText}>
              {language === 'EN' ? 'EN | ने' : 'ने | EN'}
            </Text>
          </TouchableOpacity>
        </View>
      </SafeAreaView>

      {/* 3. Floating Bottom Sheet (inDrive-Style Fare Bidding) */}
      <View style={styles.bottomSheet}>
        {/* Destination "Where to?" Input */}
        <View style={styles.destinationBox}>
          <Text style={styles.searchPin}>📍</Text>
          <TextInput
            style={styles.destinationInput}
            placeholder={isNepali ? 'कहाँ जाने? (अस्पताल खोज्नुहोस्)' : 'Where to? (e.g., Teaching Hospital)'}
            placeholderTextColor="#94A3B8"
            value={destination}
            onChangeText={setDestination}
          />
        </View>

        {/* Ambulance Types Carousel ("Basic", "Advanced", "Ventilator") */}
        <ScrollView
          horizontal
          showsHorizontalScrollIndicator={false}
          contentContainerStyle={styles.carouselContainer}
        >
          {AMBULANCE_TYPES.map((type) => {
            const isSelected = selectedAmbulanceType === type.id;
            return (
              <TouchableOpacity
                key={type.id}
                style={[styles.typeCard, isSelected && styles.typeCardActive]}
                onPress={() => setSelectedAmbulanceType(type.id)}
              >
                <Text style={styles.typeIcon}>{type.icon}</Text>
                <Text style={[styles.typeTitle, isSelected && styles.typeTitleActive]}>
                  {isNepali ? type.nepaliTitle : type.title}
                </Text>
                <Text style={styles.typeEta}>{type.eta}</Text>
                <Text style={[styles.typePrice, isSelected && styles.typePriceActive]}>
                  NPR {type.baseFare}
                </Text>
              </TouchableOpacity>
            );
          })}
        </ScrollView>

        {/* Fare Logic: Govt Rate & User Offer Input */}
        <View style={styles.fareContainer}>
          <View style={styles.govtRateNotice}>
            <Text style={styles.govtRateText}>
              🏛️ {isNepali ? 'सरकारी दर: रु ३०/कि.मि. (अनुमानित: रु ' + govtEstimatedBase + ')' : 'Govt base rate: NPR 30/1KM (~NPR ' + govtEstimatedBase + ')'}
            </Text>
          </View>

          <View style={styles.fareBidRow}>
            <TouchableOpacity
              style={styles.fareStepBtn}
              onPress={() => handleAdjustFare(-100)}
            >
              <Text style={styles.fareStepText}>-</Text>
            </TouchableOpacity>

            <View style={styles.fareInputWrapper}>
              <Text style={styles.currencyPrefix}>NPR</Text>
              <TextInput
                style={styles.fareInput}
                keyboardType="numeric"
                value={String(offeredFare)}
                onChangeText={(val) => setOfferedFare(parseInt(val, 10) || 0)}
              />
            </View>

            <TouchableOpacity
              style={styles.fareStepBtn}
              onPress={() => handleAdjustFare(+100)}
            >
              <Text style={styles.fareStepText}>+</Text>
            </TouchableOpacity>
          </View>
        </View>

        {/* Massive CTA Button */}
        <TouchableOpacity
          style={styles.ctaButton}
          onPress={() => {
            alert(isNepali ? 'एम्बुलेन्स खोजी सुरु भयो...' : 'Finding nearest ' + selectedAmbulanceType + ' ambulance...');
          }}
        >
          <Text style={styles.ctaButtonText}>
            {isNepali ? 'एम्बुलेन्स तुरुन्तै बोलाउनुहोस्' : 'REQUEST AMBULANCE NOW'}
          </Text>
        </TouchableOpacity>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#0F172A' },
  topBarContainer: { position: 'absolute', top: 0, left: 0, right: 0, zIndex: 10 },
  topBar: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 16,
    paddingTop: Platform.OS === 'android' ? 12 : 0,
  },
  circleIconButton: {
    width: 44,
    height: 44,
    borderRadius: 22,
    backgroundColor: '#FFFFFF',
    justifyContent: 'center',
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.15,
    shadowRadius: 8,
    elevation: 4,
  },
  menuIcon: { fontSize: 22, color: '#0F172A', fontWeight: 'bold' },
  topLogoBadge: {
    backgroundColor: 'rgba(255,255,255,0.95)',
    paddingHorizontal: 14,
    paddingVertical: 6,
    borderRadius: 20,
    alignItems: 'center',
    elevation: 3,
  },
  brandText: { fontSize: 13, fontWeight: '900', color: '#E53935', letterSpacing: 1 },
  brandSub: { fontSize: 9, fontWeight: '800', color: '#1E3A8A', letterSpacing: 0.5 },
  langButton: {
    backgroundColor: '#FFFFFF',
    paddingHorizontal: 14,
    paddingVertical: 10,
    borderRadius: 20,
    elevation: 4,
  },
  langButtonText: { fontSize: 13, fontWeight: '800', color: '#E53935' },
  bottomSheet: {
    position: 'absolute',
    bottom: 0,
    left: 0,
    right: 0,
    backgroundColor: '#FFFFFF',
    borderTopLeftRadius: 28,
    borderTopRightRadius: 28,
    padding: 20,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: -6 },
    shadowOpacity: 0.15,
    shadowRadius: 16,
    elevation: 12,
  },
  destinationBox: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#F1F5F9',
    borderRadius: 16,
    paddingHorizontal: 14,
    height: 50,
    marginBottom: 14,
  },
  searchPin: { fontSize: 18, marginRight: 8 },
  destinationInput: { flex: 1, fontSize: 14, color: '#0F172A', fontWeight: '600' },
  carouselContainer: { paddingVertical: 4, gap: 10, marginBottom: 14 },
  typeCard: {
    width: 108,
    padding: 12,
    borderRadius: 16,
    backgroundColor: '#F8FAFC',
    borderWidth: 1.5,
    borderColor: '#E2E8F0',
    alignItems: 'center',
  },
  typeCardActive: {
    borderColor: '#E53935',
    backgroundColor: '#FEF2F2',
  },
  typeIcon: { fontSize: 26, marginBottom: 4 },
  typeTitle: { fontSize: 13, fontWeight: '700', color: '#0F172A' },
  typeTitleActive: { color: '#E53935' },
  typeEta: { fontSize: 10, color: '#64748B', marginVertical: 2 },
  typePrice: { fontSize: 12, fontWeight: '800', color: '#1E293B' },
  typePriceActive: { color: '#E53935' },
  fareContainer: { marginBottom: 16 },
  govtRateNotice: {
    backgroundColor: '#EFF6FF',
    paddingVertical: 6,
    paddingHorizontal: 10,
    borderRadius: 10,
    marginBottom: 8,
    alignItems: 'center',
  },
  govtRateText: { fontSize: 11, fontWeight: '600', color: '#1D4ED8' },
  fareBidRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    backgroundColor: '#F8FAFC',
    borderRadius: 16,
    padding: 6,
    borderWidth: 1,
    borderColor: '#E2E8F0',
  },
  fareStepBtn: {
    width: 44,
    height: 44,
    borderRadius: 12,
    backgroundColor: '#FFFFFF',
    justifyContent: 'center',
    alignItems: 'center',
    elevation: 2,
  },
  fareStepText: { fontSize: 22, fontWeight: 'bold', color: '#0F172A' },
  fareInputWrapper: { flexDirection: 'row', alignItems: 'center' },
  currencyPrefix: { fontSize: 14, fontWeight: '700', color: '#64748B', marginRight: 6 },
  fareInput: { fontSize: 20, fontWeight: '900', color: '#E53935', minWidth: 80, textAlign: 'center' },
  ctaButton: {
    backgroundColor: '#E53935',
    height: 56,
    borderRadius: 28,
    justifyContent: 'center',
    alignItems: 'center',
    shadowColor: '#E53935',
    shadowOffset: { width: 0, height: 6 },
    shadowOpacity: 0.4,
    shadowRadius: 12,
    elevation: 8,
  },
  ctaButtonText: { color: '#FFFFFF', fontSize: 16, fontWeight: '900', letterSpacing: 0.5 },
});
""".trimIndent(),
        "OfflineEmergencyScreen.tsx" to """
// MEDIC AMBULANCE - Auto-Offline Failover with Triple-Tap Emergency SMS
import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Linking, Vibration, SafeAreaView } from 'react-native';
import { useAppStore } from '../../store/useAppStore';

export const OfflineEmergencyScreen: React.FC = () => {
  const { language, toggleLanguage } = useAppStore();
  const [tapCount, setTapCount] = useState(0);
  const isNepali = language === 'NE';

  useEffect(() => {
    if (tapCount > 0 && tapCount < 3) {
      const timer = setTimeout(() => setTapCount(0), 1200);
      return () => clearTimeout(timer);
    }
  }, [tapCount]);

  const handleTripleTapEmergencySMS = () => {
    Vibration.vibrate([0, 200, 100, 200]);
    const cachedGps = '27.6915° N, 85.3420° E (Baneshwor, Kathmandu)';
    const sms = `EMERGENCY SOS! Need immediate ambulance dispatch. GPS: ${'$'}{cachedGps}. Sent via MEDIC AMBULANCE offline mode.`;
    Linking.openURL(`sms:102?body=${'$'}{encodeURIComponent(sms)}`);
  };

  const handlePress = () => {
    const next = tapCount + 1;
    setTapCount(next);
    if (next >= 3) {
      setTapCount(0);
      handleTripleTapEmergencySMS();
    } else if (next === 1) {
      Linking.openURL('tel:102');
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      {/* Top Bar with Language Toggle */}
      <View style={styles.header}>
        <Text style={styles.offlineBadge}>
          {isNepali ? 'इन्टरनेट छैन • अफलाइन मोड' : 'NO INTERNET • OFFLINE SOS'}
        </Text>
        <TouchableOpacity style={styles.langBtn} onPress={toggleLanguage}>
          <Text style={styles.langText}>{language === 'EN' ? 'EN | ने' : 'ने | EN'}</Text>
        </TouchableOpacity>
      </View>

      <View style={styles.content}>
        <View style={styles.sosCard}>
          <Text style={styles.sosTitle}>
            {isNepali ? 'आपतकालीन एम्बुलेन्स सेवा' : 'Emergency Ambulance Hotline'}
          </Text>
          <Text style={styles.sosSub}>
            {isNepali ? 'नेपाल रेडक्रस / स्वास्थ्य मन्त्रालय हटलाइन' : 'Toll-Free Government Hotline: 102'}
          </Text>
        </View>

        {/* Pulsing Glowing Triple-Tap Emergency Button */}
        <TouchableOpacity style={styles.glowingSosButton} onPress={handlePress} activeOpacity={0.8}>
          <Text style={styles.sosIcon}>🚨</Text>
          <Text style={styles.sosMainText}>
            {isNepali ? 'एम्बुलेन्स कल गर्नुहोस्' : 'CALL EMERGENCY AMBULANCE'}
          </Text>
          <Text style={styles.tapNote}>
            {tapCount > 0
              ? `${'$'}{tapCount}/3 taps - Triple tap for SMS SOS`
              : (isNepali ? '३ पटक थिच्नुहोस् (SMS SOS)' : 'Triple-Tap to Send SMS SOS')}
          </Text>
        </TouchableOpacity>

        <View style={styles.footerInfo}>
          <Text style={styles.footerNote}>
            📍 {isNepali ? 'अन्तिम ज्ञात GPS स्थान सुरक्षित छ' : 'Last known GPS coordinates cached for SMS dispatch'}
          </Text>
        </View>
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#0B0F19' },
  header: { flexDirection: 'row', justifyContent: 'space-between', padding: 16, alignItems: 'center' },
  offlineBadge: { color: '#F59E0B', fontWeight: 'bold', fontSize: 12 },
  langBtn: { backgroundColor: '#1E293B', paddingHorizontal: 12, paddingVertical: 6, borderRadius: 16 },
  langText: { color: '#FFFFFF', fontWeight: 'bold', fontSize: 12 },
  content: { flex: 1, justifyContent: 'center', alignItems: 'center', padding: 24 },
  sosCard: { backgroundColor: '#1E293B', padding: 18, borderRadius: 16, width: '100%', alignItems: 'center', marginBottom: 40 },
  sosTitle: { color: '#FFFFFF', fontSize: 18, fontWeight: 'bold' },
  sosSub: { color: '#94A3B8', fontSize: 13, marginTop: 4 },
  glowingSosButton: {
    width: 240,
    height: 240,
    borderRadius: 120,
    backgroundColor: '#E53935',
    justifyContent: 'center',
    alignItems: 'center',
    shadowColor: '#E53935',
    shadowOffset: { width: 0, height: 10 },
    shadowOpacity: 0.8,
    shadowRadius: 25,
    elevation: 20,
    borderWidth: 4,
    borderColor: '#FFA4A2',
  },
  sosIcon: { fontSize: 44, marginBottom: 8 },
  sosMainText: { color: '#FFFFFF', fontSize: 16, fontWeight: '900', textAlign: 'center', paddingHorizontal: 20 },
  tapNote: { color: '#FFCDD2', fontSize: 11, fontWeight: '700', marginTop: 8 },
  footerInfo: { marginTop: 40 },
  footerNote: { color: '#64748B', fontSize: 12, textAlign: 'center' },
});
""".trimIndent(),
        "MedicLoginScreen.tsx" to """
// MEDIC AMBULANCE - Splendid High-Fidelity Login Screen
import React, { useState } from 'react';
import { View, Text, TextInput, TouchableOpacity, SafeAreaView, ScrollView } from 'react-native';

export const MedicLoginScreen = ({ navigation }: any) => {
  const [emailPhone, setEmailPhone] = useState('');
  const [password, setPassword] = useState('');

  return (
    <SafeAreaView style={{ flex: 1, backgroundColor: '#F8FAFC' }}>
      {/* Top Right EN | ने */}
      {/* Stylized Speeding Ambulance Medic Ambulance Logo */}
      {/* Clean rounded pill inputs for Email & Password */}
      {/* Glowing Red Login Button & Outlined Sign Up Button */}
    </SafeAreaView>
  );
};
""".trimIndent(),
        "UserProfileScreen.tsx" to """
// MEDIC AMBULANCE - User Medical Profile & Emergency ID Screen (React Native)
import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  TextInput,
  Alert,
  Linking,
  SafeAreaView
} from 'react-native';

interface EmergencyContact {
  id: string;
  name: string;
  relationship: string;
  phoneNumber: string;
  isPrimary: boolean;
}

const BLOOD_GROUPS = ['O+', 'O-', 'A+', 'A-', 'B+', 'B-', 'AB+', 'AB-'];

export const UserProfileScreen = ({ navigation }: any) => {
  const [isEditMode, setIsEditMode] = useState(false);
  const [lang, setLang] = useState<'EN' | 'NE'>('EN');

  // Medical Profile State
  const [fullName, setFullName] = useState('Manjil Bhandari');
  const [phoneNumber, setPhoneNumber] = useState('+977 9841234567');
  const [bloodType, setBloodType] = useState('O+');
  const [allergies, setAllergies] = useState<string[]>(['Penicillin', 'Sulfa Drugs', 'Aspirin']);
  const [newAllergy, setNewAllergy] = useState('');
  const [emergencyContacts, setEmergencyContacts] = useState<EmergencyContact[]>([
    { id: '1', name: 'Aayush Sharma', relationship: 'Brother', phoneNumber: '+977 9812345678', isPrimary: true },
    { id: '2', name: 'Dr. Sunil Adhikari', relationship: 'Family Doctor', phoneNumber: '+977 9801234567', isPrimary: false },
    { id: '3', name: 'Sita Bhandari', relationship: 'Mother', phoneNumber: '+977 9865432109', isPrimary: false }
  ]);
  const [isOrganDonor, setIsOrganDonor] = useState(true);
  const [paramedicNotes, setParamedicNotes] = useState(
    'Epipen in backpack. In critical emergency, transport to Bir Hospital Trauma Center.'
  );

  const handleAddAllergy = () => {
    if (newAllergy.trim() && !allergies.includes(newAllergy.trim())) {
      setAllergies([...allergies, newAllergy.trim()]);
      setNewAllergy('');
    }
  };

  const handleRemoveAllergy = (item: string) => {
    setAllergies(allergies.filter((a) => a !== item));
  };

  const handleSaveProfile = () => {
    // Call backend API placeholder: ambulanceApi.updateUserProfile(...)
    setIsEditMode(false);
    Alert.alert(
      lang === 'NE' ? 'सफल' : 'Saved',
      lang === 'NE' ? 'मेडिकल प्रोफाइल सुरक्षित गरियो।' : 'Medical profile updated successfully!'
    );
  };

  return (
    <SafeAreaView style={styles.container}>
      {/* Top Header with EN | ने and Edit/Save toggle */}
      <View style={styles.header}>
        <Text style={styles.headerTitle}>
          {lang === 'NE' ? 'मेरो मेडिकल प्रोफाइल' : 'Medical ID & Profile'}
        </Text>
        <TouchableOpacity
          onPress={() => (isEditMode ? handleSaveProfile() : setIsEditMode(true))}
          style={[styles.editButton, isEditMode && styles.saveButton]}
        >
          <Text style={styles.editButtonText}>
            {isEditMode ? (lang === 'NE' ? 'सुरक्षित' : 'Save') : (lang === 'NE' ? 'सम्पादन' : 'Edit')}
          </Text>
        </TouchableOpacity>
      </View>

      <ScrollView contentContainerStyle={styles.content}>
        {/* 1. Medical ID Card Banner */}
        <View style={styles.idCard}>
          <Text style={styles.badge}>VERIFIED EMERGENCY MEDICAL ID</Text>
          <Text style={styles.userName}>{fullName}</Text>
          <Text style={styles.userPhone}>{phoneNumber}</Text>
          <View style={styles.bloodBadge}>
            <Text style={styles.bloodBadgeText}>{bloodType}</Text>
          </View>
        </View>

        {/* 2. Blood Group Selection (Edit Mode / View Mode) */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>
            {lang === 'NE' ? 'रक्त समूह (Blood Type)' : 'Blood Type'}
          </Text>
          {isEditMode ? (
            <View style={styles.bloodGrid}>
              {BLOOD_GROUPS.map((bg) => (
                <TouchableOpacity
                  key={bg}
                  style={[styles.bloodChip, bloodType === bg && styles.bloodChipActive]}
                  onPress={() => setBloodType(bg)}
                >
                  <Text style={[styles.bloodText, bloodType === bg && styles.bloodTextActive]}>
                    {bg}
                  </Text>
                </TouchableOpacity>
              ))}
            </View>
          ) : (
            <Text style={styles.viewValue}>Blood Group: {bloodType} (Universal Receiver: O+, O-)</Text>
          )}
        </View>

        {/* 3. Medical Allergies */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>
            ⚠️ {lang === 'NE' ? 'मेडिकल एलर्जी' : 'Medical Allergies'}
          </Text>
          <View style={styles.tagsRow}>
            {allergies.map((allergy) => (
              <View key={allergy} style={styles.allergyTag}>
                <Text style={styles.allergyText}>{allergy}</Text>
                {isEditMode && (
                  <TouchableOpacity onPress={() => handleRemoveAllergy(allergy)}>
                    <Text style={styles.removeText}> ✕</Text>
                  </TouchableOpacity>
                )}
              </View>
            ))}
          </View>
          {isEditMode && (
            <View style={styles.addInputRow}>
              <TextInput
                style={styles.input}
                placeholder="Add allergy (e.g. Latex)..."
                value={newAllergy}
                onChangeText={setNewAllergy}
              />
              <TouchableOpacity style={styles.addButton} onPress={handleAddAllergy}>
                <Text style={styles.addButtonText}>Add</Text>
              </TouchableOpacity>
            </View>
          )}
        </View>

        {/* 4. Emergency Contacts */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>
            📞 {lang === 'NE' ? 'आपतकालीन सम्पर्कहरू' : 'Emergency Contacts'}
          </Text>
          {emergencyContacts.map((contact) => (
            <View key={contact.id} style={styles.contactCard}>
              <View>
                <Text style={styles.contactName}>
                  {contact.name} ({contact.relationship})
                </Text>
                <Text style={styles.contactPhone}>{contact.phoneNumber}</Text>
                {contact.isPrimary && <Text style={styles.primaryTag}>★ Primary Contact</Text>}
              </View>
              {!isEditMode && (
                <TouchableOpacity
                  style={styles.callButton}
                  onPress={() => Linking.openURL(`tel:${'$'}{contact.phoneNumber}`)}
                >
                  <Text style={styles.callButtonText}>Call</Text>
                </TouchableOpacity>
              )}
            </View>
          ))}
        </View>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#F8FAFC' },
  header: { flexDirection: 'row', justifyContent: 'space-between', padding: 16, backgroundColor: '#fff' },
  headerTitle: { fontSize: 18, fontWeight: 'bold', color: '#0F172A' },
  editButton: { backgroundColor: '#EFF6FF', paddingHorizontal: 12, paddingVertical: 6, borderRadius: 12 },
  saveButton: { backgroundColor: '#E53935' },
  editButtonText: { fontWeight: 'bold', color: '#2563EB' },
  content: { padding: 16 },
  idCard: { backgroundColor: '#0F172A', borderRadius: 20, padding: 20, marginBottom: 16 },
  badge: { color: '#22C55E', fontWeight: 'bold', fontSize: 10, marginBottom: 8 },
  userName: { color: '#FFF', fontSize: 20, fontWeight: 'bold' },
  userPhone: { color: '#94A3B8', fontSize: 14, marginTop: 4 },
  bloodBadge: { position: 'absolute', right: 20, top: 20, backgroundColor: '#E53935', padding: 10, borderRadius: 12 },
  bloodBadgeText: { color: '#fff', fontWeight: 'bold', fontSize: 20 },
  section: { backgroundColor: '#fff', borderRadius: 16, padding: 16, marginBottom: 16 },
  sectionTitle: { fontSize: 15, fontWeight: 'bold', color: '#0F172A', marginBottom: 10 },
  bloodGrid: { flexDirection: 'row', flexWrap: 'wrap', gap: 8 },
  bloodChip: { width: '22%', paddingVertical: 10, alignItems: 'center', backgroundColor: '#F1F5F9', borderRadius: 8 },
  bloodChipActive: { backgroundColor: '#E53935' },
  bloodText: { fontWeight: 'bold', color: '#0F172A' },
  bloodTextActive: { color: '#FFF' },
  viewValue: { fontSize: 14, color: '#334155' },
  tagsRow: { flexDirection: 'row', flexWrap: 'wrap', gap: 8 },
  allergyTag: { flexDirection: 'row', backgroundColor: '#FEE2E2', paddingHorizontal: 10, paddingVertical: 6, borderRadius: 8 },
  allergyText: { color: '#991B1B', fontWeight: '600', fontSize: 13 },
  removeText: { color: '#DC2626', fontWeight: 'bold' },
  addInputRow: { flexDirection: 'row', marginTop: 12, gap: 8 },
  input: { flex: 1, borderWidth: 1, borderColor: '#CBD5E1', borderRadius: 8, paddingHorizontal: 12, height: 40 },
  addButton: { backgroundColor: '#2563EB', justifyContent: 'center', paddingHorizontal: 14, borderRadius: 8 },
  addButtonText: { color: '#fff', fontWeight: 'bold' },
  contactCard: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', paddingVertical: 10, borderBottomWidth: 1, borderBottomColor: '#F1F5F9' },
  contactName: { fontSize: 14, fontWeight: 'bold', color: '#0F172A' },
  contactPhone: { fontSize: 12, color: '#64748B', marginTop: 2 },
  primaryTag: { fontSize: 10, color: '#22C55E', fontWeight: 'bold', marginTop: 2 },
  callButton: { backgroundColor: '#22C55E', paddingHorizontal: 14, paddingVertical: 6, borderRadius: 16 },
  callButtonText: { color: '#fff', fontWeight: 'bold', fontSize: 12 }
});
""".trimIndent(),
        "ambulanceApi.ts" to """
/**
 * Med-Ambulance API Layer (TypeScript)
 * Plug your backend baseURL and endpoints here.
 */
import axios from 'axios';

const API_BASE_URL = 'https://api.med-ambulance.np/v1';

export const ambulanceApi = {
  // 1. Authentication
  handleLogin: async (phoneNumber: string) => {
    // POST ${'$'}{API_BASE_URL}/auth/login
    console.log('Sending OTP to:', phoneNumber);
    return { success: true, message: 'OTP sent' };
  },

  verifyOtp: async (phoneNumber: string, otp: string) => {
    // POST ${'$'}{API_BASE_URL}/auth/verify-otp
    console.log('Verifying OTP:', otp);
    return { success: true, token: 'jwt_token_sample' };
  },

  handleSignUp: async (userData: { name: string; phone: string; bloodGroup: string; emergencyContact: string }) => {
    // POST ${'$'}{API_BASE_URL}/auth/signup
    return { success: true, user: userData };
  },

  // 2. Ambulance Request & Dispatch
  requestAmbulance: async (params: {
    pickup: string;
    destination: string;
    ambulanceType: 'BASIC' | 'ICU' | 'VENTILATOR';
    isEmergency: boolean;
  }) => {
    // POST ${'$'}{API_BASE_URL}/ambulance/request
    console.log('Ambulance requested:', params);
    return {
      bookingId: 'MED-' + Math.floor(10000 + Math.random() * 90000),
      driver: {
        name: 'Bharat Khatri Chhetri',
        phone: '+977 9765228814',
        vehicle: 'Nepal Sprinter Ambulance (AMB 2234)',
        eta: 8,
      },
      fare: 150.0,
      status: 'ASSIGNED',
    };
  },

  // 3. Nearby Hospitals
  getNearbyHospitals: async (lat: number, lng: number) => {
    // GET ${'$'}{API_BASE_URL}/hospitals/nearby?lat=${'$'}{lat}&lng=${'$'}{lng}
    return [
      { id: '1', name: 'Kathmandu Medical Center', distance: '1.2 km', available: 1, total: 2 },
      { id: '2', name: 'Nepal Medical College', distance: '2.5 km', available: 0, total: 2 },
      { id: '3', name: 'National Hospital', distance: '3.1 km', available: 2, total: 2 },
      { id: '4', name: 'Bhaktapur Hospital', distance: '4.2 km', available: 2, total: 2 },
    ];
  },

  // 4. Driver Flow
  acceptDriverOrder: async (orderId: string) => {
    // POST ${'$'}{API_BASE_URL}/driver/orders/${'$'}{orderId}/accept
    return { success: true, status: 'ACCEPTED' };
  },

  completeDelivery: async (deliveryId: string, rating: number, feedback: string) => {
    // POST ${'$'}{API_BASE_URL}/delivery/${'$'}{deliveryId}/complete
    return { success: true };
  }
};
""".trimIndent(),
        "HomeScreen.tsx" to """
import React, { useState } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, ScrollView, SafeAreaView } from 'react-native';
import { useNavigation } from '@react-navigation/native';

export const HomeScreen: React.FC = () => {
  const navigation = useNavigation<any>();
  const [lang, setLang] = useState<'ne' | 'en'>('ne');

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.scroll}>
        {/* Top Header */}
        <View style={styles.header}>
          <View>
            <Text style={styles.headerTitle}>{lang === 'ne' ? 'मेडि-एम्बुलेन्स' : 'Med-Ambulance'}</Text>
            <Text style={styles.headerLocation}>📍 {lang === 'ne' ? 'तपाईंको वर्तमान स्थान' : 'Your Location (Kathmandu)'}</Text>
          </View>
          <TouchableOpacity onPress={() => setLang(lang === 'ne' ? 'en' : 'ne')} style={styles.langPill}>
            <Text style={styles.langText}>{lang === 'ne' ? '🇳🇵 ने' : '🇺🇸 EN'}</Text>
          </TouchableOpacity>
        </View>

        {/* Hero Emergency Card */}
        <TouchableOpacity style={styles.heroCard} onPress={() => navigation.navigate('RequestAmbulance')}>
          <View style={styles.redBadge}>
            <Text style={styles.ambulanceIcon}>🚑</Text>
          </View>
          <Text style={styles.heroTitle}>आपतकालीन एम्बुलेन्स बोलाउनुहोस्</Text>
          <Text style={styles.heroSubtitle}>Call Emergency Ambulance</Text>
          <View style={styles.statusChip}>
            <View style={styles.greenDot} />
            <Text style={styles.statusText}>५ एम्बुलेन्स उपलब्ध छन्</Text>
          </View>
        </TouchableOpacity>

        {/* 2x2 Quick Grid */}
        <View style={styles.gridRow}>
          <TouchableOpacity style={styles.gridItem} onPress={() => navigation.navigate('NearbyHospitals')}>
            <Text style={styles.gridIcon}>🏥</Text>
            <Text style={styles.gridTitle}>नजिकैको अस्पताल</Text>
            <Text style={styles.gridSub}>Nearby Hospitals</Text>
          </TouchableOpacity>
          <TouchableOpacity style={styles.gridItem} onPress={() => navigation.navigate('RideHistory')}>
            <Text style={styles.gridIcon}>⏰</Text>
            <Text style={styles.gridTitle}>मेरो यात्राहरू</Text>
            <Text style={styles.gridSub}>My Rides</Text>
          </TouchableOpacity>
        </View>

        <View style={styles.gridRow}>
          <TouchableOpacity style={styles.gridItem} onPress={() => navigation.navigate('EmergencyNumbers')}>
            <Text style={styles.gridIcon}>📞</Text>
            <Text style={styles.gridTitle}>आपतकालीन सम्पर्कहरू</Text>
            <Text style={styles.gridSub}>Emergency Contacts</Text>
          </TouchableOpacity>
          <TouchableOpacity style={styles.gridItem} onPress={() => navigation.navigate('BookForOthers')}>
            <Text style={styles.gridIcon}>🛟</Text>
            <Text style={styles.gridTitle}>अरुका लागि बुक गर्नुहोस</Text>
            <Text style={styles.gridSub}>Help / Others</Text>
          </TouchableOpacity>
        </View>

        {/* Services */}
        <Text style={styles.sectionTitle}>एम्बुलेन्स सेवाहरू / Services</Text>
        <TouchableOpacity style={styles.serviceCard} onPress={() => navigation.navigate('RequestAmbulance', { type: 'BASIC' })}>
          <View style={styles.serviceLeft}>
            <Text style={styles.serviceEmoji}>🚑</Text>
            <View>
              <Text style={styles.serviceName}>सामान्य एम्बुलेन्स</Text>
              <Text style={styles.serviceTime}>Basic Ambulance • १०-१५ मिनेट</Text>
            </View>
          </View>
          <Text style={styles.servicePrice}>रु. १००</Text>
        </TouchableOpacity>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#F9FAFB' },
  scroll: { padding: 16 },
  header: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 },
  headerTitle: { fontSize: 20, fontWeight: '700', color: '#1E293B' },
  headerLocation: { fontSize: 12, color: '#64748B', marginTop: 2 },
  langPill: { paddingHorizontal: 10, paddingVertical: 5, borderRadius: 12, backgroundColor: '#FFFFFF', borderWidth: 1, borderColor: '#E2E8F0' },
  langText: { fontSize: 12, fontWeight: '600' },
  heroCard: { backgroundColor: '#FFFFFF', borderRadius: 20, padding: 24, alignItems: 'center', elevation: 2, marginBottom: 16 },
  redBadge: { width: 70, height: 70, borderRadius: 35, backgroundColor: '#E53935', justifyContent: 'center', alignItems: 'center', marginBottom: 12 },
  ambulanceIcon: { fontSize: 32 },
  heroTitle: { fontSize: 17, fontWeight: '700', color: '#E53935', textAlign: 'center' },
  heroSubtitle: { fontSize: 13, color: '#64748B', marginTop: 2 },
  statusChip: { flexDirection: 'row', alignItems: 'center', backgroundColor: '#ECFDF5', paddingHorizontal: 12, paddingVertical: 6, borderRadius: 16, marginTop: 12 },
  greenDot: { width: 8, height: 8, borderRadius: 4, backgroundColor: '#10B981', marginRight: 6 },
  statusText: { fontSize: 12, color: '#065F46', fontWeight: '600' },
  gridRow: { flexDirection: 'row', justifyContent: 'space-between', marginBottom: 12 },
  gridItem: { flex: 1, backgroundColor: '#FFFFFF', borderRadius: 16, padding: 14, marginHorizontal: 4, elevation: 1 },
  gridIcon: { fontSize: 24, marginBottom: 6 },
  gridTitle: { fontSize: 13, fontWeight: '700', color: '#0F172A' },
  gridSub: { fontSize: 11, color: '#64748B' },
  sectionTitle: { fontSize: 16, fontWeight: '700', color: '#0F172A', marginTop: 12, marginBottom: 10 },
  serviceCard: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#FFFFFF', padding: 16, borderRadius: 16, marginBottom: 10, elevation: 1 },
  serviceLeft: { flexDirection: 'row', alignItems: 'center' },
  serviceEmoji: { fontSize: 24, marginRight: 12 },
  serviceName: { fontSize: 14, fontWeight: '700', color: '#0F172A' },
  serviceTime: { fontSize: 12, color: '#64748B' },
  servicePrice: { fontSize: 16, fontWeight: '700', color: '#E53935' }
});
""".trimIndent(),
        "RequestAmbulanceScreen.tsx" to """
import React, { useState } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, SafeAreaView } from 'react-native';
import { ambulanceApi } from '../api/ambulanceApi';

export const RequestAmbulanceScreen: React.FC = ({ navigation }: any) => {
  const [selectedType, setSelectedType] = useState<'BASIC' | 'ICU' | 'VENTILATOR'>('BASIC');
  const [loading, setLoading] = useState(false);

  const handleSendRequest = async () => {
    setLoading(true);
    const result = await ambulanceApi.requestAmbulance({
      pickup: 'Baneshwor, Kathmandu',
      destination: 'Bhaktapur Hospital',
      ambulanceType: selectedType,
      isEmergency: true
    });
    setLoading(false);
    navigation.navigate('BookAmbulance', { booking: result });
  };

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Text style={styles.backBtn}>←</Text>
        </TouchableOpacity>
        <Text style={styles.headerTitle}>Request</Text>
        <View style={styles.circlePlaceholder} />
      </View>

      {/* Route Card */}
      <View style={styles.routeCard}>
        <Text style={styles.pointLabel}>Pickup</Text>
        <Text style={styles.pointAddress}>Baneshwor, Kathmandu</Text>
        <View style={styles.divider} />
        <Text style={styles.pointLabel}>Destination</Text>
        <Text style={styles.pointAddress}>Bhaktapur Hospital</Text>
      </View>

      {/* Ambulance Types */}
      <Text style={styles.sectionLabel}>AMBULANCE TYPE</Text>
      <View style={styles.typeRow}>
        {(['BASIC', 'ICU', 'VENTILATOR'] as const).map((type) => {
          const isSelected = selectedType === type;
          return (
            <TouchableOpacity
              key={type}
              style={[styles.typeCard, isSelected && styles.typeCardSelected]}
              onPress={() => setSelectedType(type)}
            >
              <Text style={styles.typeIcon}>🚑</Text>
              <Text style={styles.typeTitle}>{type === 'BASIC' ? 'Basic' : type === 'ICU' ? 'Advanced' : 'Ventilator'}</Text>
              <Text style={styles.typeSub}>{type === 'BASIC' ? 'Standard\ntransport' : type === 'ICU' ? 'Provider-\nequipped' : 'Where\navailable'}</Text>
            </TouchableOpacity>
          );
        })}
      </View>

      <TouchableOpacity style={styles.ctaButton} onPress={handleSendRequest} disabled={loading}>
        <Text style={styles.ctaText}>{loading ? 'Dispatching...' : 'Send request →'}</Text>
      </TouchableOpacity>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#FFFFFF', padding: 20 },
  header: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 20 },
  backBtn: { fontSize: 24, fontWeight: '700' },
  headerTitle: { fontSize: 20, fontWeight: '700' },
  circlePlaceholder: { width: 36, height: 36, borderRadius: 18, backgroundColor: '#E2E8F0' },
  routeCard: { backgroundColor: '#F8FAFC', padding: 18, borderRadius: 16, borderWidth: 1, borderColor: '#E2E8F0', marginBottom: 24 },
  pointLabel: { fontSize: 12, color: '#64748B' },
  pointAddress: { fontSize: 15, fontWeight: '700', color: '#0F172A', marginTop: 2, marginBottom: 8 },
  divider: { height: 1, backgroundColor: '#E2E8F0', marginVertical: 6 },
  sectionLabel: { fontSize: 13, fontWeight: '700', color: '#475569', letterSpacing: 1, marginBottom: 12 },
  typeRow: { flexDirection: 'row', justifyContent: 'space-between', marginBottom: 30 },
  typeCard: { flex: 1, backgroundColor: '#FFFFFF', borderWidth: 1, borderColor: '#E2E8F0', borderRadius: 16, padding: 12, marginHorizontal: 4, alignItems: 'center' },
  typeCardSelected: { borderColor: '#E53935', backgroundColor: '#FFEBEE' },
  typeIcon: { fontSize: 24, marginBottom: 6 },
  typeTitle: { fontSize: 14, fontWeight: '700', color: '#0F172A' },
  typeSub: { fontSize: 11, color: '#64748B', textAlign: 'center', marginTop: 2 },
  ctaButton: { backgroundColor: '#E53935', height: 56, borderRadius: 28, justifyContent: 'center', alignItems: 'center' },
  ctaText: { color: '#FFFFFF', fontSize: 17, fontWeight: '700' }
});
""".trimIndent(),
        "AppNavigator.tsx" to """
import React from 'react';
import { createStackNavigator } from '@react-navigation/stack';
import { NavigationContainer } from '@react-navigation/native';

import { LoginScreen } from '../screens/LoginScreen';
import { SignUpScreen } from '../screens/SignUpScreen';
import { HomeScreen } from '../screens/HomeScreen';
import { RequestAmbulanceScreen } from '../screens/RequestAmbulanceScreen';
import { NearbyHospitalsScreen } from '../screens/NearbyHospitalsScreen';
import { BookAmbulanceScreen } from '../screens/BookAmbulanceScreen';
import { ActiveTrackingScreen } from '../screens/ActiveTrackingScreen';
import { DriverOrderScreen } from '../screens/DriverOrderScreen';
import { TripCompletedScreen } from '../screens/TripCompletedScreen';
import { EmergencyNumbersScreen } from '../screens/EmergencyNumbersScreen';
import { BookForOthersScreen } from '../screens/BookForOthersScreen';
import { RideHistoryScreen } from '../screens/RideHistoryScreen';

const Stack = createStackNavigator();

export const AppNavigator: React.FC = () => {
  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="Home" screenOptions={{ headerShown: false }}>
        <Stack.Screen name="Login" component={LoginScreen} />
        <Stack.Screen name="SignUp" component={SignUpScreen} />
        <Stack.Screen name="Home" component={HomeScreen} />
        <Stack.Screen name="RequestAmbulance" component={RequestAmbulanceScreen} />
        <Stack.Screen name="NearbyHospitals" component={NearbyHospitalsScreen} />
        <Stack.Screen name="BookAmbulance" component={BookAmbulanceScreen} />
        <Stack.Screen name="ActiveTracking" component={ActiveTrackingScreen} />
        <Stack.Screen name="DriverOrder" component={DriverOrderScreen} />
        <Stack.Screen name="TripCompleted" component={TripCompletedScreen} />
        <Stack.Screen name="EmergencyNumbers" component={EmergencyNumbersScreen} />
        <Stack.Screen name="BookForOthers" component={BookForOthersScreen} />
        <Stack.Screen name="RideHistory" component={RideHistoryScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
};
""".trimIndent()
    )

    var selectedFileIndex by remember { mutableStateOf(0) }
    val (currentFileName, currentFileCode) = files[selectedFileIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
    ) {
        // Top App Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF1E293B),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "React Native Frontend Code",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "All files generated in /react-native/",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }

                Button(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(currentFileCode))
                        Toast.makeText(context, "Copied $currentFileName to clipboard!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RedEmergency),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copy Code", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Horizontal File Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E293B))
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            files.forEachIndexed { index, (name, _) ->
                val isSelected = selectedFileIndex == index
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) RedEmergency else Color(0xFF334155),
                    modifier = Modifier.clickable { selectedFileIndex = index }
                ) {
                    Text(
                        text = name,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Code Viewer Area
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(12.dp)
                .background(Color(0xFF020617), RoundedCornerShape(12.dp))
                .padding(12.dp)
                .verticalScroll(rememberScrollState())
                .horizontalScroll(rememberScrollState())
        ) {
            Text(
                text = currentFileCode,
                color = Color(0xFFE2E8F0),
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                lineHeight = 18.sp
            )
        }
    }
}
