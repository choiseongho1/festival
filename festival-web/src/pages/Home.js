import React from 'react';
import { 
  Container, 
  Typography, 
  Grid, 
  Box,
  Button
} from '@mui/material';
import Header from '../components/Header';
import GroupCard from '../components/GroupCard';
import { dummyGroups } from '../data/dummyData';

function Home() {
  return (
    <div>
      <Header />
      <Container>
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
          <Button 
            variant="contained" 
            size="large"
            sx={{ 
              mt: 2,
              backgroundColor: '#E74C3C',
              '&:hover': {
                backgroundColor: '#C0392B'
              }
            }}
          >
            그룹 만들기
          </Button>
        </Box>

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
