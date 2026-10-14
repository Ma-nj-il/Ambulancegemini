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

export const BookForOthersScreen = ({ navigation }: any) => {
  const [name, setName] = useState('');
  const [phone, setPhone] = useState('');
  const [pickup, setPickup] = useState('Baneshwor, Kathmandu');
  const [drop, setDrop] = useState('Bir Hospital, Kathmandu');
  const [note, setNote] = useState('');

  const handleBook = () => {
    if (!name || !phone) {
      Alert.alert('Required', 'Please enter passenger name and phone number');
      return;
    }
    navigation.navigate('ActiveTracking');
  };

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.appBar}>
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Text style={styles.backBtn}>←</Text>
        </TouchableOpacity>
        <Text style={styles.title}>Book for Others</Text>
      </View>

      <ScrollView contentContainerStyle={styles.scroll}>
        <Text style={styles.sectionHeader}>Passenger Details</Text>
        <TextInput
          style={styles.input}
          placeholder="👤 Full Name"
          value={name}
          onChangeText={setName}
        />
        <TextInput
          style={styles.input}
          placeholder="📞 Phone Number"
          value={phone}
          onChangeText={setPhone}
          keyboardType="phone-pad"
        />

        <Text style={[styles.sectionHeader, { marginTop: 16 }]}>Location Details</Text>
        <View style={styles.inputWithDot}>
          <View style={styles.greenDot} />
          <TextInput
            style={styles.inputInner}
            placeholder="Pickup Location"
            value={pickup}
            onChangeText={setPickup}
          />
        </View>

        <View style={styles.inputWithDot}>
          <View style={styles.redDot} />
          <TextInput
            style={styles.inputInner}
            placeholder="Drop Location"
            value={drop}
            onChangeText={setDrop}
          />
        </View>

        <Text style={[styles.sectionHeader, { marginTop: 16 }]}>Additional Details</Text>
        <TextInput
          style={[styles.input, { height: 70, textAlignVertical: 'top' }]}
          placeholder="Add note for driver (optional)"
          value={note}
          onChangeText={setNote}
          multiline
        />

        {/* Fare Estimate Card (Screenshot Page 11) */}
        <View style={styles.estimateCard}>
          <Text style={styles.estimateTitle}>Fare Estimate</Text>
          <View style={styles.estimateRow}>
            <Text style={styles.estimateSub}>Estimated fare</Text>
            <Text style={styles.estimateAmount}>Rs 30-50</Text>
          </View>
          <Text style={styles.estimateDisclaimer}>Final fare may vary based on traffic and distance</Text>
        </View>

        {/* Blue CTA: Book Ride (Screenshot Page 11) */}
        <TouchableOpacity style={styles.bookBtn} onPress={handleBook}>
          <Text style={styles.bookBtnText}>Book Ride</Text>
        </TouchableOpacity>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#FFFFFF' },
  appBar: { flexDirection: 'row', alignItems: 'center', paddingHorizontal: 16, paddingVertical: 14, borderBottomWidth: 1, borderColor: '#F1F5F9' },
  backBtn: { fontSize: 24, fontWeight: '700', color: Colors.textPrimary, marginRight: 12 },
  title: { fontSize: 18, fontWeight: '700', color: Colors.textPrimary },
  scroll: { padding: 20 },
  sectionHeader: { fontSize: 14, fontWeight: '700', color: Colors.textPrimary, marginBottom: 10 },
  input: { backgroundColor: '#F8FAFC', borderRadius: 12, height: 48, paddingHorizontal: 14, fontSize: 14, marginBottom: 12 },
  inputWithDot: { flexDirection: 'row', alignItems: 'center', backgroundColor: '#F8FAFC', borderRadius: 12, height: 48, paddingHorizontal: 14, marginBottom: 12 },
  greenDot: { width: 8, height: 8, borderRadius: 4, backgroundColor: Colors.greenAvailable, marginRight: 10 },
  redDot: { width: 8, height: 8, borderRadius: 4, backgroundColor: Colors.primaryRed, marginRight: 10 },
  inputInner: { flex: 1, fontSize: 14, color: Colors.textPrimary },
  estimateCard: { backgroundColor: '#F8FAFC', borderRadius: 14, padding: 16, marginVertical: 18 },
  estimateTitle: { fontSize: 13, fontWeight: '600', color: Colors.textPrimary },
  estimateRow: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginVertical: 4 },
  estimateSub: { fontSize: 13, color: Colors.textSecondary },
  estimateAmount: { fontSize: 17, fontWeight: '700', color: Colors.textPrimary },
  estimateDisclaimer: { fontSize: 11, color: Colors.textSecondary, marginTop: 2 },
  bookBtn: { backgroundColor: Colors.primaryBlue, height: 52, borderRadius: 14, justifyContent: 'center', alignItems: 'center', marginTop: 10 },
  bookBtnText: { color: '#FFFFFF', fontSize: 16, fontWeight: '700' }
});
