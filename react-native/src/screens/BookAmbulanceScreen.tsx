import React from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  SafeAreaView,
  ScrollView,
  Alert
} from 'react-native';
import { Colors } from '../theme/colors';

export const BookAmbulanceScreen = ({ navigation, route }: any) => {
  const driver = {
    name: 'Bharat Khatri Chhetri',
    phone: '+977 9765228814',
    vehicle: 'Nepal Sprinter Ambulance • AMB 2234',
    eta: 8,
    distance: '2.3 km'
  };

  const hospital = {
    name: 'Bhaktapur Hospital',
    address: '44600 Radheradhe, Bhaktapur',
    phone: '+977 9765228814'
  };

  const handleConfirm = () => {
    navigation.navigate('ActiveTracking', { driver, hospital });
  };

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.appBar}>
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Text style={styles.backBtn}>←</Text>
        </TouchableOpacity>
        <Text style={styles.barTitle}>Book Ambulance</Text>
        <View style={{ width: 24 }} />
      </View>

      <ScrollView contentContainerStyle={styles.scroll}>
        {/* Map Header with ETA Badge (Screenshot Page 3 & 4) */}
        <View style={styles.mapSnippet}>
          <View style={styles.etaBadge}>
            <Text style={styles.etaText}>⏰ 8 mins away • 2.3 km</Text>
          </View>
        </View>

        {/* Driver Card (Screenshot Page 3 & 4) */}
        <View style={styles.card}>
          <View style={styles.driverRow}>
            <View style={styles.driverLeft}>
              <View style={styles.avatarCircle}>
                <Text style={{ fontSize: 22 }}>👨‍⚕️</Text>
              </View>
              <View>
                <Text style={styles.driverName}>{driver.name}</Text>
                <Text style={styles.vehicleText}>🚑 {driver.vehicle}</Text>
              </View>
            </View>

            <TouchableOpacity
              style={styles.callCircle}
              onPress={() => Alert.alert('Dialing', driver.phone)}
            >
              <Text style={styles.callIcon}>📞</Text>
            </TouchableOpacity>
          </View>
        </View>

        {/* Receiving Hospital Card */}
        <View style={styles.card}>
          <Text style={styles.hospitalTitle}>🏥 {hospital.name}</Text>
          <Text style={styles.hospitalAddress}>{hospital.address}</Text>
          <View style={styles.hospitalStatusRow}>
            <Text style={styles.readyTag}>Ready to receive</Text>
            <Text style={styles.hospitalPhone}>• 📞 {hospital.phone}</Text>
          </View>
        </View>

        {/* Price Details (Screenshot Page 3 & 4) */}
        <View style={styles.card}>
          <Text style={styles.cardSectionTitle}>💳 Price Details</Text>
          <View style={styles.priceRow}>
            <Text style={styles.priceLabel}>Base fare</Text>
            <Text style={styles.priceVal}>Rs 50.00</Text>
          </View>
          <View style={styles.priceRow}>
            <Text style={styles.priceLabel}>Distance charge</Text>
            <Text style={styles.priceVal}>Rs 25.00</Text>
          </View>
          <View style={styles.priceRow}>
            <Text style={styles.priceLabel}>Emergency equipment</Text>
            <Text style={styles.priceVal}>Rs 75.00</Text>
          </View>

          <View style={styles.divider} />

          <View style={styles.priceRow}>
            <Text style={styles.totalLabel}>Total amount</Text>
            <Text style={styles.totalVal}>Rs 150.00</Text>
          </View>
          <Text style={styles.paymentMethod}>Payment method: Esewa / COD</Text>
        </View>

        {/* Emergency Instructions (Screenshot Page 3 & 4) */}
        <Text style={styles.instructionHeader}>Emergency Instructions</Text>
        <Text style={styles.instructionItem}>• Keep the emergency contact number ready</Text>
        <Text style={styles.instructionItem}>• Have medical history documents if available</Text>
        <Text style={styles.instructionItem}>• Clear the path for stretcher access</Text>

        <TouchableOpacity style={styles.confirmBtn} onPress={handleConfirm}>
          <Text style={styles.confirmBtnText}>Confirm Emergency Ambulance</Text>
        </TouchableOpacity>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: Colors.background },
  appBar: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#FFFFFF', paddingHorizontal: 16, paddingVertical: 14, elevation: 2 },
  backBtn: { fontSize: 24, fontWeight: '700', color: Colors.textPrimary },
  barTitle: { fontSize: 18, fontWeight: '700', color: Colors.textPrimary },
  scroll: { padding: 16 },
  mapSnippet: { height: 160, backgroundColor: '#E2E8F0', borderRadius: 16, justifyContent: 'flex-end', padding: 12, marginBottom: 14 },
  etaBadge: { backgroundColor: '#FFFFFF', paddingHorizontal: 14, paddingVertical: 7, borderRadius: 20, alignSelf: 'flex-start', elevation: 2 },
  etaText: { fontSize: 13, fontWeight: '700', color: Colors.textPrimary },
  card: { backgroundColor: '#FFFFFF', borderRadius: 16, padding: 16, marginBottom: 12, elevation: 1 },
  driverRow: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' },
  driverLeft: { flexDirection: 'row', alignItems: 'center' },
  avatarCircle: { width: 50, height: 50, borderRadius: 25, backgroundColor: '#E2E8F0', justifyContent: 'center', alignItems: 'center', marginRight: 12 },
  driverName: { fontSize: 15, fontWeight: '700', color: Colors.textPrimary },
  vehicleText: { fontSize: 12, color: Colors.textSecondary, marginTop: 2 },
  callCircle: { width: 44, height: 44, borderRadius: 22, backgroundColor: Colors.primaryRed, justifyContent: 'center', alignItems: 'center' },
  callIcon: { fontSize: 18, color: '#FFFFFF' },
  hospitalTitle: { fontSize: 15, fontWeight: '700', color: Colors.textPrimary },
  hospitalAddress: { fontSize: 12, color: Colors.textSecondary, marginTop: 4 },
  hospitalStatusRow: { flexDirection: 'row', alignItems: 'center', marginTop: 8 },
  readyTag: { color: Colors.greenAvailable, fontSize: 12, fontWeight: '700' },
  hospitalPhone: { color: Colors.primaryRed, fontSize: 12, fontWeight: '600', marginLeft: 4 },
  cardSectionTitle: { fontSize: 15, fontWeight: '700', color: Colors.textPrimary, marginBottom: 10 },
  priceRow: { flexDirection: 'row', justifyContent: 'space-between', marginVertical: 3 },
  priceLabel: { fontSize: 13, color: Colors.textSecondary },
  priceVal: { fontSize: 13, fontWeight: '600', color: Colors.textPrimary },
  divider: { height: 1, backgroundColor: Colors.border, marginVertical: 8 },
  totalLabel: { fontSize: 15, fontWeight: '700', color: Colors.textPrimary },
  totalVal: { fontSize: 17, fontWeight: '700', color: Colors.primaryRed },
  paymentMethod: { fontSize: 12, color: Colors.textSecondary, marginTop: 4 },
  instructionHeader: { fontSize: 15, fontWeight: '700', color: Colors.textPrimary, marginTop: 6, marginBottom: 6 },
  instructionItem: { fontSize: 13, color: '#334155', marginVertical: 2 },
  confirmBtn: { backgroundColor: Colors.primaryRed, height: 54, borderRadius: 14, justifyContent: 'center', alignItems: 'center', marginTop: 24, marginBottom: 20 },
  confirmBtnText: { color: '#FFFFFF', fontSize: 16, fontWeight: '700' }
});
