/**
 * Med-Ambulance React Native API Service
 * 
 * Clean, well-commented placeholder functions.
 * Replace simulated responses with your Axios/fetch calls to connect your live backend.
 */

export interface DriverInfo {
  id: string;
  name: string;
  phone: string;
  vehicleModel: string;
  vehicleNumber: string;
  hospitalAffiliation: string;
  rating: number;
  etaMinutes: number;
  distanceKm: number;
}

export interface HospitalInfo {
  id: string;
  name: string;
  address: string;
  phone: string;
  distanceKm: number;
  distanceText: string;
  availableAmbulances: number;
  totalAmbulances: number;
  tags: string[];
}

export interface BookingRequestPayload {
  pickupLocation: string;
  dropLocation: string;
  ambulanceType: 'BASIC' | 'ICU' | 'VENTILATOR' | 'DEAD_BODY_VAN';
  isEmergency: boolean;
  notes?: string;
  patientName?: string;
  patientPhone?: string;
}

const API_BASE_URL = 'https://api.med-ambulance.gov.np/api/v1';

export const ambulanceApi = {
  /**
   * 1. Authentication
   * Endpoint: POST /api/v1/auth/login
   */
  handleLogin: async (phoneNumber: string) => {
    // Placeholder for: await axios.post(`${API_BASE_URL}/auth/login`, { phoneNumber });
    console.log('[API] Sending OTP to phone:', phoneNumber);
    return {
      success: true,
      message: `OTP sent to ${phoneNumber}`,
      demoOtp: '1234'
    };
  },

  /**
   * Verify OTP
   * Endpoint: POST /api/v1/auth/verify-otp
   */
  verifyOtp: async (phoneNumber: string, otp: string) => {
    // Placeholder for: await axios.post(`${API_BASE_URL}/auth/verify-otp`, { phoneNumber, otp });
    console.log('[API] Verifying OTP:', otp, 'for', phoneNumber);
    return {
      success: true,
      token: 'jwt_auth_token_sample',
      user: {
        id: 'usr_123',
        phone: phoneNumber,
        name: 'Ramhari Khadka',
        bloodGroup: 'O+'
      }
    };
  },

  /**
   * Sign-Up / Register Account
   * Endpoint: POST /api/v1/auth/signup
   */
  handleSignUp: async (userData: {
    fullName: string;
    phoneNumber: string;
    bloodGroup: string;
    emergencyContactName: string;
    emergencyContactPhone: string;
  }) => {
    // Placeholder for: await axios.post(`${API_BASE_URL}/auth/signup`, userData);
    console.log('[API] Registering new user:', userData);
    return {
      success: true,
      user: userData
    };
  },

  /**
   * 2. Request & Dispatch Ambulance
   * Endpoint: POST /api/v1/ambulance/request
   */
  requestAmbulance: async (payload: BookingRequestPayload) => {
    // Placeholder for: await axios.post(`${API_BASE_URL}/ambulance/request`, payload);
    console.log('[API] Dispatching ambulance request:', payload);
    return {
      bookingId: '#MED' + Math.floor(10000 + Math.random() * 90000),
      status: 'ASSIGNED',
      driver: {
        id: 'drv_001',
        name: 'Bharat Khatri Chhetri',
        phone: '+977 9765228814',
        vehicleModel: 'Nepal Sprinter Ambulance',
        vehicleNumber: 'AMB 2234',
        hospitalAffiliation: 'Bhaktapur Hospital',
        rating: 4.95,
        etaMinutes: 8,
        distanceKm: 2.3
      },
      hospital: {
        id: 'hosp_5',
        name: 'Bhaktapur Hospital',
        address: '44600 Radheradhe, Bhaktapur',
        phone: '+977 9765228814'
      },
      fareBreakdown: {
        baseFare: 50.0,
        distanceCharge: 25.0,
        emergencyEquipment: 75.0,
        totalAmount: 150.0,
        currency: 'Rs'
      }
    };
  },

  /**
   * 3. Fetch Nearby Hospitals with Ambulance Availability
   * Endpoint: GET /api/v1/hospitals/nearby?lat=...&lng=...
   */
  getNearbyHospitals: async (latitude?: number, longitude?: number): Promise<HospitalInfo[]> => {
    // Placeholder for: await axios.get(`${API_BASE_URL}/hospitals/nearby`, { params: { latitude, longitude } });
    return [
      {
        id: '1',
        name: 'Kathmandu Medical Center',
        address: 'Bhaisepati, Kathmandu',
        phone: '+977 1-4476152',
        distanceKm: 1.2,
        distanceText: '1.2 km away',
        availableAmbulances: 1,
        totalAmbulances: 2,
        tags: ['24/7 Open', 'Emergency Room']
      },
      {
        id: '2',
        name: 'Nepal Medical College Hospital',
        address: 'Tripureshwor, Kathmandu',
        phone: '+977 1-4911000',
        distanceKm: 2.5,
        distanceText: '2.5 km away',
        availableAmbulances: 0,
        totalAmbulances: 2,
        tags: ['24/7 Open', 'Emergency Room']
      },
      {
        id: '3',
        name: 'National Hospital',
        address: 'Teku, Kathmandu',
        phone: '+977 1-4240805',
        distanceKm: 3.1,
        distanceText: '3.1 km away',
        availableAmbulances: 2,
        totalAmbulances: 2,
        tags: ['24/7 Open', 'Emergency Room']
      },
      {
        id: '4',
        name: 'Kathmandu Model Hospital',
        address: 'Kalimati, Kathmandu',
        phone: '+977 1-4240805',
        distanceKm: 3.8,
        distanceText: '3.8 km away',
        availableAmbulances: 1,
        totalAmbulances: 2,
        tags: ['24/7 Open']
      },
      {
        id: '5',
        name: 'Bhaktapur Hospital',
        address: '44600 Radheradhe, Bhaktapur',
        phone: '+977 9765228814',
        distanceKm: 4.2,
        distanceText: '4.2 km away',
        availableAmbulances: 0,
        totalAmbulances: 2,
        tags: ['Emergency Room']
      }
    ];
  },

  /**
   * 4. Driver Accept Order
   * Endpoint: POST /api/v1/driver/orders/:id/accept
   */
  acceptDriverOrder: async (orderId: string) => {
    // Placeholder for: await axios.post(`${API_BASE_URL}/driver/orders/${orderId}/accept`);
    console.log('[API] Driver accepted order:', orderId);
    return { success: true, status: 'IN_TRANSIT' };
  },

  /**
   * 5. Complete Delivery & Submit Review
   * Endpoint: POST /api/v1/delivery/:id/complete
   */
  completeDelivery: async (deliveryId: string, rating: number, feedback: string) => {
    // Placeholder for: await axios.post(`${API_BASE_URL}/delivery/${deliveryId}/complete`, { rating, feedback });
    console.log('[API] Trip completed:', deliveryId, rating, feedback);
    return { success: true, message: 'Delivery recorded successfully' };
  }
};
