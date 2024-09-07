package com.study.vue.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.study.vue.entity.Account;
import com.study.vue.mapper.UserMapper;

import jakarta.annotation.Resource;

@Service
public class AuthorizeService implements UserDetailsService {

    @Resource
    UserMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ((!StringUtils.hasLength(username) || username.trim().isEmpty()))
            throw new UsernameNotFoundException("用户名不能为空");
        Account account = mapper.findAccountByNameOrEmail(username);
        if (account == null)
            throw new UsernameNotFoundException("用户不存在");
        // if(!StringUtils.hasLength(account.getUsername()))
        // throw new UsernameNotFoundException("用户名不存在");
        // if(!StringUtils.hasLength(account.getPassword()))
        // throw new UsernameNotFoundException("密码不存在");
        return User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles("user")
                .build();
    }

}
