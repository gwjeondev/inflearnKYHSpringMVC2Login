package hello.login.web.filter;

import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@Component //loginCheckFilter를 Spring Bean으로 등록하기(@Autowired 의존관계 주입을 받기 위해서) 등록법은 WebConfig 참고
public class LoginCheckFilter implements Filter {

    private static final String[] whitelist = {"/", "/members/add", "/login", "/logout", "/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            log.info("인증 체크 필터 시작=[{}]", requestURI);

            //requestURI가 login을 체크하는 path일 경우
            if(isLoginCheckPath(requestURI)) {
                log.info("인증체크 로직 실행=[{}]", requestURI);

                HttpSession session = httpRequest.getSession(false);

                if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
                    log.info("미인증 사용자 요청=[{}]", requestURI);
                    //로그인으로 redirect parameter에 redirectURL을 넣어주는 이유는 로그인에 성공했을때 해당 page로 보내기 위함.
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
                    return;
                }
            }
            chain.doFilter(request, response);
        }
        catch (Exception e) {
            throw e; //예외 로깅 가능 하지만, 톰캣까지 예외를 보내주어야 함. 만약 톰캣까지 보내지 않을 경우 정상흐름처럼 동작함.
        }
        finally {
            log.info("인증 체크 필터 종료=[{}]", requestURI);
        }
    }

    /**
     * 화이트 리스트의 경우 인증 체크X
     */

    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }

}
