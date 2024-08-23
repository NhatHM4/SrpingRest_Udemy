package vn.hoidanit.jobhunter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createNewUserPost(@RequestBody User user) {
        String hashPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        User newUser = userService.createNewUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) throws IdInvalidException {

        if (id > 1500) {
            throw new IdInvalidException(" ID khong lon hon 1500");
        }
        userService.deleteUser(id);

        return ResponseEntity.ok("delete successfully");
        // return ResponseEntity.status(HttpStatus.OK).body("delete successfully");
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User newUser = userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(newUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> listUser = userService.findAllUser();
        return ResponseEntity.status(HttpStatus.OK).body(listUser);
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User newUser = userService.updateUser(user);

        return ResponseEntity.status(HttpStatus.OK).body(newUser);
    }

}
