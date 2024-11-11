// src/components/FestivalDetail.js
import React, { useEffect, useState } from 'react';
import { Container, Typography, CircularProgress, Card, CardContent, CardMedia, Box, Button } from '@mui/material';
import { useParams } from 'react-router-dom';
import { fetchFestivalDetail } from '../api/festivalApi';
import KakaoMap from './KakaoMap';

const FestivalDetail = () => {
  const { contentId } = useParams();
  const [festival, setFestival] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadFestivalDetail = async () => {
      setLoading(true);
      try {
        const data = await fetchFestivalDetail(contentId);
        setFestival(data);
      } catch (error) {
        console.error('축제 상세 정보를 가져오는 중 오류 발생:', error);
      } finally {
        setLoading(false);
      }
    };

    loadFestivalDetail();
  }, [contentId]);

  return (
    <Container maxWidth="lg"> {/* 컨테이너 크기 조정 */}
      {loading ? (
        <Box display="flex" justifyContent="center" mt={4}>
          <CircularProgress />
        </Box>
      ) : (
        festival && (
          <Card sx={{ marginTop: 3, marginBottom: 3 }}>
            <CardContent>
              <Typography 
                variant="h4" 
                gutterBottom 
                sx={{ 
                  textAlign: 'center',
                  fontWeight: 'bold',
                  mb: 3
                }}
              >
                {festival.title}
              </Typography>
              
              {/* 이미지 섹션 */}
              {festival.firstImage && (
                <Box 
                  display="flex" 
                  justifyContent="center" 
                  mb={4}
                  sx={{
                    width: '100%',
                    maxHeight: '500px', // 최대 높이 설정
                    overflow: 'hidden',
                    borderRadius: 2,
                    boxShadow: '0 4px 8px rgba(0,0,0,0.1)'
                  }}
                >
                  <CardMedia
                    component="img"
                    image={festival.firstImage}
                    alt={festival.title}
                    sx={{
                      width: '100%',
                      height: 'auto',
                      maxHeight: '500px', // 이미지 최대 높이
                      objectFit: 'contain' // 이미지 비율 유지
                    }}
                  />
                </Box>
              )}

              {/* 축제 정보 섹션 */}
              <Box sx={{ mb: 4 }}>
                <Typography variant="body1" gutterBottom sx={{ fontSize: '1.1rem', mb: 2 }}>
                  <strong>위치:</strong> {festival.addr1} {festival.addr2}
                </Typography>
                <Typography variant="body1" gutterBottom sx={{ fontSize: '1.1rem', mb: 2 }}>
                  <strong>시작일:</strong> {festival.eventStartDate}
                </Typography>
                <Typography variant="body1" gutterBottom sx={{ fontSize: '1.1rem', mb: 2 }}>
                  <strong>종료일:</strong> {festival.eventEndDate}
                </Typography>
                <Typography variant="body1" gutterBottom sx={{ fontSize: '1.1rem', mb: 2 }}>
                  <strong>전화번호:</strong> {festival.tel}
                </Typography>
              </Box>

              {/* 카카오맵 섹션 */}
              {festival.mapx && festival.mapy && (
                <Box sx={{ mb: 4 }}>
                  <Typography variant="h5" gutterBottom sx={{ mb: 2 }}>
                    축제 위치
                  </Typography>
                  <KakaoMap
                    mapx={festival.mapx}
                    mapy={festival.mapy}
                    title={festival.title}
                  />
                  <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
                    위도: {festival.mapy}, 경도: {festival.mapx}
                  </Typography>
                  <Button
                    variant="contained"
                    color="primary"
                    href={`https://map.kakao.com/link/map/${festival.title},${festival.mapy},${festival.mapx}`}
                    target="_blank"
                    sx={{ mt: 2 }}
                  >
                    카카오맵에서 보기
                  </Button>
                </Box>
              )}

              {/* 상세 설명 섹션 */}
              <Box sx={{ mt: 4 }}>
                <Typography variant="h6" gutterBottom>
                  상세 설명
                </Typography>
                <Typography 
                  variant="body1" 
                  sx={{ 
                    lineHeight: 1.8,
                    whiteSpace: 'pre-line' // 줄바꿈 보존
                  }}
                >
                  {festival.description || '상세 설명이 없습니다.'}
                </Typography>
              </Box>
            </CardContent>
          </Card>
        )
      )}
    </Container>
  );
};

export default FestivalDetail;