package com.example.project.data;

import com.example.project.model.User;

public class UserDataLayer {
    public User getUserByNameAndAge(String name, int age) {
        return new User(name, age);
    }

    public String addUser(User user) {
        return user.getName();
    }
}
