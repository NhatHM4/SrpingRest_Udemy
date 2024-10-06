package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.SkillService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    public ResponseEntity<Skill> createSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        Skill newSkill = skillService.save(skill);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSkill);
    }

    @PutMapping("/skills")
    public ResponseEntity<Skill> updateSKill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        Skill updateSkill = skillService.update(skill);
        return ResponseEntity.ok().body(updateSkill);
    }

    @GetMapping("/skills")
    @ApiMessage("fetch skills")
    public ResponseEntity<ResultPaginationDTO> getSkill(@Filter Specification<Skill> spec, Pageable pageable) {
        return ResponseEntity.ok(this.skillService.handleGetSkill(spec, pageable));
    }

    @GetMapping("/skills/{id}")
    @ApiMessage(value = "fetch user by id")
    public ResponseEntity<Skill> getUser(@PathVariable Long id) throws IdInvalidException {
        Skill newSkill = skillService.findById(id);

        return ResponseEntity.ok(newSkill);

    }
}
