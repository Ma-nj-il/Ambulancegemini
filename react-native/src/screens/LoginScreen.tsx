import React, { useState } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  SafeAreaView,
  ScrollView,
  ActivityIndicator,
  Alert
} from 'react-native';
import { Colors } from '../theme/colors';
import { ambulanceApi } from '../api/ambulanceApi';

export const LoginScreen = ({ navigation }: any) => {
  const [phone, setPhone] = useState('9841234567');
  const [otp, setOtp] = useState('');
  const [isOtpSent, setIsOtpSent] = useState(false);
  const [loading, setLoading] = useState(false);
  const [role, setRole] = useState<'CITIZEN' | 'DRIVER' | 'HOSPITAL'>('CITIZEN');
  const [lang, setLang] = useState<'ne' | 'en'>('ne');

  const handleSendOtp = async () => {
    if (!phone) {
      Alert.alert('Required', 'Please enter your phone number');
      return;
    }
    setLoading(true);
    try {
      const res = await ambulanceApi.handleLogin(phone);
      setIsOtpSent(true);
      setOtp('1234'); // Pre-fill test OTP for frictionless experience
    } catch (err: any) {
      Alert.alert('Error', err.message || 'Failed to send OTP');
    } finally {
      setLoading(false);
    }
  };

  const handleVerify = async () => {
    setLoading(true);
    try {
      await ambulanceApi.verifyOtp(phone, otp);
      if (role === 'DRIVER') {
        navigation.navigate('DriverOrder');
      } else {
        navigation.navigate('Home');
      }
    } catch (err: any) {
      Alert.alert('Error', 'Invalid verification code');
    } finally {
      setLoading(false);
    }
  };

  const isNe = lang === 'ne';

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.scroll}>
        {/* Top bar with Guest & Lang toggle */}
        <View style={styles.topRow}>
          <TouchableOpacity
            style={styles.guestBadge}
            onPress={() => navigation.navigate('RequestAmbulance')}
          >
            <Text style={styles.guestBadgeText}>🚨 {isNe ? 'द्रुत आपतकालीन पहुँच' : 'Instant Emergency'}</Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={styles.langPill}
            onPress={() => setLang(lang === 'ne' ? 'en' : 'ne')}
          >
            <Text style={styles.langText}>{isNe ? '🇳🇵 ने (EN)' : '🇺🇸 EN (ने)'}</Text>
          </TouchableOpacity>
        </View>

        {/* Emblem */}
        <View style={styles.emblemContainer}>
          <View style={styles.emblem}>
            <Text style={styles.emblemIcon}>🚑</Text>
          </View>
        </View>

        <Text style={styles.title}>{isNe ? 'मेडि-एम्बुलेन्स नेपाल' : 'Med-Ambulance Nepal'}</Text>
        <Text style={styles.subtitle}>
          {isNe ? 'नेपालभर द्रुत, पारदर्शी एम्बुलेन्स सेवा' : 'Fast & reliable emergency ambulance dispatch'}
        </Text>

        {/* Profile Selector */}
        <Text style={styles.inputLabel}>{isNe ? 'तपाईंको भूमिका छान्नुहोस्:' : 'Select Profile:'}</Text>
        <View style={styles.roleRow}>
          {[
            { key: 'CITIZEN', label: isNe ? 'नागरिक' : 'Citizen' },
            { key: 'DRIVER', label: isNe ? 'चालक' : 'Driver' },
            { key: 'HOSPITAL', label: isNe ? 'अस्पताल' : 'Hospital' },
          ].map((r: any) => {
            const isSel = role === r.key;
            return (
              <TouchableOpacity
                key={r.key}
                style={[styles.roleBtn, isSel && styles.roleBtnActive]}
                onPress={() => setRole(r.key)}
              >
                <Text style={[styles.roleBtnText, isSel && styles.roleBtnTextActive]}>{r.label}</Text>
              </TouchableOpacity>
            );
          })}
        </View>

        {/* Phone Input */}
        <Text style={styles.inputLabel}>{isNe ? 'फोन नम्बर' : 'Phone Number'}</Text>
        <View style={styles.phoneInputRow}>
          <Text style={styles.prefix}>🇳🇵 +977</Text>
          <TextInput
            style={styles.phoneInput}
            value={phone}
            onChangeText={setPhone}
            keyboardType="phone-pad"
            placeholder="98XXXXXXXX"
            placeholderTextColor="#94A3B8"
          />
        </View>

        {isOtpSent && (
          <View style={{ marginTop: 14 }}>
            <Text style={styles.inputLabel}>{isNe ? '४-अङ्कको ओटीपी कोड (Use: 1234)' : '4-Digit OTP Code'}</Text>
            <TextInput
              style={styles.textInput}
              value={otp}
              onChangeText={setOtp}
              keyboardType="number-pad"
              placeholder="1234"
              maxLength={4}
            />
          </View>
        )}

        {/* Submit Button */}
        <TouchableOpacity
          style={styles.primaryBtn}
          onPress={isOtpSent ? handleVerify : handleSendOtp}
          disabled={loading}
        >
          {loading ? (
            <ActivityIndicator color="#FFFFFF" />
          ) : (
            <Text style={styles.primaryBtnText}>
              {!isOtpSent ? (isNe ? 'ओटीपी कोड पठाउनुहोस्' : 'Send Verification Code') : (isNe ? 'लगइन गर्नुहोस्' : 'Verify & Continue')}
            </Text>
          )}
        </TouchableOpacity>

        {/* Guest Access Emergency Button */}
        <TouchableOpacity
          style={styles.guestEmergencyBtn}
          onPress={() => navigation.navigate('RequestAmbulance')}
        >
          <Text style={styles.guestEmergencyText}>🚨 {isNe ? 'दर्ता बिना सिधै आपतकालीन बुक' : 'Emergency Guest Booking (No Login)'}</Text>
        </TouchableOpacity>

        {/* Sign up navigation */}
        <View style={styles.footerRow}>
          <Text style={styles.footerText}>{isNe ? 'खाता छैन? ' : "Don't have an account? "}</Text>
          <TouchableOpacity onPress={() => navigation.navigate('SignUp')}>
            <Text style={styles.footerLink}>{isNe ? 'नयाँ दर्ता गर्नुहोस्' : 'Sign Up'}</Text>
          </TouchableOpacity>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: Colors.background },
  scroll: { padding: 24 },
  topRow: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 20 },
  guestBadge: { backgroundColor: Colors.primaryRedLight, paddingHorizontal: 10, paddingVertical: 6, borderRadius: 12 },
  guestBadgeText: { color: Colors.primaryRed, fontSize: 11, fontWeight: '700' },
  langPill: { backgroundColor: '#FFFFFF', paddingHorizontal: 12, paddingVertical: 6, borderRadius: 16, borderWidth: 1, borderColor: Colors.border },
  langText: { fontSize: 12, fontWeight: '600', color: Colors.textPrimary },
  emblemContainer: { alignItems: 'center', marginVertical: 14 },
  emblem: { width: 76, height: 76, borderRadius: 38, backgroundColor: Colors.primaryRedLight, justifyContent: 'center', alignItems: 'center' },
  emblemIcon: { fontSize: 36 },
  title: { fontSize: 24, fontWeight: '700', color: Colors.textPrimary, textAlign: 'center' },
  subtitle: { fontSize: 13, color: Colors.textSecondary, textAlign: 'center', marginTop: 4, marginBottom: 20 },
  inputLabel: { fontSize: 13, fontWeight: '600', color: Colors.textSecondary, marginBottom: 6 },
  roleRow: { flexDirection: 'row', justifyContent: 'space-between', marginBottom: 16 },
  roleBtn: { flex: 1, backgroundColor: '#FFFFFF', borderWidth: 1, borderColor: Colors.border, paddingVertical: 10, borderRadius: 10, marginHorizontal: 3, alignItems: 'center' },
  roleBtnActive: { backgroundColor: Colors.primaryRed, borderColor: Colors.primaryRed },
  roleBtnText: { fontSize: 12, color: Colors.textPrimary },
  roleBtnTextActive: { color: '#FFFFFF', fontWeight: '700' },
  phoneInputRow: { flexDirection: 'row', backgroundColor: '#FFFFFF', borderWidth: 1, borderColor: Colors.border, borderRadius: 12, alignItems: 'center', paddingHorizontal: 12, height: 50 },
  prefix: { fontSize: 14, fontWeight: '700', color: Colors.textSecondary, marginRight: 8 },
  phoneInput: { flex: 1, fontSize: 15, color: Colors.textPrimary },
  textInput: { backgroundColor: '#FFFFFF', borderWidth: 1, borderColor: Colors.border, borderRadius: 12, height: 50, paddingHorizontal: 14, fontSize: 15, color: Colors.textPrimary },
  primaryBtn: { backgroundColor: Colors.primaryRed, height: 52, borderRadius: 14, justifyContent: 'center', alignItems: 'center', marginTop: 20 },
  primaryBtnText: { color: '#FFFFFF', fontSize: 16, fontWeight: '700' },
  guestEmergencyBtn: { marginTop: 12, height: 50, borderRadius: 14, borderWidth: 1.5, borderColor: Colors.primaryRed, justifyContent: 'center', alignItems: 'center', backgroundColor: '#FFFFFF' },
  guestEmergencyText: { color: Colors.primaryRed, fontSize: 13, fontWeight: '700' },
  footerRow: { flexDirection: 'row', justifyContent: 'center', marginTop: 24 },
  footerText: { fontSize: 13, color: Colors.textSecondary },
  footerLink: { fontSize: 13, fontWeight: '700', color: Colors.primaryRed }
});
