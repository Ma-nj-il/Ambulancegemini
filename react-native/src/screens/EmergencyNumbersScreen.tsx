import React from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  SafeAreaView,
  ScrollView,
  Linking
} from 'react-native';
import { Colors } from '../theme/colors';

export const EmergencyNumbersScreen = ({ navigation }: any) => {
  const dial = (num: string) => {
    Linking.openURL(`tel:${num}`);
  };

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.appBar}>
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Text style={styles.backBtn}>←</Text>
        </TouchableOpacity>
        <View style={{ marginLeft: 8 }}>
          <Text style={styles.title}>Emergency Numbers</Text>
          <Text style={styles.sub}>Quick access to emergency services</Text>
        </View>
      </View>

      <ScrollView contentContainerStyle={styles.scroll}>
        {/* Hero 911 / 102 Card (Screenshot Page 10) */}
        <View style={styles.heroCard}>
          <View>
            <Text style={styles.heroNumber}>102 / 911</Text>
            <Text style={styles.heroLabel}>Universal Emergency & Nepal Ambulance</Text>
          </View>
          <TouchableOpacity style={styles.callCircle} onPress={() => dial('102')}>
            <Text style={styles.callIcon}>📞</Text>
          </TouchableOpacity>
        </View>

        {/* Medical Emergency (Screenshot Page 10) */}
        <View style={styles.card}>
          <Text style={styles.categoryTitle}>🏥 Medical Emergency</Text>
          {[
            { title: 'Nepal Ambulance Dispatch', num: '102' },
            { title: 'Emergency Room', num: '102 / 911' },
            { title: 'Red Cross Ambulance Kathmandu', num: '01-4288488' },
            { title: 'Poison Control', num: '1-800-222-1222' },
            { title: 'Mental Health Crisis Hotline', num: '988 / 1166' },
          ].map((item, idx) => (
            <TouchableOpacity key={idx} style={styles.numberRow} onPress={() => dial(item.num)}>
              <View>
                <Text style={styles.itemTitle}>{item.title}</Text>
                <Text style={styles.itemNum}>{item.num}</Text>
              </View>
              <Text style={styles.chevron}>›</Text>
            </TouchableOpacity>
          ))}
        </View>

        {/* Law Enforcement (Screenshot Page 10) */}
        <View style={styles.card}>
          <Text style={styles.categoryTitle}>👮 Law Enforcement</Text>
          {[
            { title: 'Nepal Police', num: '100' },
            { title: 'Traffic Police Helpline', num: '103' },
            { title: 'Tourist Police', num: '1144' },
          ].map((item, idx) => (
            <TouchableOpacity key={idx} style={styles.numberRow} onPress={() => dial(item.num)}>
              <View>
                <Text style={styles.itemTitle}>{item.title}</Text>
                <Text style={styles.itemNumBlue}>{item.num}</Text>
              </View>
              <Text style={styles.chevron}>›</Text>
            </TouchableOpacity>
          ))}
        </View>

        {/* Fire & Rescue (Screenshot Page 10) */}
        <View style={styles.card}>
          <Text style={styles.categoryTitle}>🚒 Fire & Rescue</Text>
          {[
            { title: 'Fire Department (Damkal)', num: '101' },
            { title: 'Armed Police Force Rescue', num: '1114' },
          ].map((item, idx) => (
            <TouchableOpacity key={idx} style={styles.numberRow} onPress={() => dial(item.num)}>
              <View>
                <Text style={styles.itemTitle}>{item.title}</Text>
                <Text style={styles.itemNumOrange}>{item.num}</Text>
              </View>
              <Text style={styles.chevron}>›</Text>
            </TouchableOpacity>
          ))}
        </View>

        {/* Yellow Warning Card (Screenshot Page 10) */}
        <View style={styles.warningCard}>
          <Text style={styles.warningIcon}>⚠️</Text>
          <Text style={styles.warningText}>
            Always call 102 or 100 for immediate life-threatening emergencies. Do not wait to see if the situation improves.
          </Text>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: Colors.background },
  appBar: { flexDirection: 'row', alignItems: 'center', backgroundColor: '#FFFFFF', paddingHorizontal: 16, paddingVertical: 14, elevation: 2 },
  backBtn: { fontSize: 24, fontWeight: '700', color: Colors.textPrimary },
  title: { fontSize: 18, fontWeight: '700', color: Colors.textPrimary },
  sub: { fontSize: 12, color: Colors.textSecondary },
  scroll: { padding: 16 },
  heroCard: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#FFFFFF', borderRadius: 16, padding: 20, marginBottom: 14, elevation: 1 },
  heroNumber: { fontSize: 32, fontWeight: '800', color: Colors.primaryRed },
  heroLabel: { fontSize: 13, color: Colors.textSecondary, marginTop: 2 },
  callCircle: { width: 52, height: 52, borderRadius: 26, backgroundColor: Colors.primaryRed, justifyContent: 'center', alignItems: 'center' },
  callIcon: { fontSize: 22, color: '#FFFFFF' },
  card: { backgroundColor: '#FFFFFF', borderRadius: 16, padding: 16, marginBottom: 14, elevation: 1 },
  categoryTitle: { fontSize: 15, fontWeight: '700', color: Colors.textPrimary, marginBottom: 10 },
  numberRow: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', paddingVertical: 8, borderBottomWidth: 1, borderColor: '#F1F5F9' },
  itemTitle: { fontSize: 13, fontWeight: '500', color: Colors.textPrimary },
  itemNum: { fontSize: 13, fontWeight: '700', color: Colors.primaryRed, marginTop: 2 },
  itemNumBlue: { fontSize: 13, fontWeight: '700', color: Colors.primaryBlue, marginTop: 2 },
  itemNumOrange: { fontSize: 13, fontWeight: '700', color: '#EA580C', marginTop: 2 },
  chevron: { fontSize: 20, color: '#94A3B8' },
  warningCard: { flexDirection: 'row', backgroundColor: Colors.warningLight, padding: 14, borderRadius: 14, marginTop: 4, marginBottom: 20 },
  warningIcon: { fontSize: 18, marginRight: 8 },
  warningText: { flex: 1, fontSize: 12, color: '#92400E', lineHeight: 16 }
});
