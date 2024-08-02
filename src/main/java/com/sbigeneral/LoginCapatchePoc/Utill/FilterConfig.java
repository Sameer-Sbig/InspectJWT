package com.sbigeneral.LoginCapatchePoc.Utill;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
	@Bean
    public FilterRegistrationBean<XFrameOptionsHeaderFilter> loggingFilter() {
        FilterRegistrationBean<XFrameOptionsHeaderFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new XFrameOptionsHeaderFilter());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }

}
