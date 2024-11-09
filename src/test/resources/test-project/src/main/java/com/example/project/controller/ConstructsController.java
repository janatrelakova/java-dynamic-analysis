package com.example.project.controller;

import com.example.project.service.UserService;
import com.example.project.test_constructs.ConstructAbstractClass;
import com.example.project.test_constructs.ConstructInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for test cases.
 * Test cases:
 *   - calling method on interface
 *   - using default method on interface
 *   - using bean with qualifier
 */
@RestController
@RequestMapping(value = { "/constructs" })
public class ConstructsController {
    private final ConstructInterface construct;
    private final UserService userService;
    private final ConstructAbstractClass abstractClass;

    public ConstructsController(@Autowired ConstructInterface construct,
                                @Autowired @Qualifier("fakeUserService") UserService userService,
                                @Autowired ConstructAbstractClass abstractClass
                                ) {
        this.construct = construct;
        this.userService = userService;
        this.abstractClass = abstractClass;
    }

    @RequestMapping("/test")
    public void test() {
        System.out.println("testing interface");

        // implemented method
        construct.foo();
        // default method
        construct.bar();
    }

    @RequestMapping("/testBean")
    public void testBean() {
        System.out.println("testing bean");

        // calling method on bean
        userService.getUserIds();

    }

    @RequestMapping("/testAbstract")
    public void testAbstract() {
        System.out.println("testing abstract class");

        // calling method on abstract class
        abstractClass.abstractFoo();
    }
}
