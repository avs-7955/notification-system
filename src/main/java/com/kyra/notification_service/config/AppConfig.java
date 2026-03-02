package com.kyra.notification_service.config;

import com.kyra.notification_service.core.correlation.CorrelationIdFilter;
import com.kyra.notification_service.core.timing.RequestTimingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public FilterRegistrationBean<CorrelationIdFilter> correlationIdFilterRegistration() {
        FilterRegistrationBean<CorrelationIdFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CorrelationIdFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<RequestTimingFilter> requestTimingFilterRegistration() {
        FilterRegistrationBean<RequestTimingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestTimingFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
