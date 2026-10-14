# Med-Ambulance Mobile App Frontend (React Native)

Pixel-perfect React Native frontend implementation for the **Med-Ambulance** on-demand emergency ambulance booking platform across Nepal, matching the Figma wireframes and screenshots.

## 📁 Directory Structure
```
react-native/
├── App.tsx                     # Root App component with SafeAreaProvider
├── package.json                # Project dependencies
├── src/
│   ├── api/
│   │   └── ambulanceApi.ts     # Well-commented API placeholder functions
│   ├── navigation/
│   │   └── AppNavigator.tsx    # Stack Navigation routing
│   ├── screens/
│   │   ├── LoginScreen.tsx             # Phone OTP login, Guest Emergency mode, Language switch
│   │   ├── SignUpScreen.tsx            # Patient registration with blood group & emergency contacts
│   │   ├── HomeScreen.tsx              # Bilingual Nepali/English, Hero emergency CTA, 2x2 grid, services, recent rides
│   │   ├── NearbyHospitalsScreen.tsx   # Distance, ambulance availability, 24/7 filter chips
│   │   ├── RequestAmbulanceScreen.tsx  # Map route polyline, pickup/drop card, Basic/Advanced/Ventilator types
│   │   ├── BookAmbulanceScreen.tsx     # Driver & hospital info, fare breakdown, emergency checklist
│   │   ├── DriverOrderScreen.tsx       # Incoming order modal, countdown timer, patient notes, Accept/Decline
│   │   ├── ActiveTrackingScreen.tsx    # Live navigation banner, patient status, ETA, emergency call
│   │   ├── TripCompletedScreen.tsx     # Completed receipt, 5-star rating, delivery report
│   │   ├── EmergencyNumbersScreen.tsx  # 102 Nepal Ambulance, 911 Universal, Police, Fire, Poison
│   │   ├── BookForOthersScreen.tsx     # Book for family/friends, fare estimation
│   │   └── RideHistoryScreen.tsx       # Search by destination, filters, trip receipts
│   └── theme/
│       └── colors.ts           # Med-Ambulance design color tokens
```

## 🚀 Running the React Native Project
```bash
cd react-native
npm install
npm start
```

## 🔌 API Integration Points
All endpoints are cleanly centralized in `src/api/ambulanceApi.ts`:
- `handleLogin(phone)` -> `POST /api/v1/auth/login`
- `verifyOtp(phone, otp)` -> `POST /api/v1/auth/verify-otp`
- `handleSignUp(data)` -> `POST /api/v1/auth/signup`
- `requestAmbulance(payload)` -> `POST /api/v1/ambulance/request`
- `getNearbyHospitals(lat, lng)` -> `GET /api/v1/hospitals/nearby`
- `acceptDriverOrder(orderId)` -> `POST /api/v1/driver/orders/:id/accept`
- `completeDelivery(id, rating, feedback)` -> `POST /api/v1/delivery/:id/complete`
