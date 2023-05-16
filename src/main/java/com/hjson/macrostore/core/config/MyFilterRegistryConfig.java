package com.hjson.macrostore.core.config;


import com.hjson.macrostore.core.filter.MyTempFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.FilterRegistration;

@Configuration
public class MyFilterRegistryConfig {
    @Bean
    public FilterRegistrationBean<?> filter1(){
        FilterRegistrationBean<MyTempFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new MyTempFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }
}
