package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.login.form.LoginForm;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;


    @GetMapping("/login")
    public String loginForm(@ModelAttribute LoginForm loginForm) {
        return "login/loginForm";
    }

    //@PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletResponse response) {
        if(bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "login/loginForm";
        }

        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if(loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 처리
        //쿠키에 시간정를 주지 않으면 세션 쿠키(브라우저 종료시 모두 종료)
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(idCookie);
        return "redirect:/";
    }

    //@PostMapping("/login")
    public String loginV2(@Validated @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletResponse response) {
        if(bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "login/loginForm";
        }

        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if(loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 처리
        //sessionManager를 통해 세션을 생성하고, 회원 데이터 보관.
        sessionManager.createSession(loginMember, response);
        return "redirect:/";
    }

    //@PostMapping("/login")
    public String loginV3(@Validated @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "login/loginForm";
        }

        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if(loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 처리
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        /* 세션의 create 옵션에 대해 알아보자.
            - request.getSession(true)
                1. 세션이 있으면 기존 세션을 반환한다.
                2. 세션이 없으면 새로운 세션을 생성해서 반환한다.
            - request.getSession(false)
                1. 세션이 있으면 기존 세션을 반환한다.
                2. 세션이 없으면 새로운 세션을 생성하지 않는다. null 을 반환한다.
        request.getSession() : 신규 세션을 생성하는 request.getSession(true) 와 동일하다 */
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        //SessionConst.LOGIN_MEMBER == "loginMember"가 key가 되고, value로는 loginMember를 session에 저장.
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/";
    }

    @PostMapping("/login")
    public String loginV4(@Validated @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request,
                          //redirectURL parameter가 있을시 해당 parameter로 redirect 함.
                          @RequestParam(defaultValue = "/") String redirectURL) {
        if(bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "login/loginForm";
        }

        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if(loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 처리
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        /* 세션의 create 옵션에 대해 알아보자.
            - request.getSession(true)
                1. 세션이 있으면 기존 세션을 반환한다.
                2. 세션이 없으면 새로운 세션을 생성해서 반환한다.
            - request.getSession(false)
                1. 세션이 있으면 기존 세션을 반환한다.
                2. 세션이 없으면 새로운 세션을 생성하지 않는다. null 을 반환한다.
        request.getSession() : 신규 세션을 생성하는 request.getSession(true) 와 동일하다 */
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        //SessionConst.LOGIN_MEMBER == "loginMember"가 key가 되고, value로는 loginMember를 session에 저장.
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:" + redirectURL;
    }

    //@PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        //쿠키만료처리 method 호출
        expireCookie(response, "memberId");
        return "redirect:/";
    }

    //@PostMapping("/logout")
    public String logoutV2(HttpServletRequest request) {
        //쿠키만료처리 method 호출
        //세션 store에서 삭제한다.
        sessionManager.expire(request);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            //세션 무효화, 삭제
            session.invalidate();
        }

        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        //쿠키를 지우는 방법은 time을 초기화하면 된다.
        Cookie cookie = new Cookie(cookieName, null);
        //타임 초기화
        cookie.setMaxAge(0);
        //초기화된 쿠키 response에 등록
        response.addCookie(cookie);
    }
}
