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
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.RoleService;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    public ResponseEntity<Role> insertRole(@Valid @RequestBody Role role) throws IdInvalidException {
        Role roleInDB = roleService.insertRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleInDB);
    }

    @PutMapping("/roles")
    public ResponseEntity<Role> updateRole(@Valid @RequestBody Role role) throws IdInvalidException {
        Role roleInDB = roleService.updateRole(role);
        return ResponseEntity.ok(roleInDB);
    }

    @GetMapping("/roles")
    public ResponseEntity<ResultPaginationDTO> getAllPermission(@Filter Specification<Role> spec,
            Pageable pageable)
            throws IdInvalidException {
        ResultPaginationDTO result = roleService.handleGetRole(spec, pageable);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") String id) throws IdInvalidException {
        roleService.deleteRole(Long.parseLong(id));
        return ResponseEntity.ok(null);
    }

}
