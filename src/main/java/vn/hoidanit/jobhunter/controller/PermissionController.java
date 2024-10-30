package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.PermissionService;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    public ResponseEntity<Permission> insertPermission(@Valid @RequestBody Permission permission)
            throws IdInvalidException {
        Permission permissionInDB = permissionService.insertPermission(permission);
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionInDB);
    }

    @PutMapping("/permissions")
    public ResponseEntity<Permission> updatePermission(@Valid @RequestBody Permission permission)
            throws IdInvalidException {
        Permission permissionInDB = permissionService.updatePermission(permission);
        return ResponseEntity.ok().body(permissionInDB);
    }

    @GetMapping("/permissions")
    public ResponseEntity<ResultPaginationDTO> getAllPermission(@Filter Specification<Permission> spec,
            Pageable pageable)
            throws IdInvalidException {
        ResultPaginationDTO result = permissionService.handleGetPermission(spec, pageable);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/permissions/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") String id) throws IdInvalidException {
        permissionService.deletePermission(Long.parseLong(id));
        return ResponseEntity.ok(null);
    }

}
