#  DevMate
## 프로젝트 모집 플랫폼

**💪 개요**

- 개발자/ 예비 개발자/ 취준생 들이 프로젝트를 준비/ 인원을 모집할 수 있는 플랫폼

### ⏳ 개발 기간

- **기간**: 1개월

---

### 📊 기능 목록

### 1. 로그인 / 마이페이지

- Github / Google 소셜 로그인
- 마이페이지: 닉네임, 지역, 기술 스택(복수 스택), 개발 실력(전문/중급/초급/처음), 진행 했던 프로젝트 목록, 개인 평판 혹은 레벨, 원하는 그룹 인원 수(복수 선택 가능), 원하는 그룹 성향(복수 선택 가능)
    
    

### 2. 모집 게시판

- 회원 또는 미완성 모임 추천 (회원 정보 기반으로 추천하는 단일 회원 또는 미완성 모임을 제공, AI 활용 여부 보류)
- 모임 모집 직접 포스

### 3. 나의 프로젝트

- 진행 중 프로젝트 페이지 →  채팅방(공지, wbs, api명세서, todo list등 소기능) 제공
- 완료 프로젝트 페이지 → 평가/ peer review 기능
- 승인 대기 프로젝트 페이지 → 목록

### 4. 사용자 검색

- 필터: 기술스택, 지역, 개발 실력, 기간, 원하는 그룹 성향




---

### 👀 IA (Information Architecture)

https://gitmind.com/app/docs/fjm0xdsb

<img width="794" height="880" alt="제목없음" src="https://github.com/user-attachments/assets/c76a6105-f303-4ab5-a5a1-3b7ceb92ed44" />

ㅡㅡㅡㅡ
### 


---

### 👀 ERD (Entity Relation Diagram)

chatAttachment							
Key	Logical	Physical	Domain	Type	Allow Null	Default Value	Comment
PK	id	Attachment_id	Not Null	long	N		
FK	채팅아이디	chat_id	Not Null	long	N		
	이미지url	url	Not Null	varchar(200)	Y		
	스토리지키	storagekey	Not Null	varchar(200)	Y		
	파일이름	filename	Not Null	varchar(200)	Y		
	파일사이즈	filesize	Not Null	long	Y		
							
review							
Key	Logical	Physical	Domain	Type	Allow Null	Default Value	Comment
PK	id	review_id	Not Null	long	N		
FK	평가자아이디	user_id	Not Null	long	N		
FK	프로젝트아이디	project_id	Not Null	long	N		
	프로젝트리뷰	project_review	Not Null	long	Y		
	별점	star_point	Not Null	int	Y		
							
project							
Key	Logical	Physical	Domain	Type	Allow Null	Default Value	Comment
PK	id	project_id	Not Null	long	N		
	이름	project_name	Not Null	varchar(20)	Y		
	총원	project_members	Not Null	int	Y		
	현재원	members	Not Null	int	Y		
	상태	project_status	Not Null	ENUM	Y		
	시작일	project_start	Not Null	DateTime	Y		
	종료일	project_end	Not Null	DateTime	Y		
	소개	title		varchar(300)	Y		
	협업방식	collaborate_style		ENUM	Y		
	선호지역	preferred_region		ENUM	Y		
	레벨	level		ENUM	Y		
	백인원	backend_members		int	Y		
	백현재원	backend_now		int	Y		
	프론트원	frontend_members		int	Y		
	프론트현재원	frontend_now		int	Y		
	디자인원	design_members		int	Y		
	디자인현재원	design_now		int	Y		
	기획원	manager_members		int	Y		
	기획현재원	manager_now		int	Y		
							
chatchannel							
Key	Logical	Physical	Domain	Type	Allow Null	Default Value	Comment
PK	id	channel_id	Not Null	long	N		
FK	프로젝트아이디	project_id	Not Null	long	N		
	채팅방이름	name		varchar(20)	Y		
	채팅인원	members		int	Y		
							
apies							
Key	Logical	Physical	Domain	Type	Allow Null	Default Value	Comment
PK	id	api_id	Not Null	long	N		
FK	프로젝트아이디	project_id	Not Null	long	N		
	메소드	method	Not Null	ENUM	Y		
	경로	path	Not Null	Text	Y		
	응답예문	response_example		Text	Y		
	파라미터	parameters		TEXT	Y		
							
todolist							
Key	Logical	Physical	Domain	Type	Allow Null	Default Value	Comment
PK	id	todo_id	Not Null	long	N		
FK	프로젝트아이디	project_id	Not Null	long	N		
	내용	content	Not Null	Text	Y		
	우선순위	proirity	Not Null	ENUM	Y		
	시작일	startTime		DateTime	Y		
	마감일	endTime		DateTime	Y		
	유형	type	Not Null	ENUM	Y		
	제목	title		varchar(30)	Y		
	완료여부	is_done	Not NUll	boolean	Y		
							
application							
Key	Logical	Physical	Domain	Type	Allow Null	Default Value	Comment
PK	id	app_id	Not Null	long	N		
FK	지원프로젝트아이디	project_id	Not Null	long	N		
FK	지원자아이디	user_id	Not Null	long	N		
	지원내용	content		TEXT	Y		
	지원상태	app_status	Not Null	ENUM	Y		
	지원시간	applicated_at	Not Null	DateTime	Y		
							
Notification							
Key	Logical	Physical	Domain	Type	Allow Null	Default Value	Comment
PK	id	notification_id	Not Null	long	N		
FK	수신자아이디	user_id	Not Null	long	N		
	알림유형	notification__type	Not Null	ENUM	Y		
	알림내용	notification_content	Not Null	varchar(200)	Y		
	알림시간	notifyed_at	Not Null	DateTime	Y		
	읽은시간	read_at	Not Null	DateTime	Y		
	소스아이디	source_id	Not Null	long	Y		
							
stack							
Key	Logical	Physical	Domain	Type	Allow Null	Default Value	Comment
PK	id	us_id	Not Null	long	N		
FK	id	user_id	Not Null	long	N		
	기술 유형	stack_type		ENUM	Y		
	이름	name	Not Null	varchar(20)	Y		
							
chatmembership							
Key	Logical	Physical	Domain	Type	Allow Null	Default Value	Comment
PK	id	Key	Not Null	long	N		
FK	유저아이디	user_id	Not Null	long	N		
FK	채널아이디	channel_id	Not Null	long	N		
	마지막읽은시간	last_read_at	Not Null	DateTime	Y		
							
chatMessage							
Key	Logical	Physical	Domain	Type	Allow Null	Default Value	Comment
PK	id	chat_id	Not Null	long	N		
FK	보낸이	sender_id	Not Null	long	N		
FK	채널아이디	channel_id	Not Null	long	N		
	메시지유형	message_type	Not Null	ENUM	Y		
	메시지내용	content	Not Null	TEXT	Y		
	메시지생성시간	createdAt	Not Null	DateTime	Y		
							
user							
Key	Logical	Physical	Domain	Type	Allow Null	Default Value	Comment
PK	id	user_id	Not Null	long	N		
	이메일	email	Not Null	varchar(40)	Y		
	닉네임	nickname	Not Null	varchar(20)	Y		
	지역	region		enum	Y		
	실력	level		enum	Y		
	인기도	popularity		double	Y		
	회원 유형	user_role	Not Null	enum	Y		
	수정일	updated_at	Not Null	datetime	Y		
	가입일	created_at	Not Null	datetime	Y		
	선호	preference		ENUM	Y		
							
membership							
Key	Logical	Physical	Domain	Type	Allow Null	Default Value	Comment
PK	id	team_id	Not Null	long	N		
FK	유저아이디	user_id	Not Null	long	N		
	프로젝트역할	project_role	Not Null	enum	Y		
	참여일	joined_at	Not Null	DateTime	Y		
FK	id	project_id	Not Null	long	N		
							
