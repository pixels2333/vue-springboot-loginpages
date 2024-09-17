package com.study.vue.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.vue.entity.RestBean;
import com.study.vue.service.AuthorizeService;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {
    @Resource
    AuthorizeService service;

    @PostMapping("/validate-email")
    public RestBean<String> validateEmail(@NotBlank @Email @RequestParam("email") String email) {
        if (service.sendValidateEmail(email))
            return RestBean.success("发送邮件成功");
        else
            return RestBean.failure(400, "发送邮件失败");
    }
}
