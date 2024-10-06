package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.RestResponse;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.UserDTO;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    @ApiMessage("create user")
    public ResponseEntity<Object> createNewUserPost(@Valid @RequestBody User user) {
        String hashPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        User newUser = userService.createNewUser(user);
        if (newUser != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(newUser.getId());
            userDTO.setName(newUser.getName());
            userDTO.setEmail(newUser.getEmail());
            userDTO.setGender(newUser.getGender());
            userDTO.setAge(newUser.getAge());
            userDTO.setAddress(newUser.getAddress());
            userDTO.setCreatedAt(newUser.getCreatedAt());
            userDTO.setCompany(newUser.getCompany());
            return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
        }

        RestResponse<Object> res = new RestResponse<Object>();
        res.setError("Email " + user.getEmail() + " is exists !!!");
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setMessage(" Exception occurs ... ");
        return ResponseEntity.badRequest().body(res);
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("delete user by id")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws IdInvalidException {
        User newUser = userService.findById(id);
        if (newUser != null) {
            userService.deleteUser(id);
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping("/users/{id}")
    @ApiMessage(value = "fetch user by id")
    public ResponseEntity<Object> getUser(@PathVariable Long id) {
        User newUser = userService.findById(id);
        if (newUser != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(newUser.getId());
            userDTO.setName(newUser.getName());
            userDTO.setEmail(newUser.getEmail());
            userDTO.setGender(newUser.getGender());
            userDTO.setAge(newUser.getAge());
            userDTO.setAddress(newUser.getAddress());
            userDTO.setCompany(newUser.getCompany());
            return ResponseEntity.ok(userDTO);
        }

        RestResponse<Object> res = new RestResponse<Object>();
        res.setError("ID = '" + id + "' is not exists !!!");
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setMessage(" Exception occurs ... ");
        return ResponseEntity.badRequest().body(res);

    }

    @GetMapping("/users")
    @ApiMessage(value = "fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(
            @Filter Specification<User> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUser(spec, pageable));
    }

    @PutMapping("/users")
    @ApiMessage("update user")
    public ResponseEntity<Object> updateUser(@RequestBody User user) {
        User newUser = userService.updateUser(user);
        if (newUser != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(newUser.getId());
            userDTO.setName(newUser.getName());
            userDTO.setEmail(newUser.getEmail());
            userDTO.setGender(newUser.getGender());
            userDTO.setAge(newUser.getAge());
            userDTO.setAddress(newUser.getAddress());
            userDTO.setUpdatedAt(newUser.getUpdatedAt());
            userDTO.setCompany(newUser.getCompany());
            return ResponseEntity.ok(userDTO);
        }

        RestResponse<Object> res = new RestResponse<Object>();
        res.setError("ID = '" + user.getId() + "' is not exists !!!");
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setMessage(" Exception occurs ... ");
        return ResponseEntity.badRequest().body(res);
    }

}
