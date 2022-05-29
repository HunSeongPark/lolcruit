# lolcruit
롤크루트 - 리그오브레전드 라이너 채용공고 게시판 🎲                                                             
![logo](https://user-images.githubusercontent.com/71416677/169736957-bb11e1d4-397c-4a0e-9103-5d1235fcdcfb.jpeg)                   
Logo designed by 이량([@2ryangg](https://www.instagram.com/2ryangg))                        
                  
- Notion - [lolcruit]()      

## Details
게시글 목록                            
![image](https://user-images.githubusercontent.com/71416677/170858064-91296f20-0957-4ac8-8161-ba2b14d0fb6b.png)                       

게시글 상세                        
![image](https://user-images.githubusercontent.com/71416677/170858076-fa612358-d3d8-4b27-a4ab-ef71679ab392.png)                 

글 작성                            
![image](https://user-images.githubusercontent.com/71416677/170858085-c93e9879-e625-4586-82fc-13c69776754e.png)               

## Features
- 사용자
  - ID, PW 회원가입
  - 일반 로그인, SNS 로그인 및 기존 계정과 연동 (구글, 페이스북, 네이버)
  - 회원정보 수정

- 게시판
  - 게시글 및 댓글 기본 CRUD
  - 포지션 별 글 목록
  - 키워드 검색 / 포지션 별 키워드 검색
  - 페이징

## Tech Stack
- Java 17 / Spring Boot v2.6.7
- Gradle
- MySQL (DB)

## Dependencies
- Spring Boot
- Spring Web
- Spring Data JPA
- Validation
- Spring Boot DevTools
- Spring Security
- OAuth2 Client
- Thymeleaf
- Lombok
- MySQL Driver
- H2 Database (In-memory DB for test)

## EER Diagram
![image](https://user-images.githubusercontent.com/71416677/170858395-d7f86674-97ba-46ec-acbb-abaa76c9d5bc.png)


## API Specification
<img width="715" alt="image" src="https://user-images.githubusercontent.com/71416677/170858361-942fc426-0e7b-4140-869a-9c783d4e2551.png">
<img width="712" alt="image" src="https://user-images.githubusercontent.com/71416677/170858375-fb4b51e3-adb0-494c-be4e-bbee34dc6215.png">
<img width="717" alt="image" src="https://user-images.githubusercontent.com/71416677/170858384-2b3b343e-0e66-402d-9a8f-465ec38a6619.png">

## Validation
- 회원가입
  - 아이디 : 6~15자, 영문 소문자, 숫자, 중복 X
  - 비밀번호 : 8~15자, 영문 대소문자, 숫자, 특수문자 포함
  - 닉네임 : 2~15자, 영문 대소문자, 숫자, 한글(가~힣), 중복X
  - 이메일 : 이메일 패턴에 맞게, 중복X

- 회원정보 수정
  - 비밀번호 : 8~15자, 영문 대소문자, 숫자, 특수문자 포함 (일반 사용자만 변경 가능)
  - 닉네임 : 2~15자, 영문 대소문자, 숫자, 한글(가~힣), 중복X

- 로그인
  - 로그인 실패 시 하단에 로그인 실패 원인 출력
    - `아이디 또는 비밀번호가 일치하지 않습니다.`
    - `존재하지 않는 아이디입니다.`
    - `서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.`
    - `알 수 없는 이유로 오류가 발생했습니다.`
  - 로그인 성공 시 로그인 이전 페이지로 redirect
  - 회원가입 → 로그인 경로로 로그인 시 index page로 redirect

- 게시글 작성 / 수정
  - 제목 : 공백X, 최대 30자
  - 내용 : 공백X

- 댓글 작성 / 수정
  - 내용 : 공백X

## Business Logic
- 회원가입 - 일반
  - Validation 확인은 Backend 단에서
  - error 여부에 따라 Form 테두리 빨간색, 하단에 error message 출력
  - ID, 닉네임, 이메일 중복 시 하단에 “중복된 OO입니다.” 메시지 출력
  - SNS 계정과 이메일 중복 시 “이미 SNS 계정으로 가입된 이메일입니다.” 출력
  - 가입 성공 시 로그인 페이지로 redirect

- 회원가입 - OAuth(SNS)
  - 일반 회원과 email 중복 시
    - 기존 계정과 SNS 통합
    - 닉네임, Entity Id, email 동기화
    - 엔티티 아이디, 비밀번호 서버 내부 secret으로 변경
    - “해당 이메일로 가입된 일반 계정이 존재하여 SNS 계정이 연동되었습니다. 이후 해당 SNS 계정으로 로그인 해주세요.” 안내 페이지 redirect
  - 다른 SNS와 email 중복 시
    - “다른 SNS 계정으로 이미 가입된 이메일입니다. 해당 SNS 계정으로 로그인 해주세요.” 안내 페이지 redirect
  - 중복 안될 시
    - 아이디 : provider_code(5자리)
    - 비밀번호 : 서버 내부 자체 secret
    - email : 해당 SNS email
    - 닉네임 : provider_code(5자리) (회원정보 수정 페이지에서 수정 가능)
    - redirect : index (” / ”)

- 게시글
  - 작성 날짜 오늘일 경우 “HH:mm” format 적용
  - 작성 날짜 오늘이 아닐 경우 “yy.MM.dd” format 적용
  - 본인이 작성한 게시글만 수정 / 삭제 가능
  - (페이징) Controller → Model & View 넘겨줄 땐 무조건 페이지 값(0부터 시작) 기준으로 넘겨줌.
    - View에서 page+1 처리

- 댓글
  - 작성 날짜 오늘일 경우 “HH:mm” format 적용
  - 작성 날짜 오늘이 아닐 경우 “yy.MM.dd” format 적용
  - 본인이 작성한 댓글만 수정 / 삭제 가능

## 새롭게 알게된 점 (추후 블로그 정리 후 링크)
- SuccessHandler 권한 없는 페이지 접근 / 직접 로그인 경로 요청 / else에 대한 이전 페이지 이동
- (Spring Security) 회원 정보 수정 - UsernamePasswordAuthenticationToken의 password Parameter에 인코딩 된 비밀번호를 넣으면 안되고, 원본 비밀번호 넣기
```java
@Transactional
    public void edit(EditRequestDto editRequestDto, SessionUser sessionUser) {
        User user = userRepository.findByUsername(sessionUser.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String encodePw = passwordEncoder.encode(editRequestDto.getPassword());
        user.update(editRequestDto.getNickname(), encodePw);

        // Security 세션 변경 처리
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(), editRequestDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
```
- (Spring Security) username만 매칭되면 user 세션 생성되던 문제 해결 - AuthenticationProvider implements하여 password 기반 인증 + 성공 시 세션에 user 정보 저장
- (Spring Security) OAuth2UserService(session에 oauthInfo 저장) -> indexPage("/")에서 session.getAttributes("oauthInfo") 통해 안내 페이지 이동 
