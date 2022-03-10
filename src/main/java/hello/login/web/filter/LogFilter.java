package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter");

        //ServletRequest는 HttpServletRequest의 부모로, 사용 가능한 기능이 제한적이므로 HttpServletRequest로 down cast 한다.
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        String requestURI = httpRequest.getRequestURI();

        String uuid = UUID.randomUUID().toString();

        try {
            log.info("REQUEST [{}][{}]", uuid, requestURI);
            //chain.doFilter를 통해 다음 Filter를 호출하고, 다음 Filter가 없을 경우 Servlet을 호출한다.
            chain.doFilter(request, response);
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            //Servlet의 작업(모든 작업)이 마무리 되면 finally부분이 실행된다.
            //HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 컨트롤러 -> 서블릿 -> 필터 -> WAS -> HTTP 응답
            log.info("RESPONSE [{}][{}]", uuid, requestURI);
        }

    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
