package egovframework.com.jwt;

import egovframework.com.cmm.LoginVO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * JWT 토큰을 모든 요청에 대해 검사하는 필터 (입장 게이트).
 * Spring Security 필터 체인에 추가되어, 지정된 엔드포인트에 접근하기 전에 실행됩니다.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private EgovJwtTokenUtil jwtTokenUtil;
    public static final String HEADER_STRING = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        // 1. request header에서 "Authorization" 헤더 값을 가져옵니다.
        final String requestTokenHeader = req.getHeader(HEADER_STRING);

        String id = null;
        String jwtToken = null;

        // 2. 헤더가 존재하고, "Bearer "로 시작하는지 확인합니다.
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            // 3. "Bearer " 7글자를 제외하고, 앞뒤 공백을 모두 제거(trim)하여 순수한 토큰만 추출합니다.
            jwtToken = requestTokenHeader.substring(7).trim();
            try {
                // 4. 추출된 순수 토큰에서 사용자 ID를 가져옵니다.
                id = jwtTokenUtil.getUserIdFromToken(jwtToken);
            } catch (IllegalArgumentException | ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | SignatureException e) {
                logger.error("JWT Token parsing error: " + e.getMessage());
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String or is missing.");
        }

        // 5. 사용자 ID가 성공적으로 추출되었고, 아직 Security Context에 인증 정보가 없는 경우에만 인증 절차를 진행합니다.
        if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6. 토큰에서 추출한 정보로 LoginVO 객체를 생성합니다.
            LoginVO loginVO = new LoginVO();
            loginVO.setId(id);
            loginVO.setName(jwtTokenUtil.getInfoFromToken("name", jwtToken));
            loginVO.setRole(jwtTokenUtil.getInfoFromToken("role", jwtToken));
            // 7. LoginVO의 권한 정보(role)를 기반으로 Spring Security용 인증 토큰을 생성합니다.
            UsernamePasswordAuthenticationToken authentication;
            if ("ROLE_ADMIN".equals(loginVO.getRole())) {
                authentication = new UsernamePasswordAuthenticationToken(loginVO, null,
                        Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
            } else {
                authentication = new UsernamePasswordAuthenticationToken(loginVO, null,
                        Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            }

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

            // 8. Spring Security의 '보안 컨텍스트'에 인증 정보를 저장합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 9. 다음 필터로 요청을 전달합니다.
        chain.doFilter(req, res);
    }
}