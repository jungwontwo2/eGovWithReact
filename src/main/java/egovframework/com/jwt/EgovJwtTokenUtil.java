package egovframework.com.jwt;

import egovframework.com.cmm.LoginVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class EgovJwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -5180902194184255251L;
    public static final long JWT_TOKEN_VALIDITY = (long) ((1 * 60 * 60) / 60) * 60; // 유효시간 60분

    // 1. globals.properties에서 문자열 비밀키를 읽어옵니다.
    @Value("${Globals.jwt.secret}")
    private String secretString;

    // 2. 암호화/복호화에 실제로 사용될 Key 객체를 선언합니다.
    private Key key;

    // 3. @PostConstruct: 의존성 주입이 완료된 후, 서버가 시작될 때 딱 한 번 실행되는 초기화 메소드입니다.
    @PostConstruct
    public void init() {
        // 읽어온 문자열 비밀키(secretString)를 UTF-8 바이트 배열로 변환한 뒤,
        // HMAC-SHA 알고리즘에 맞는 안전한 Key 객체로 생성하여 key 필드에 저장합니다.
        // 이 과정을 통해 WeakKeyException과 Base64 인코딩 오류를 모두 해결합니다.
        this.key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }

    // 토큰에서 사용자 아이디 추출 (템플릿 기존 방식 유지)
    public String getUserIdFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("id").toString();
    }

    // 토큰에서 사용자 구분(userSe) 추출
    public String getUserSeFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("userSe").toString();
    }

    // (템플릿 기존 메소드들)
    public String getInfoFromToken(String type, String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get(type).toString();
    }

    public Claims getClaimFromToken(String token) {
        return getAllClaimsFromToken(token);
    }

    // 토큰의 모든 정보를 추출 (해독)
    public Claims getAllClaimsFromToken(String token) {
        // 해독할 때도 초기화 시 만들어 둔 안전한 key 객체를 사용합니다.
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 생성 진입점
    public String generateToken(LoginVO loginVO) {
        return doGenerateToken(loginVO, loginVO.getId());
    }

    // 실제 토큰 생성 로직
    private String doGenerateToken(LoginVO loginVO, String subject) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", loginVO.getId());
        claims.put("name", loginVO.getName());
        claims.put("role", loginVO.getRole());

        // 불필요한 Base64 인코딩 로직을 모두 제거하고, 깔끔하게 정리합니다.
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                // 서명할 때도 초기화 시 만들어 둔 안전한 key 객체를 사용합니다.
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}