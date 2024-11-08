import axios from 'axios';

const API_URL = 'http://localhost:8000/api/data-collection';

export const fetchFestivals = async (page) => {
  try {
    const response = await axios.get(`${API_URL}?page=${page}`);
    return response.data;
  } catch (error) {
    console.error('축제 목록을 가져오는 중 오류 발생:', error);
    throw error; // 오류를 호출한 곳으로 전달
  }
};

// 축제 상세 조회
export const fetchFestivalDetail = async (contentId) => {
  try {
    const response = await axios.get(`${API_URL}/${contentId}`);
    return response.data;
  } catch (error) {
    console.error('축제 상세 정보를 가져오는 중 오류 발생:', error);
    throw error; // 오류를 호출한 곳으로 전달
  }
};
