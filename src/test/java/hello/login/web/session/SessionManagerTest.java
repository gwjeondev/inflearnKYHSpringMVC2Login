package hello.login.web.session;

import hello.login.domain.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.*;


class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    void sessionTest() {
        //세션 생성(server)
        MockHttpServletResponse response = new MockHttpServletResponse();
        Member member = new Member("test", "테스터", "test1");
        sessionManager.createSession(member, response);

        //요청에 응답 쿠키 저장(client)
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());
        //세션 조회
        Object result = sessionManager.getSession(request);
        assertThat(result).isSameAs(member);

        //세션 만료
        sessionManager.expire(request);
        //세션 조회
        Object result1 = sessionManager.getSession(request);
        assertThat(result1).isSameAs(null);
    }
}