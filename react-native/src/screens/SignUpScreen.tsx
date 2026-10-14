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

export const SignUpScreen = ({ navigation }: any) => {
  const [name, setName] = useState('Ramhari Khadka');
  const [phone, setPhone] = useState('9841234567');
  const [bloodGroup, setBloodGroup] = useState('O+');
  const [emergencyContactName, setEmergencyContactName] = useState('Sita Khadka');
  const [emergencyContactPhone, setEmergencyContactPhone] = useState('9801234567');
  const [loading, setLoading] = useState(false);

  const handleRegister = async () => {
    setLoading(true);
    try {
      await ambulanceApi.handleSignUp({
        fullName: name,
        phoneNumber: phone,
        bloodGroup,
        emergencyContactName,
        emergencyContactPhone
      });
      Alert.alert('Success', 'Profile registered successfully!', [
        { text: 'OK', onPress: () => navigation.navigate('Home') }
      ]);
    } catch (err: any) {
      Alert.alert('Error', err.message || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.scroll}>
        <Text style={styles.title}>Create Account</Text>
        <Text style={styles.subtitle}>Save essential medical & emergency contact details</Text>

        <Text style={styles.label}>Full Name</Text>
        <TextInput style={styles.input} value={name} onChangeText={setName} placeholder="Your Full Name" />

        <Text style={styles.label}>Mobile Number</Text>
        <TextInput style={styles.input} value={phone} onChangeText={setPhone} keyboardType="phone-pad" />

        <Text style={styles.label}>Blood Group</Text>
        <View style={styles.bloodRow}>
          {['A+', 'B+', 'O+', 'AB+', 'O-'].map((bg) => {
            const isSel = bloodGroup === bg;
            return (
              <TouchableOpacity
                key={bg}
                style={[styles.bloodChip, isSel && styles.bloodChipActive]}
                onPress={() => setBloodGroup(bg)}
              >
                <Text style={[styles.bloodText, isSel && styles.bloodTextActive]}>{bg}</Text>
              </TouchableOpacity>
            );
          })}
        </View>

        <Text style={[styles.label, { marginTop: 14 }]}>Primary Emergency Contact Person</Text>
        <TextInput
          style={styles.input}
          value={emergencyContactName}
          onChangeText={setEmergencyContactName}
          placeholder="Contact Person Name"
        />

        <Text style={styles.label}>Emergency Phone Number</Text>
        <TextInput
          style={styles.input}
          value={emergencyContactPhone}
          onChangeText={setEmergencyContactPhone}
          keyboardType="phone-pad"
        />

        <TouchableOpacity style={styles.primaryBtn} onPress={handleRegister} disabled={loading}>
          {loading ? (
            <ActivityIndicator color="#FFFFFF" />
          ) : (
            <Text style={styles.primaryBtnText}>Complete Registration</Text>
          )}
        </TouchableOpacity>

        <TouchableOpacity style={styles.linkRow} onPress={() => navigation.navigate('Login')}>
          <Text style={styles.linkText}>Already have an account? Sign In</Text>
        </TouchableOpacity>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: Colors.background },
  scroll: { padding: 24 },
  title: { fontSize: 24, fontWeight: '700', color: Colors.textPrimary },
  subtitle: { fontSize: 13, color: Colors.textSecondary, marginTop: 4, marginBottom: 20 },
  label: { fontSize: 13, fontWeight: '600', color: Colors.textSecondary, marginBottom: 6, marginTop: 10 },
  input: { backgroundColor: '#FFFFFF', borderWidth: 1, borderColor: Colors.border, borderRadius: 12, height: 48, paddingHorizontal: 14, fontSize: 14, color: Colors.textPrimary },
  bloodRow: { flexDirection: 'row', justifyContent: 'space-between', marginTop: 4 },
  bloodChip: { flex: 1, backgroundColor: '#FFFFFF', borderWidth: 1, borderColor: Colors.border, borderRadius: 8, paddingVertical: 8, alignItems: 'center', marginHorizontal: 3 },
  bloodChipActive: { backgroundColor: Colors.primaryRed, borderColor: Colors.primaryRed },
  bloodText: { fontSize: 13, fontWeight: '600', color: Colors.textPrimary },
  bloodTextActive: { color: '#FFFFFF', fontWeight: '700' },
  primaryBtn: { backgroundColor: Colors.primaryRed, height: 52, borderRadius: 14, justifyContent: 'center', alignItems: 'center', marginTop: 28 },
  primaryBtnText: { color: '#FFFFFF', fontSize: 16, fontWeight: '700' },
  linkRow: { marginTop: 18, alignItems: 'center' },
  linkText: { color: Colors.textSecondary, fontSize: 13, fontWeight: '600' }
});
