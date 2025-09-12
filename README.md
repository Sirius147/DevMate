#  DevMate
<BR/><BR/><BR/>

<img width="512" height="512" alt="image" src="https://github.com/user-attachments/assets/3ae93a90-7015-403d-ac21-46397fd92902" />
<BR/><BR/>

|<img src="https://avatars.githubusercontent.com/iamyeongg" width="100" height="100"/>|<img src="https://avatars.githubusercontent.com/Sirius147" width="100" height="100"/>|<BR/>
|[@iamyeongg](https://github.com/iamyeongg)    |[@Sirius147](https://github.com/Sirius147)|

<BR/><BR/><BR/>
<BR/>

---

<BR/><BR/><BR/>
<BR/>
**💪 프로젝트 개요**

- **실력을 높이고** 싶고,  **비전이 같은 파트너**를 만나고 싶거나, **추구하는 개발 환경이 비슷한 동료**를 구하는 **개발자/ 예비 개발자/ 취준생 들이 프로젝트를 인원을 모집하거나 지원하며 진행하는 프로젝트를 효율적 관리를 도우는**  플랫폼

  
---

<BR/><BR/><BR/>
<BR/>
### 📊 핵심 기능
---
<BR/><BR/><BR/>
<BR/>
### 1. 로그인/ 마이페이지

- **OAuth2 기반 Github / Google 소셜 로그인**
- **자체 JWT 발급을 통한 인증** 서비스 (무상태 세션)
    - **ACCESS** 토큰을 통한 접근, **REFRESH** 토큰을 통해 갱신
    - 만료 시 재로그인으로 유저 인증 최적화
    - **redis**를 통한 간단한 **token** 검증 시스템
- 닉네임, 지역, 기술 스택, 개발 실력(전문/중급/초급/입문), 개인 평판,  원**하는 그룹 성향을 통한 유저 관리, 수정 기능**
    
    

### 2. 프로젝트 찾기

- **지역, 실력, 선호 개발 환경 등 다양한 검색 조건 기반으로 프로젝트 맵핑**

### 3. 새 프로젝트

- 유저 선호 기반으로 자체 프로젝트 모집
    - **시작일, 종료일**
    - **목표 환경**
    - **원하는 파트너 및 원하는 인원 설정**

### 4. 나의 프로젝트

- 모집 중 프로젝트 페이지 →  내가 직접 프로젝트 공고를 통해 맞는 동료를 구할 수 있음
    - 지원서 확인, 지원 승인 거부
- 완료 프로젝트 페이지
    - **프로젝트 종합 평가/ peer review 확인**
    - 목록에서 상시 확인 가능
    - **개인 평판 업데이트** 기능
- 참여 프로젝트 페이지 → 현재 참여 중인 프로젝트 목록 확인
    - 팀 채팅
        - 프로젝트 멤버십 기반 채팅 시스템
        - **STOMP 기반  메시지 브로커 실시간 소켓 채팅**
        - **S3 스토리지 url 첨부 파일**, 미읽음 채팅 확인
    - Todo LIst
        - 협업 필수 작업 작성 및 관리
        - 우선순위 기반 정렬, 동순위 시 종료일 기반 정렬로 **유저에게 작업 가이드라인 제시**
    - API 관리
        - 팀 프로젝트의 api 명세서를 직접 작성 및 관리를 통해 효율적인 프로젝트 설계를 도움
        - 간편한 작성 포맷으로, **자유롭고 활발한 관리를 통한 협업** 극대화
- 지원한 프로젝트
    - 지원한 프로젝트 상태 확인
    - 수락, 거부, 대기 등

### 5. 프로젝트 지원

- 메인 페이지에서 프로젝트 목록 제공 (시간 순)
    - **원하는 프로젝트** 조건에 있는 프로젝트 **실 지원** 기능
    - **프로젝트 리더에게 알림 전송** 및 수락/거부 액션 가능

### 6. 알림

- 지원서 제출, 지원서 승인/거부, 프로젝트 시작, 채팅 등 **인 앱 알림** 기능
    - **알림 이벤트 발생 시 서비스 메인 페이지에서 알림 여부를 유저에게 UI로 알림**
    - 유저가 알림 확인 시 해당 알림 읽음 처리

### 7. EC2 인스턴스 기반 서버 + GITHUB ACTIONS 자동화

- CI/CD를 통해 서비스 상시 제공 및 정상화

<BR/><BR/><BR/>
<BR/>

---
### 🤝 기술 스택

<BR/><BR/><BR/>
<BR/>

**LANGUAGE & FRAMEWORK** </BR>
![java](https://github.com/user-attachments/assets/a9cd03e7-07d6-477e-b3dd-32e7a6ae1e08)
![jpa](https://github.com/user-attachments/assets/dd9fdaec-6850-4401-9c67-af2da34ddf5d) 
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)
![gradle](https://github.com/user-attachments/assets/3e2aecfc-6ca4-4c16-b05a-857ea967c265)
<img src="https://img.shields.io/badge/SpringBoot-10B146?style=for-the-badge&logo=SpringBoot&logoColor=white">
</BR> </BR>

**DB** </BR>
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
![amazon](https://github.com/user-attachments/assets/0713b793-2d1e-40df-b31a-0005c2d18625)
</BR> </BR>

**SECURITY** </BR>

![jwt](https://github.com/user-attachments/assets/83bddf8b-d556-4e60-8391-2074704103c4)
<img src="https://img.shields.io/badge/SpringSecurity-3B66BC?style=for-the-badge&logo=SpringSecurity&logoColor=white"> </BR> </BR>


**CI/CD** </BR>
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)
![EC2](https://github.com/user-attachments/assets/4869b01e-be93-4da6-9d18-8d098e3b1971)
![image](https://github.com/user-attachments/assets/5e1795a5-88c5-4411-93cd-8afcf16d781e)
![YAML](https://img.shields.io/badge/yaml-%23ffffff.svg?style=for-the-badge&logo=yaml&logoColor=151515)
![Linux](https://img.shields.io/badge/Linux-FCC624?style=for-the-badge&logo=linux&logoColor=black)
![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)

**IDE** </BR>

![postman](https://github.com/user-attachments/assets/4bcd5043-6841-4cd1-b864-dec4dc39f918)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white) </BR> </BR>

**WIRE_FRAME** </BR>

![Figma](https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white)  </BR>



<BR/><BR/><BR/>
<BR/>







---

### 👀 IA (Information Architecture)

<BR/><BR/><BR/>
<BR/>
https://gitmind.com/app/docs/fjm0xdsb

<img width="794" height="880" alt="제목없음" src="https://github.com/user-attachments/assets/c76a6105-f303-4ab5-a5a1-3b7ceb92ed44" />

ㅡㅡㅡㅡ
### API


---

<BR/><BR/><BR/>
<BR/>
### 👀 ERD (Entity Relation Diagram)

<BR/><BR/><BR/>
<BR/>
<img width="1342" height="834" alt="image" src="https://github.com/user-attachments/assets/bbd31ed5-0536-48cf-a447-5708cd027550" />


### OAuth
