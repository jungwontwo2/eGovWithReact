package egovframework.com.domain;

import lombok.Data;

@Data
public class Member {
    private String memberId;
    private String password;
    private String memberName;
    private String role;
}
