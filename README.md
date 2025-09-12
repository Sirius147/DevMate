#  DevMate
<BR/><BR/><BR/>

<img width="512" height="512" alt="image" src="https://github.com/user-attachments/assets/3ae93a90-7015-403d-ac21-46397fd92902" />
<BR/><BR/>

|<img src="https://avatars.githubusercontent.com/iamyeongg" width="100" height="100"/>|<img src="https://avatars.githubusercontent.com/Sirius147" width="100" height="100"/>|  <BR/>
  |[@iamyeongg](https://github.com/iamyeongg)      |[@Sirius147](https://github.com/Sirius147)|

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

### 👀 IA (Information Architecture) & Wire Frame

<BR/><BR/><BR/>



[IA 링크](https://gitmind.com/app/docs/fjm0xdsb)  <br>
[wire frame 링크](https://www.figma.com/proto/HyJTKOmrgnTOPXabQGGvZf/DevMate-WF?node-id=0-1&t=Cn70nTap1sznKDgZ-1)
<br>

<details>
	<summary>IA 이미지</summary>
<img width="794" height="880" alt="제목없음" src="https://github.com/user-attachments/assets/c76a6105-f303-4ab5-a5a1-3b7ceb92ed44"/> 
 
 <br>
	
</details>


 
  <br>
 




<BR/><BR/>

---

<BR/><BR/>
### 🧒 API
[API명세서 링크](https://www.notion.so/API-256c1d551b838048ac6cd561b7c18b1e) <br>

<details>
	<summary>요청/응답 예시</summary>

	```
REQUEST BODY

POST /login/basic

{
    "nickname": "bruceLee",
    "regions": "SEOUL",
    "skillLevel": "BEGINNER"
}



POST /login/detail

{
    "preferredAtmosphere": "RELAXED",
    "collaborateStyle": "HYBRID",
    "position": "BACKEND"
}


POST /login/stack

[
    {
        "stackName": "Java",
        "stackType": "LANGUAGE"
    },
     {
        "stackName": "Java",
        "stackType": "FRAMEWORK"
    },
     {
        "stackName": "Java",
        "stackType": "DEPLOY"
    },
     {
        "stackName": "Java",
        "stackType": "DATABASE"
    }
]


PATCH /main/my-page

{
    "nickname": "SUI",
    "intro": "인생힘들다",
    "regions": "SEOUL",
    "skillLevel": "BEGINNER",
    "collaborateStyle": "HYBRID",
    "position": "BACKEND",
    "stackSetUpDtos": [
        {
            "stackName": "Python",
            "stackType": "LANGUAGE"
        },
        {
            "stackName": "Spring",
            "stackType": "FRAMEWORK"
        },
        {
		        "stackName": "Spring",
            "stackType": "FRAMEWORK"
        },
        {
		        "stackName": "Spring",
            "stackType": "FRAMEWORK"
        }
    ]
}

POST /main/new-project

{
    "title": "devmate", > 프로젝트 이름
    "shortDescription": "마음 맞는 개발자끼리 어쩌고", > 짧은 소개글
    "startDate": "2025-09-09", > 프로젝트 시작일
    "endDate": "2025-10-01", > 프로젝트 종료일
    "collaborateStyle": "HYBRID", > 개발 유형 (혼합, 온, 오프라인)
    "preferredRegion": "SEOUL", > 선호 지역
    "projectLevel": "BEGINNER", > 개발 실력
    "backendMembers": 3, > 백엔드 총원
    "currentBackend": 1, > 현재원
    "frontendMembers": 2,
    "currentFrontend": 2,
    "designMembers": 1,
    "currentDesign": 1,
    "pmMembers": 1,
    "currentPm": 1
}

POST /main/projects

{
    "page": 0,
    "regions": "SEOUL",
    "preferredAtmosphere": "RELAXED",
    "skillLevel": null,
    "projectStatus": "RECRUITING"
}

POST /main/apply/1?position=BACKEND&content=잘할수있어요뽑아주세요


POST /my-project/participation/1/docs


{
    "name": "로그인하기",
    "method": "GET",
    "path": "/main/login",
    "responseExample": "{ content: [], totalCount: 0}",
    "parameter": "{ content: [], totalCount: 0}"
}

PATCH /my-project/participation/1/docs/1

{
    "name": null,
    "method": "POST",
    "path": "/main/login/",
    "responseExample": null,
    "parameter": null
}



POST /my-project/participation/1/todoLists

{
    "title": "니맘대로해",
    "position": "BACKEND",
    "content": "채팅테스트하고 원리 파악하기 그리고 배포 완료하고 문서정리 자동화까지",
    "priority": "HIGH",
    "startDate": "2025-09-09",
    "endDate": "2025-09-09",
    "done": false
}




PATCH /my-project/participation/1/todoLists/1

{
    "title": "곧이야",
    "position": null,
    "content": "채팅테스트하고 원리 파악하기 그리고 배포 완료하고 문서정리 자동화까지다잘해낼거다",
    "priority": null,
    "startDate": null,
    "endDate": null,
    "done": false
}


GET /my-project/completed




POST /my-project/completed/1/review

{
    "star": 5,
    "content": "내 인생 가장 열심히 한 프로젝트, 아쉬움이 많지만 앞으로 더 잘할거니께"
}

 ```

```
RESPONSE BODY

GET /login/basic

GET /main/my-page

{
    "name": "이성일",
    "email": "lsimma7@gmail.com",
    "nickname": "bruceLee",
    "intro": null,
    "region": "SEOUL",
    "skillLevel": "BEGINNER",
    "popularity": null,
    "preferredAtmosphere": "RELAXED",
    "collaborateStyle": "HYBRID",
    "position": "BACKEND",
    "stackResponseDtos": [
        {
            "stackType": "LANGUAGE",
            "stackName": "Java"
        }
    ]
}


POST /main/my-page?nickname=
{
    "available": true
}


GET /main?page=0

{
    "pageList": {
        "content": [
            {
                "projectId": 1,
                "title": "devmate",
                "shortDescription": "마음 맞는 개발자끼리 어쩌고",
                "recruitSize": 7,
                "currentSize": 5,
                "startDate": "2025-09-09",
                "endDate": "2025-10-01",
                "collaborateStyle": "HYBRID",
                "preferredRegion": "SEOUL",
                "projectLevel": "BEGINNER",
                "projectStatus": "RECRUITING",
                "backendMembers": 3,
                "currentBackend": 1,
                "frontendMembers": 2,
                "currentFrontend": 2,
                "designMembers": 1,
                "currentDesign": 1,
                "pmMembers": 1,
                "currentPm": 1
            }
        ],
        "totalCount": 1
    },
    "needNotify": false
}

POST /main/projects

{
    "content": [
        {
            "projectId": 102,
            "title": "testProject",
            "shortDescription": "진짜 힘들어",
            "recruitSize": 7,
            "currentSize": 5,
            "startDate": "2025-09-09",
            "endDate": "2025-10-01",
            "collaborateStyle": "HYBRID",
            "preferredRegion": "SEOUL",
            "projectLevel": "BEGINNER",
            "projectStatus": "RECRUITING",
            "backendMembers": 3,
            "currentBackend": 1,
            "frontendMembers": 2,
            "currentFrontend": 2,
            "designMembers": 1,
            "currentDesign": 1,
            "pmMembers": 1,
            "currentPm": 1
        },
        {
            "projectId": 1,
            "title": "devmate",
            "shortDescription": "마음 맞는 개발자끼리 어쩌고",
            "recruitSize": 7,
            "currentSize": 5,
            "startDate": "2025-09-09",
            "endDate": "2025-10-01",
            "collaborateStyle": "HYBRID",
            "preferredRegion": "SEOUL",
            "projectLevel": "BEGINNER",
            "projectStatus": "RECRUITING",
            "backendMembers": 3,
            "currentBackend": 1,
            "frontendMembers": 2,
            "currentFrontend": 2,
            "designMembers": 1,
            "currentDesign": 1,
            "pmMembers": 1,
            "currentPm": 1
        }
    ],
    "totalCount": 2
}

GET /main/notifications

{
    "content": [
        {
            "notificationType": "APPLICATION_SUBMITTED",
            "content": "프로젝트devmate에 지원 완료!",
            "createdAt": "2025-09-08T18:49:07.684343Z"
        }
    ],
    "totalCount": 1
}

GET /main/notifications

{
    "content": [
        {
            "notificationType": "APPLICATION_SUBMITTED",
            "content": "프로젝트devmate에 지원자: siriusGITHUB-a44d3e24가 포지션: BACKEND으로 지원하였습니다!",
            "createdAt": "2025-09-08T18:49:07.688343Z"
        }
    ],
    "totalCount": 1
}


GET /main/notifications

{
    "content": [
        {
            "notificationType": "APPLICATION_ACCEPTED",
            "content": "프로젝트devmate 에 참가 승인 되었습니다",
            "createdAt": "2025-09-08T19:09:24.461847Z"
        }
    ],
    "totalCount": 1
}

GET /my-project/recruiting

{
    "content": [
        {
            "ProjectId": 102,
            "title": "testProject",
            "recruitSize": 7,
            "currentSize": 5,
            "backendMembers": 3,
            "currentBackend": 1,
            "frontendMembers": 2,
            "currentFrontend": 2,
            "designMembers": 1,
            "currentDesign": 1,
            "pmMembers": 1,
            "currentPm": 1,
            "projectStatus": "RECRUITING"
        }
    ],
    "totalCount": 1
}

GET /my-project/recruiting/1/applications

{
    "content": [
        {
		        "applicationId": 1,
            "nickname": "siriusGITHUB-a44d3e24",
            "applyPosition": "BACKEND",
            "content": "잘할수있어요뽑아주세요",
            "applicationStatus": "STANDBY"
        }
    ],
    "totalCount": 1
}

PATCH /my-project/recruiting/1/applications/1?accept=true

200 OK

GET /my-project/applied-project

{
    "content": [
        {
            "id": 1,
            "title": "devmate",
            "applicationStatus": "APPROVAL",
            "content": "잘할수있어요뽑아주세요"
        }
    ],
    "totalCount": 1
}



GET /my-project/recruiting/1/start-project

200 OK




GET /my-project/participation

{
    "content": [
        {
            "projectId": 1,
            "title": "devmate",
            "shortDescription": "마음 맞는 개발자끼리 어쩌고",
            "projectStatus": "IN_PROGRESS"
        }
    ],
    "totalCount": 1
}


GET /my-project/participation/1/docs

[
		{

        docId: 1
        projectTitle: "유영이"
        name : "멋있어"
        method: "GET"
        path: "/my-project/participation"
        responseExample: "SDKFJSLDFK"
        parameter: "ASD;FKAJKFDK"

		}
]

POST /my-project/participation/1/docs

200 OK


PATCH /my-project/participation/1/docs/1

204 NO_CONTENT


DELETE /my-project/participation/1/docs/2

204 NO_CONTENT


GET /my-project/participation/{projectId}/todoLists 
[
		{
				toDoListId: 1
        projectTitle: "EC2"
        title: "투두리스트아이디"
        position: "BACKEND"
        content: "내일까지 ㅇㅇ할것"
        priority: "HIGH"
        startDate: "2025-09-10"
        endDate: "2025-09-11"
        isDone: false
        updatedAt: "2025-09-10-12:00:00"

		},
				{
				toDoListId: 1
        projectTitle: "EC2"
        title: "투두리스트아이디"
        position: "BACKEND"
        content: "내일까지 ㅇㅇ할것"
        priority: "HIGH"
        startDate: "2025-09-10"
        endDate: "2025-09-11"
        isDone: false
        updatedAt: "2025-09-10-12:00:00"

		}


]
POST /my-project/participation/1/todoLists

200 OK


PATCH /my-project/participation/1/todoLists/1

204 NO_CONTENT


GET /my-project/participation/1/complete

200 OK


GET  /my-project/completed

{
    "content": [
        {
            "projectId": 1,
            "title": "devmate",
            "shortDescription": "마음 맞는 개발자끼리 어쩌고",
            "projectStatus": "COMPLETED"
        }
    ],
    "totalCount": 1
}


POST /my-project/completed/1/review

200 OK


GET /my-project/completed/1/review

{
    "content": [
        {
            "projectStar": 5,
            "content": "내 인생 가장 열심히 한 프로젝트, 아쉬움이 많지만 앞으로 더 잘할거니께"
        }
    ],
    "totalCount": 1
}


GET /api/chat/channels/{channelId}/messages


(
        id,
        channelId,
        senderUserId,
        messageType, // "TEXT"/"FILE"/"IMAGE"/"SYSTEM" 등
        content,
        createdAt,
        attachments : {
		        id,
            url,
            filename,
            contentType,
            storageSize
        }
) 

```
	
</details>

<BR/><BR/>

---

<BR/><BR/>
### 👀 ERD (Entity Relation Diagram) / 테이블 명세서

<BR/><BR/><BR/>


<details>
  <summary> 테이블 명세서 </summary>
    application <br/>
<img width="505" height="199" alt="image" src="https://github.com/user-attachments/assets/e91f2fbe-9cfe-4bd6-96ab-ba669f1bec64" /> </br>
chat_attachment <br/>

<img width="433" height="177" alt="image" src="https://github.com/user-attachments/assets/d9e8282c-b6e3-49e1-bf7a-dfce2eca108f" /> </br>

chat_channel <br/>
<img width="433" height="155" alt="image" src="https://github.com/user-attachments/assets/be9564a7-7627-4c71-8d72-52ab9685a372" /> <br/>

chat_membership <br/>
<img width="433" height="111" alt="image" src="https://github.com/user-attachments/assets/7d76b235-b799-45a0-9608-a090d5cc3398" /> <br/>

chat_message <br/>

<img width="433" height="177" alt="image" src="https://github.com/user-attachments/assets/d1e7ae96-3b65-4fc5-a406-dc03700a1d67" /> <br/>

doc <br/>

<img width="433" height="221" alt="image" src="https://github.com/user-attachments/assets/3b6fce87-e271-4a24-970b-af37349a2386" /> <br/>

notification </br>
	
<img width="433" height="199" alt="image" src="https://github.com/user-attachments/assets/e70c23c0-606c-4d75-9d55-e5aa9f802d37" /> <br/>

membership </br>
<img width="433" height="177" alt="image" src="https://github.com/user-attachments/assets/c0f3e93e-8317-4b7e-a071-bf5b0cd999ca" /> <br/>


review </br>
<img width="433" height="133" alt="image" src="https://github.com/user-attachments/assets/c9ea3540-bfd5-4a3c-8d74-ac25f9eefcbf" /> <br/>

stack </br>

<img width="433" height="111" alt="image" src="https://github.com/user-attachments/assets/34cdfdb6-3fa6-47c7-ba89-168af35c7e9b" /> <br/>

todolist </br>

<img width="433" height="265" alt="image" src="https://github.com/user-attachments/assets/6dc7896c-fcac-4d57-b388-214526b0680b" /> <br/>

project </br>
<img width="433" height="485" alt="image" src="https://github.com/user-attachments/assets/3e4a7c76-f742-4d92-89ce-22ae74a5ee41" /> </br>

users </br>
<img width="433" height="397" alt="image" src="https://github.com/user-attachments/assets/d3ccd588-6bd1-4fce-baea-2529f7667f64" /> <br/>

</details>

<BR/>

<details>
	<summary>ERD</summary>
	
<img width="1342" height="834" alt="image" src="https://github.com/user-attachments/assets/bbd31ed5-0536-48cf-a447-5708cd027550" />
 	
</details>

<BR/><BR/>

