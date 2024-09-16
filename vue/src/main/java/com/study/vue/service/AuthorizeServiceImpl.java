/**
 * AuthorizeService 类实现了 Spring Security 的 UserDetailsService 接口，
 * 用于根据用户名加载用户的详细信息。
 * 
 * 该类通过 UserMapper 从数据库中查找用户信息，并将其转换为 Spring Security 所需的 UserDetails 对象。
 * 
 * 方法：
 * - loadUserByUsername(String username): 根据用户名加载用户信息，如果用户名为空或用户不存在，则抛出 UsernameNotFoundException 异常。
 * 
 * 注解：
 * - @Service: 标识该类为 Spring 的服务组件。
 * - @Resource: 注入 UserMapper 依赖。
 * 
 * 异常：
 * - UsernameNotFoundException: 当用户名为空或用户不存在时抛出。
 * 
 * 使用示例：
 * 
 * ```java
 * @Autowired
 * private UserDetailsService userDetailsService;
 * 
 * public void someMethod() {
 *     try {
 *         UserDetails userDetails = userDetailsService.loadUserByUsername("someUsername");
 *         // 处理 userDetails
 *     } catch (UsernameNotFoundException e) {
 *         // 处理异常
 *     }
 * }
 * ```
 * 
 * 注意：
 * - 该类假定数据库中的用户信息包含用户名和密码。
 * - 角色默认为 "user"。
 */
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
public class AuthorizeServiceImpl implements AuthorizeService {

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

    public boolean sendValidateEmail(String email) {
        
        return true;
    }

}
