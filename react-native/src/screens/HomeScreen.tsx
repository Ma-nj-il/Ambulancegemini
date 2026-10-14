import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  ScrollView,
  SafeAreaView,
  Modal,
  Alert
} from 'react-native';
import { Colors } from '../theme/colors';

export const HomeScreen = ({ navigation }: any) => {
  const [lang, setLang] = useState<'ne' | 'en'>('ne');
  const [showSosModal, setShowSosModal] = useState(false);
  const [countdown, setCountdown] = useState(3);
  const isNe = lang === 'ne';

  // 3-second safety countdown effect when SOS is triggered
  useEffect(() => {
    let timer: any;
    if (showSosModal) {
      setCountdown(3);
      timer = setInterval(() => {
        setCountdown((prev) => {
          if (prev <= 1) {
            clearInterval(timer);
            setShowSosModal(false);
            navigation.navigate('ActiveTracking');
            return 0;
          }
          return prev - 1;
        });
      }, 1000);
    }
    return () => clearInterval(timer);
  }, [showSosModal]);

  const handleInstantDispatch = () => {
    setShowSosModal(false);
    navigation.navigate('ActiveTracking');
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.scroll}>
        {/* Top Header (Screenshot Page 1) */}
        <View style={styles.header}>
          <View>
            <View style={styles.logoRow}>
              <Text style={styles.logoIcon}>🚑</Text>
              <Text style={styles.appName}>
                {isNe ? 'मेडि-एम्बुलेन्स' : 'Med-Ambulance'}
              </Text>
            </View>
            <Text style={styles.locationSub}>
              📍 {isNe ? 'तपाईंको वर्तमान स्थान (काठमाडौँ)' : 'Your Current Location (Kathmandu)'}
            </Text>
          </View>

          <View style={styles.headerRight}>
            <TouchableOpacity
              style={styles.langPill}
              onPress={() => setLang(lang === 'ne' ? 'en' : 'ne')}
            >
              <Text style={styles.langText}>{isNe ? '🇳🇵 ने' : '🇺🇸 EN'}</Text>
            </TouchableOpacity>

            <TouchableOpacity
              style={styles.profileCircle}
              onPress={() => navigation.navigate('Login')}
            >
              <Text style={styles.profileIcon}>👤</Text>
            </TouchableOpacity>
          </View>
        </View>

        {/* Hero Emergency Card (Screenshot Page 1) */}
        <TouchableOpacity
          style={styles.heroCard}
          onPress={() => setShowSosModal(true)}
          activeOpacity={0.88}
        >
          <View style={styles.emblemBadge}>
            <Text style={styles.emblemBadgeIcon}>🚑</Text>
          </View>

          <Text style={styles.heroTitle}>
            {isNe ? 'आपतकालीन एम्बुलेन्स बोलाउनुहोस्' : 'Call Emergency Ambulance'}
          </Text>
          <Text style={styles.heroSubtitle}>
            {isNe ? 'Call Emergency Ambulance' : 'Fastest Medical Dispatch'}
          </Text>

          <View style={styles.availableChip}>
            <View style={styles.greenDot} />
            <Text style={styles.availableText}>
              {isNe ? '५ एम्बुलेन्स उपलब्ध छन्' : '5 Ambulances Available'}
            </Text>
          </View>
        </TouchableOpacity>

        {/* 2x2 Quick Grid (Screenshot Page 1) */}
        <View style={styles.gridRow}>
          <TouchableOpacity
            style={styles.gridCard}
            onPress={() => navigation.navigate('NearbyHospitals')}
          >
            <Text style={styles.gridCardIcon}>🏥</Text>
            <Text style={styles.gridCardTitle}>
              {isNe ? 'नजिकैको अस्पताल' : 'Nearby Hospitals'}
            </Text>
            <Text style={styles.gridCardSub}>Nearby Hospitals</Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={styles.gridCard}
            onPress={() => navigation.navigate('RideHistory')}
          >
            <Text style={styles.gridCardIcon}>🕒</Text>
            <Text style={styles.gridCardTitle}>
              {isNe ? 'मेरो यात्राहरू' : 'My Rides'}
            </Text>
            <Text style={styles.gridCardSub}>My Rides</Text>
          </TouchableOpacity>
        </View>

        <View style={styles.gridRow}>
          <TouchableOpacity
            style={styles.gridCard}
            onPress={() => navigation.navigate('EmergencyNumbers')}
          >
            <Text style={styles.gridCardIcon}>📞</Text>
            <Text style={styles.gridCardTitle}>
              {isNe ? 'आपतकालीन सम्पर्कहरू' : 'Emergency Contacts'}
            </Text>
            <Text style={styles.gridCardSub}>Emergency Contacts</Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={styles.gridCard}
            onPress={() => navigation.navigate('BookForOthers')}
          >
            <Text style={styles.gridCardIcon}>🛟</Text>
            <Text style={styles.gridCardTitle}>
              {isNe ? 'अरुका लागि बुक गर्नुहोस' : 'Book for Others'}
            </Text>
            <Text style={styles.gridCardSub}>Help</Text>
          </TouchableOpacity>
        </View>

        {/* Services Section (Screenshot Page 1) */}
        <Text style={styles.sectionHeader}>
          {isNe ? 'एम्बुलेन्स सेवाहरू / Services' : 'Ambulance Services / Types'}
        </Text>

        <TouchableOpacity
          style={styles.serviceRow}
          onPress={() => navigation.navigate('RequestAmbulance', { type: 'BASIC' })}
        >
          <View style={styles.serviceInfo}>
            <View style={styles.serviceSquare}>
              <Text style={{ fontSize: 20 }}>🚑</Text>
            </View>
            <View>
              <Text style={styles.serviceTitle}>
                {isNe ? 'सामान्य एम्बुलेन्स' : 'Basic Ambulance'}
              </Text>
              <Text style={styles.serviceSub}>
                Basic Ambulance • 🕒 {isNe ? '१०-१५ मिनेट' : '10-15 min'}
              </Text>
            </View>
          </View>
          <Text style={styles.serviceFare}>रु. १००</Text>
        </TouchableOpacity>

        <TouchableOpacity
          style={styles.serviceRow}
          onPress={() => navigation.navigate('RequestAmbulance', { type: 'ICU' })}
        >
          <View style={styles.serviceInfo}>
            <View style={styles.serviceSquare}>
              <Text style={{ fontSize: 20 }}>🏥</Text>
            </View>
            <View>
              <Text style={styles.serviceTitle}>
                {isNe ? 'आईसीयू एम्बुलेन्स' : 'ICU Ambulance'}
              </Text>
              <Text style={styles.serviceSub}>
                ICU Ambulance • 🕒 {isNe ? '१५-२० मिनेट' : '15-20 min'}
              </Text>
            </View>
          </View>
          <Text style={styles.serviceFare}>रु. २००</Text>
        </TouchableOpacity>

        {/* Recent Rides (Screenshot Page 1) */}
        <Text style={styles.sectionHeader}>
          {isNe ? 'पछिल्ला यात्राहरू / Recent Rides' : 'Recent Rides'}
        </Text>

        <View style={styles.recentRideCard}>
          <View>
            <Text style={styles.recentDestination}>
              {isNe ? 'बिर अस्पताल' : 'Bir Hospital'}
            </Text>
            <Text style={styles.recentTime}>
              {isNe ? 'आज, ११:३० बिहान' : 'Today, 11:30 AM'}
            </Text>
          </View>
          <Text style={styles.statusCompleted}>
            {isNe ? 'पूरा भयो' : 'Completed'}
          </Text>
        </View>

        <View style={styles.recentRideCard}>
          <View>
            <Text style={styles.recentDestination}>
              {isNe ? 'पाटन अस्पताल' : 'Patan Hospital'}
            </Text>
            <Text style={styles.recentTime}>
              {isNe ? 'हिजो, ३:२० दिउँसो' : 'Yesterday, 3:20 PM'}
            </Text>
          </View>
          <Text style={styles.statusCompleted}>
            {isNe ? 'पूरा भयो' : 'Completed'}
          </Text>
        </View>

        <View style={{ height: 90 }} />
      </ScrollView>

      {/* Floating Action Button for Emergency SOS (User Request) */}
      <TouchableOpacity
        style={styles.sosFab}
        onPress={() => setShowSosModal(true)}
        activeOpacity={0.85}
      >
        <Text style={styles.sosFabIcon}>🚨</Text>
        <Text style={styles.sosFabText}>
          {isNe ? 'आपतकालीन SOS' : 'SOS DISPATCH'}
        </Text>
      </TouchableOpacity>

      {/* 3-Second Countdown SOS Modal */}
      <Modal
        visible={showSosModal}
        transparent
        animationType="fade"
        onRequestClose={() => setShowSosModal(false)}
      >
        <View style={styles.modalBackdrop}>
          <View style={styles.modalCard}>
            <View style={styles.modalHeader}>
              <View style={styles.priorityPill}>
                <Text style={styles.priorityText}>⚠️ HIGH PRIORITY</Text>
              </View>
              <TouchableOpacity onPress={() => setShowSosModal(false)}>
                <Text style={styles.closeModalText}>✕</Text>
              </TouchableOpacity>
            </View>

            {/* Countdown Badge */}
            <View style={styles.countdownCircle}>
              <Text style={styles.countdownNumber}>{countdown}</Text>
              <Text style={styles.countdownSecLabel}>SEC</Text>
            </View>

            <Text style={styles.modalTitle}>EMERGENCY SOS ALERT</Text>
            <Text style={styles.modalSub}>
              {isNe
                ? 'नजिकैको एम्बुलेन्स र अस्पतालमा तत्काल अलर्ट पठाइँदैछ...'
                : 'Connecting to nearest ambulance unit & hospital dispatch...'}
            </Text>

            <View style={styles.locationSnippet}>
              <Text style={styles.locationSnippetText}>
                📍 Baneshwor, Kathmandu (GPS Locked)
              </Text>
              <Text style={styles.hospitalSnippetText}>
                🏥 Nearest: Kathmandu Medical Center (1.2 km)
              </Text>
            </View>

            <Text style={styles.warningNote}>
              Automatic dispatch in 3s to prevent accidental triggers
            </Text>

            {/* Dispatch Immediately Button */}
            <TouchableOpacity
              style={styles.instantDispatchBtn}
              onPress={handleInstantDispatch}
            >
              <Text style={styles.instantDispatchText}>⚡ Dispatch Immediately</Text>
            </TouchableOpacity>

            {/* Cancel Button */}
            <TouchableOpacity
              style={styles.cancelAlertBtn}
              onPress={() => setShowSosModal(false)}
            >
              <Text style={styles.cancelAlertText}>Cancel Alert (रद्द गर्नुहोस्)</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: Colors.background },
  scroll: { padding: 16 },
  header: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 },
  logoRow: { flexDirection: 'row', alignItems: 'center' },
  logoIcon: { fontSize: 22, marginRight: 6 },
  appName: { fontSize: 20, fontWeight: '700', color: Colors.textPrimary },
  locationSub: { fontSize: 12, color: Colors.textSecondary, marginTop: 2 },
  headerRight: { flexDirection: 'row', alignItems: 'center' },
  langPill: { paddingHorizontal: 10, paddingVertical: 5, borderRadius: 14, backgroundColor: '#FFFFFF', borderWidth: 1, borderColor: Colors.border, marginRight: 8 },
  langText: { fontSize: 11, fontWeight: '700', color: Colors.textPrimary },
  profileCircle: { width: 36, height: 36, borderRadius: 18, backgroundColor: '#FFFFFF', borderWidth: 1, borderColor: Colors.border, justifyContent: 'center', alignItems: 'center' },
  profileIcon: { fontSize: 16 },
  heroCard: { backgroundColor: '#FFFFFF', borderRadius: 20, padding: 22, alignItems: 'center', elevation: 2, marginBottom: 16, shadowColor: '#000', shadowOffset: { width: 0, height: 1 }, shadowOpacity: 0.08, shadowRadius: 4 },
  emblemBadge: { width: 72, height: 72, borderRadius: 36, backgroundColor: Colors.primaryRed, justifyContent: 'center', alignItems: 'center', marginBottom: 12 },
  emblemBadgeIcon: { fontSize: 34 },
  heroTitle: { fontSize: 17, fontWeight: '700', color: Colors.primaryRed, textAlign: 'center' },
  heroSubtitle: { fontSize: 12, color: Colors.textSecondary, marginTop: 2 },
  availableChip: { flexDirection: 'row', alignItems: 'center', backgroundColor: Colors.greenLight, paddingHorizontal: 14, paddingVertical: 6, borderRadius: 16, marginTop: 12 },
  greenDot: { width: 8, height: 8, borderRadius: 4, backgroundColor: Colors.greenAvailable, marginRight: 6 },
  availableText: { fontSize: 12, color: '#065F46', fontWeight: '600' },
  gridRow: { flexDirection: 'row', justifyContent: 'space-between', marginBottom: 12 },
  gridCard: { flex: 1, backgroundColor: '#FFFFFF', borderRadius: 16, padding: 14, marginHorizontal: 4, elevation: 1 },
  gridCardIcon: { fontSize: 26, marginBottom: 6 },
  gridCardTitle: { fontSize: 13, fontWeight: '700', color: Colors.textPrimary },
  gridCardSub: { fontSize: 11, color: Colors.textSecondary, marginTop: 1 },
  sectionHeader: { fontSize: 16, fontWeight: '700', color: Colors.textPrimary, marginTop: 14, marginBottom: 10 },
  serviceRow: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#FFFFFF', padding: 16, borderRadius: 16, marginBottom: 10, elevation: 1 },
  serviceInfo: { flexDirection: 'row', alignItems: 'center' },
  serviceSquare: { width: 44, height: 44, borderRadius: 12, backgroundColor: Colors.primaryRedLight, justifyContent: 'center', alignItems: 'center', marginRight: 12 },
  serviceTitle: { fontSize: 14, fontWeight: '700', color: Colors.textPrimary },
  serviceSub: { fontSize: 12, color: Colors.textSecondary, marginTop: 2 },
  serviceFare: { fontSize: 16, fontWeight: '700', color: Colors.primaryRed },
  recentRideCard: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#FFFFFF', padding: 16, borderRadius: 14, marginBottom: 8, elevation: 1 },
  recentDestination: { fontSize: 14, fontWeight: '700', color: Colors.textPrimary },
  recentTime: { fontSize: 12, color: Colors.textSecondary, marginTop: 2 },
  statusCompleted: { fontSize: 13, fontWeight: '600', color: Colors.greenAvailable },
  sosFab: { position: 'absolute', bottom: 20, right: 16, flexDirection: 'row', alignItems: 'center', backgroundColor: Colors.primaryRed, paddingHorizontal: 18, paddingVertical: 14, borderRadius: 28, elevation: 6, shadowColor: '#000', shadowOffset: { width: 0, height: 3 }, shadowOpacity: 0.25, shadowRadius: 5 },
  sosFabIcon: { fontSize: 20, marginRight: 8 },
  sosFabText: { color: '#FFFFFF', fontWeight: '800', fontSize: 14, letterSpacing: 0.5 },
  modalBackdrop: { flex: 1, backgroundColor: 'rgba(0,0,0,0.7)', justifyContent: 'center', alignItems: 'center', padding: 20 },
  modalCard: { width: '100%', backgroundColor: '#FFFFFF', borderRadius: 24, padding: 24, alignItems: 'center' },
  modalHeader: { width: '100%', flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 },
  priorityPill: { backgroundColor: Colors.primaryRedLight, paddingHorizontal: 10, paddingVertical: 4, borderRadius: 8 },
  priorityText: { color: Colors.primaryRed, fontWeight: '700', fontSize: 11 },
  closeModalText: { fontSize: 18, color: '#64748B', fontWeight: '700' },
  countdownCircle: { width: 90, height: 90, borderRadius: 45, backgroundColor: Colors.primaryRed, justifyContent: 'center', alignItems: 'center', marginVertical: 10 },
  countdownNumber: { fontSize: 36, fontWeight: '900', color: '#FFFFFF' },
  countdownSecLabel: { fontSize: 10, fontWeight: '700', color: 'rgba(255,255,255,0.8)' },
  modalTitle: { fontSize: 18, fontWeight: '800', color: Colors.primaryRed, marginTop: 12 },
  modalSub: { fontSize: 13, color: Colors.textSecondary, textAlign: 'center', marginTop: 4, marginBottom: 16 },
  locationSnippet: { width: '100%', backgroundColor: '#F8FAFC', borderWidth: 1, borderColor: Colors.border, borderRadius: 12, padding: 12, marginBottom: 10 },
  locationSnippetText: { fontSize: 12, fontWeight: '600', color: Colors.textPrimary },
  hospitalSnippetText: { fontSize: 12, color: '#065F46', marginTop: 4, fontWeight: '500' },
  warningNote: { fontSize: 11, color: '#94A3B8', textAlign: 'center', marginBottom: 16 },
  instantDispatchBtn: { width: '100%', height: 48, backgroundColor: Colors.primaryRed, borderRadius: 12, justifyContent: 'center', alignItems: 'center', marginBottom: 10 },
  instantDispatchText: { color: '#FFFFFF', fontWeight: '700', fontSize: 14 },
  cancelAlertBtn: { width: '100%', height: 46, borderRadius: 12, borderWidth: 1, borderColor: Colors.border, justifyContent: 'center', alignItems: 'center' },
  cancelAlertText: { color: Colors.textSecondary, fontWeight: '600', fontSize: 13 }
});
