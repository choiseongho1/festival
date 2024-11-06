// src/pages/SignUp.js
import React, { useState } from 'react';
import {
  Container,
  Paper,
  Typography,
  TextField,
  Button,
  Box,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  FormHelperText,
  Alert,
  Snackbar
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { registerUser } from '../api/userApi';

function SignUp() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: '',
    inputPassword: '',
    passwordConfirm: '',
    fullName: '',
    phoneNumber: '',
    favoriteFestival: '',
    preferredFestivalType: '',
    availableForMatching: true
  });

  const [errors, setErrors] = useState({});
  const [showAlert, setShowAlert] = useState(false);
  const [loading, setLoading] = useState(false);
  const [alertMessage, setAlertMessage] = useState('');
  const [alertSeverity, setAlertSeverity] = useState('success');

  // 축제 종류 목록
  const festivalTypes = [
    '음악축제',
    '문화축제',
    '예술축제',
    '먹거리축제',
    '지역축제'
  ];

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    // 에러 메시지 초기화
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  const validateForm = () => {
    const newErrors = {};

    // 아이디 검증
    if (!formData.username) {
      newErrors.username = '아이디를 입력해주세요';
    } else if (formData.username.length < 4) {
      newErrors.username = '아이디는 4자 이상이어야 합니다';
    }

    // 비밀번호 검증
    if (!formData.inputPassword) {
      newErrors.inputPassword = '비밀번호를 입력해주세요';
    } else if (formData.inputPassword.length < 8) {
      newErrors.inputPassword = '비밀번호는 8자 이상이어야 합니다';
    }

    // 비밀번호 확인
    if (formData.inputPassword !== formData.passwordConfirm) {
      newErrors.passwordConfirm = '비밀번호가 일치하지 않습니다';
    }

    // 이름 검증
    if (!formData.fullName) {
      newErrors.fullName = '이름을 입력해주세요';
    }

    // 전화번호 검증
    const phoneRegex = /^[0-9]{10,11}$/;
    if (!formData.phoneNumber) {
      newErrors.phoneNumber = '전화번호를 입력해주세요';
    } else if (!phoneRegex.test(formData.phoneNumber)) {
      newErrors.phoneNumber = '올바른 전화번호 형식이 아닙니다';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (validateForm()) {
      setLoading(true);
      try {
        // 비밀번호 확인 필드 제거
        const { passwordConfirm, ...registerData } = formData;
        
        // API 호출
        await registerUser(registerData);
        
        setAlertSeverity('success');
        setAlertMessage('회원가입이 완료되었습니다!');
        setShowAlert(true);

        // 성공 시 3초 후 로그인 페이지로 이동
        setTimeout(() => {
          navigate('/login');
        }, 3000);

      } catch (error) {
        setAlertSeverity('error');
        setAlertMessage(error.message);
        setShowAlert(true);
      } finally {
        setLoading(false);
      }
    }
  };

  return (
    <Container maxWidth="sm">
      <Paper elevation={3} sx={{ p: 4, mt: 8 }}>
        <Typography variant="h4" component="h1" align="center" gutterBottom>
          회원가입
        </Typography>
        
        <Box component="form" onSubmit={handleSubmit} sx={{ mt: 3 }}>
          <TextField
            fullWidth
            label="아이디"
            name="username"
            value={formData.username}
            onChange={handleChange}
            error={!!errors.username}
            helperText={errors.username}
            margin="normal"
            required
          />

          <TextField
            fullWidth
            label="비밀번호"
            name="inputPassword"
            type="password"
            value={formData.inputPassword}
            onChange={handleChange}
            error={!!errors.inputPassword}
            helperText={errors.inputPassword}
            margin="normal"
            required
          />

          <TextField
            fullWidth
            label="비밀번호 확인"
            name="passwordConfirm"
            type="password"
            value={formData.passwordConfirm}
            onChange={handleChange}
            error={!!errors.passwordConfirm}
            helperText={errors.passwordConfirm}
            margin="normal"
            required
          />

          <TextField
            fullWidth
            label="이름"
            name="fullName"
            value={formData.fullName}
            onChange={handleChange}
            error={!!errors.fullName}
            helperText={errors.fullName}
            margin="normal"
            required
          />

          <TextField
            fullWidth
            label="전화번호"
            name="phoneNumber"
            value={formData.phoneNumber}
            onChange={handleChange}
            error={!!errors.phoneNumber}
            helperText={errors.phoneNumber}
            margin="normal"
            required
          />

          <TextField
            fullWidth
            label="선호하는 축제"
            name="favoriteFestival"
            value={formData.favoriteFestival}
            onChange={handleChange}
            margin="normal"
          />

          <FormControl fullWidth margin="normal">
            <InputLabel>선호하는 축제 종류</InputLabel>
            <Select
              name="preferredFestivalType"
              value={formData.preferredFestivalType}
              onChange={handleChange}
            >
              {festivalTypes.map((type) => (
                <MenuItem key={type} value={type}>
                  {type}
                </MenuItem>
              ))}
            </Select>
          </FormControl>

          <FormControl fullWidth margin="normal">
            <InputLabel>매칭 가능 여부</InputLabel>
            <Select
              name="availableForMatching"
              value={formData.availableForMatching}
              onChange={handleChange}
            >
              <MenuItem value={true}>가능</MenuItem>
              <MenuItem value={false}>불가능</MenuItem>
            </Select>
            <FormHelperText>다른 사용자와의 매칭 가능 여부를 선택해주세요</FormHelperText>
          </FormControl>

          <Button
            type="submit"
            fullWidth
            variant="contained"
            disabled={loading}
            sx={{ 
              mt: 3, 
              mb: 2,
              backgroundColor: '#E74C3C',
              '&:hover': {
                backgroundColor: '#C0392B'
              }
            }}
          >
            {loading ? '처리중...' : '가입하기'}
          </Button>

          <Button
            fullWidth
            variant="text"
            onClick={() => navigate('/login')}
            sx={{ color: '#7F8C8D' }}
          >
            이미 계정이 있으신가요? 로그인
          </Button>
        </Box>
      </Paper>

      <Snackbar
        open={showAlert}
        autoHideDuration={6000}
        onClose={() => setShowAlert(false)}
        anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
      >
        <Alert 
          onClose={() => setShowAlert(false)} 
          severity={alertSeverity}
          sx={{ width: '100%' }}
        >
          {alertMessage}
        </Alert>
      </Snackbar>
    </Container>
  );
}

export default SignUp;