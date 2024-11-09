package com.example.project.test_constructs;

import org.springframework.stereotype.Component;

@Component
public class ConstructImplementationOfAbstract extends ConstructAbstractClass {
    @Override
    public void abstractFoo() {
        System.out.println("abstract foo implementation");
    }

}
