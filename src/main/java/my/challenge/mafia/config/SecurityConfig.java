package my.challenge.mafia.config;


import my.challenge.mafia.security.JwtAuthenticationEntryPoint;
import my.challenge.mafia.security.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // test용 입니다
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().antMatchers(  "/home", "/members/login", "/members/signupForm", "/members/signup", "/wrong", "/", "/favicon.ico","/day","/api/hello", "/css/*", "/js/*", "/img/*", "/login", "/test").permitAll()
                .antMatchers("/admin").hasRole("ADMIN") // user는 admin에 접속 불가, admin도 user에 접속 불가
                //.anyRequest().hasRole("USER")
                .anyRequest().permitAll()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .logout()
                .logoutUrl("/logout")
                .deleteCookies("JW-TOKEN")
                .deleteCookies("REFRESH-TOKEN")
                .logoutSuccessUrl("/");


        // 클라이언트가 요청한 리소스에 권한이 없을 경우 UsernamePasswordAuthenticationFilter가 기본 로그인 페이지로 이동시킨다. 때문에 그 앞에 커스텀 릴터를 등록한다.
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
