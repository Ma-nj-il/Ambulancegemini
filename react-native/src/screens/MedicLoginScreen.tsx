import React, { useState } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  SafeAreaView,
  ScrollView,
  Alert
} from 'react-native';
import { Colors } from '../theme/colors';

export const MedicLoginScreen: React.FC<{ navigation: any }> = ({ navigation }) => {
  const [lang, setLang] = useState<'EN' | 'NE'>('EN');
  const [emailPhone, setEmailPhone] = useState('9841234567');
  const [password, setPassword] = useState('••••••••');
  const [secureText, setSecureText] = useState(true);

  const isNe = lang === 'NE';

  const handleLogin = () => {
    navigation.navigate('OnlineBooking');
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.scroll}>
        {/* Top Right EN | ने Glassmorphic Toggle */}
        <View style={styles.topRow}>
          <TouchableOpacity
            style={styles.langPill}
            onPress={() => setLang(lang === 'EN' ? 'NE' : 'EN')}
          >
            <Text style={[styles.langText, lang === 'EN' && styles.langActive]}>EN</Text>
            <Text style={styles.langDivider}> | </Text>
            <Text style={[styles.langText, lang === 'NE' && styles.langActive]}>ने</Text>
          </TouchableOpacity>
        </View>

        {/* Medic Ambulance Brand Logo */}
        <View style={styles.logoSection}>
          <View style={styles.emblemBox}>
            <Text style={styles.emblemEmoji}>🚑</Text>
          </View>
          <Text style={styles.brandTitle}>MEDIC</Text>
          <Text style={styles.brandSubtitle}>AMBULANCE</Text>
        </View>

        {/* Header Texts */}
        <Text style={styles.welcomeText}>
          {isNe ? 'पुनः स्वागत छ' : 'Welcome Back'}
        </Text>
        <Text style={styles.accessText}>
          {isNe ? 'द्रुत र भरपर्दो स्वास्थ्य यातायात सेवा' : 'Access fast & reliable medical transit'}
        </Text>

        {/* Input 1: Email or Phone Number */}
        <View style={styles.inputContainer}>
          <Text style={styles.inputIcon}>✉️</Text>
          <TextInput
            style={styles.textInput}
            value={emailPhone}
            onChangeText={setEmailPhone}
            placeholder={isNe ? 'इमेल वा फोन नम्बर' : 'Email or Phone Number'}
            placeholderTextColor="#94A3B8"
          />
        </View>

        {/* Input 2: Password */}
        <View style={styles.inputContainer}>
          <Text style={styles.inputIcon}>🔒</Text>
          <TextInput
            style={styles.textInput}
            value={password}
            onChangeText={setPassword}
            secureTextEntry={secureText}
            placeholder={isNe ? 'गोप्य पासवर्ड' : 'Password'}
            placeholderTextColor="#94A3B8"
          />
          <TouchableOpacity onPress={() => setSecureText(!secureText)}>
            <Text style={styles.eyeIcon}>{secureText ? '👁️' : '🙈'}</Text>
          </TouchableOpacity>
        </View>

        {/* Glowing Red Login Button (Matching Mockup) */}
        <TouchableOpacity
          style={styles.loginBtn}
          onPress={handleLogin}
          activeOpacity={0.88}
        >
          <Text style={styles.loginBtnText}>
            {isNe ? 'लगइन गर्नुहोस्' : 'Login'}
          </Text>
        </TouchableOpacity>

        {/* Outlined Sign Up Button */}
        <TouchableOpacity
          style={styles.signUpBtn}
          onPress={() => navigation.navigate('SignUp')}
          activeOpacity={0.88}
        >
          <Text style={styles.signUpBtnText}>
            {isNe ? 'दर्ता गर्नुहोस्' : 'Sign Up'}
          </Text>
        </TouchableOpacity>

        {/* Social Login Divider */}
        <View style={styles.socialDividerRow}>
          <View style={styles.dividerLine} />
          <Text style={styles.socialText}>
            {isNe ? 'सामाजिक सञ्जालबाट लगइन' : 'Social login'}
          </Text>
          <View style={styles.dividerLine} />
        </View>

        {/* Social Icons */}
        <View style={styles.socialIconsRow}>
          <TouchableOpacity style={styles.socialCircle} onPress={handleLogin}>
            <Text style={styles.facebookText}>f</Text>
          </TouchableOpacity>
          <TouchableOpacity style={styles.socialCircle} onPress={handleLogin}>
            <Text style={styles.googleText}>G</Text>
          </TouchableOpacity>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#F8FAFC' },
  scroll: { padding: 24, alignItems: 'center' },
  topRow: { width: '100%', alignItems: 'flex-end', marginBottom: 12 },
  langPill: { flexDirection: 'row', backgroundColor: '#FFFFFF', borderWidth: 1, borderColor: Colors.border, borderRadius: 14, paddingHorizontal: 12, paddingVertical: 6, elevation: 1 },
  langText: { fontSize: 12, fontWeight: '500', color: Colors.textSecondary },
  langActive: { color: Colors.primaryRed, fontWeight: '800' },
  langDivider: { color: '#CBD5E1', fontSize: 12 },
  logoSection: { alignItems: 'center', marginVertical: 18 },
  emblemBox: { width: 80, height: 80, borderRadius: 24, backgroundColor: '#EFF6FF', justifyContent: 'center', alignItems: 'center', marginBottom: 10 },
  emblemEmoji: { fontSize: 40 },
  brandTitle: { fontSize: 28, fontWeight: '900', color: '#1E3A8A', letterSpacing: 2 },
  brandSubtitle: { fontSize: 13, fontWeight: '900', color: Colors.primaryRed, letterSpacing: 4 },
  welcomeText: { fontSize: 24, fontWeight: '700', color: Colors.textPrimary, marginTop: 12 },
  accessText: { fontSize: 13, color: Colors.textSecondary, marginTop: 4, marginBottom: 28, textAlign: 'center' },
  inputContainer: { width: '100%', flexDirection: 'row', alignItems: 'center', backgroundColor: '#FFFFFF', borderWidth: 1, borderColor: '#E2E8F0', borderRadius: 27, paddingHorizontal: 16, height: 54, marginBottom: 14 },
  inputIcon: { fontSize: 16, marginRight: 10 },
  textInput: { flex: 1, fontSize: 14, color: Colors.textPrimary },
  eyeIcon: { fontSize: 16, padding: 4 },
  loginBtn: { width: '100%', height: 54, borderRadius: 27, backgroundColor: Colors.primaryRed, justifyContent: 'center', alignItems: 'center', marginTop: 16, elevation: 10, shadowColor: Colors.primaryRed, shadowOffset: { width: 0, height: 6 }, shadowOpacity: 0.4, shadowRadius: 10 },
  loginBtnText: { color: '#FFFFFF', fontSize: 16, fontWeight: '700' },
  signUpBtn: { width: '100%', height: 52, borderRadius: 26, borderWidth: 1.2, borderColor: '#1E293B', justifyContent: 'center', alignItems: 'center', marginTop: 14 },
  signUpBtnText: { color: '#0F172A', fontSize: 15, fontWeight: '700' },
  socialDividerRow: { width: '100%', flexDirection: 'row', alignItems: 'center', marginVertical: 24 },
  dividerLine: { flex: 1, height: 1, backgroundColor: '#E2E8F0' },
  socialText: { fontSize: 12, color: '#94A3B8', marginHorizontal: 12 },
  socialIconsRow: { flexDirection: 'row', justifyContent: 'center' },
  socialCircle: { width: 46, height: 46, borderRadius: 23, backgroundColor: '#FFFFFF', borderWidth: 1, borderColor: '#E2E8F0', justifyContent: 'center', alignItems: 'center', marginHorizontal: 10, elevation: 1 },
  facebookText: { fontSize: 22, fontWeight: 'bold', color: '#1877F2' },
  googleText: { fontSize: 20, fontWeight: 'bold', color: '#EA4335' }
});
