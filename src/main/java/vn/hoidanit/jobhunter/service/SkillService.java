package vn.hoidanit.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill save(Skill skill) throws IdInvalidException {
        if (skillRepository.existsByName(skill.getName())) {
            throw new IdInvalidException("skill's name is exists !!!");
        }
        return skillRepository.save(skill);

    }

    public Skill update(Skill skill) throws IdInvalidException {
        Optional<Skill> optionalSkill = skillRepository.findById(skill.getId());
        if (!optionalSkill.isPresent()) {
            throw new IdInvalidException("skill is not exists !!!");
        }
        if (optionalSkill.get().getCreatedAt() != null) {
            skill.setCreatedAt(optionalSkill.get().getCreatedAt());
        }
        if (optionalSkill.get().getCreatedBy() != null) {
            skill.setCreatedBy(optionalSkill.get().getCreatedBy());
        }
        return save(skill);
    }

    public ResultPaginationDTO handleGetSkill(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> pCompany = this.skillRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pCompany.getTotalPages());
        mt.setTotal(pCompany.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pCompany.getContent());
        return rs;
    }

    public Skill findById(Long id) throws IdInvalidException {
        if (!skillRepository.findById(id).isPresent()) {
            throw new IdInvalidException("skill is not exists !!!");
        }

        return skillRepository.findById(id).get();
    }

    public void deleteSkillById(Long id) throws IdInvalidException {
        Optional<Skill> skillOptional = skillRepository.findById(id);
        if (!skillRepository.findById(id).isPresent()) {
            throw new IdInvalidException("skill is not exists !!!");
        }
        skillOptional.get().getJobs().stream().forEach(job -> job.getSkills().remove(skillOptional.get()));
        skillRepository.delete(skillOptional.get());
    }
}
