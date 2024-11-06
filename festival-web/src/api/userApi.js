// src/api/userApi.js
import api from './axios';

export const registerUser = async (userData) => {
  try {
    const response = await api.post('/user/register', userData);
    return response.data;
  } catch (error) {
    throw new Error(error.response?.data?.message || '회원가입 중 오류가 발생했습니다.');
  }
};

export const checkPhoneVerification = async (userId) => {
  try {
    const response = await api.get(`/user/${userId}/verification/status`);
    return response.data;
  } catch (error) {
    throw new Error('인증 상태 확인 중 오류가 발생했습니다.');
  }
};

// 로그인 함수 수정
export const loginUser = async (loginData) => {
  try {
    const loginRequest = {
      email: loginData.username,
      password: loginData.inputPassword
    };

    const response = await api.post('/auth/login', loginRequest);
    const { status, message, accessToken, refreshToken } = response.data;

    if (status === 'success') {
      // 토큰 저장
      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('refreshToken', refreshToken);
      // axios 기본 헤더에 토큰 설정
      api.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;
      return { status, message };
    } else {
      throw new Error(message);
    }
  } catch (error) {
    if (error.response?.data) {
      throw new Error(error.response.data.message);
    }
    throw new Error('로그인 중 오류가 발생했습니다.');
  }
};

// 토큰 갱신 함수 추가
export const refreshToken = async () => {
  try {
    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) {
      throw new Error('Refresh token not found');
    }

    const response = await api.post('/auth/refresh', null, {
      headers: {
        'Authorization': refreshToken
      }
    });

    if (response.data.accessToken) {
      localStorage.setItem('token', response.data.accessToken);
      api.defaults.headers.common['Authorization'] = `Bearer ${response.data.accessToken}`;
    }

    return response.data;
  } catch (error) {
    throw new Error('토큰 갱신에 실패했습니다.');
  }
};