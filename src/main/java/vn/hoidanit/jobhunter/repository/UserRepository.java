package vn.hoidanit.jobhunter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    // @Query(value = "SELECT u FROM User u WHERE u.email = ?1 ")
    // User findUsersByEmail(String email);

    User findByEmail(String email);

    Boolean existsByEmail(String email);

    User findByRefreshTokenStringAndEmail(String refresh_token, String email);

    @Query(value = "SELECT u FROM User u WHERE u.company = :com ")
    List<User> findByCompany(Company com);

}
