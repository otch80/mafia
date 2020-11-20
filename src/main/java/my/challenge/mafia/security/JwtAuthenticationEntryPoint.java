package my.challenge.mafia.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


// 인증에 실패한 사용자의 response에 401상태코드(권한 X)를 담아 전송한다.
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        System.out.println(authException);
        System.out.println("authorization fail");
        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UnAuthorized");
        Cookie[] cookies = request.getCookies(); // 모든 쿠키의 정보를 cookies에 저장

        // 잘못된 접근을 한 것이므로
        // 모든 쿠키 삭제
        if(cookies != null){ // 쿠키가 한개라도 있으면 실행
            for(int i=0; i< cookies.length; i++){

                cookies[i].setMaxAge(0); // 유효시간을 0으로 설정
                response.addCookie(cookies[i]); // 응답 헤더에 추가
            }
        }
        response.sendRedirect("/wrong");
    }
}
