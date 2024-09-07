package com.study.vue.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.sql.DataSource;

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
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
        @Resource // 注入DataSource
        DataSource dataSource;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http, PersistentTokenRepository repository)
                        throws Exception {

                return http
                                .authorizeHttpRequests(authz -> authz
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginProcessingUrl("/api/auth/login")
                                                .successHandler(this::onAuthenticationSuccess)// 登录成功

                                                .failureHandler(this::onAuthenticationFailure))

                                .logout(logout -> logout
                                                .logoutUrl("/api/auth/logout")
                                                .logoutSuccessHandler(this::onAuthenticationSuccess))
                                .rememberMe(me -> me
                                                .rememberMeParameter("remember")
                                                .tokenRepository(repository)
                                                .tokenValiditySeconds(3600 * 24 * 7))
                                .csrf(csrf -> csrf.disable())
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .exceptionHandling(exp -> exp.authenticationEntryPoint(this::onAuthenticationFailure))
                                .build();
        }

        @Bean
        public PersistentTokenRepository tokenRepository() {
                JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
                jdbcTokenRepository.setDataSource(dataSource);// 自定义数据源
                jdbcTokenRepository.setCreateTableOnStartup(false);// 自动建表
                return jdbcTokenRepository;
        }

        private CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration cors = new CorsConfiguration();
                cors.setAllowCredentials(true); // 允许cookies跨域
                cors.addAllowedOriginPattern("*"); // 允许向该服务器提交请求的URI,*表示全部允许
                cors.addAllowedHeader("*");// 允许访问的头信息,*表示全部
                cors.addExposedHeader("*");// 指定客户端能够获取的头信息
                // cors.addAllowedMethod("OPTIONS");// 允许提交请求的方法
                // cors.addAllowedMethod("HEAD"); // 允许Head的请求方法
                // cors.addAllowedMethod("GET");// 允许Get的请求方法
                // cors.addAllowedMethod("PUT");// 允许Put的请求方法
                // cors.addAllowedMethod("POST");// 允许Post的请求方法
                // cors.addAllowedMethod("DELETE");// 允许Delete的请求方法
                // cors.addAllowedMethod("PATCH");// 允许Patch的请求方法
                cors.addAllowedMethod("*");
                cors.setMaxAge(3600L); // 预检请求的缓存时间(秒),即在这个时间段里，对于相同的跨域请求，会从这里获取头部信息
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", cors);
                return source;
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
                if (request.getRequestURI().endsWith("/login")) {
                        response.getWriter().write(JSONObject.toJSONString(RestBean.success("登录成功")));
                } else if (request.getRequestURI().endsWith("/logout")) {
                        response.getWriter().write(JSONObject.toJSONString(RestBean.success("登出成功")));
                }

        }

        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException exception) throws IOException, ServletException {
                // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                // response.setCharacterEncoding("utf-8");
                response.getWriter().write(JSONObject.toJSONString(RestBean.failure(401, exception.getMessage())));

        }
}
