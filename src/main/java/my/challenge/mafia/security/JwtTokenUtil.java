package my.challenge.mafia.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    //@Value("${jwt.validTime}")
    private long validTime = 60*60*1000; // 1시간 - 1분

    private long refreshValidTime = 60*60*24*14; // 2주

    // JWT 중 payload에서 sub(username)을 가져온다.
    public String getUsernameFromToken(String token) {
        // Claim클래스의 getSubject함수를 가져온다.
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Function<Claims, T> claimsResolver = Claims 클래스의 getSubject() 메소드
    // Function<T, R> 는 T는 매개변수 타입, R은 리턴 타입으로 제네릭하게 함수를 사용가능
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims); // 매개변수로 전달받은 메소드의 리턴값을 리턴한다.
    }

    // JWT 중에서 payload부분을 가져온다.
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    // JWT 생성
    public String generateToken(Authentication authentication) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, (String)authentication.getPrincipal());
    }

    // JWT 생성 함수
    public String doGenerateToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validTime))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    // refresh 토큰 생성
    public String generateRefreshToken(Authentication authentication) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateRefreshToken(claims, (String) authentication.getPrincipal());
    }

    // refresh 토큰 생성 함수
    public String doGenerateRefreshToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshValidTime))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    // 토큰이 유효한지 검사, 전달받은 토큰의 이름이 DB에 저장된 이름과 같은지, 토큰의 유효시간이 남았는지
    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // 토큰 유효 시간 확인
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date()); // 전달된 date객체의 날짜가 현재 날짜 이후면 true
    }

    // 토큰의 만료시간 가져오기
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
}
