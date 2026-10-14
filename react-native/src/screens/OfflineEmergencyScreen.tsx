import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  SafeAreaView,
  ScrollView,
  Linking,
  Alert,
  Vibration
} from 'react-native';
import { Colors } from '../theme/colors';

export const OfflineEmergencyScreen: React.FC<{ navigation: any }> = ({ navigation }) => {
  const [lang, setLang] = useState<'EN' | 'NE'>('EN');
  const [tapCount, setTapCount] = useState<number>(0);
  const [smsSent, setSmsSent] = useState<boolean>(false);

  const isNe = lang === 'NE';

  // Triple-Tap Timeout Window (1.4 seconds)
  useEffect(() => {
    let timer: any;
    if (tapCount > 0 && tapCount < 3) {
      timer = setTimeout(() => {
        setTapCount(0);
      }, 1400);
    }
    return () => clearTimeout(timer);
  }, [tapCount]);

  // Handle Triple-Tap SMS Dispatch
  const handleTripleTapEmergencySMS = () => {
    Vibration.vibrate([0, 150, 100, 150]);
    setSmsSent(true);
    const cachedGps = '27.6915° N, 85.3420° E (Baneshwor, Kathmandu)';
    const smsMessage = `EMERGENCY SOS! Need immediate ambulance dispatch. Location: ${cachedGps}. Patient requires urgent medical response.`;
    
    // Open native SMS composer to toll-free 102
    Linking.openURL(`sms:102?body=${encodeURIComponent(smsMessage)}`).catch(() => {
      Alert.alert('SMS Prepared', smsMessage);
    });
  };

  const handleCentralButtonPress = () => {
    const nextCount = tapCount + 1;
    setTapCount(nextCount);
    Vibration.vibrate(60);

    if (nextCount >= 3) {
      setTapCount(0);
      handleTripleTapEmergencySMS();
    } else if (nextCount === 1) {
      // Direct call fallback on first normal tap
      Linking.openURL('tel:102').catch(() => {
        Alert.alert('Emergency Call', 'Dialing 102 (Ambulance Dispatch Nepal)');
      });
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      {/* 1. Offline Mode Banner */}
      <View style={styles.banner}>
        <Text style={styles.bannerText}>
          ⚠️ {isNe ? 'इन्टरनेट छैन - अफलाइन मोड सक्रिय' : 'No Internet - Offline Mode Active'}
        </Text>
        <TouchableOpacity
          style={styles.onlineSwitchBtn}
          onPress={() => navigation.navigate('OnlineBooking')}
        >
          <Text style={styles.onlineSwitchText}>
            🟢 {isNe ? 'अनलाइन' : 'Online'}
          </Text>
        </TouchableOpacity>
      </View>

      <ScrollView contentContainerStyle={styles.scroll}>
        {/* Top Header with Language Switcher */}
        <View style={styles.header}>
          <Text style={styles.appTitle}>MEDIC AMBULANCE</Text>
          <TouchableOpacity
            style={styles.langPill}
            onPress={() => setLang(lang === 'EN' ? 'NE' : 'EN')}
          >
            <Text style={[styles.langText, lang === 'EN' && styles.langActive]}>EN</Text>
            <Text style={styles.langDivider}> | </Text>
            <Text style={[styles.langText, lang === 'NE' && styles.langActive]}>ने</Text>
          </TouchableOpacity>
        </View>

        {smsSent && (
          <View style={styles.smsAlertSuccess}>
            <Text style={styles.smsAlertSuccessText}>
              ✓ {isNe ? 'आपतकालीन एसएमएस पठाइयो (१०२ डेस्क)' : 'Emergency SMS Dispatched (102 Dispatch Desk)'}
            </Text>
          </View>
        )}

        {/* 2. Massive Heavily-Shadowed Pulsing Circular Red Button */}
        <View style={styles.buttonContainer}>
          <View style={styles.haloRing} />
          <TouchableOpacity
            style={styles.centralButton}
            onPress={handleCentralButtonPress}
            activeOpacity={0.85}
          >
            <Text style={styles.phoneIcon}>📞</Text>
            <Text style={styles.buttonLabel}>
              {isNe ? 'आपतकालीन एम्बुलेन्स कल' : 'CALL EMERGENCY AMBULANCE'}
            </Text>
          </TouchableOpacity>
        </View>

        {/* Triple-Tap Tap Counter Indicator */}
        <View style={styles.tapIndicatorCard}>
          <Text style={styles.tapCountText}>
            {tapCount === 0
              ? (isNe ? '⚡ आपतकालीन एसएमएसका लागि ३ पटक छिटो थिच्नुहोस्' : '⚡ Triple-Tap to Send Emergency SMS with GPS')
              : `Tap: ${tapCount} / 3 (Tap ${3 - tapCount} more times!)`}
          </Text>
        </View>

        {/* 3. Secondary Offline Actions (Clean, Borderless Cards) */}
        <Text style={styles.subHeader}>
          {isNe ? 'अफलाइन द्रुत सेवाहरू' : 'Secondary Offline Actions'}
        </Text>

        {/* Action 1: Send Emergency SMS */}
        <TouchableOpacity
          style={styles.actionCard}
          onPress={handleTripleTapEmergencySMS}
        >
          <View style={styles.actionCardIconBox}>
            <Text style={{ fontSize: 20 }}>💬</Text>
          </View>
          <View style={{ marginLeft: 14 }}>
            <Text style={styles.actionCardTitle}>
              {isNe ? 'आपतकालीन एसएमएस पठाउनुहोस्' : 'Send Emergency SMS'}
            </Text>
            <Text style={styles.actionCardSub}>
              📍 27.6915° N, 85.3420° E (Baneshwor GPS)
            </Text>
          </View>
        </TouchableOpacity>

        {/* Action 2: Direct Call 102 */}
        <TouchableOpacity
          style={styles.actionCard}
          onPress={() => Linking.openURL('tel:102')}
        >
          <View style={[styles.actionCardIconBox, { backgroundColor: '#EFF6FF' }]}>
            <Text style={{ fontSize: 20 }}>📞</Text>
          </View>
          <View style={{ marginLeft: 14 }}>
            <Text style={styles.actionCardTitle}>
              {isNe ? 'सिधै १०२ मा डायल गर्नुहोस्' : 'Direct Call 102'}
            </Text>
            <Text style={styles.actionCardSub}>
              {isNe ? 'नेपाल रेडक्रस र सरकारी डेस्क' : 'Nepal Red Cross & Govt Dispatch'}
            </Text>
          </View>
        </TouchableOpacity>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#F8FAFC' },
  banner: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#FEF2F2', paddingHorizontal: 16, paddingVertical: 10, borderBottomWidth: 1, borderColor: '#FCA5A5' },
  bannerText: { fontSize: 12, fontWeight: '700', color: '#991B1B' },
  onlineSwitchBtn: { backgroundColor: '#FFFFFF', paddingHorizontal: 10, paddingVertical: 4, borderRadius: 10, borderWidth: 1, borderColor: Colors.border },
  onlineSwitchText: { fontSize: 11, fontWeight: '700', color: '#0F172A' },
  scroll: { padding: 20, alignItems: 'center' },
  header: { width: '100%', flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 20 },
  appTitle: { fontSize: 14, fontWeight: '800', color: '#64748B', letterSpacing: 1 },
  langPill: { flexDirection: 'row', backgroundColor: '#FFFFFF', borderWidth: 1, borderColor: Colors.border, borderRadius: 14, paddingHorizontal: 10, paddingVertical: 5 },
  langText: { fontSize: 11, fontWeight: '500', color: Colors.textSecondary },
  langActive: { color: Colors.primaryRed, fontWeight: '800' },
  langDivider: { color: '#CBD5E1', fontSize: 11 },
  smsAlertSuccess: { width: '100%', backgroundColor: '#ECFDF5', padding: 12, borderRadius: 12, marginBottom: 14, borderWidth: 1, borderColor: '#A7F3D0' },
  smsAlertSuccessText: { fontSize: 12, fontWeight: '700', color: '#065F46', textAlign: 'center' },
  buttonContainer: { width: 220, height: 220, justifyContent: 'center', alignItems: 'center', marginVertical: 24 },
  haloRing: { position: 'absolute', width: 220, height: 220, borderRadius: 110, backgroundColor: 'rgba(229, 57, 53, 0.15)' },
  centralButton: { width: 175, height: 175, borderRadius: 88, backgroundColor: Colors.primaryRed, justifyContent: 'center', alignItems: 'center', padding: 16, elevation: 16, shadowColor: Colors.primaryRed, shadowOffset: { width: 0, height: 8 }, shadowOpacity: 0.5, shadowRadius: 16 },
  phoneIcon: { fontSize: 36, color: '#FFFFFF', marginBottom: 6 },
  buttonLabel: { color: '#FFFFFF', fontSize: 12, fontWeight: '900', textAlign: 'center', letterSpacing: 0.5 },
  tapIndicatorCard: { backgroundColor: '#F1F5F9', paddingHorizontal: 16, paddingVertical: 8, borderRadius: 20, marginBottom: 24 },
  tapCountText: { fontSize: 12, fontWeight: '700', color: '#334155' },
  subHeader: { width: '100%', fontSize: 15, fontWeight: '700', color: '#0F172A', marginBottom: 12 },
  actionCard: { width: '100%', flexDirection: 'row', alignItems: 'center', backgroundColor: '#FFFFFF', padding: 16, borderRadius: 16, marginBottom: 12, elevation: 1 },
  actionCardIconBox: { width: 44, height: 44, borderRadius: 12, backgroundColor: Colors.primaryRedLight, justifyContent: 'center', alignItems: 'center' },
  actionCardTitle: { fontSize: 14, fontWeight: '700', color: Colors.textPrimary },
  actionCardSub: { fontSize: 12, color: Colors.textSecondary, marginTop: 2 }
});
