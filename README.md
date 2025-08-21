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
<img width="1342" height="834" alt="image" src="https://github.com/user-attachments/assets/bbd31ed5-0536-48cf-a447-5708cd027550" />
<br>
chatAttachment			<br>				
Key	Logical	Physical	Domain	Type	Allow Null	Default Value	Comment<br>
PK	id	Attachment_id	Not Null	long	N		<br>
FK	채팅아이디	chat_id	Not Null	long	N		<br>
	이미지url	url	Not Null	varchar(200)	Y		<br>
	스토리지키	storagekey	Not Null	varchar(200)	Y		<br>
	파일이름	filename	Not Null	varchar(200)	Y		<br>
	파일사이즈	filesize	Not Null	long	Y		<br>
							<br>
review							<br>
Key	Logical	Physical	Domain	Type	Allow Null	Default Value	Comment <br>
PK	id	review_id	Not Null	long	N		<br>
FK	평가자아이디	user_id	Not Null	long	N		<br>
FK	프로젝트아이디	project_id	Not Null	long	N		<br>
	프로젝트리뷰	project_review	Not Null	long	Y		<br>
	별점	star_point	Not Null	int	Y		<br>
							<br>
project							<br>
Key	Logical	Physical	Domain	Type	Allow Null	Default Value	Comment <br>
PK	id	project_id	Not Null	long	N		<br>
	이름	project_name	Not Null	varchar(20)	Y		<br>
	총원	project_members	Not Null	int	Y		<br>
	현재원	members	Not Null	int	Y		<br>
	상태	project_status	Not Null	ENUM	Y	<br>	
	시작일	project_start	Not Null	DateTime	Y	<br>	
	종료일	project_end	Not Null	DateTime	Y		<br>
	소개	title		varchar(300)	Y		<br>
	협업방식	collaborate_style		ENUM	Y	<br>	
	선호지역	preferred_region		ENUM	Y		<br>
	레벨	level		ENUM	Y		<br>
	백인원	backend_members		int	Y		<br>
	백현재원	backend_now		int	Y		<br>
	프론트원	frontend_members		int	Y	<br>	
	프론트현재원	frontend_now		int	Y		<br>
	디자인원	design_members		int	Y		<br>
	디자인현재원	design_now		int	Y		<br>
	기획원	manager_members		int	Y		<br>
	기획현재원	manager_now		int	Y		<br>
							<br>
chatchannel						<br>	
Key	Logical	Physical	Domain	Type	Allow Null	Default Value	Comment <br>
PK	id	channel_id	Not Null	long	N		<br>
FK	프로젝트아이디	project_id	Not Null	long	N	<br>	
	채팅방이름	name		varchar(20)	Y		<br>
	채팅인원	members		int	Y		<br>
							<br>
apies							<br>
Key	Logical	Physical	Domain	Type	Allow Null	Default Value	Comment <br>
PK	id	api_id	Not Null	long	N		<br>
FK	프로젝트아이디	project_id	Not Null	long	N	<br>	
	메소드	method	Not Null	ENUM	Y		<br>
	경로	path	Not Null	Text	Y		<br>
	응답예문	response_example		Text	Y	<br>	
	파라미터	parameters		TEXT	Y		<br>
							
todolist							<br>
Key	Logical	Physical	Domain	Type	Allow Null	Default Value	Comment <br>
PK	id	todo_id	Not Null	long	N		<br>
FK	프로젝트아이디	project_id	Not Null	long	N	<br>	
	내용	content	Not Null	Text	Y		<br>
	우선순위	proirity	Not Null	ENUM	Y	<br>	
	시작일	startTime		DateTime	Y		<br>
	마감일	endTime		DateTime	Y		<br>
	유형	type	Not Null	ENUM	Y		<br>
	제목	title		varchar(30)	Y		<br>
	완료여부	is_done	Not NUll	boolean	Y	<br>	
							<br>
application						<br>	
Key	Logical	Physical	Domain	Type	Allow Null	Default Value	Comment <br>
PK	id	app_id	Not Null	long	N		<br>
FK	지원프로젝트아이디	project_id	Not Null	long	N	<br>	
FK	지원자아이디	user_id	Not Null	long	N		<br>
	지원내용	content		TEXT	Y		<br>
	지원상태	app_status	Not Null	ENUM	Y	<br>	
	지원시간	applicated_at	Not Null	DateTime	Y	<br>	
							<br>
Notification					<br>		
Key	Logical	Physical	Domain	Type	Allow Null	Default Value	Comment <br>
PK	id	notification_id	Not Null	long	N		<br>
FK	수신자아이디	user_id	Not Null	long	N		<br>
	알림유형	notification__type	Not Null	ENUM	Y	<br>	
	알림내용	notification_content	Not Null	varchar(200)	Y	<br>	
	알림시간	notifyed_at	Not Null	DateTime	Y		<br>
	읽은시간	read_at	Not Null	DateTime	Y		<br>
	소스아이디	source_id	Not Null	long	Y		<br>
							<br>
stack							<br>
Key	Logical	Physical	Domain	Type	Allow Null	Default Value	Comment
PK	id	us_id	Not Null	long	N		
FK	id	user_id	Not Null	long	N		
	기술 유형	stack_type		ENUM	Y		
	이름	name	Not Null	varchar(20)	Y		
							
chatmembership							
Key	Logical	Physical	Domain	Type	Allow Null	Default Value	Comment <br>
PK	id	Key	Not Null	long	N		<br>
FK	유저아이디	user_id	Not Null	long	N		<br>
FK	채널아이디	channel_id	Not Null	long	N		<br>
	마지막읽은시간	last_read_at	Not Null	DateTime	Y		<br>
							<br>
chatMessage							<br>
Key	Logical	Physical	Domain	Type	Allow Null	Default Value	Comment <br>
PK	id	chat_id	Not Null	long	N		<br>
FK	보낸이	sender_id	Not Null	long	N		<br>
FK	채널아이디	channel_id	Not Null	long	N		<br>
	메시지유형	message_type	Not Null	ENUM	Y		<br>
	메시지내용	content	Not Null	TEXT	Y		<br>
	메시지생성시간	createdAt	Not Null	DateTime	Y		<br>
							<br>
user							<br>
Key	Logical	Physical	Domain	Type	Allow Null	Default Value	Comment <br>
PK	id	user_id	Not Null	long	N		<br>
	이메일	email	Not Null	varchar(40)	Y		<br>
	닉네임	nickname	Not Null	varchar(20)	Y		<br>
	지역	region		enum	Y		<br>
	실력	level		enum	Y		<br>
	인기도	popularity		double	Y		<br>
	회원 유형	user_role	Not Null	enum	Y		<br>
	수정일	updated_at	Not Null	datetime	Y		<br>
	가입일	created_at	Not Null	datetime	Y		<br>
	선호	preference		ENUM	Y		<br>
							<br>
membership						<br>	
Key	Logical	Physical	Domain	Type	Allow Null	Default Value	Comment <br>
PK	id	team_id	Not Null	long	N		<br>
FK	유저아이디	user_id	Not Null	long	N	<br>	
	프로젝트역할	project_role	Not Null	enum	Y	<br>	
	참여일	joined_at	Not Null	DateTime	Y		<br>
FK	id	project_id	Not Null	long	N		<br>
							<br>
<br>
