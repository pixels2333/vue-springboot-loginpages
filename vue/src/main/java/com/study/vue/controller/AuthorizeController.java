package com.study.vue.controller;

import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.vue.entity.RestBean;
import com.study.vue.service.AuthorizeService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {

    @Resource
    AuthorizeService service;

    @PostMapping("/validate-email")
    public RestBean<String> validateEmail(@NotBlank @Email @RequestParam("email") String email, HttpSession session) {
        if (service.sendValidateEmail(email, session.getId()) == null)
            return RestBean.success("发送邮件成功");
        else
            return RestBean.failure(400, "发送邮件失败");

    }

    @PostMapping("/register")
    public RestBean<String> registerUser(
            @RequestParam("username") @Length(min = 2, max = 8) @Pattern(regexp = "^[a-zA-Z0-9\u4e00-\u9fa5]+$") String username,
            @RequestParam("password") @Length(min = 6, max = 8) String password,
            @RequestParam("email") @Email String email,
            @RequestParam("code") @Length(min = 6, max = 6) String code,
            HttpSession session) {
        if (service.validateAndRegister(username, password, email, code, session.getId())==null)
            return RestBean.success("注册成功");

        else
            return RestBean.failure(400, "注册失败");

    }

}
