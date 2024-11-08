// src/components/FestivalDetail.js
import React, { useEffect, useState } from 'react';
import { Container, Typography, CircularProgress, Card, CardContent, CardMedia, Box, Button } from '@mui/material';
import { useParams } from 'react-router-dom';
import { fetchFestivalDetail } from '../api/festivalApi'; // API 호출 함수 가져오기

const FestivalDetail = () => {
  const { contentId } = useParams();
  const [festival, setFestival] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadFestivalDetail = async () => {
      setLoading(true);
      try {
        const data = await fetchFestivalDetail(contentId); // API 호출
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
    <Container>
      {loading ? (
        <CircularProgress />
      ) : (
        festival && (
          <Card sx={{ marginTop: 2 }}>
            <CardContent>
              <Typography variant="h4" gutterBottom>
                {festival.title}
              </Typography>
              <Box display="flex" justifyContent="center" mb={2}>
                {festival.firstImage && (
                  <CardMedia
                    component="img"
                    height="200"
                    image={festival.firstImage}
                    alt={festival.title}
                    sx={{ borderRadius: 1 }}
                  />
                )}
              </Box>
              <Typography variant="body1" gutterBottom>
                <strong>위치:</strong> {festival.addr1} {festival.addr2}
              </Typography>
              <Typography variant="body1" gutterBottom>
                <strong>시작일:</strong> {festival.eventStartDate}
              </Typography>
              <Typography variant="body1" gutterBottom>
                <strong>종료일:</strong> {festival.eventEndDate}
              </Typography>
              <Typography variant="body1" gutterBottom>
                <strong>전화번호:</strong> {festival.tel}
              </Typography>
              {/* KakaoMap 컴포넌트 사용 */}
              {/* {festival.mapx && festival.mapy && (
                <KakaoMap mapy={festival.mapy} mapx={festival.mapx} title={festival.title} />
              )} */}
              {/* 지도 링크 추가 */}
              {festival.mapx && festival.mapy && (
                <Button
                  variant="contained"
                  color="primary"
                  href={`https://map.kakao.com/link/map/${festival.title},${festival.mapy},${festival.mapx}`}
                  target="_blank"
                  sx={{ mt: 2 }}
                >
                  지도에서 보기
                </Button>
              )}
              {/* 추가적인 상세 정보 표시 */}
              <Typography variant="body1" gutterBottom>
                <strong>상세 설명:</strong> {festival.description || '상세 설명이 없습니다.'}
              </Typography>
            </CardContent>
          </Card>
        )
      )}
    </Container>
  );
};

export default FestivalDetail;