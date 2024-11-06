// src/pages/Home.js
import React from 'react';
import { 
  Container, 
  Typography, 
  Grid, 
  Box,
  Button,
  Paper
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import Header from '../components/Header';
import GroupCard from '../components/GroupCard';
import { dummyGroups } from '../data/dummyData';

function Home() {
  const navigate = useNavigate();

  return (
    <div>
      <Header />
      <Container>
        {/* 메인 히어로 섹션 */}
        <Box sx={{ my: 4, textAlign: 'center' }}>
          <Typography 
            variant="h3" 
            component="h1" 
            gutterBottom
            sx={{ 
              fontWeight: 'bold',
              color: '#2C3E50'
            }}
          >
            축제를 함께 즐길 그룹을 찾아보세요!
          </Typography>
          <Typography 
            variant="h6" 
            color="text.secondary" 
            paragraph
          >
            관심사가 비슷한 사람들과 특별한 축제의 순간을 공유하세요
          </Typography>
          
          {/* 회원가입 유도 섹션 추가 */}
          <Box sx={{ mt: 4, mb: 6 }}>
            <Button 
              variant="contained" 
              size="large"
              onClick={() => navigate('/signup')}
              sx={{ 
                mr: 2,
                backgroundColor: '#E74C3C',
                '&:hover': {
                  backgroundColor: '#C0392B'
                }
              }}
            >
              지금 시작하기
            </Button>
            <Button 
              variant="outlined"
              size="large"
              onClick={() => navigate('/login')}
              sx={{ 
                color: '#2C3E50',
                borderColor: '#2C3E50',
                '&:hover': {
                  borderColor: '#2C3E50',
                  backgroundColor: 'rgba(44, 62, 80, 0.04)'
                }
              }}
            >
              로그인
            </Button>
          </Box>
        </Box>

        {/* 회원가입 혜택 섹션 */}
        <Box sx={{ mb: 6 }}>
          <Grid container spacing={3}>
            <Grid item xs={12} md={4}>
              <Paper sx={{ p: 3, textAlign: 'center', height: '100%' }}>
                <Typography variant="h6" gutterBottom>
                  👥 그룹 매칭
                </Typography>
                <Typography>
                  관심사가 비슷한 사람들과 함께 축제를 즐기세요
                </Typography>
              </Paper>
            </Grid>
            <Grid item xs={12} md={4}>
              <Paper sx={{ p: 3, textAlign: 'center', height: '100%' }}>
                <Typography variant="h6" gutterBottom>
                  🎉 축제 정보
                </Typography>
                <Typography>
                  다양한 축제 정보를 한눈에 확인하세요
                </Typography>
              </Paper>
            </Grid>
            <Grid item xs={12} md={4}>
              <Paper sx={{ p: 3, textAlign: 'center', height: '100%' }}>
                <Typography variant="h6" gutterBottom>
                  ⭐ 맞춤 추천
                </Typography>
                <Typography>
                  나에게 맞는 축제와 그룹을 추천받으세요
                </Typography>
              </Paper>
            </Grid>
          </Grid>
        </Box>

        {/* 기존 그룹 목록 섹션 */}
        <Box sx={{ my: 4 }}>
          <Typography 
            variant="h4" 
            component="h2" 
            gutterBottom
            sx={{ 
              fontWeight: 'bold',
              color: '#2C3E50',
              mb: 3
            }}
          >
            인기 있는 그룹
          </Typography>
          <Grid container spacing={3}>
            {dummyGroups.map((group) => (
              <Grid item xs={12} sm={6} md={4} key={group.id}>
                <GroupCard group={group} />
              </Grid>
            ))}
          </Grid>
        </Box>
      </Container>
    </div>
  );
}

export default Home;