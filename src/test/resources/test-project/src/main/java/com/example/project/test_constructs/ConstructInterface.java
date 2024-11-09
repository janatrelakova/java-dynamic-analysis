package com.example.project.test_constructs;

public interface ConstructInterface {
    void foo();

    default void bar() {
        System.out.println("bar is default void method");
    }
}
