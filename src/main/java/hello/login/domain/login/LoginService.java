package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * loginId를 key로 회원을 조회하고, Parameter의 password와 조회된 회원의 password가 같으면 성공.
     * @return null이면 로그인 실패.
     */
    public Member login(String loginId, String password) {
/*        Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);

        Member member = findMemberOptional.get();
        if(member.getPassword().equals(password)) {
            return member;
        } else {
            return null;
        }*/

        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);

    }
}
