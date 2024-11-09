package com.example.project.controller;

import com.example.project.model.Product;
import com.example.project.model.User;
import com.example.project.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService = new UserService();

    @GetMapping("/search")
    public User searchUser(@RequestParam String username, @RequestParam int age) {
        return userService.getUserByNameAndAge(username, age);
    }

    @PostMapping("/add")
    public String addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping(value = { "/greet", "/hello"})
    public String greetUser(@ModelAttribute User user) {
        return "Hello, " + user.getName() + "!";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<String> getUsers() {
        return userService.getUserIds();
    }

    @RequestMapping(value = {"/updateUserSomehow"}, method = { RequestMethod.PUT, RequestMethod.PATCH })
    public String updateUser(@RequestBody User user) {
        return "User updated: " + user.getName() + ", Age: " + user.getAge();
    }

    @RequestMapping(value = "/{id}/products", method = RequestMethod.GET)
    public List<Product> getUserProducts(@PathVariable("id") int id) {
        return userService.getUserProducts(id);
    }
}
