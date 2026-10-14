import React, { useState } from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  SafeAreaView,
  ScrollView,
  TextInput,
  Alert
} from 'react-native';
import { Colors } from '../theme/colors';
import { ambulanceApi } from '../api/ambulanceApi';

export const TripCompletedScreen = ({ navigation }: any) => {
  const [rating, setRating] = useState(5);
  const [feedback, setFeedback] = useState('');

  const handleFinish = async () => {
    await ambulanceApi.completeDelivery('#MED82947', rating, feedback);
    navigation.navigate('Home');
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.scroll}>
        {/* Green Checkmark Circle (Screenshot Page 7) */}
        <View style={styles.checkCircle}>
          <Text style={styles.checkIcon}>✓</Text>
        </View>

        <Text style={styles.title}>Delivery Successfully Completed</Text>
        <Text style={styles.subtitle}>Delivery ID: #MED82947{'\n'}2:45 PM, Today</Text>

        {/* Delivery Details Card (Screenshot Page 7) */}
        <View style={styles.card}>
          <Text style={styles.cardTitle}>Delivery Details</Text>

          <View style={styles.detailRow}>
            <Text style={styles.icon}>🏥</Text>
            <View>
              <Text style={styles.mainDetail}>Bhaktapur Hospital</Text>
              <Text style={styles.subDetail}>Emergency Department</Text>
            </View>
          </View>

          <View style={styles.detailRow}>
            <Text style={styles.icon}>📍</Text>
            <View>
              <Text style={styles.label}>Location</Text>
              <Text style={styles.mainDetail}>RadheRadhe Bhaktapur</Text>
            </View>
          </View>

          <View style={styles.detailRow}>
            <Text style={styles.icon}>👨‍⚕️</Text>
            <View>
              <Text style={styles.label}>Recipient</Text>
              <Text style={styles.mainDetail}>Dr. Priyanka Kc</Text>
            </View>
          </View>

          <View style={styles.detailRow}>
            <Text style={styles.icon}>📄</Text>
            <View>
              <Text style={styles.label}>Patient Contact</Text>
              <Text style={styles.mainDetail}>9800000000</Text>
            </View>
          </View>
        </View>

        {/* Delivery Summary Card (Screenshot Page 7) */}
        <View style={styles.card}>
          <Text style={styles.cardTitle}>Delivery Summary</Text>
          <View style={styles.summaryGrid}>
            <View>
              <Text style={styles.label}>Start Time</Text>
              <Text style={styles.boldText}>2:15 PM</Text>
            </View>
            <View>
              <Text style={styles.label}>End Time</Text>
              <Text style={styles.boldText}>2:45 PM</Text>
            </View>
            <View>
              <Text style={styles.label}>Distance</Text>
              <Text style={styles.boldText}>3.2 miles</Text>
            </View>
            <View>
              <Text style={styles.label}>Items</Text>
              <Text style={styles.boldText}>1 Patient</Text>
            </View>
          </View>
        </View>

        {/* Rating Card (Screenshot Page 7) */}
        <View style={styles.card}>
          <Text style={[styles.cardTitle, { textAlign: 'center' }]}>How was your delivery experience?</Text>
          <View style={styles.starRow}>
            {[1, 2, 3, 4, 5].map((star) => (
              <TouchableOpacity key={star} onPress={() => setRating(star)}>
                <Text style={[styles.starIcon, star <= rating ? styles.starActive : styles.starInactive]}>
                  ★
                </Text>
              </TouchableOpacity>
            ))}
          </View>

          <TextInput
            style={styles.feedbackInput}
            value={feedback}
            onChangeText={setFeedback}
            placeholder="Add optional feedback..."
          />
        </View>

        {/* Blue CTA: Complete & Return (Screenshot Page 7) */}
        <TouchableOpacity style={styles.completeBtn} onPress={handleFinish}>
          <Text style={styles.completeBtnText}>Complete & Return</Text>
        </TouchableOpacity>

        {/* Outline Button: View Delivery Report */}
        <TouchableOpacity
          style={styles.reportBtn}
          onPress={() => navigation.navigate('RideHistory')}
        >
          <Text style={styles.reportBtnText}>View Delivery Report</Text>
        </TouchableOpacity>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: Colors.background },
  scroll: { padding: 20, alignItems: 'center' },
  checkCircle: { width: 68, height: 68, borderRadius: 34, backgroundColor: Colors.greenAvailable, justifyContent: 'center', alignItems: 'center', marginVertical: 14 },
  checkIcon: { fontSize: 36, color: '#FFFFFF', fontWeight: 'bold' },
  title: { fontSize: 20, fontWeight: '700', color: Colors.textPrimary, textAlign: 'center' },
  subtitle: { fontSize: 13, color: Colors.textSecondary, textAlign: 'center', marginTop: 4, marginBottom: 20, lineHeight: 18 },
  card: { width: '100%', backgroundColor: '#FFFFFF', borderRadius: 16, padding: 18, elevation: 1, marginBottom: 14 },
  cardTitle: { fontSize: 15, fontWeight: '700', color: Colors.textPrimary, marginBottom: 12 },
  detailRow: { flexDirection: 'row', alignItems: 'center', marginVertical: 6 },
  icon: { fontSize: 18, marginRight: 12 },
  label: { fontSize: 11, color: Colors.textSecondary },
  mainDetail: { fontSize: 13, fontWeight: '600', color: Colors.textPrimary },
  subDetail: { fontSize: 12, color: Colors.textSecondary },
  summaryGrid: { flexDirection: 'row', justifyContent: 'space-between', marginTop: 4 },
  boldText: { fontSize: 14, fontWeight: '700', color: Colors.textPrimary, marginTop: 2 },
  starRow: { flexDirection: 'row', justifyContent: 'center', marginVertical: 12 },
  starIcon: { fontSize: 32, marginHorizontal: 4 },
  starActive: { color: Colors.warningAmber },
  starInactive: { color: '#CBD5E1' },
  feedbackInput: { backgroundColor: '#F8FAFC', borderWidth: 1, borderColor: Colors.border, borderRadius: 12, height: 46, paddingHorizontal: 14, marginTop: 8 },
  completeBtn: { width: '100%', height: 52, backgroundColor: Colors.primaryBlue, borderRadius: 14, justifyContent: 'center', alignItems: 'center', marginTop: 12 },
  completeBtnText: { color: '#FFFFFF', fontSize: 16, fontWeight: '700' },
  reportBtn: { width: '100%', height: 48, borderWidth: 1, borderColor: Colors.primaryBlue, borderRadius: 14, justifyContent: 'center', alignItems: 'center', marginTop: 10, marginBottom: 20 },
  reportBtnText: { color: Colors.primaryBlue, fontSize: 14, fontWeight: '700' }
});
