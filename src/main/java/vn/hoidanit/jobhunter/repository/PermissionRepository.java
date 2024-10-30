package vn.hoidanit.jobhunter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hoidanit.jobhunter.domain.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    boolean existsByNameAndApiPathAndMethod(String name, String apiPath, String method);

    boolean existsByNameAndApiPathAndMethodAndIdNot(String name, String apiPath, String method, Long id);

    Page<Permission> findAll(Specification<Permission> spec, Pageable pageable);
}
