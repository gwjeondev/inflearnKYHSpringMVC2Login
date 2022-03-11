package hello.login;

import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    //interceptor 등록
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //필터와 비교해보면 인터셉터는 addPathPatterns, excludePathPatterns로 매우 정밀하게 URL Pattern을 지정 할 수 있다.
        //Spring에서 제공하는 PathPattern 공식 문서: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/util/pattern/PathPattern.html
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**") //서블릿 filter와 pattern이 조금 다르다.
                .excludePathPatterns("/css/**", "/*.ico", "/error"); //예외 path pattern
    }

    //filter 등록
    //@Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter()); //filter 추가
        filterRegistrationBean.setOrder(1); //filter chain 순서 지정
        filterRegistrationBean.addUrlPatterns("/*"); //filter 처리 할 url patterns

        return filterRegistrationBean;
    }

    //filter 등록
    //@Bean
    public FilterRegistrationBean loginCheckFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter()); //filter 추가
        filterRegistrationBean.setOrder(2); //filter chain 순서 지정
        filterRegistrationBean.addUrlPatterns("/*"); //filter 처리 할 url patterns

        return filterRegistrationBean;
    }

}
