import React, { useState } from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  SafeAreaView,
  ScrollView,
  ActivityIndicator
} from 'react-native';
import { Colors } from '../theme/colors';
import { ambulanceApi } from '../api/ambulanceApi';

export const RequestAmbulanceScreen = ({ navigation, route }: any) => {
  const initialType = route?.params?.type || 'BASIC';
  const [selectedType, setSelectedType] = useState<'BASIC' | 'ICU' | 'VENTILATOR'>(initialType);
  const [loading, setLoading] = useState(false);

  const handleSendRequest = async () => {
    setLoading(true);
    try {
      const booking = await ambulanceApi.requestAmbulance({
        pickupLocation: 'Baneshwor, Kathmandu',
        dropLocation: 'Bhaktapur Hospital',
        ambulanceType: selectedType,
        isEmergency: true
      });
      navigation.navigate('BookAmbulance', { booking });
    } finally {
      setLoading(false);
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.scroll}>
        {/* Top Header (Screenshot 14) */}
        <View style={styles.topBar}>
          <TouchableOpacity onPress={() => navigation.goBack()}>
            <Text style={styles.backBtn}>←</Text>
          </TouchableOpacity>

          <View style={styles.titleWithIcon}>
            <View style={styles.redIconBadge}>
              <Text style={{ fontSize: 18 }}>➕</Text>
            </View>
            <Text style={styles.titleText}>Request</Text>
          </View>

          <View style={styles.circlePlaceholder} />
        </View>

        {/* Map Preview Card (Screenshot 14) */}
        <View style={styles.mapCard}>
          <View style={styles.mapSimulation}>
            <Text style={styles.mapPinStart}>🟢 Pickup (Baneshwor)</Text>
            <View style={styles.steppedLine} />
            <Text style={styles.mapPinEnd}>🔴 Destination (Bhaktapur)</Text>
          </View>
        </View>

        {/* Route Details Card (Screenshot 14) */}
        <View style={styles.routeCard}>
          <View style={styles.routeRow}>
            <View style={styles.pickupDot} />
            <View>
              <Text style={styles.routeLabel}>Pickup</Text>
              <Text style={styles.routeValue}>Baneshwor, Kathmandu</Text>
            </View>
          </View>

          <View style={styles.dottedConnector} />

          <TouchableOpacity
            style={styles.routeRow}
            onPress={() => navigation.navigate('NearbyHospitals')}
          >
            <Text style={styles.destPin}>📍</Text>
            <View>
              <Text style={styles.routeLabel}>Destination</Text>
              <Text style={styles.routeValue}>Bhaktapur Hospital</Text>
            </View>
          </TouchableOpacity>
        </View>

        {/* Ambulance Type Section (Screenshot 14) */}
        <Text style={styles.sectionLabel}>AMBULANCE TYPE</Text>

        <View style={styles.typeRow}>
          {[
            { key: 'BASIC', title: 'Basic', sub: 'Standard\ntransport' },
            { key: 'ICU', title: 'Advanced', sub: 'Provider-\nequipped' },
            { key: 'VENTILATOR', title: 'Ventilator', sub: 'Where\navailable' },
          ].map((item: any) => {
            const isSel = selectedType === item.key;
            return (
              <TouchableOpacity
                key={item.key}
                style={[styles.typeCard, isSel && styles.typeCardActive]}
                onPress={() => setSelectedType(item.key)}
              >
                <Text style={styles.ambulanceIcon}>🚑</Text>
                <Text style={styles.typeTitle}>{item.title}</Text>
                <Text style={styles.typeSub}>{item.sub}</Text>
              </TouchableOpacity>
            );
          })}
        </View>

        {/* Send Request CTA Pill Button (Screenshot 14) */}
        <TouchableOpacity
          style={styles.sendRequestBtn}
          onPress={handleSendRequest}
          disabled={loading}
        >
          {loading ? (
            <ActivityIndicator color="#FFFFFF" />
          ) : (
            <Text style={styles.sendRequestText}>Send request →</Text>
          )}
        </TouchableOpacity>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#FFFFFF' },
  scroll: { padding: 20 },
  topBar: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 },
  backBtn: { fontSize: 24, fontWeight: '700', color: Colors.textPrimary },
  titleWithIcon: { flexDirection: 'row', alignItems: 'center' },
  redIconBadge: { width: 34, height: 34, borderRadius: 10, backgroundColor: Colors.primaryRed, justifyContent: 'center', alignItems: 'center', marginRight: 8 },
  titleText: { fontSize: 20, fontWeight: '700', color: Colors.textPrimary },
  circlePlaceholder: { width: 34, height: 34, borderRadius: 17, backgroundColor: '#E2E8F0' },
  mapCard: { height: 180, backgroundColor: '#F1F5F9', borderRadius: 24, overflow: 'hidden', marginBottom: 18, justifyContent: 'center', alignItems: 'center' },
  mapSimulation: { alignItems: 'center' },
  mapPinStart: { fontSize: 13, fontWeight: '600', color: '#0F172A' },
  steppedLine: { width: 2, height: 40, backgroundColor: Colors.primaryRed, marginVertical: 4 },
  mapPinEnd: { fontSize: 13, fontWeight: '700', color: Colors.primaryRed },
  routeCard: { backgroundColor: '#FFFFFF', borderWidth: 1, borderColor: '#E2E8F0', borderRadius: 20, padding: 18, marginBottom: 24 },
  routeRow: { flexDirection: 'row', alignItems: 'center' },
  pickupDot: { width: 10, height: 10, borderRadius: 5, backgroundColor: '#0F172A', marginRight: 12 },
  destPin: { fontSize: 16, marginRight: 8 },
  dottedConnector: { width: 2, height: 20, borderStyle: 'dotted', borderWidth: 1, borderColor: '#CBD5E1', marginLeft: 4, marginVertical: 4 },
  routeLabel: { fontSize: 12, color: Colors.textSecondary },
  routeValue: { fontSize: 15, fontWeight: '700', color: Colors.textPrimary },
  sectionLabel: { fontSize: 13, fontWeight: '700', color: '#475569', letterSpacing: 1, marginBottom: 12 },
  typeRow: { flexDirection: 'row', justifyContent: 'space-between', marginBottom: 32 },
  typeCard: { flex: 1, backgroundColor: '#FFFFFF', borderWidth: 1.5, borderColor: '#E2E8F0', borderRadius: 20, padding: 14, marginHorizontal: 4, alignItems: 'center', minHeight: 125, justifyContent: 'center' },
  typeCardActive: { borderColor: Colors.primaryRed, backgroundColor: Colors.primaryRedLight },
  ambulanceIcon: { fontSize: 24, marginBottom: 6 },
  typeTitle: { fontSize: 14, fontWeight: '700', color: Colors.textPrimary },
  typeSub: { fontSize: 11, color: Colors.textSecondary, textAlign: 'center', marginTop: 2, lineHeight: 14 },
  sendRequestBtn: { backgroundColor: Colors.primaryRed, height: 56, borderRadius: 28, justifyContent: 'center', alignItems: 'center', elevation: 3 },
  sendRequestText: { color: '#FFFFFF', fontSize: 17, fontWeight: '700' }
});
