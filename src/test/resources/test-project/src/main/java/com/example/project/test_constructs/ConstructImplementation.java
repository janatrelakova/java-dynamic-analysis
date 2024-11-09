package com.example.project.test_constructs;

import org.springframework.stereotype.Component;

@Component
public class ConstructImplementation implements ConstructInterface {
    @Override
    public void foo() {
        System.out.println("foo implementation");
    }
}
