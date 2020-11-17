>>> 동작과정 <<<

[초기접속] : localhost:9000 접속 -> HomeController 13번쨰 줄 @GetMapping("/")에서 return "/home/home"; 으로 로그인 창으로 이동
┖ [로그인] : home.html에서 ID/PW 입력 (11.16.(월) 기준 입력값 검증 없음) -> MemberController에서 security 검증 수행 -> ready.html로 보내게 작업해야 함
┖ [회원가입] (작업예정) : home.html에서 회원가입 클릭 시 sign_up.html으로 이동 -> MemberController의 67번째 줄 부터 회원가입 처리

[게임방 입장] (작업예정) : 로그인 완료 시 ready.html로 이동 -> 방번호 입력으로 원하는방 입장 혹은 방 생성 -> (처리)
