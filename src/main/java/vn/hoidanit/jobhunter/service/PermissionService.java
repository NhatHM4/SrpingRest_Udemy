package vn.hoidanit.jobhunter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
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
public class PermissionService {

    private final PermissionRepository permissionRepository;

    private final RoleRepository roleRepository;

    public PermissionService(PermissionRepository permissionRepository, RoleRepository roleRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }

    public Permission insertPermission(Permission permission) throws IdInvalidException {
        Optional<Permission> permissionOptional = permissionRepository.findById(permission.getId());
        if (permissionOptional.isPresent()) {
            throw new IdInvalidException("Permission id is exists !!!");
        }

        if (permissionRepository.existsByNameAndApiPathAndMethod(permission.getName(), permission.getApiPath(),
                permission.getMethod())) {
            throw new IdInvalidException("Permission name and api Path and method is exists !!!");
        }
        return permissionRepository.save(permission);
    }

    public Permission updatePermission(Permission permission) throws IdInvalidException {
        Optional<Permission> permissionOptional = permissionRepository.findById(permission.getId());
        if (!permissionOptional.isPresent()) {
            throw new IdInvalidException("Permission id is not exists !!!");
        }

        if (permissionRepository.existsByNameAndApiPathAndMethodAndIdNot(permission.getName(), permission.getApiPath(),
                permission.getMethod(), permission.getId())) {
            throw new IdInvalidException("Permission name and api Path and method is exists !!!");
        }
        if (SecurityUtil.isNotBlank(permission.getName())) {
            permissionOptional.get().setName(permission.getName());
        }
        if (SecurityUtil.isNotBlank(permission.getApiPath())) {
            permissionOptional.get().setApiPath(permission.getApiPath());
        }
        if (SecurityUtil.isNotBlank(permission.getModule())) {
            permissionOptional.get().setModule(permission.getModule());
        }
        if (SecurityUtil.isNotBlank(permission.getMethod())) {
            permissionOptional.get().setMethod(permission.getMethod());
        }
        return permissionRepository.save(permissionOptional.get());
    }

    public ResultPaginationDTO handleGetPermission(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> pPermission = this.permissionRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pPermission.getTotalPages());
        mt.setTotal(pPermission.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pPermission.getContent());
        return rs;
    }

    public void deletePermission(Long id) throws IdInvalidException {
        Optional<Permission> permissionOptional = permissionRepository.findById(id);
        if (!permissionOptional.isPresent()) {
            throw new IdInvalidException("Permission id is not exists !!!");
        }

        List<Role> listRole = roleRepository.findRoleInPermission(new ArrayList<>(List.of(permissionOptional.get())));
        listRole.stream().filter(role -> role.getPermissions().remove(permissionOptional.get()))
                .collect(Collectors.toList());
        permissionRepository.delete(permissionOptional.get());

    }

}
