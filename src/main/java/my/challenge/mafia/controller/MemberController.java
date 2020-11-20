package my.challenge.mafia.controller;


import my.challenge.mafia.domain.LoginToken;
import my.challenge.mafia.domain.Member;
import my.challenge.mafia.domain.LoginUser;
import my.challenge.mafia.repository.exception.DuplicateInsertException;
import my.challenge.mafia.security.JwtAuthenticationProvider;
import my.challenge.mafia.security.JwtTokenUtil;
import my.challenge.mafia.security.JwtUserDetailsService;
import my.challenge.mafia.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }



    // 잘못된 접속을 할 경우에 나오는 페이지
    @GetMapping("/wrong")
    public String wrong() {
        return "/home/wrongAccess";
    }

    // 관리자 페이지
    @GetMapping("/admin")
    public String admin() {
        return "/admin/admin";
    }

    // 테스트 페이지
    @GetMapping("/test")
    public String test(){
        return "/home/test";
    }


    // 회원 가입 페이지
    @GetMapping("/signup")
    public String signupForm(){
        return "/members/signup";
    }

    // 회원 가입 수행
    @PostMapping("/signup")
    public String signup(@RequestParam("id") String id, @RequestParam("password") String password){
        Member member = new Member();
        member.setId(id);
        member.setPassword(passwordEncoder.encode(password));
        System.out.println("여기까지 완료");
        memberService.join(member);
        System.out.println("여기까지 완료2");
        return "/home/home";

    }



    // 로그인 수행

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletResponse response) throws Exception {
    //public String login(@RequestParam LoginUser loginUser, HttpServletResponse response) throws Exception {

        final String token;
        final String refreshToken;
        // 인증에 성공시 인증완료된 객체를 반환받는다.
        Authentication authenticatedToken = authenticate(username, password);

        try {
            // JWT를 생성한다. 만약 잔달된 매개 변수인 authenticatedToken이 null일 경우 로그인 실패
            token = jwtTokenUtil.generateToken(authenticatedToken);
            //refreshToken = jwtTokenUtil.generateRefreshToken(authenticatedToken);

            // 생성된 토큰을 쿠키에 저장한다.
            Cookie jwCookie = new Cookie("JW-TOKEN", "Bearer" + token);
            //Cookie refreshCookie = new Cookie("REFRESH-TOKEN", "Bearer" + refreshToken);

            jwCookie.setMaxAge(60 * 60 * 24); // 쿠키 만료일 = 하루
            //jwCookie.setHttpOnly(true); // http 요청에만 쿠키 전송 - javascript를 통한 쿠키 접근 금지
            //jwCookie.setDomain("");
            //jwCookie.setSecure(true); // https only
            //jwCookie.setDomain("local.host.com"); // 해당 도메인 이외에는 쿠키 전송 X

            //refreshCookie.setMaxAge(60 * 60 * 24 * 14); // 쿠키 만료일 = 2주
            //refreshCookie.setHttpOnly(true);
            //refreshCookie.setDomain("localhost.com");
            //refreshCookie.setSecure(true);
            //refreshCookie.setDomain("localhost.com");


            // 토큰값을 일단 로컬 스토리지에 보관한다.
            response.addCookie(jwCookie); // 쿠키값을 세팅해줄 때, 세미콜로, 콤마, = , 공백을 포함할 수 없다.
            //response.addCookie(refreshCookie);


            //response.sendRedirect("/home");
            //return new ResponseEntity(new LoginToken("Bearer" + token), HttpStatus.OK);

            return "/home/ready";
        } catch (NullPointerException e) {
            //return new ResponseEntity<String>("fail login", HttpStatus.NOT_FOUND);
            return "/home/home";
        }
    }

    // login에 사용되는 함수
    // 자체 제작 인증함수, 다른 인터페이스에 내장되어 있는 함수가 아니다.
    private Authentication authenticate(String username, String password) throws Exception{
        try {
            Authentication authentication = jwtAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return authentication;
        } catch (DisabledException e) {
            System.out.println("USER_DISABLED");
            return null;
            //throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            System.out.println("INVALID_CREDENTIALS");
            return null;
            //throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    // 중복 가입 시 에러 페이지 출력
    @ExceptionHandler(DuplicateInsertException.class)
    public String handleDuplicateInsertException() {
        return "/members/duplicateInsertException";
    }
}
