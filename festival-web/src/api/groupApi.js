import axios from 'axios';

const API_URL = 'http://localhost:8000/api/group';

export const groupApi = {
  /**
   * 새로운 그룹을 생성합니다.
   * @param {Object} groupData - 그룹 생성에 필요한 데이터
   * @param {string} groupData.groupName - 그룹 이름
   * @param {string} groupData.leaderId - 그룹장 ID
   * @param {string} groupData.festivalId - 축제 ID
   * @param {number} groupData.maxMembers - 최대 인원 수
   * @returns {Promise} 생성된 그룹 정보
   */
  createGroup: async (groupData) => {
    try {
      const response = await axios.post(`${API_URL}/create`, groupData);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  /**
   * 특정 그룹의 상세 정보를 조회합니다.
   * @param {string} groupId - 조회할 그룹 ID
   * @returns {Promise} 그룹 상세 정보
   */
  getGroupDetails: async (groupId) => {
    try {
      const response = await axios.get(`${API_URL}/${groupId}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  /**
   * 그룹 참가 요청을 생성합니다.
   * @param {string} groupId - 참가할 그룹 ID
   * @param {Object} requestData - 참가 요청 데이터
   * @returns {Promise} 생성된 참가 요청 정보
   */
  requestToJoin: async (groupId, requestData) => {
    try {
      const response = await axios.post(`${API_URL}/${groupId}/join`, requestData);
      return response.data;
    } catch (error) {
      throw error;
    }
  }
};