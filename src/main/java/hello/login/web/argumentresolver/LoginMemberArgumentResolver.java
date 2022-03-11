package hello.login.web.argumentresolver;

import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");

        //넘어온 parameter에 Login annotation이 있는지 확인
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        //넘어온 parameter Type이 Member인지 확인
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());

        //true이면 resolveArgument 실행, false이면 실행 안됨
        return hasLoginAnnotation && hasMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("resolveArgument 실행");

        HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();

        HttpSession session = request.getSession(false);
        if(session == null) {
            return null; //parameter에 null을 반환
        }

        return session.getAttribute(SessionConst.LOGIN_MEMBER);
    }
}
