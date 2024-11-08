// src/components/FestivalList.js
import React, { useEffect, useState } from 'react';
import {
  Container,
  Typography,
  Card,
  CardContent,
  CardMedia,
  CircularProgress,
  Pagination,
  Box,
  Grid,
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { fetchFestivals } from '../api/festivalApi';

const FestivalList = () => {
  const [festivals, setFestivals] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const navigate = useNavigate();

  useEffect(() => {
    const loadFestivals = async () => {
      setLoading(true);
      try {
        const data = await fetchFestivals(page - 1);
        setFestivals(data.content);
        setTotalPages(data.totalPages);
      } catch (error) {
        console.error('축제 목록을 가져오는 중 오류 발생:', error);
      } finally {
        setLoading(false);
      }
    };

    loadFestivals();
  }, [page]);

  const handleFestivalClick = (contentId) => {
    navigate(`/festivals/${contentId}`);
  };

  return (
    <Container>
      <Typography variant="h4" gutterBottom>
        축제 목록
      </Typography>
      {loading ? (
        <CircularProgress />
      ) : (
        <Grid container spacing={3}>
          {festivals.map((festival) => (
            <Grid item xs={12} sm={6} md={4} key={festival.contentId}>
              <Card onClick={() => handleFestivalClick(festival.contentId)} sx={{ cursor: 'pointer' }}>
                {festival.firstImage ? (
                  <CardMedia
                    component="img"
                    height="140"
                    image={festival.firstImage} // 이미지 URL
                    alt={festival.title}
                  />
                ) : (
                  <Box
                    height="140" // 이미지가 없을 때도 카드의 높이를 일정하게 유지
                    display="flex"
                    alignItems="center"
                    justifyContent="center"
                    bgcolor="grey.300" // 배경색 설정
                  >
                    <Typography variant="body2" color="text.secondary" style={{ height: '140px', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                      대표 이미지가 없습니다
                    </Typography>
                  </Box>
                )}
                <CardContent>
                  <Typography variant="h6" component="div">
                    {festival.title}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {festival.addr1}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      )}
      <Box display="flex" justifyContent="center" mt={2}>
        <Pagination
          count={totalPages}
          page={page}
          onChange={(event, value) => setPage(value)}
          color="primary"
        />
      </Box>
    </Container>
  );
};

export default FestivalList;