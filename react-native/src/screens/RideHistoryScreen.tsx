import React, { useState } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  SafeAreaView,
  FlatList
} from 'react-native';
import { Colors } from '../theme/colors';

const SAMPLE_RIDES = [
  {
    id: '1',
    date: 'Today, 2:30 PM',
    status: 'Completed',
    pickup: 'putalisadak, Kathmandu',
    drop: 'Bir Hospital',
    driver: 'Ramhari Bhandari',
    rating: 4.8,
    fare: 'Rs 30',
    type: 'Standard'
  },
  {
    id: '2',
    date: 'Yesterday, 9:15 AM',
    status: 'Completed',
    pickup: 'Kalanki, Kathmandu',
    drop: 'Tu Teaching Hospital',
    driver: 'Ramesh Khadka',
    rating: 4.9,
    fare: 'Rs 18.75',
    type: 'Standard'
  },
  {
    id: '3',
    date: 'Jan 15, 4:45 PM',
    status: 'Completed',
    pickup: 'Airport Terminal TIA',
    drop: 'Manmohan Hospital',
    driver: 'Nishant Kc',
    rating: 4.7,
    fare: 'Rs 45.00',
    type: 'Premium'
  },
  {
    id: '4',
    date: 'Jan 14, 11:20 AM',
    status: 'Cancelled',
    pickup: 'Central Park, Kathmandu',
    drop: 'Bhaktapur Hospital',
    driver: 'Neelam Bhandari',
    rating: 4.6,
    fare: 'Rs 50',
    type: 'Standard'
  }
];

export const RideHistoryScreen = ({ navigation }: any) => {
  const [filter, setFilter] = useState('All');
  const [query, setQuery] = useState('');

  const filtered = SAMPLE_RIDES.filter((r) => {
    return (
      (filter === 'All' || r.date.toLowerCase().includes(filter.toLowerCase())) &&
      (query.trim() === '' ||
        r.drop.toLowerCase().includes(query.toLowerCase()) ||
        r.pickup.toLowerCase().includes(query.toLowerCase()))
    );
  });

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.appBar}>
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Text style={styles.backBtn}>←</Text>
        </TouchableOpacity>
        <Text style={styles.title}>Ride History</Text>
        <Text style={styles.menuBtn}>☰</Text>
      </View>

      {/* Search Input (Screenshot Page 13) */}
      <View style={styles.searchBox}>
        <Text style={styles.searchIcon}>🔍</Text>
        <TextInput
          style={styles.searchInput}
          placeholder="Search rides by destination"
          value={query}
          onChangeText={setQuery}
        />
      </View>

      {/* Filter Chips (Screenshot Page 13) */}
      <View style={styles.filterRow}>
        {['All', 'This Month', 'Last Month', 'This Year'].map((f) => {
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

      {/* Rides List (Screenshot Page 13) */}
      <FlatList
        data={filtered}
        keyExtractor={(item) => item.id}
        contentContainerStyle={{ padding: 16 }}
        renderItem={({ item }) => {
          const isDone = item.status === 'Completed';
          return (
            <View style={styles.card}>
              <View style={styles.cardHeader}>
                <Text style={styles.dateText}>🕒 {item.date}</Text>
                <Text style={[styles.statusText, { color: isDone ? Colors.greenAvailable : Colors.primaryRed }]}>
                  {item.status}
                </Text>
              </View>

              <View style={styles.locRow}>
                <Text style={styles.locIconBlue}>📍</Text>
                <View>
                  <Text style={styles.locSub}>Pickup</Text>
                  <Text style={styles.locTitle}>{item.pickup}</Text>
                </View>
              </View>

              <View style={styles.locRow}>
                <Text style={styles.locIconRed}>📍</Text>
                <View>
                  <Text style={styles.locSub}>Drop-off</Text>
                  <Text style={styles.locTitle}>{item.drop}</Text>
                </View>
              </View>

              <View style={styles.divider} />

              <View style={styles.cardFooter}>
                <View style={styles.driverCol}>
                  <Text style={styles.driverName}>🚙 {item.driver}</Text>
                  <Text style={styles.ratingText}>⭐ {item.rating}</Text>
                </View>

                <View style={styles.fareCol}>
                  <Text style={styles.fareAmount}>{item.fare}</Text>
                  <Text style={styles.fareType}>{item.type}</Text>
                </View>
              </View>
            </View>
          );
        }}
      />
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: Colors.background },
  appBar: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#FFFFFF', paddingHorizontal: 16, paddingVertical: 14, elevation: 2 },
  backBtn: { fontSize: 24, fontWeight: '700', color: Colors.textPrimary },
  title: { fontSize: 18, fontWeight: '700', color: Colors.textPrimary },
  menuBtn: { fontSize: 20, color: Colors.textSecondary },
  searchBox: { flexDirection: 'row', alignItems: 'center', backgroundColor: '#FFFFFF', marginHorizontal: 16, marginTop: 14, paddingHorizontal: 14, height: 48, borderRadius: 12, borderWidth: 1, borderColor: Colors.border },
  searchIcon: { fontSize: 16, marginRight: 8 },
  searchInput: { flex: 1, fontSize: 14, color: Colors.textPrimary },
  filterRow: { flexDirection: 'row', paddingHorizontal: 16, paddingVertical: 12 },
  filterChip: { backgroundColor: '#FFFFFF', borderWidth: 1, borderColor: Colors.border, borderRadius: 20, paddingHorizontal: 14, paddingVertical: 6, marginRight: 8 },
  filterChipActive: { backgroundColor: Colors.primaryBlue, borderColor: Colors.primaryBlue },
  filterText: { fontSize: 12, color: Colors.textSecondary },
  filterTextActive: { color: '#FFFFFF', fontWeight: '700' },
  card: { backgroundColor: '#FFFFFF', borderRadius: 16, padding: 16, marginBottom: 12, elevation: 1 },
  cardHeader: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 12 },
  dateText: { fontSize: 12, color: Colors.textSecondary },
  statusText: { fontSize: 12, fontWeight: '700' },
  locRow: { flexDirection: 'row', alignItems: 'flex-start', marginVertical: 4 },
  locIconBlue: { fontSize: 16, marginRight: 8 },
  locIconRed: { fontSize: 16, marginRight: 8 },
  locSub: { fontSize: 11, color: Colors.textSecondary },
  locTitle: { fontSize: 13, fontWeight: '700', color: Colors.textPrimary },
  divider: { height: 1, backgroundColor: '#F1F5F9', marginVertical: 10 },
  cardFooter: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' },
  driverCol: { flexDirection: 'column' },
  driverName: { fontSize: 13, fontWeight: '700', color: Colors.textPrimary },
  ratingText: { fontSize: 11, color: Colors.textSecondary, marginTop: 2 },
  fareCol: { alignItems: 'flex-end' },
  fareAmount: { fontSize: 15, fontWeight: '700', color: Colors.textPrimary },
  fareType: { fontSize: 11, color: Colors.textSecondary }
});
