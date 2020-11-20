package my.challenge.mafia.security;

import my.challenge.mafia.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;



/*
실제 인증이 수행된다.
 */
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private Repository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();


        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authentication.getName());

        try {
            // 패스워드 검증
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                // equals 를 사용하려고 할 경우 계속해서 값이 다르게 나온다. 때문에 passwordEncoder내부의 mathches함수를 사용할 것
                throw new RuntimeException("Unauthorized");
            }

            // 인증이 완료된 토큰을 반환
            return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        } catch (NullPointerException e) {
            System.out.println("NULL pointer 에러" + e);
            // 인증실패 시 null반환
            return null;
        } catch (RuntimeException e) {
            // 패스워드 검증 에러
            System.out.println("RuntimeException" + e);
            return null;
        } catch (Exception e) {
            System.out.println("general error" + e);
            return null;
        }


    }


    // 뭐하는 놈이여 ㅅㅂ
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
