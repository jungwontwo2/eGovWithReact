package egovframework.com.service.impl;

import egovframework.com.domain.LoginRequestDto;
import egovframework.com.domain.Member;
import egovframework.com.jwt.JwtTokenProvider;
import egovframework.com.mapper.MemberMapper;
import egovframework.com.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String login(LoginRequestDto loginRequestDto) {
        Member member = memberMapper.findByMemberId(loginRequestDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디입니다."));

        System.out.println("==================== [비밀번호 검증 시작] ====================");
        System.out.println("사용된 PasswordEncoder 구현체: " + passwordEncoder.getClass().getName());
        System.out.println("사용자 입력 비밀번호: " + loginRequestDto.getPassword());
        System.out.println("DB에 저장된 비밀번호: " + member.getPassword());
        boolean isPasswordMatch = passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword());
        System.out.println("비밀번호 일치 여부: " + isPasswordMatch);
        System.out.println("==================== [비밀번호 검증 종료] ====================");


        if(!passwordEncoder.matches(loginRequestDto.getPassword(),member.getPassword())){
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        String s = jwtTokenProvider.generateToken(member);
        return s;
    }
}
