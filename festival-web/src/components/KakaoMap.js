// src/components/KakaoMap.js
import React, { useEffect, useRef } from 'react';

const KakaoMap = ({ mapx, mapy, title }) => {
  const mapContainer = useRef(null);
  const map = useRef(null);

  useEffect(() => {
    // 좌표값이 없으면 실행하지 않음
    if (!mapx || !mapy) {
      console.error('좌표값이 유효하지 않습니다:', { mapx, mapy });
      return;
    }

    const initializeMap = () => {
      // kakao.maps가 로드되었는지 확인
      if (typeof window.kakao === 'undefined' || typeof window.kakao.maps === 'undefined') {
        console.log('Kakao Maps API가 로드되지 않았습니다. 잠시 후 다시 시도합니다.');
        setTimeout(initializeMap, 100);
        return;
      }

      // maps API가 로드된 후 실행
      window.kakao.maps.load(() => {
        try {
          console.log('Kakao Maps API 로드 완료');
          
          // 문자열로 된 좌표값을 숫자로 변환
          const lat = parseFloat(mapy);
          const lng = parseFloat(mapx);

          console.log('지도 초기화 시작:', { lat, lng, title });

          // 지도 옵션 설정
          const options = {
            center: new window.kakao.maps.LatLng(lat, lng),
            level: 3
          };

          // 지도 생성
          const mapInstance = new window.kakao.maps.Map(mapContainer.current, options);
          map.current = mapInstance;

          // 마커 생성
          const markerPosition = new window.kakao.maps.LatLng(lat, lng);
          const marker = new window.kakao.maps.Marker({
            position: markerPosition,
            map: mapInstance
          });

          // 인포윈도우 생성
          const iwContent = `
            <div style="padding:5px;text-align:center;min-width:150px;">
              <strong>${title}</strong>
            </div>
          `;

          const infowindow = new window.kakao.maps.InfoWindow({
            content: iwContent,
            removable: true
          });

          // 마커 클릭 이벤트
          window.kakao.maps.event.addListener(marker, 'click', () => {
            infowindow.open(mapInstance, marker);
          });

          // 지도 컨트롤 추가
          const zoomControl = new window.kakao.maps.ZoomControl();
          mapInstance.addControl(zoomControl, window.kakao.maps.ControlPosition.RIGHT);

          const mapTypeControl = new window.kakao.maps.MapTypeControl();
          mapInstance.addControl(mapTypeControl, window.kakao.maps.ControlPosition.TOPRIGHT);

          console.log('지도 초기화 완료');
        } catch (error) {
          console.error('지도 초기화 중 오류 발생:', error);
        }
      });
    };

    initializeMap();

    // cleanup function
    return () => {
      if (map.current) {
        map.current = null;
      }
    };
  }, [mapx, mapy, title]);

  return (
    <div
      ref={mapContainer}
      style={{
        width: '100%',
        height: '400px',
        borderRadius: '8px',
        marginTop: '20px',
        marginBottom: '20px',
        border: '1px solid #ddd'
      }}
    />
  );
};

export default KakaoMap;