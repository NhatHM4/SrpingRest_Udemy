package vn.hoidanit.jobhunter.service;

import java.util.List;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createNewUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    public User updateUser(User user) {
        User userUpdated = findById(user.getId());
        if (userUpdated != null) {
            userUpdated.setEmail(user.getEmail());
            userUpdated.setName(user.getName());
            userUpdated.setPassword(user.getPassword());
            return userRepository.save(user);
        }

        return user;

    }

    public User getUserByUserName(String email) {
        return userRepository.findByEmail(email);
    }

}
