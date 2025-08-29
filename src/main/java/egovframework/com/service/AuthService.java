package egovframework.com.service;

import egovframework.com.domain.LoginRequestDto;

public interface AuthService {
    String login(LoginRequestDto loginRequestDto);
}
