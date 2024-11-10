package com.example.project.model;

// Ignore warning "Could be record" for testing purposes.
public class User {
    private String name = null;
    private int age = 0;

    public User() {}

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
