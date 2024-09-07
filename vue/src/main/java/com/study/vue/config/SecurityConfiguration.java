package com.study.vue.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.alibaba.fastjson2.JSONObject;
import com.study.vue.entity.RestBean;
import com.study.vue.service.AuthorizeService;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
        @Resource
        AuthorizeService authorizeService;// 配置UserDetailsService,用于自定义登录,注入到SecurityFilterChain,并且自定义登录成功和失败处理

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                return http
                                .authorizeHttpRequests(authz -> authz
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginProcessingUrl("/api/auth/login")
                                                .successHandler(this::onAuthenticationSuccess)// 登录成功

                                                .failureHandler(this::onAuthenticationFailure))

                                .logout(logout -> logout
                                                .logoutUrl("/api/auth/logout"))
                                .csrf(csrf -> csrf.disable())
                                .exceptionHandling(exp -> exp.authenticationEntryPoint(this::onAuthenticationFailure))
                                .build();

        }

        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity security) throws Exception {

                AuthenticationManagerBuilder authenticationManagerBuilder = security
                                .getSharedObject(AuthenticationManagerBuilder.class);// 获取AuthenticationManagerBuilder
                authenticationManagerBuilder.userDetailsService(authorizeService); // 配置UserDetailsService
                return authenticationManagerBuilder.build(); // 调用build()方法构建AuthenticationManager
        }

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                        Authentication authentication) throws IOException, ServletException {

                // response.setStatus(HttpServletResponse.SC_OK);
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                // response.setCharacterEncoding("utf-8");
                response.getWriter().write(JSONObject.toJSONString(RestBean.success("登录成功")));

        }

        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException exception) throws IOException, ServletException {
                // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                // response.setCharacterEncoding("utf-8");
                response.getWriter().write(JSONObject.toJSONString(RestBean.failure(401, exception.getMessage())));

        }
}
