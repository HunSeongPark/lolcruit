# lolcruit
롤크루트 - 리그오브레전드 라이너 채용공고 게시판 🎲                    
                                
![logo](https://user-images.githubusercontent.com/71416677/169736957-bb11e1d4-397c-4a0e-9103-5d1235fcdcfb.jpeg)                   
Logo designed by 이량([@2ryangg](https://www.instagram.com/2ryangg))                  


## 기록
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
