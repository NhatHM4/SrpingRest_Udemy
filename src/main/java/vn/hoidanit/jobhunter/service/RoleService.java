package vn.hoidanit.jobhunter.service;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public Role insertRole(Role role) throws IdInvalidException {
        Optional<Role> roleOptional = roleRepository.findById(role.getId());
        if (roleOptional.isPresent()) {
            throw new IdInvalidException(" Role id is exists !!!");
        }

        if (roleRepository.existsByName(role.getName())) {
            throw new IdInvalidException(" Role's name  is exists !!!");
        }

        if (role.getPermissions() != null && role.getPermissions().size() > 0) {
            List<Permission> listPermission = role.getPermissions().stream()
                    .map(permission -> permissionRepository.findById(permission.getId()).isPresent()
                            ? permissionRepository.findById(permission.getId()).get()
                            : null)
                    .filter(x -> x != null)
                    .collect(Collectors.toList());
            role.setPermissions(listPermission);
        }
        return roleRepository.save(role);
    }

    public Role updateRole(Role role) throws IdInvalidException {
        Optional<Role> roleOptional = roleRepository.findById(role.getId());
        if (!roleOptional.isPresent()) {
            throw new IdInvalidException(" Role id is not exists !!!");
        }

        if (role.getPermissions() != null && role.getPermissions().size() > 0) {
            List<Permission> listPermission = role.getPermissions().stream()
                    .map(permission -> permissionRepository.findById(permission.getId()).isPresent()
                            ? permissionRepository.findById(permission.getId()).get()
                            : null)
                    .filter(x -> x != null)
                    .collect(Collectors.toList());
            roleOptional.get().setPermissions(listPermission);
        }

        if (SecurityUtil.isNotBlank(role.getName())) {
            roleOptional.get().setName(role.getName());
        }
        if (SecurityUtil.isNotBlank(role.getDescription())) {
            roleOptional.get().setDescription(role.getDescription());
        }
        roleOptional.get().setActive(role.isActive());

        return roleRepository.save(roleOptional.get());
    }

    public ResultPaginationDTO handleGetRole(Specification<Role> spec, Pageable pageable) {
        Page<Role> pRole = this.roleRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pRole.getTotalPages());
        mt.setTotal(pRole.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pRole.getContent());
        return rs;
    }

    public void deleteRole(Long id) throws IdInvalidException {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (!roleOptional.isPresent()) {
            throw new IdInvalidException(" Role id is not exists !!!");
        }
        roleRepository.deleteById(id);
    }

    public Role findRoleById(Long id) throws IdInvalidException {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (!roleOptional.isPresent()) {
            throw new IdInvalidException(" Role id is not exists !!!");
        }

        return roleOptional.get();
    }

}
