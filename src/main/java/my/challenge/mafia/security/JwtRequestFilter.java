package my.challenge.mafia.security;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/*
 * 권한을 확인하는 필터
 * 유저의 모든 리소스 요청은 우선 해당 필터에서 권한을 확인한다.
 *
 * */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //String requestTokenHeader = request.getHeader("Cookie");

        String username = null;
        String jwtToken = null;
        String refreshToken = null;

        String jwCookie = null;
        String refreshCookie = null;


        Cookie[] cookies = request.getCookies();


        // 쿠키가 있다면 가져온다.
        if (cookies != null) {
            System.out.println("jwt : "+cookies[0].getValue());
            //System.out.println("refresh : "+cookies[1].getValue());
            jwCookie = cookies[0].getValue();
            //refreshCookie = cookies[1].getValue();
        }

        // 토큰의 시작 부가 Bearer인지 확인
        if (jwCookie != null && jwCookie.startsWith("Bearer")) { //

            jwtToken = jwCookie.substring(6);
            //refreshToken = refreshCookie.substring(6);


            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token" + "\n" + e);

            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired" + "\n" + e);

            }
        } else {
            logger.warn("JWT Token doesn't begin with Bearer String");
        }


        // 토큰에서 성공적으로 이름을 가져왔는데 SecurityContextHolder에는 저장되어 있지 않을 때
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // DB에서 username에 해당하는 계정 데이터를 가져온다.

            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);


            // 토큰이 유효하다면 인증이 완료된 UsernamePasswordAuthenticationToken 객체 생성
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // 잘 모르겠음, 상세 정보를 세팅
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // SecurityContextHolder에 등록해준다.
                // 등록이 되어야 인가(Authorize)할 때 로그인 성공을 할 수 있다.
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }
        }
        // 다음 수행되어야 할 필터를 호출
        filterChain.doFilter(request, response);
    }

}
