package vn.hoidanit.jobhunter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hoidanit.jobhunter.domain.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    boolean existsByName(String name);

    Page<Skill> findAll(Specification<Skill> spec, Pageable pageable);

}
