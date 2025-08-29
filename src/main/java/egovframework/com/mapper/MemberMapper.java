package egovframework.com.mapper;

import egovframework.com.domain.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {
    Optional<Member> findByMemberId(String memberId);
}
