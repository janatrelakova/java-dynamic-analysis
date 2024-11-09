package com.example.project.service;

import com.example.project.data.ProductDataLayer;
import com.example.project.data.UserDataLayer;
import com.example.project.model.Product;
import com.example.project.model.User;

import java.util.Arrays;
import java.util.List;

public class UserService {
    private final UserDataLayer userDataLayer = new UserDataLayer();
    private final ProductService productService = new ProductService(new ProductDataLayer());

    public List<String> getUserIds() {
        return Arrays.asList("user1", "user2", "user3");
    }

    public String addUser(User user) {
        return userDataLayer.addUser(user);
    }

    public User getUserByNameAndAge(String name, int age) {
        return userDataLayer.getUserByNameAndAge(name, age);
    }

    public List<Product> getUserProducts(int id) {
        return productService.getAllProducts();
    }
}
