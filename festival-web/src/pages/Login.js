import React, { useState } from 'react';
import {
  Container,
  Paper,
  Typography,
  TextField,
  Button,
  Box,
  Alert,
  Snackbar,
  Link,
  InputAdornment,
  IconButton,
  CircularProgress
} from '@mui/material';
import { Visibility, VisibilityOff } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import { loginUser } from '../api/userApi';

function Login() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: '',
    inputPassword: ''
  });

  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [showAlert, setShowAlert] = useState(false);
  const [alertMessage, setAlertMessage] = useState('');
  const [alertSeverity, setAlertSeverity] = useState('success');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const result = await loginUser(formData);
      
      if (result.status === 'success') {
        setAlertSeverity('success');
        setAlertMessage('로그인 성공!');
        setShowAlert(true);

        // 로그인 성공 후 홈페이지로 이동
        setTimeout(() => {
          navigate('/');
        }, 1000);
      }
    } catch (error) {
      setAlertSeverity('error');
      setAlertMessage(error.message || '로그인에 실패했습니다.');
      setShowAlert(true);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="sm">
      <Paper elevation={3} sx={{ p: 4, mt: 8 }}>
        <Typography variant="h4" component="h1" align="center" gutterBottom>
          로그인
        </Typography>
        
        <Box component="form" onSubmit={handleSubmit} sx={{ mt: 3 }}>
          <TextField
            fullWidth
            label="이메일"
            name="username"
            type="email"
            value={formData.username}
            onChange={(e) => setFormData(prev => ({
              ...prev,
              username: e.target.value
            }))}
            margin="normal"
            required
            autoFocus
            error={alertSeverity === 'error'}
          />

          <TextField
            fullWidth
            label="비밀번호"
            name="inputPassword"
            type={showPassword ? 'text' : 'password'}
            value={formData.inputPassword}
            onChange={(e) => setFormData(prev => ({
              ...prev,
              inputPassword: e.target.value
            }))}
            margin="normal"
            required
            error={alertSeverity === 'error'}
            InputProps={{
              endAdornment: (
                <InputAdornment position="end">
                  <IconButton
                    onClick={() => setShowPassword(!showPassword)}
                    edge="end"
                  >
                    {showPassword ? <VisibilityOff /> : <Visibility />}
                  </IconButton>
                </InputAdornment>
              ),
            }}
          />

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
            {loading ? (
              <CircularProgress size={24} color="inherit" />
            ) : (
              '로그인'
            )}
          </Button>

          <Box sx={{ textAlign: 'center', mt: 2 }}>
            <Link
              component="button"
              variant="body2"
              onClick={() => navigate('/signup')}
              sx={{ 
                textDecoration: 'none',
                color: '#7F8C8D',
                '&:hover': {
                  color: '#2C3E50'
                }
              }}
            >
              계정이 없으신가요? 회원가입
            </Link>
          </Box>
        </Box>
      </Paper>

      <Snackbar
        open={showAlert}
        autoHideDuration={3000}
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

export default Login;