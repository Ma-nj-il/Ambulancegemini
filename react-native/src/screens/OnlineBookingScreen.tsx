import React, { useState } from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  SafeAreaView,
  ScrollView,
  TextInput,
  ActivityIndicator,
  Dimensions
} from 'react-native';
import { Colors } from '../theme/colors';
import { ambulanceApi } from '../api/ambulanceApi';

const { width } = Dimensions.get('window');

export const OnlineBookingScreen: React.FC<{ navigation: any }> = ({ navigation }) => {
  const [lang, setLang] = useState<'EN' | 'NE'>('EN');
  const [selectedType, setSelectedType] = useState<'BLS' | 'ALS' | 'CARDIAC'>('BLS');
  const [offeredFare, setOfferedFare] = useState<number>(1500);
  const [loading, setLoading] = useState<boolean>(false);
  const [destination, setDestination] = useState<string>('');

  const isNe = lang === 'NE';

  const ambulanceOptions = [
    {
      id: 'BLS',
      title: isNe ? 'आधारभूत (BLS)' : 'Basic Life Support (BLS)',
      sub: isNe ? 'मानक आपतकालीन' : 'Standard transport',
      eta: '5 mins',
      price: 'NPR 1,500',
      defaultFare: 1500,
      icon: '🚑',
    },
    {
      id: 'ALS',
      title: isNe ? 'उन्नत (ALS)' : 'Advanced Life Support (ALS)',
      sub: isNe ? 'आईसीयू सुसज्जित' : 'ICU-equipped',
      eta: '5 mins',
      price: 'NPR 2,500',
      defaultFare: 2500,
      icon: '🏥',
    },
    {
      id: 'CARDIAC',
      title: isNe ? 'कार्डियाक एम्बुलेन्स' : 'Cardiac Ambulance',
      sub: isNe ? 'कार्डियाक टोली सहित' : 'ICU + Cardiac team',
      eta: '5 mins',
      price: 'NPR 3,500',
      defaultFare: 3500,
      icon: '❤️',
    },
  ];

  const handleRequest = async () => {
    setLoading(true);
    try {
      const res = await ambulanceApi.requestAmbulance({
        pickupLocation: 'Baneshwor, Kathmandu',
        dropLocation: destination || 'Bir Hospital / Nearest Hospital',
        ambulanceType: selectedType === 'BLS' ? 'BASIC' : selectedType === 'ALS' ? 'ICU' : 'VENTILATOR',
        isEmergency: true,
      });
      navigation.navigate('ActiveTracking', { booking: res });
    } finally {
      setLoading(false);
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      {/* Full-Screen 3D Map Simulation View */}
      <View style={styles.mapContainer}>
        {/* Grid street layout */}
        <View style={styles.mapGrid}>
          <View style={styles.streetRoadH} />
          <View style={styles.streetRoadV} />
          <View style={styles.goldenRouteLine} />

          {/* User Pulsing Radar Aura */}
          <View style={styles.userLocationMarker}>
            <View style={styles.pulseAura} />
            <View style={styles.userDot} />
          </View>

          {/* 3D Moving Ambulance Markers */}
          <View style={[styles.miniAmbulance, { top: '25%', left: '30%' }]}>
            <Text style={styles.ambulanceEmoji}>🚑</Text>
          </View>
          <View style={[styles.miniAmbulance, { top: '35%', right: '20%' }]}>
            <Text style={styles.ambulanceEmoji}>🚑</Text>
          </View>
        </View>

        {/* Floating Re-center Crosshair */}
        <TouchableOpacity style={styles.recenterBtn}>
          <Text style={styles.recenterIcon}>⌖</Text>
        </TouchableOpacity>
      </View>

      {/* Top Glassmorphic Navigation Bar */}
      <View style={styles.topBar}>
        <View style={styles.glassHeader}>
          {/* User Profile */}
          <TouchableOpacity
            style={styles.profileSection}
            onPress={() => navigation.navigate('Login')}
          >
            <View style={styles.avatarCircle}>
              <Text style={{ fontSize: 16 }}>👤</Text>
            </View>
            <View style={{ marginLeft: 8 }}>
              <Text style={styles.userTitle}>{isNe ? 'हालको प्रयोगकर्ता' : 'Current user'}</Text>
              <Text style={styles.userSub}>{isNe ? 'बानेश्वर, काठमाडौँ' : "Baneshwor, Kathmandu"}</Text>
            </View>
          </TouchableOpacity>

          {/* Offline Mode Switcher & EN|ने Toggle */}
          <View style={styles.topRightControls}>
            <TouchableOpacity
              style={styles.offlineChip}
              onPress={() => navigation.navigate('OfflineEmergency')}
            >
              <Text style={styles.offlineChipText}>⚠️ {isNe ? 'अफलाइन' : 'Offline'}</Text>
            </TouchableOpacity>

            <TouchableOpacity
              style={styles.langPill}
              onPress={() => setLang(lang === 'EN' ? 'NE' : 'EN')}
            >
              <Text style={[styles.langText, lang === 'EN' && styles.langActive]}>EN</Text>
              <Text style={styles.langDivider}> | </Text>
              <Text style={[styles.langText, lang === 'NE' && styles.langActive]}>ने</Text>
            </TouchableOpacity>
          </View>
        </View>

        {/* Floating Search Pill */}
        <View style={styles.searchBarContainer}>
          <Text style={styles.searchIcon}>🔍</Text>
          <TextInput
            style={styles.searchInput}
            placeholder={isNe ? 'आपतकालीन अवस्था कहाँ छ?' : 'Where is the emergency?'}
            placeholderTextColor="#94A3B8"
            value={destination}
            onChangeText={setDestination}
          />
          <TouchableOpacity>
            <Text style={styles.scanIcon}>🔲</Text>
          </TouchableOpacity>
        </View>
      </View>

      {/* Bottom Floating InDrive / Pathao Card */}
      <View style={styles.bottomCard}>
        <View style={styles.grabHandle} />

        <Text style={styles.sectionTitle}>
          {isNe ? 'एम्बुलेन्स प्रकार छान्नुहोस्' : 'Select Ambulance Type'}
        </Text>

        {/* Horizontal Carousel of Ambulances */}
        <ScrollView
          horizontal
          showsHorizontalScrollIndicator={false}
          contentContainerStyle={styles.carouselContainer}
        >
          {ambulanceOptions.map((opt) => {
            const isSel = selectedType === opt.id;
            return (
              <TouchableOpacity
                key={opt.id}
                style={[styles.carouselCard, isSel && styles.carouselCardActive]}
                onPress={() => {
                  setSelectedType(opt.id as any);
                  setOfferedFare(opt.defaultFare);
                }}
              >
                <View style={[styles.ambulancePreviewBox, isSel && { backgroundColor: '#FFFFFF' }]}>
                  <Text style={{ fontSize: 26 }}>{opt.icon}</Text>
                </View>
                <Text style={styles.cardTitle}>{opt.title}</Text>
                <Text style={styles.cardEta}>🕒 {opt.eta}</Text>
                <Text style={[styles.cardPrice, isSel && { color: Colors.primaryRed }]}>{opt.price}</Text>
              </TouchableOpacity>
            );
          })}
        </ScrollView>

        {/* inDrive Fare Bidding Row */}
        <View style={styles.fareRow}>
          <View>
            <Text style={styles.fareLabel}>{isNe ? 'आफ्नो भाडा प्रस्ताव' : 'Offer Your Fare'}</Text>
            <Text style={styles.fareValue}>NPR {offeredFare.toLocaleString()}</Text>
          </View>

          <View style={styles.counterRow}>
            <TouchableOpacity
              style={styles.suggestPill}
              onPress={() => setOfferedFare(selectedType === 'BLS' ? 1500 : selectedType === 'ALS' ? 2500 : 3500)}
            >
              <Text style={styles.suggestText}>{isNe ? 'सुझावित' : 'Suggest a fare'}</Text>
            </TouchableOpacity>

            <TouchableOpacity
              style={styles.counterBtn}
              onPress={() => setOfferedFare((prev) => Math.max(500, prev - 100))}
            >
              <Text style={styles.counterBtnText}>-</Text>
            </TouchableOpacity>

            <TouchableOpacity
              style={styles.counterBtn}
              onPress={() => setOfferedFare((prev) => prev + 100)}
            >
              <Text style={styles.counterBtnText}>+</Text>
            </TouchableOpacity>
          </View>
        </View>

        {/* Massive Glowing Red CTA */}
        <TouchableOpacity
          style={styles.requestCta}
          onPress={handleRequest}
          disabled={loading}
          activeOpacity={0.88}
        >
          {loading ? (
            <ActivityIndicator color="#FFFFFF" />
          ) : (
            <Text style={styles.requestCtaText}>
              {isNe ? 'एम्बुलेन्स तुरुन्तै बोलाउनुहोस्' : 'REQUEST AMBULANCE NOW'}
            </Text>
          )}
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#E5E7EB' },
  mapContainer: { flex: 1, backgroundColor: '#E2E8F0', overflow: 'hidden' },
  mapGrid: { flex: 1, backgroundColor: '#F1F5F9' },
  streetRoadH: { position: 'absolute', top: '40%', width: '100%', height: 36, backgroundColor: '#FFFFFF', transform: [{ rotate: '-25deg' }] },
  streetRoadV: { position: 'absolute', left: '45%', height: '100%', width: 36, backgroundColor: '#FFFFFF', transform: [{ rotate: '35deg' }] },
  goldenRouteLine: { position: 'absolute', top: '35%', left: '20%', width: 220, height: 6, backgroundColor: '#F59E0B', borderRadius: 3, transform: [{ rotate: '-20deg' }] },
  userLocationMarker: { position: 'absolute', top: '45%', left: '50%', transform: [{ translateX: -15 }, { translateY: -15 }] },
  pulseAura: { width: 38, height: 38, borderRadius: 19, backgroundColor: 'rgba(37, 99, 235, 0.25)', position: 'absolute', top: -4, left: -4 },
  userDot: { width: 30, height: 30, borderRadius: 15, backgroundColor: Colors.primaryBlue, borderWidth: 3, borderColor: '#FFFFFF' },
  miniAmbulance: { position: 'absolute', width: 36, height: 24, backgroundColor: '#FFFFFF', borderRadius: 6, justifyContent: 'center', alignItems: 'center', elevation: 3, shadowColor: '#000', shadowOpacity: 0.2, shadowRadius: 3 },
  ambulanceEmoji: { fontSize: 16 },
  recenterBtn: { position: 'absolute', bottom: 310, right: 18, width: 44, height: 44, borderRadius: 22, backgroundColor: '#FFFFFF', justifyContent: 'center', alignItems: 'center', elevation: 5, shadowColor: '#000', shadowOpacity: 0.15, shadowRadius: 5 },
  recenterIcon: { fontSize: 20, color: '#1E293B' },
  topBar: { position: 'absolute', top: 12, left: 16, right: 16 },
  glassHeader: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: 'rgba(255, 255, 255, 0.94)', borderRadius: 20, paddingHorizontal: 14, paddingVertical: 8, elevation: 4 },
  profileSection: { flexDirection: 'row', alignItems: 'center' },
  avatarCircle: { width: 34, height: 34, borderRadius: 17, backgroundColor: '#E2E8F0', justifyContent: 'center', alignItems: 'center' },
  userTitle: { fontSize: 13, fontWeight: '700', color: Colors.textPrimary },
  userSub: { fontSize: 11, color: Colors.textSecondary },
  topRightControls: { flexDirection: 'row', alignItems: 'center' },
  offlineChip: { backgroundColor: '#FEF3C7', paddingHorizontal: 8, paddingVertical: 5, borderRadius: 10, marginRight: 6 },
  offlineChipText: { fontSize: 11, fontWeight: '700', color: '#92400E' },
  langPill: { flexDirection: 'row', backgroundColor: '#FFFFFF', borderWidth: 1, borderColor: Colors.border, borderRadius: 14, paddingHorizontal: 10, paddingVertical: 5 },
  langText: { fontSize: 11, fontWeight: '500', color: Colors.textSecondary },
  langActive: { color: Colors.primaryRed, fontWeight: '800' },
  langDivider: { color: '#CBD5E1', fontSize: 11 },
  searchBarContainer: { flexDirection: 'row', alignItems: 'center', backgroundColor: '#FFFFFF', borderRadius: 22, paddingHorizontal: 14, height: 48, marginTop: 10, elevation: 4, shadowColor: '#000', shadowOpacity: 0.1, shadowRadius: 6 },
  searchIcon: { fontSize: 16, marginRight: 8 },
  searchInput: { flex: 1, fontSize: 14, color: Colors.textPrimary },
  scanIcon: { fontSize: 16, color: '#64748B' },
  bottomCard: { position: 'absolute', bottom: 0, left: 0, right: 0, backgroundColor: '#FFFFFF', borderTopLeftRadius: 28, borderTopRightRadius: 28, paddingHorizontal: 18, paddingVertical: 14, elevation: 16, shadowColor: '#000', shadowOpacity: 0.15, shadowRadius: 12 },
  grabHandle: { width: 40, height: 4, borderRadius: 2, backgroundColor: '#CBD5E1', alignSelf: 'center', marginBottom: 12 },
  sectionTitle: { fontSize: 16, fontWeight: '700', color: Colors.textPrimary, marginBottom: 12 },
  carouselContainer: { paddingBottom: 6 },
  carouselCard: { width: 130, backgroundColor: '#FFFFFF', borderWidth: 1.5, borderColor: Colors.border, borderRadius: 16, padding: 10, marginRight: 10 },
  carouselCardActive: { borderColor: Colors.primaryRed, backgroundColor: '#FFF5F5' },
  ambulancePreviewBox: { width: '100%', height: 56, backgroundColor: '#F1F5F9', borderRadius: 10, justifyContent: 'center', alignItems: 'center', marginBottom: 8 },
  cardTitle: { fontSize: 12, fontWeight: '700', color: Colors.textPrimary, lineHeight: 15 },
  cardEta: { fontSize: 11, color: Colors.textSecondary, marginTop: 4 },
  cardPrice: { fontSize: 13, fontWeight: '800', color: Colors.textPrimary, marginTop: 2 },
  fareRow: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#F8FAFC', borderRadius: 14, padding: 12, marginVertical: 12, borderWidth: 1, borderColor: Colors.border },
  fareLabel: { fontSize: 11, color: Colors.textSecondary },
  fareValue: { fontSize: 16, fontWeight: '800', color: Colors.textPrimary },
  counterRow: { flexDirection: 'row', alignItems: 'center' },
  suggestPill: { backgroundColor: '#EFF6FF', paddingHorizontal: 8, paddingVertical: 5, borderRadius: 8, marginRight: 8 },
  suggestText: { fontSize: 11, fontWeight: '600', color: Colors.primaryBlue },
  counterBtn: { width: 32, height: 32, borderRadius: 16, backgroundColor: '#FFFFFF', borderWidth: 1, borderColor: '#CBD5E1', justifyContent: 'center', alignItems: 'center', marginLeft: 6 },
  counterBtnText: { fontSize: 18, fontWeight: '700', color: Colors.textPrimary },
  requestCta: { backgroundColor: Colors.primaryRed, height: 54, borderRadius: 27, justifyContent: 'center', alignItems: 'center', elevation: 8, shadowColor: Colors.primaryRed, shadowOffset: { width: 0, height: 6 }, shadowOpacity: 0.45, shadowRadius: 10 },
  requestCtaText: { color: '#FFFFFF', fontSize: 15, fontWeight: '900', letterSpacing: 0.5 }
});
