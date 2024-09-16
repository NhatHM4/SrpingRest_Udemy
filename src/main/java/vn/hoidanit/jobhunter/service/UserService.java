package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
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

    public ResultPaginationDTO findAllUser(Optional<String> currentOptional, Optional<String> pageSizeOptional) {
        Pageable paging = null;
        Page<User> pagedResult = null;
        ResultPaginationDTO rs = new ResultPaginationDTO();
        if (currentOptional.isPresent() && pageSizeOptional.isPresent()) {
            paging = PageRequest.of(Integer.parseInt(currentOptional.get()) - 1,
                    Integer.parseInt(pageSizeOptional.get()));
            pagedResult = userRepository.findAll(paging);
        }
        if (pagedResult != null && pagedResult.hasContent()) {
            Meta meta = new Meta();
            meta.setPage(pagedResult.getNumber() + 1);
            meta.setPageSize(pagedResult.getSize());
            meta.setPages(pagedResult.getTotalPages());
            meta.setTotal(pagedResult.getTotalElements());
            rs.setMeta(meta);
            rs.setData(pagedResult.getContent());
            return rs;
        }
        return rs;
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
