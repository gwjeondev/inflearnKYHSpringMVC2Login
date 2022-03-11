package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentresolver.Login;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    //@GetMapping("/")
    public String home() {
        return "home";
    }

    //@GetMapping("/")
    public String homeLogin(@CookieValue(name ="memberId", required = false) Long memberId, Model model) {

        if(memberId == null) {
            return "home";
        }

        //로그인시 처리
        Member loginMember = memberRepository.findById(memberId);
        if(memberId == null) {
            return "home";
        }

        //로그인 성공
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    //@GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {
        //sessionManager에 저장된 회원 정보 조회
        Member sessionMember = (Member)sessionManager.getSession(request);

        if(sessionMember == null) {
            return "home";
        }

        //로그인 성공
        model.addAttribute("member", sessionMember);
        return "loginHome";
    }

    //@GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);
        if(session == null) {
            return "home";
        }

        //등록된 session중 SessionConst.LOGIN_MEMBER == "loginMember"를 키로 가지는 value를 가져옴
        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);

        //세션에 회원 데이터가 없으면 home
        if(loginMember == null) {
            return "home";
        }

        //세션이 유지되면 로그인 성공
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    /*
        @SessionAttribute Annotation을 활용한 session 처리

            HttpSession session = request.getSession(false);
            if(session == null) {
                return "home";
            }

            Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
            의 기능을 모두 수행해준다.

            순서를 정리 해보자면,
            1. request.getSession(false)를 통해 session을 가져온다. --> required = false
            2. SessionConst.LOGIN_MEMBER == "loginMember"를 key로 가지는 session value를 가져온다. --> name=SessionConst.LOGIN_MEMBER
            3. Member 타입으로 casting 되어 loginMember 변수에 저장된다. --> Member loginMember
            4. 만약 session에 값이 없을 경우 null이 return 된다.
     */
    //@GetMapping("/")
    public String homeLoginV4(@SessionAttribute(name=SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {

        //세션에 회원 데이터가 없으면 home
        if(loginMember == null) {
            return "home";
        }

        //세션이 유지되면 로그인 성공
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @GetMapping("/")
    public String homeLoginV5(@Login Member loginMember, Model model) {

        //세션에 회원 데이터가 없으면 home
        if(loginMember == null) {
            return "home";
        }

        //세션이 유지되면 로그인 성공
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}