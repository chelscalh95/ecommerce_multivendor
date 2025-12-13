// Auto-login utility for development testing
import { api } from '../Config/Api'

export interface LoginRequest {
  email: string;
  password: string;
  otp: string;
}

export interface AuthResponse {
  jwt: string;
  message: string;
  role: string;
}

// Test user credentials (using the sample data we created)
const TEST_USER = {
  email: 'john.doe@example.com',
  password: 'password123', // This should match the actual password for the sample user
  otp: '123456' // Default OTP for testing
};

export const autoLoginTestUser = async (): Promise<boolean> => {
  try {
    // Check if already logged in
    const existingToken = localStorage.getItem('jwt');
    if (existingToken && existingToken !== 'null' && existingToken !== 'undefined') {
      return true;
    }

    // Attempt to login with test user
    const loginRequest: LoginRequest = {
      email: TEST_USER.email,
      password: TEST_USER.password,
      otp: TEST_USER.otp
    };

    const response = await api.post<AuthResponse>('/auth/signin', loginRequest);
    
    if (response.data.jwt) {
      localStorage.setItem('jwt', response.data.jwt);
      console.log('Auto-login successful for test user:', TEST_USER.email);
      return true;
    }
    
    return false;
  } catch (error) {
    console.log('Auto-login failed, user needs to login manually:', error);
    return false;
  }
};

export const clearTestLogin = () => {
  localStorage.removeItem('jwt');
  console.log('Test user logged out');
};