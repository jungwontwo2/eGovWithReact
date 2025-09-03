package egovframework.com.jwt;

import egovframework.com.cmm.LoginVO; // LoginVO를 사용하신다면 이 경로가 맞는지 확인해주세요.
import egovframework.com.domain.Member; // Member를 사용하신다면 이 경로가 맞는지 확인해주세요.
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
// 클래스 이름은 사용자님의 프로젝트에 맞게 JwtTokenProvider 또는 EgovJwtTokenUtil로 사용하세요.
public class JwtTokenProvider implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;
    public static final long JWT_TOKEN_VALIDITY = 30 * 60 * 1000; // 30분

    @Value("${Globals.jwt.secret}")
    private String secretString;

    private Key key;

    @PostConstruct
    public void init() {
        // 프로퍼티에서 읽어온 문자열 비밀키를 HMAC-SHA 알고리즘에 맞는 안전한 Key 객체로 변환합니다.
        // 이 과정을 거치면 Base64, WeakKeyException 등의 오류가 모두 해결됩니다.
        this.key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        // 토큰을 해독할 때도 문자열이 아닌 Key 객체를 사용합니다.
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // generateToken 메소드의 파라미터 타입을 Member 또는 LoginVO 중
    // 사용자님의 AuthServiceImpl에서 사용하는 타입으로 맞춰주세요.
    public String generateToken(Member member) { // 또는 public String generateToken(LoginVO loginVO)
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", member.getMemberId());
        claims.put("name", member.getMemberName()); // 또는 loginVO.getName()
        claims.put("role", member.getRole());           // 또는 loginVO.get...()
        return doGenerateToken(claims, member.getMemberId()); // 또는 loginVO.getId()
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                // ▼▼▼▼▼ 핵심 수정 부분 ▼▼▼▼▼
                // 문자열(secretKey)이 아닌 Key 객체(key)를 사용합니다.
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}