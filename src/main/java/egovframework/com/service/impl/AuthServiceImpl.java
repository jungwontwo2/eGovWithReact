package egovframework.com.service.impl;

import egovframework.com.domain.LoginRequestDto;
import egovframework.com.domain.Member;
import egovframework.com.jwt.JwtTokenProvider;
import egovframework.com.mapper.MemberMapper;
import egovframework.com.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public String login(LoginRequestDto loginRequestDto) {
        Member member = memberMapper.findByMemberId(loginRequestDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디입니다."));

        if (!loginRequestDto.getPassword().equals(member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        String s = jwtTokenProvider.generateToken(member);
        System.out.println(s);
        return s;
    }
}
