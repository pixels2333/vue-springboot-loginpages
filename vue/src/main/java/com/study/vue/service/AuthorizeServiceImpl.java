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

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.study.vue.entity.Account;
import com.study.vue.mapper.UserMapper;

import jakarta.annotation.Resource;

@Service
public class AuthorizeServiceImpl implements AuthorizeService {

    @Value("${spring.mail.username}")
    String from;

    @Resource
    UserMapper mapper;

    @Resource
    MailSender mailSender;

    @Resource
    StringRedisTemplate template;
    @Resource
    BCryptPasswordEncoder encoder;

    // 根据用户名加载用户信息，如果用户名为空或用户不存在，则抛出 UsernameNotFoundException 异常
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

    @Override
    public String sendValidateEmail(String email, String sessionId) {
        String key = "email:" + sessionId + ":" + email;

        if (Boolean.TRUE.equals(template.hasKey(key))) {
            long expire = Optional.ofNullable(template.getExpire(key, TimeUnit.SECONDS)).orElse(0L);
            if (expire > 120)
                return "验证码发送过于频繁";

        }
        if (mapper.findAccountByNameOrEmail(email) != null) {
            return "该邮箱已被注册";

        }
        Random random = new Random();
        int code = random.nextInt(899999) + 100000;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("您的验证邮件");
        message.setText("验证码是:" + code);
        try {
            mailSender.send(message);

            template.opsForValue().set(key, String.valueOf(code), 60 * 10, TimeUnit.SECONDS); // 设置10分钟过期
            mailSender.send();
            return null;
        } catch (MailException e) {
            e.printStackTrace();
            return "验证码发送失败";
        }

    }

    @Override
    public String validateAndRegister(String username, String password, String email, String code, String sessionId) {

        String key = "email:" + sessionId + ":" + email;
        if (Boolean.TRUE.equals(template.hasKey(key))) {
            String s = template.opsForValue().get(key);
            if (s == null)
                return "验证码已失效";

            else if (s.equals(code)) {
                password = encoder.encode(password);
                if (mapper.creatAccount(username, password, email) > 0) {
                    return null;

                } else
                    return "注册失败";

            } else
                return "验证码错误";
        } else
            return "请先发送验证码";

    }

}
