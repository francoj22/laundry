package com.microservice.laundry;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.Servlet;

@Configuration
@ConditionalOnClass(name = "org.h2.server.web.JakartaWebServlet")
@ConditionalOnProperty(prefix = "spring.h2.console", name = "enabled", havingValue = "true")
public class H2ConsoleConfig {

    @Bean
    public ServletRegistrationBean<Servlet> h2ConsoleServletRegistration() {
        Servlet servlet;
        try {
            // Resolve the H2 servlet at runtime so this config does not require a compile-time H2 type.
            Class<?> servletClass = Class.forName("org.h2.server.web.JakartaWebServlet");
            servlet = (Servlet) servletClass.getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to initialize H2 console servlet", ex);
        }

        ServletRegistrationBean<Servlet> registration =
                new ServletRegistrationBean<>(servlet, "/h2-console/*");
        registration.addInitParameter("webAllowOthers", "true");
        registration.addInitParameter("trace", "false");
        return registration;
    }
}
