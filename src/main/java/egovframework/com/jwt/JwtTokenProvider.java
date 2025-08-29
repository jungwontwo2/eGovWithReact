package egovframework.com.jwt;

import egovframework.com.domain.Member;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    @Value("${Globals.jwt.secret}")
    private String secretKey;

    private static final long JWT_TOKEN_VALIDITY = 30 * 60 * 1000;

    public String generateToken(Member member){
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberName",member.getMemberId());
        claims.put("role",member.getRole());
        return doGenerateToken(claims, member.getMemberId());
    }

    private String doGenerateToken(Map<String,Object> claims, String subject){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
}
