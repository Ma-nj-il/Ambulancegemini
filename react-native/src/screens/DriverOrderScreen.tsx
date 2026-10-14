import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  SafeAreaView,
  ScrollView
} from 'react-native';
import { Colors } from '../theme/colors';
import { ambulanceApi } from '../api/ambulanceApi';

export const DriverOrderScreen = ({ navigation }: any) => {
  const [seconds, setSeconds] = useState(29);

  useEffect(() => {
    const timer = setInterval(() => {
      setSeconds((prev) => (prev > 0 ? prev - 1 : 0));
    }, 1000);
    return () => clearInterval(timer);
  }, []);

  const handleAccept = async () => {
    await ambulanceApi.acceptDriverOrder('order_12345');
    navigation.navigate('ActiveTracking');
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.scroll}>
        {/* Pulsing indicator */}
        <View style={styles.topPulseContainer}>
          <View style={styles.pulseOuter}>
            <View style={styles.pulseInner} />
          </View>
        </View>

        <Text style={styles.orderTitle}>New Order!</Text>
        <Text style={styles.orderAmount}>Rs 30</Text>
        <Text style={styles.countdownText}>0:{seconds.toString().padStart(2, '0')}</Text>

        {/* Location & Passenger Card (Screenshot Page 5) */}
        <View style={styles.card}>
          <View style={styles.row}>
            <Text style={styles.pinIcon}>📍</Text>
            <View>
              <Text style={styles.smallLabel}>Pickup Location</Text>
              <Text style={styles.boldText}>44600 Radhe Radhe , Bhaktapur</Text>
              <Text style={styles.subText}>2.3 miles away</Text>
            </View>
          </View>

          <View style={styles.divider} />

          <View style={styles.passengerRow}>
            <View style={styles.avatar}>
              <Text style={{ fontSize: 20 }}>👤</Text>
            </View>
            <View>
              <Text style={styles.boldText}>Ramhari Khadka</Text>
              <Text style={styles.subText}>Order #12345</Text>
            </View>
          </View>

          <View style={styles.detailsRow}>
            <Text style={styles.subText}>🚑 Standard Delivery (15-20 min)</Text>
            <View style={styles.esewaChip}>
              <Text style={styles.esewaText}>💵 Esewa Payment</Text>
            </View>
          </View>
        </View>

        {/* Map Preview */}
        <View style={styles.mapSnippet}>
          <Text style={styles.mapText}>🗺️ Live Kathmandu Route Preview</Text>
        </View>

        {/* Order Notes (Screenshot Page 5) */}
        <View style={styles.notesCard}>
          <Text style={styles.notesLabel}>ℹ️ Order Notes</Text>
          <Text style={styles.notesText}>
            Birami ekdam serious hunuhuncha sakdo chitto aunu hola .
          </Text>
        </View>

        {/* Action Buttons (Screenshot Page 5) */}
        <TouchableOpacity style={styles.acceptBtn} onPress={handleAccept}>
          <Text style={styles.acceptText}>Accept Order</Text>
        </TouchableOpacity>

        <TouchableOpacity style={styles.declineBtn} onPress={() => navigation.navigate('Home')}>
          <Text style={styles.declineText}>Decline</Text>
        </TouchableOpacity>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: Colors.background },
  scroll: { padding: 20, alignItems: 'center' },
  topPulseContainer: { marginVertical: 14 },
  pulseOuter: { width: 44, height: 44, borderRadius: 22, backgroundColor: Colors.greenAvailable, justifyContent: 'center', alignItems: 'center' },
  pulseInner: { width: 22, height: 22, borderRadius: 11, backgroundColor: '#FFFFFF' },
  orderTitle: { fontSize: 22, fontWeight: '700', color: Colors.textPrimary },
  orderAmount: { fontSize: 32, fontWeight: '800', color: Colors.greenAvailable, marginVertical: 4 },
  countdownText: { fontSize: 18, fontWeight: '700', color: Colors.primaryRed, marginBottom: 16 },
  card: { width: '100%', backgroundColor: '#FFFFFF', borderRadius: 16, padding: 18, elevation: 1, marginBottom: 14 },
  row: { flexDirection: 'row', alignItems: 'flex-start' },
  pinIcon: { fontSize: 18, marginRight: 10 },
  smallLabel: { fontSize: 12, color: Colors.textSecondary },
  boldText: { fontSize: 14, fontWeight: '700', color: Colors.textPrimary },
  subText: { fontSize: 12, color: Colors.textSecondary },
  divider: { height: 1, backgroundColor: Colors.border, marginVertical: 12 },
  passengerRow: { flexDirection: 'row', alignItems: 'center', marginBottom: 10 },
  avatar: { width: 40, height: 40, borderRadius: 20, backgroundColor: '#E2E8F0', justifyContent: 'center', alignItems: 'center', marginRight: 10 },
  detailsRow: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' },
  esewaChip: { backgroundColor: Colors.greenLight, paddingHorizontal: 10, paddingVertical: 4, borderRadius: 8 },
  esewaText: { color: '#065F46', fontSize: 11, fontWeight: '600' },
  mapSnippet: { width: '100%', height: 120, backgroundColor: '#E2E8F0', borderRadius: 14, justifyContent: 'center', alignItems: 'center', marginBottom: 14 },
  mapText: { fontSize: 13, color: '#475569', fontWeight: '600' },
  notesCard: { width: '100%', backgroundColor: '#F1F5F9', borderRadius: 14, padding: 14, marginBottom: 24 },
  notesLabel: { fontSize: 12, fontWeight: '700', color: Colors.textPrimary, marginBottom: 4 },
  notesText: { fontSize: 13, color: '#334155', fontWeight: '500' },
  acceptBtn: { width: '100%', height: 52, backgroundColor: Colors.greenAvailable, borderRadius: 14, justifyContent: 'center', alignItems: 'center', marginBottom: 10 },
  acceptText: { color: '#FFFFFF', fontSize: 16, fontWeight: '700' },
  declineBtn: { width: '100%', height: 48, borderWidth: 1, borderColor: Colors.border, borderRadius: 14, justifyContent: 'center', alignItems: 'center' },
  declineText: { color: Colors.textSecondary, fontSize: 14, fontWeight: '600' }
});
