// src/data/dummyData.js
export const dummyGroups = [
  {
    id: 1,
    festivalName: "서울 랜턴 페스티벌",
    groupName: "밤하늘 구경팟",
    leaderName: "별똥별",
    currentMembers: 3,
    maxMembers: 6,
    description: "랜턴 구경하면서 맛있는 것도 먹어요!",
    status: "모집중",
    createdAt: "2024-03-15",
    tags: ["서울", "야간", "사진촬영"]
  },
  {
    id: 2,
    festivalName: "부산 불꽃축제",
    groupName: "불꽃놀이 동호회",
    leaderName: "불꽃남자",
    currentMembers: 4,
    maxMembers: 5,
    description: "부산 불꽃축제 함께 즐기실 분들 모집합니다!",
    status: "모집중",
    createdAt: "2024-03-14",
    tags: ["부산", "불꽃놀이", "사진촬영"]
  },
  // ... 더 많은 더미 데이터
];

export const dummyFestivals = [
  {
    id: 1,
    name: "서울 랜턴 페스티벌",
    location: "서울특별시 중구",
    startDate: "2024-04-01",
    endDate: "2024-04-15",
    description: "아름다운 빛의 향연",
    imageUrl: "/images/lantern-festival.jpg",
    category: "문화예술",
  },
  // ... 더 많은 축제 데이터
];