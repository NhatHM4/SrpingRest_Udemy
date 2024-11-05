package vn.hoidanit.jobhunter.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    Page<Role> findAll(Specification<Role> spec, Pageable pageable);

    @Query("SELECT r FROM Role r JOIN r.permissions p WHERE p IN :permissions")
    List<Role> findRoleInPermission(List<Permission> permissions);

    Role findByName(String name);

}
