package vn.hoidanit.jobhunter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.hoidanit.jobhunter.domain.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    boolean existsByName(String name);

    Page<Skill> findAll(Specification<Skill> spec, Pageable pageable);

    @Query(value = "SELECT s.name FROM Skill s  WHERE s.id = :id")
    String findNameBySkillId(long id);

    @Query(value = "SELECT s FROM Skill s  WHERE s.id = :id")
    Skill findSkillBySkillId(long id);

}
