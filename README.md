# 🛒 Sparta-MZ Backoffice

이커머스 백오피스 API · Spring Boot + MySQL 기반 백엔드 프로젝트
관리자가 고객·상품·주문·리뷰를 관리하는 REST API를 쿠키·세션 인증과 역할 기반 인가로 구현했습니다.
---

## 📌 1. 프로젝트 개요 (Overview)

### 👥 팀원 및 역할

| 팀원   | 역할               | 담당 도메인·기능                             |
|--------|--------------------|----------------------------------------------|
| 이정현 | 리더               | 공통 예외처리·인증/인가, 주문 CRUD, 상품(부) |
| 김다현 | 발표자료 제작·발표 | 관리자 전체·프로필 조회, 가입 승인/거부      |
| 김명현 | 기록·README 관리   | 관리자 가입·로그인·수정·역할/상태 변경·삭제  |
| 전주한 | 발표자료 제작      | 상품·고객·리뷰 CRUD, 주문·공통(부)           |

### 📅 일정

| 단계             | 기간         | 주요 내용                                        |
|------------------|--------------|--------------------------------------------------|
| 기획·설계        | 10/27        | 역할 분담, ERD·API 명세 설계                     |
| 개발             | 10/27 ~ 11/2 | 공통·인증/인가 → 관리자·상품·고객·주문·리뷰 CRUD |
| 테스트·코드 리뷰 | 10/29 ~ 11/2 | 튜터 코드 리뷰(10/29) 반영, Postman API 테스트   |
| 발표 준비        | 11/2 ~ 11/3  | API 명세·트러블슈팅 정리, 발표 자료 제작         |

### 🤝 그라운드 룰

- 정기 회의 10:00, 데일리 스크럼 10:30 (ZEP) 필참
- 개인 일정 특이사항은 캘린더에 공유
- 코드 피드백은 GitHub 코드 리뷰로 진행
- 중요 사항·튜터 피드백은 노션·슬랙에 정리
- API 도구는 로컬에서 사용하고, 필요 시 export해 공유
- **어떤 상황이건 말 예쁘게 하기**

### 📏 코드 컨벤션

- Naver Code Convention 적용
- 커밋 메시지: `feat` · `fix` · `refactor` · `docs` · `chore` · `style`
- 계층형 패키지: `controller` · `service` · `repository` · `dto` · `entity` · `common`
- 공통 에러 응답: `code` · `message` · `timestamp`
- ErrorCode: 도메인 접두사 + 번호 (예: `G001` 공통, `A001` 관리자)

### 🌿 Git 전략

![Git 전략](./images/gitflow.png)

- Git Flow 변형: `main` / `develop` / `feature/*`
- `feature` → `develop` PR, PR 템플릿 작성
- 승인 2명 이후 Squash and merge
- 머지된 브랜치는 재사용하지 않고 최신 `develop`에서 새로 생성

---

## 🛠 2. Tech Stack

### 💻 개발·실행 구성 (Development & Runtime)

* **IDE**: IntelliJ IDEA
* **Language & Framework**: Java, Spring Boot, Gradle, Hibernate
* **Database**: MySQL
* **Build/Tool**: Postman (API 테스트 등)

### 📂 형상 관리 (Version Control)

* **VCS**: Git
* **Platform**: GitHub

### 💬 협업 툴 (Collaboration)

* Slack

---

## 📏 3. ERD

![ERD](./images/ERD.png)

---

## ✏️ 4. API 명세서

* [👉 Sparta-MZ 팀 노션 API 명세서 보러 가기](https://app.notion.com/p/teamsparta/74f2dc3ef51483b094d30152bee49bd8?v=2b42dc3ef514836d8c260804a87084b8)

---

## 💡 5. 플로우 차트

### 관리자 플로우차트

![관리자 플로우차트](./images/adminsflowchar.png)

### 고객 플로우차트

![고객 플로우차트](./images/customersflowchar.png)

### 주문 플로우차트

![주문 플로우차트](./images/orderflowchar.png)

### 상품 플로우차트

![상품 플로우차트](./images/productsflowchar.png)

---

## 📦 6. Package Diagram

![패키지 다이어그램](./images/packagediagram.png)

---

## 🎈 7. Swimlane Diagram

![스윔레인 다이어그램](./images/swimlane.png)

## 🔫 8. Trouble Shooting

### 8-1. 세션 DB 불일치 문제

![세션DB불일치](./images/troubleshooting1.png)
![세션DB불일치](./images/19.png)
![세션DB불일치](./images/20.png)
![세션DB불일치](./images/21.png)

### 8-2. 세션 고정 공격 방어

![세선고정공격방어](./images/22.png)
![세선고정공격방어](./images/23.png)
![세선고정공격방어](./images/24.png)
![세선고정공격방어](./images/25.png)
![세선고정공격방어](./images/26.png)
![세선고정공격방어](./images/27.png)
![세선고정공격방어](./images/28.png)

### 8-3. 머지 충돌 관리

![머지충돌관리](./images/29.png)
![머지충돌관리](./images/30.png)
![머지충돌관리](./images/31.png)
![머지충돌관리](./images/32.png)
![머지충돌관리](./images/33.png)