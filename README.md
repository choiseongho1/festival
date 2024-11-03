Here's a revised version of your README.md that enhances clarity and readability while maintaining the essential features of your Festival Group Matching System. 

```markdown
# 🎪 Festival Group Matching System

축제를 함께 즐길 그룹을 찾고 계신가요? 관심사가 비슷한 사람들과 함께 축제를 즐겨보세요! 

## 🎯 프로젝트 소개

**Festival Group Matching System**은 축제 참가자들을 위한 그룹 매칭 플랫폼입니다. 사용자의 관심사와 선호도를 기반으로 최적의 그룹을 추천해드립니다.

### 주요 기능
- 🤝 리더와 참가자로 구성된 1:N 그룹 매칭
- 🎨 사용자 선호도에 기반한 맞춤형 추천
- 📊 실시간 축제 정보 연동
- ⚡ 이벤트 기반 실시간 매칭

## 🛠 기술 스택

### Backend
- ![Java](https://img.shields.io/badge/Java-17-orange) : 자바 언어
- ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.0-green) : 빠른 애플리케이션 개발
- ![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-2023.0.0-green) : 클라우드 구축
- ![Gradle](https://img.shields.io/badge/Gradle-8.5-blue) : 의존성 관리 도구

### Database
- ![MongoDB](https://img.shields.io/badge/MongoDB-6.0-green) : 사용자 및 매칭 데이터
- ![MySQL](https://img.shields.io/badge/MySQL-8.0-blue) : 그룹 및 축제 정보

### Infrastructure
- ![Kafka](https://img.shields.io/badge/Kafka-3.6-red) : 이벤트 메시징 시스템
- ![Eureka](https://img.shields.io/badge/Eureka-Service_Discovery-lightgrey) : 서비스 검색
- ![Docker](https://img.shields.io/badge/Docker-24.0-blue) : 컨테이너 관리

## 📦 시스템 구성

GitHub에서 디렉토리 구조를 마크다운 형식으로 출력할 때, 코드 블록 안에서 줄바꿈과 정렬이 제대로 나타나지 않을 수 있습니다. 이를 해결하기 위해 각 항목의 앞에 공백을 추가하여 계층 구조를 명확하게 표시할 수 있습니다. 아래와 같이 수정해 보세요:

```markdown
## 📦 시스템 구성

```
festival-matching/
├── api-gateway/           # API 라우팅, 인증/인가, 로드밸런싱
├── eureka-server/         # 서비스 디스커버리 및 레지스트리
├── group/                 # 그룹 생성 및 관리, 참가자 관리
├── match/                 # 사용자-그룹 매칭 알고리즘
├── user/                  # 회원 관리, 인증, 프로필 관리
└── data-collection/       # 공공 데이터 수집 및 가공
```
```

### 각 서비스별 주요 역할

#### 🚀 API Gateway
- 클라이언트 요청 라우팅
- JWT 기반 인증/인가
- 요청/응답 필터링
- 로드밸런싱

#### 🔍 Eureka Server
- 서비스 등록 및 발견
- 상태 모니터링
- 서비스 레지스트리 관리

#### 📊 Data Collection
- 공공 데이터 수집
- 배치 작업 관리

## ⭐ 주요 기능

### 1. 그룹 매칭 시스템 🤝
- 축제별 그룹 생성 및 관리
- 사용자 선호도 기반 그룹 추천
- 실시간 매칭 알고리즘 적용
- 그룹 참가 신청 및 수락/거절 기능

### 2. 축제 정보 관리 🎉
- 공공데이터 포털 연동
- 실시간 축제 정보 업데이트
- 지역별/카테고리별 축제 검색
- 축제 상세 정보 제공

### 3. 사용자 매칭 서비스 👥
- 사용자 프로필 기반 매칭
- 관심사 기반 그룹 추천
- 실시간 알림 서비스
- 매칭 이력 관리

---
이 시스템은 축제의 즐거움을 두 배로 만들어 줄 것입니다! 질문이 있으시면 언제든지 물어보세요. 😊
```
