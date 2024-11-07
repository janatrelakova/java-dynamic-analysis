package cz.muni.fi.xtrelak.controller;

import cz.muni.fi.xtrelak.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/search")
    public String searchUser(@RequestParam String username, @RequestParam int age) {
        return "Searching user: " + username + " with age: " + age;
    }

    @PostMapping("/add")
    public String addUser(@RequestBody User user) {
        return "User added: " + user.getName() + ", Age: " + user.getAge();
    }

    @GetMapping(value = { "/greet", "/hello"})
    public String greetUser(@ModelAttribute User user) {
        return "Hello, " + user.getName() + "!";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<String> getUsers() {
        return List.of("User1", "User2", "User3");
    }

    @RequestMapping(value = {"/updateUserSomehow"}, method = { RequestMethod.PUT, RequestMethod.PATCH })
    public String updateUser(@RequestBody User user) {
        return "User updated: " + user.getName() + ", Age: " + user.getAge();
    }
}
