import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';

import { OnlineBookingScreen } from '../screens/OnlineBookingScreen';
import { OfflineEmergencyScreen } from '../screens/OfflineEmergencyScreen';
import { MedicLoginScreen } from '../screens/MedicLoginScreen';
import { SignUpScreen } from '../screens/SignUpScreen';
import { HomeScreen } from '../screens/HomeScreen';
import { NearbyHospitalsScreen } from '../screens/NearbyHospitalsScreen';
import { RequestAmbulanceScreen } from '../screens/RequestAmbulanceScreen';
import { BookAmbulanceScreen } from '../screens/BookAmbulanceScreen';
import { ActiveTrackingScreen } from '../screens/ActiveTrackingScreen';
import { DriverOrderScreen } from '../screens/DriverOrderScreen';
import { TripCompletedScreen } from '../screens/TripCompletedScreen';
import { EmergencyNumbersScreen } from '../screens/EmergencyNumbersScreen';
import { BookForOthersScreen } from '../screens/BookForOthersScreen';
import { RideHistoryScreen } from '../screens/RideHistoryScreen';

export type RootStackParamList = {
  OnlineBooking: undefined;
  OfflineEmergency: undefined;
  Login: undefined;
  SignUp: undefined;
  Home: undefined;
  NearbyHospitals: undefined;
  RequestAmbulance: { type?: 'BASIC' | 'ICU' | 'VENTILATOR' } | undefined;
  BookAmbulance: { booking?: any; hospital?: any };
  ActiveTracking: { booking?: any; driver?: any; hospital?: any } | undefined;
  DriverOrder: undefined;
  TripCompleted: undefined;
  EmergencyNumbers: undefined;
  BookForOthers: undefined;
  RideHistory: undefined;
};

const Stack = createStackNavigator<RootStackParamList>();

export const AppNavigator: React.FC = () => {
  return (
    <NavigationContainer>
      <Stack.Navigator
        initialRouteName="OnlineBooking"
        screenOptions={{
          headerShown: false,
          cardStyle: { backgroundColor: '#F8FAFC' },
        }}
      >
        <Stack.Screen name="OnlineBooking" component={OnlineBookingScreen} />
        <Stack.Screen name="OfflineEmergency" component={OfflineEmergencyScreen} />
        <Stack.Screen name="Login" component={MedicLoginScreen} />
        <Stack.Screen name="SignUp" component={SignUpScreen} />
        <Stack.Screen name="Home" component={HomeScreen} />
        <Stack.Screen name="NearbyHospitals" component={NearbyHospitalsScreen} />
        <Stack.Screen name="RequestAmbulance" component={RequestAmbulanceScreen} />
        <Stack.Screen name="BookAmbulance" component={BookAmbulanceScreen} />
        <Stack.Screen name="ActiveTracking" component={ActiveTrackingScreen} />
        <Stack.Screen name="DriverOrder" component={DriverOrderScreen} />
        <Stack.Screen name="TripCompleted" component={TripCompletedScreen} />
        <Stack.Screen name="EmergencyNumbers" component={EmergencyNumbersScreen} />
        <Stack.Screen name="BookForOthers" component={BookForOthersScreen} />
        <Stack.Screen name="RideHistory" component={RideHistoryScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
};
