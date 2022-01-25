package hello.login.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    private MemberRepository memberRepository;

    @BeforeEach
    void beforEach() {
        memberRepository = new MemberRepository();
    }
    @Test
    void save() {
        Member member = new Member("it1639", "JEON", "123");
        memberRepository.save(member);
        Optional<Member> m = memberRepository.findByLoginId("it1639");
        Assertions.assertThat(member.getName()).isEqualTo(m.get().getName());
    }

    @Test
    void save2() {
        Optional<Member> m = memberRepository.findByLoginId("it1639");
        System.out.println(m);
        Assertions.assertThat("JEON").isEqualTo(m.get().getName());
    }

    @Test
    void findById() {
    }

    @Test
    void findByLoginId() {
    }

    @Test
    void findAll() {
    }
}