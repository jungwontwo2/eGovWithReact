package egovframework.com.domain;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String memberId;
    private String password;
}
