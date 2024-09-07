package com.study.vue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class StudyProjectBackendApplicationTests {
    @Test
    void contextLoads() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // String encodedPassword = encoder.encode("123456");

        // // 打印加密结果
        // System.out.println(encodedPassword);

        // // 验证加密结果是否符合BCrypt格式
        // assertTrue(encodedPassword.startsWith("$2a$"), "Encoded password should start with $2a$");
    }
}