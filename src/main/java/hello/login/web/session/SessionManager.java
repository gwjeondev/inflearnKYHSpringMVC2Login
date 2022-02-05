package hello.login.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 세션 관리
 */
@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "mySessionId";
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    /**
     * 세션 생성
     */
    public void createSession(Object value, HttpServletResponse response) {
        //세션 id를 생성하고, 값을 세션에 저장
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);

        //쿠키 생성
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(mySessionCookie);
    }

    /**
     * 세션 조회
     */
    public Object getSession(HttpServletRequest request) {
        //request로 부터 전달받은 cookie의 name이 session store에서 관리하는 cookie 이름인지 확인.
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);

        //session store에 존재하지 않는 cookie name일 경우 null을 리턴.
        if(sessionCookie == null) {
            return null;
        }

        //session store에 존재하는 쿠키일 경우 저장된 session을 get.
        return sessionStore.get(sessionCookie.getValue());
    }

    /**
     * 세션 만료
     */
    public void expire(HttpServletRequest request) {
        //request로 부터 전달받은 cookie의 name이 session store에서 관리하는 cookie 이름인지 확인.
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);

        //sessionCookie가 존재할 경우 session store에서 삭제.
        if(sessionCookie != null) {
            sessionStore.remove(sessionCookie.getValue());
        }
    }

    private Cookie findCookie(HttpServletRequest request, String cookieName) {
        //request 요청에 cookie가 없을 경우 null을 리턴.
        if(request.getCookies() == null) {
            return null;
        }

        //request 요청에 cookie의 getName중 parameter cookieName과 일치하는 것이 있다면 리턴. 없다면 null을 리턴.
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }
}
