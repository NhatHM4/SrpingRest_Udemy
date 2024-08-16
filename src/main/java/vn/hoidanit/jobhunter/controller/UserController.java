package vn.hoidanit.jobhunter.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public User createNewUserPost(@RequestBody User user) {
        User newUser = userService.createNewUser(user);

        return newUser;
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);

        return "delete successfully";
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Long id) {

        return userService.findById(id);
    }

    @GetMapping("/user")
    public List<User> getAllUser() {
        return userService.findAllUser();
    }

    @PutMapping("/user")
    public User updateUser(@RequestBody User user) {
        User newUser = userService.createNewUser(user);

        return newUser;
    }
}
