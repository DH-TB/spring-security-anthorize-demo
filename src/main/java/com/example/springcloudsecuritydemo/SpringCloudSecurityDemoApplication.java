package com.example.springcloudsecuritydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@SpringBootApplication
@RestController

public class SpringCloudSecurityDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudSecurityDemoApplication.class, args);
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

    @RequestMapping("/context")
    public Authentication context() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }

    @Component
    @EnableOAuth2Sso // 基于OAuth2的单点登录
    public static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.
                    antMatcher("/**")
                    // 所有请求都得经过认证和授权
                    .authorizeRequests().anyRequest().authenticated()
                    .and().authorizeRequests().antMatchers("/", "/anon").permitAll()
                    .and()
                    .csrf().disable()
                    // 退出的URL是/logout
                    .logout().logoutUrl("/logout").permitAll()
                    // 退出成功后，跳转到/路径。
                    .logoutSuccessUrl("/");
        }
    }
}
