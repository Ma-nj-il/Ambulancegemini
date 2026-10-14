import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  FlatList,
  SafeAreaView
} from 'react-native';
import { Colors } from '../theme/colors';
import { ambulanceApi, HospitalInfo } from '../api/ambulanceApi';

export const NearbyHospitalsScreen = ({ navigation }: any) => {
  const [hospitals, setHospitals] = useState<HospitalInfo[]>([]);
  const [filter, setFilter] = useState('Nearest First');

  useEffect(() => {
    ambulanceApi.getNearbyHospitals().then(setHospitals);
  }, []);

  const filtered = hospitals.filter((h) => {
    if (filter === 'Ambulance Available') return h.availableAmbulances > 0;
    return true;
  });

  return (
    <SafeAreaView style={styles.container}>
      {/* Top Header (Screenshot Page 2) */}
      <View style={styles.appBar}>
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Text style={styles.backArrow}>←</Text>
        </TouchableOpacity>
        <Text style={styles.barTitle}>Nearby Hospitals</Text>
        <Text style={styles.filterIcon}>⚙️</Text>
      </View>

      {/* Mini Map Banner */}
      <View style={styles.mapBanner}>
        <Text style={styles.mapPin}>📍 Bagmati River • Kathmandu City Map View</Text>
      </View>

      {/* Stat Bar (Screenshot Page 2) */}
      <View style={styles.statBar}>
        <View style={styles.statItem}>
          <Text style={styles.statIcon}>🏥</Text>
          <Text style={styles.statText}>5 Hospitals Nearby</Text>
        </View>
        <View style={styles.statDivider} />
        <View style={styles.statItem}>
          <Text style={styles.statIcon}>🚑</Text>
          <Text style={styles.statText}>2 Ambulances Available</Text>
        </View>
      </View>

      {/* Filter Chips (Screenshot Page 2) */}
      <View style={styles.filterRow}>
        {['Nearest First', 'Ambulance Available'].map((f) => {
          const isSel = filter === f;
          return (
            <TouchableOpacity
              key={f}
              style={[styles.filterChip, isSel && styles.filterChipActive]}
              onPress={() => setFilter(f)}
            >
              <Text style={[styles.filterText, isSel && styles.filterTextActive]}>{f}</Text>
            </TouchableOpacity>
          );
        })}
      </View>

      {/* Hospital Cards List (Screenshot Page 2) */}
      <FlatList
        data={filtered}
        keyExtractor={(item) => item.id}
        contentContainerStyle={{ padding: 16 }}
        renderItem={({ item }) => {
          const hasAvailable = item.availableAmbulances > 0;
          return (
            <View style={styles.card}>
              <View style={styles.cardHeader}>
                <View style={{ flex: 1 }}>
                  <Text style={styles.hospitalName}>{item.name}</Text>
                  <View style={styles.statusLine}>
                    <View style={[styles.dot, { backgroundColor: hasAvailable ? Colors.greenAvailable : '#EF4444' }]} />
                    <Text style={[styles.availableLabel, { color: hasAvailable ? '#065F46' : '#991B1B' }]}>
                      {item.availableAmbulances}/{item.totalAmbulances} Ambulance Available
                    </Text>
                  </View>
                  <Text style={styles.addressText}>📍 {item.address}</Text>
                </View>
                <Text style={styles.distanceText}>{item.distanceText}</Text>
              </View>

              <View style={styles.cardBottom}>
                <View style={styles.tagRow}>
                  {item.tags.map((t, idx) => (
                    <View key={idx} style={styles.tag}>
                      <Text style={styles.tagText}>{t}</Text>
                    </View>
                  ))}
                </View>

                <TouchableOpacity
                  style={[styles.bookBtn, !hasAvailable && styles.bookBtnDisabled]}
                  disabled={!hasAvailable}
                  onPress={() => navigation.navigate('BookAmbulance', { hospital: item })}
                >
                  <Text style={[styles.bookBtnText, !hasAvailable && styles.bookBtnTextDisabled]}>
                    Book Ambulance
                  </Text>
                </TouchableOpacity>
              </View>
            </View>
          );
        }}
        ListFooterComponent={
          <Text style={styles.footerRadius}>Showing hospitals within 5km radius</Text>
        }
      />

      {/* Bottom Emergency Bar (Screenshot Page 12) */}
      <View style={styles.bottomBar}>
        <TouchableOpacity
          style={styles.emergencyCallBtn}
          onPress={() => navigation.navigate('EmergencyNumbers')}
        >
          <Text style={styles.emergencyCallText}>📞 Emergency Call</Text>
        </TouchableOpacity>

        <TouchableOpacity
          style={styles.bookHospitalBtn}
          onPress={() => {
            const first = hospitals.find((h) => h.availableAmbulances > 0) || hospitals[0];
            navigation.navigate('BookAmbulance', { hospital: first });
          }}
        >
          <Text style={styles.bookHospitalText}>Book Ambulance</Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: Colors.background },
  appBar: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#FFFFFF', paddingHorizontal: 16, paddingVertical: 14, elevation: 2 },
  backArrow: { fontSize: 22, fontWeight: '700', color: Colors.textPrimary },
  barTitle: { fontSize: 18, fontWeight: '700', color: Colors.textPrimary },
  filterIcon: { fontSize: 18 },
  mapBanner: { height: 130, backgroundColor: '#E2E8F0', justifyContent: 'center', alignItems: 'center' },
  mapPin: { fontSize: 13, fontWeight: '600', color: '#475569' },
  statBar: { flexDirection: 'row', backgroundColor: '#FFFFFF', paddingVertical: 12, justifyContent: 'space-evenly', alignItems: 'center', borderBottomWidth: 1, borderColor: Colors.border },
  statItem: { flexDirection: 'row', alignItems: 'center' },
  statIcon: { fontSize: 16, marginRight: 6 },
  statText: { fontSize: 13, fontWeight: '700', color: Colors.textPrimary },
  statDivider: { width: 1, height: 16, backgroundColor: Colors.border },
  filterRow: { flexDirection: 'row', paddingHorizontal: 16, paddingVertical: 10 },
  filterChip: { backgroundColor: '#FFFFFF', borderWidth: 1, borderColor: Colors.border, borderRadius: 20, paddingHorizontal: 14, paddingVertical: 6, marginRight: 8 },
  filterChipActive: { backgroundColor: Colors.primaryRed, borderColor: Colors.primaryRed },
  filterText: { fontSize: 12, color: Colors.textSecondary },
  filterTextActive: { color: '#FFFFFF', fontWeight: '700' },
  card: { backgroundColor: '#FFFFFF', borderRadius: 16, padding: 16, marginBottom: 10, elevation: 1 },
  cardHeader: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'flex-start' },
  hospitalName: { fontSize: 15, fontWeight: '700', color: Colors.textPrimary },
  statusLine: { flexDirection: 'row', alignItems: 'center', marginVertical: 4 },
  dot: { width: 8, height: 8, borderRadius: 4, marginRight: 6 },
  availableLabel: { fontSize: 12, fontWeight: '500' },
  addressText: { fontSize: 12, color: Colors.textSecondary },
  distanceText: { fontSize: 12, color: Colors.textSecondary, fontWeight: '600' },
  cardBottom: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginTop: 12 },
  tagRow: { flexDirection: 'row' },
  tag: { backgroundColor: Colors.primaryRedLight, borderRadius: 6, paddingHorizontal: 8, paddingVertical: 3, marginRight: 6 },
  tagText: { fontSize: 11, color: Colors.primaryRed, fontWeight: '600' },
  bookBtn: { backgroundColor: Colors.primaryRed, paddingHorizontal: 14, paddingVertical: 8, borderRadius: 10 },
  bookBtnDisabled: { backgroundColor: '#F1F5F9' },
  bookBtnText: { color: '#FFFFFF', fontSize: 12, fontWeight: '700' },
  bookBtnTextDisabled: { color: '#94A3B8' },
  footerRadius: { textAlign: 'center', fontSize: 12, color: Colors.textSecondary, marginVertical: 14 },
  bottomBar: { flexDirection: 'row', backgroundColor: '#FFFFFF', padding: 16, elevation: 6 },
  emergencyCallBtn: { flex: 1, backgroundColor: Colors.primaryRed, height: 48, borderRadius: 12, justifyContent: 'center', alignItems: 'center', marginRight: 8 },
  emergencyCallText: { color: '#FFFFFF', fontWeight: '700', fontSize: 13 },
  bookHospitalBtn: { flex: 1, backgroundColor: Colors.primaryBlue, height: 48, borderRadius: 12, justifyContent: 'center', alignItems: 'center', marginLeft: 8 },
  bookHospitalText: { color: '#FFFFFF', fontWeight: '700', fontSize: 13 }
});
