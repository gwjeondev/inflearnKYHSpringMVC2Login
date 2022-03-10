package hello.login;

import hello.login.web.filter.LogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter()); //filter 추가
        filterRegistrationBean.setOrder(1); //filter chain 순서 지정
        filterRegistrationBean.addUrlPatterns("/*"); //filter 처리 할 url patterns

        return filterRegistrationBean;
    }
}
