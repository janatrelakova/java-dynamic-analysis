package com.example.project.test_constructs;

import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ConstructBean {
    @Bean
    @Qualifier("fakeUserService")
    public UserService userService() {
        System.out.println("userService bean created");
        return new UserService();
    }
}

