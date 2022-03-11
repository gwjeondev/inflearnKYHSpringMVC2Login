package hello.login.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";

    //컨트롤러(핸들러) 수행 전
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        request.setAttribute(LOG_ID, uuid);

        //일반적으로 스프링을 사용할 때 @Controller, @RequestMapping를 통해 핸들러 매핑시: HandlerMethod
        //static 정적 리소스: ResourceHttpRequestHandler
        if(handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler; //호출할 컨트롤러 메서드의 모든 정보가 포함되어 있다.
            //hm.getBean()=[hello.login.web.HomeController@14d6cf58]
            log.info("hm.getBean()=[{}]", hm.getBean());
            //hm.getMethod()=[public java.lang.String hello.login.web.HomeController.homeLoginV4(hello.login.domain.member.Member,org.springframework.ui.Model)]
            log.info("hm.getMethod()=[{}]", hm.getMethod());
        }

        //REQUEST=[9321236a-ef31-4b30-b319-16f48e3f8e8b][/][hello.login.web.HomeController#homeLoginV4(Member, Model)]
        log.info("REQUEST=[{}][{}][{}]", uuid, requestURI, handler);

        return true;
    }

    //컨트롤러(핸들러) 수행 후
    //Exception 발생시 postHandle은 호출되지 않는다.
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //postHandle=[ModelAndView [view="home"; model={}]]
        log.info("postHandle=[{}]", modelAndView);
    }

    //render(model)를 반환 후
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = (String)request.getAttribute(LOG_ID);

        //RESPONSE=[9321236a-ef31-4b30-b319-16f48e3f8e8b][/][hello.login.web.HomeController#homeLoginV4(Member, Model)]
        log.info("RESPONSE=[{}][{}][{}]", uuid, requestURI, handler);

        if(ex != null) {
            //error는 {}이 필요없다.
            log.error("afterCompletion error=", ex);
        }
    }
}
