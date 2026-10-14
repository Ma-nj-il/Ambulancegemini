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

export const ActiveTrackingScreen = ({ navigation }: any) => {
  return (
    <SafeAreaView style={styles.container}>
      {/* Red Alert Banner (Screenshot Page 8) */}
      <View style={styles.redBanner}>
        <Text style={styles.redBannerText}>ACTIVE EMERGENCY TRANSPORT</Text>
      </View>

      <ScrollView contentContainerStyle={styles.scroll}>
        {/* Navigation Map snippet with ETA (Screenshot Page 8) */}
        <View style={styles.mapView}>
          <View style={styles.etaBadge}>
            <Text style={styles.etaText}>⏰ ETA: 8 minutes</Text>
          </View>
        </View>

        {/* Turn-by-Turn Instruction Card (Screenshot Page 8) */}
        <View style={styles.turnCard}>
          <Text style={styles.turnArrow}>↪️</Text>
          <View style={{ marginLeft: 10 }}>
            <Text style={styles.turnTitle}>Turn right onto Medical Cent...</Text>
            <Text style={styles.turnSub}>0.5 miles</Text>
          </View>
        </View>

        {/* Patient Status (Screenshot Page 8) */}
        <View style={styles.card}>
          <View style={styles.patientRow}>
            <Text style={styles.patientName}>Bharat Khatri Chhetri</Text>
            <View style={styles.urgentBadge}>
              <Text style={styles.urgentText}>URGENT</Text>
            </View>
          </View>
          <Text style={styles.statusLabel}>
            Status: <Text style={styles.statusGreen}>Stable</Text>
          </Text>
          <Text style={styles.conditionText}>Condition: Chest Pain</Text>
          <Text style={styles.locationText}>📍 44600 Radhe Radhe, Bhaktapur</Text>
        </View>

        {/* Destination Hospital (Screenshot Page 8) */}
        <View style={styles.card}>
          <Text style={styles.hospitalName}>🏥 Bhaktapur Hospital</Text>
          <Text style={styles.hospitalAddress}>44600, Naya Thimi, Bhaktaprur</Text>
          <Text style={styles.hospitalEta}>🕒 8 min   📐 2.3 miles</Text>
        </View>

        {/* Action Buttons Triplet (Screenshot Page 8) */}
        <View style={styles.tripletRow}>
          <TouchableOpacity
            style={[styles.actionBtn, { backgroundColor: Colors.primaryBlue }]}
            onPress={() => Alert.alert('Calling Hospital', '+977 9765228814')}
          >
            <Text style={styles.actionBtnIcon}>📞</Text>
            <Text style={styles.actionBtnText}>Call Hospital</Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={[styles.actionBtn, { backgroundColor: Colors.primaryRed }]}
            onPress={() => Alert.alert('SOS Broadcasted', 'Admin & Police notified.')}
          >
            <Text style={styles.actionBtnIcon}>🚨</Text>
            <Text style={styles.actionBtnText}>Emergency</Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={[styles.actionBtn, { backgroundColor: '#F1F5F9' }]}
            onPress={() => Alert.alert('Notes', 'Patient is conscious and waiting at lobby.')}
          >
            <Text style={[styles.actionBtnIcon, { color: '#334155' }]}>📝</Text>
            <Text style={[styles.actionBtnText, { color: '#334155' }]}>Notes</Text>
          </TouchableOpacity>
        </View>

        {/* Safety Warning */}
        <Text style={styles.safetyNotice}>⚠️ Please ensure safe driving practices</Text>

        {/* End Trip / Arrived CTA */}
        <TouchableOpacity
          style={styles.completeBtn}
          onPress={() => navigation.navigate('TripCompleted')}
        >
          <Text style={styles.completeBtnText}>End Trip / Hospital Arrival</Text>
        </TouchableOpacity>

        <TouchableOpacity
          style={styles.cancelBtn}
          onPress={() => navigation.navigate('Home')}
        >
          <Text style={styles.cancelBtnText}>Cancel Trip</Text>
        </TouchableOpacity>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: Colors.background },
  redBanner: { backgroundColor: Colors.primaryRed, paddingVertical: 10, alignItems: 'center' },
  redBannerText: { color: '#FFFFFF', fontWeight: '800', fontSize: 13, letterSpacing: 1 },
  scroll: { padding: 16 },
  mapView: { height: 180, backgroundColor: '#E2E8F0', borderRadius: 16, justifyContent: 'flex-end', padding: 12, marginBottom: 12 },
  etaBadge: { backgroundColor: '#FFFFFF', paddingHorizontal: 14, paddingVertical: 6, borderRadius: 18, alignSelf: 'flex-start', elevation: 2 },
  etaText: { fontSize: 13, fontWeight: '700', color: Colors.textPrimary },
  turnCard: { flexDirection: 'row', alignItems: 'center', backgroundColor: '#FFFFFF', padding: 14, borderRadius: 14, marginBottom: 12, elevation: 1 },
  turnArrow: { fontSize: 20 },
  turnTitle: { fontSize: 14, fontWeight: '700', color: Colors.textPrimary },
  turnSub: { fontSize: 12, color: Colors.textSecondary },
  card: { backgroundColor: '#FFFFFF', borderRadius: 16, padding: 16, marginBottom: 12, elevation: 1 },
  patientRow: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 6 },
  patientName: { fontSize: 16, fontWeight: '700', color: Colors.textPrimary },
  urgentBadge: { backgroundColor: Colors.primaryRed, paddingHorizontal: 8, paddingVertical: 3, borderRadius: 6 },
  urgentText: { color: '#FFFFFF', fontSize: 11, fontWeight: '700' },
  statusLabel: { fontSize: 13, color: Colors.textSecondary, marginBottom: 3 },
  statusGreen: { color: Colors.greenAvailable, fontWeight: '700' },
  conditionText: { fontSize: 13, color: Colors.textPrimary, marginBottom: 4 },
  locationText: { fontSize: 12, color: Colors.textSecondary },
  hospitalName: { fontSize: 15, fontWeight: '700', color: Colors.textPrimary },
  hospitalAddress: { fontSize: 12, color: Colors.textSecondary, marginTop: 2 },
  hospitalEta: { fontSize: 12, fontWeight: '600', color: Colors.textPrimary, marginTop: 4 },
  tripletRow: { flexDirection: 'row', justifyContent: 'space-between', marginVertical: 8 },
  actionBtn: { flex: 1, paddingVertical: 12, borderRadius: 12, alignItems: 'center', marginHorizontal: 3 },
  actionBtnIcon: { fontSize: 16, color: '#FFFFFF', marginBottom: 2 },
  actionBtnText: { fontSize: 11, fontWeight: '700', color: '#FFFFFF' },
  safetyNotice: { textAlign: 'center', fontSize: 12, color: '#475569', marginVertical: 10 },
  completeBtn: { backgroundColor: Colors.greenAvailable, height: 52, borderRadius: 14, justifyContent: 'center', alignItems: 'center', marginTop: 10 },
  completeBtnText: { color: '#FFFFFF', fontSize: 15, fontWeight: '700' },
  cancelBtn: { height: 46, justifyContent: 'center', alignItems: 'center', marginTop: 6 },
  cancelBtnText: { color: Colors.textSecondary, fontSize: 14 }
});
