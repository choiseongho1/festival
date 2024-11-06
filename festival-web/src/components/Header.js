import React from 'react';
import { 
  AppBar, 
  Toolbar, 
  Typography, 
  Button, 
  Box 
} from '@mui/material';
import { useNavigate } from 'react-router-dom';

function Header() {
  const navigate = useNavigate();

  return (
    <AppBar position="static" sx={{ backgroundColor: '#2C3E50' }}>
      <Toolbar>
        <Typography 
          variant="h6" 
          component="div" 
          sx={{ flexGrow: 1, cursor: 'pointer' }}
          onClick={() => navigate('/')}
        >
          🎪 Festival Matching
        </Typography>
        <Box>
          <Button color="inherit" onClick={() => navigate('/login')}>
            로그인
          </Button>
          <Button 
            variant="contained" 
            sx={{ 
              ml: 2, 
              backgroundColor: '#E74C3C',
              '&:hover': {
                backgroundColor: '#C0392B'
              }
            }}
            onClick={() => navigate('/signup')}
          >
            회원가입
          </Button>
        </Box>
      </Toolbar>
    </AppBar>
  );
}

export default Header;
