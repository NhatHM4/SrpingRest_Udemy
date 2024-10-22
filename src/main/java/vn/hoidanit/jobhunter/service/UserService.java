package vn.hoidanit.jobhunter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.UserDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CompanyService companyService;

    public UserService(UserRepository userRepository, CompanyService companyService) {
        this.userRepository = userRepository;
        this.companyService = companyService;
    }

    public User createNewUser(User user) {
        if (user.getCompany() != null) {
            Company company = companyService.findById(user.getCompany().getId());
            if (company != null) {
                user.setCompany(company);
            }
        }
        Boolean existsByEmail = userRepository.existsByEmail(user.getEmail());
        if (!existsByEmail) {
            return userRepository.save(user);
        }
        return null;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User findById(Long id) {
        Optional<User> userGetById = userRepository.findById(id);
        if (userGetById.isPresent()) {
            return userRepository.findById(id).get();
        }
        return null;
    }

    public ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);
        List<UserDTO> arrUserdto = new ArrayList<UserDTO>();
        UserDTO userdto = null;
        for (User user : pageUser.getContent()) {
            userdto = new UserDTO();
            userdto.setId(user.getId());
            userdto.setEmail(user.getEmail());
            userdto.setName(user.getName());
            userdto.setGender(user.getGender());
            userdto.setAddress(user.getAddress());
            userdto.setAge(user.getAge());
            userdto.setCompany(user.getCompany());
            userdto.setCreatedAt(user.getCreatedAt());
            userdto.setUpdatedAt(user.getUpdatedAt());
            arrUserdto.add(userdto);
        }
        rs.setResult(arrUserdto);

        return rs;
    }

    public User updateUser(User user) {
        User userUpdated = findById(user.getId());
        if (userUpdated != null) {
            if (user.getEmail() != null) {
                userUpdated.setEmail(user.getEmail());
            }
            if (user.getName() != null) {
                userUpdated.setName(user.getName());
            }
            if (user.getGender() != null) {
                userUpdated.setGender(user.getGender());
            }
            if (user.getAge() != 0) {
                userUpdated.setAge(user.getAge());
            }
            if (user.getAddress() != null) {
                userUpdated.setAddress(user.getAddress());
            }
            if (user.getAddress() != null) {
                userUpdated.setCompany(user.getCompany());
            }

            return userRepository.save(userUpdated);
        }

        return userUpdated;

    }

    public User getUserByUserName(String email) {
        return userRepository.findByEmail(email);
    }

    public void updateRefreshToken(String refreshToken, String email) {
        User currentUser = userRepository.findByEmail(email);
        if (currentUser != null) {
            currentUser.setRefreshTokenString(refreshToken);
            userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return userRepository.findByRefreshTokenStringAndEmail(token, email);
    }

}
