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
        const response = await fetchFestivals(page - 1); // API는 0-based pagination 사용
        setFestivals(response.content);
        setTotalPages(response.totalPages);
      } catch (error) {
        console.error('축제 목록을 불러오는 중 오류 발생:', error);
      } finally {
        setLoading(false);
      }
    };

    loadFestivals();
  }, [page]);

  const handleFestivalClick = (contentId) => {
    navigate(`/festivals/${contentId}`);
  };

  // 주소 조합 함수
  const getFullAddress = (addr1, addr2) => {
    return [addr1, addr2].filter(Boolean).join(' ');
  };

  return (
    <Container>
      <Typography variant="h4" gutterBottom>
        축제 목록
      </Typography>
      {loading ? (
        <Box display="flex" justifyContent="center" mt={4}>
          <CircularProgress />
        </Box>
      ) : (
        <Grid container spacing={3}>
          {festivals.map((festival) => (
            <Grid item xs={12} sm={6} md={4} key={festival.contentId}>
              <Card 
                onClick={() => handleFestivalClick(festival.contentId)} 
                sx={{ 
                  cursor: 'pointer',
                  height: '100%',
                  display: 'flex',
                  flexDirection: 'column',
                  transition: 'transform 0.2s ease-in-out',
                  '&:hover': {
                    transform: 'scale(1.02)',
                    boxShadow: 3
                  }
                }}
              >
                {festival.firstImage ? (
                  <CardMedia
                    component="img"
                    height="140"
                    image={festival.firstImage}
                    alt={festival.title}
                    sx={{
                      objectFit: 'cover'
                    }}
                  />
                ) : (
                  <Box
                    height="140"
                    display="flex"
                    alignItems="center"
                    justifyContent="center"
                    bgcolor="grey.300"
                  >
                    <Typography 
                      variant="body2" 
                      color="text.secondary"
                      sx={{ height: '140px', display: 'flex', alignItems: 'center' }}
                    >
                      대표 이미지가 없습니다
                    </Typography>
                  </Box>
                )}
                <CardContent 
                  sx={{ 
                    flexGrow: 1,
                    display: 'flex',
                    flexDirection: 'column',
                    gap: 1
                  }}
                >
                  <Typography 
                    variant="h6" 
                    component="div"
                    sx={{
                      overflow: 'hidden',
                      textOverflow: 'ellipsis',
                      display: '-webkit-box',
                      WebkitLineClamp: 1,
                      WebkitBoxOrient: 'vertical',
                      lineHeight: '1.5em',
                      height: '1.5em',
                      fontWeight: 'bold'
                    }}
                    title={festival.title}
                  >
                    {festival.title}
                  </Typography>
                  <Typography 
                    variant="body2" 
                    color="text.secondary"
                    sx={{
                      overflow: 'hidden',
                      textOverflow: 'ellipsis',
                      display: '-webkit-box',
                      WebkitLineClamp: 1,
                      WebkitBoxOrient: 'vertical',
                      lineHeight: '1.5em',
                      height: '1.5em'
                    }}
                    title={getFullAddress(festival.addr1, festival.addr2)}
                  >
                    {getFullAddress(festival.addr1, festival.addr2)}
                  </Typography>
                  {festival.eventStartDate && festival.eventEndDate && (
                    <Typography 
                      variant="body2" 
                      color="text.secondary"
                      sx={{
                        overflow: 'hidden',
                        textOverflow: 'ellipsis',
                        display: '-webkit-box',
                        WebkitLineClamp: 1,
                        WebkitBoxOrient: 'vertical',
                        lineHeight: '1.5em',
                        height: '1.5em'
                      }}
                    >
                      {festival.eventStartDate} ~ {festival.eventEndDate}
                    </Typography>
                  )}
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      )}
      <Box display="flex" justifyContent="center" mt={3} mb={3}>
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