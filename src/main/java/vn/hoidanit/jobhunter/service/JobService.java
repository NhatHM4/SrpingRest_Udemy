package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.JobDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
    }

    public JobDTO createJob(Job job) throws IdInvalidException {
        if (jobRepository.findById(job.getId()).isPresent()) {
            throw new IdInvalidException("Job id is exists !!!");
        }
        Set<Skill> listSkillContainsInDB = job.getSkills().stream()
                .map(skill -> skillRepository.findSkillBySkillId(skill.getId()))
                .filter(skill -> skill != null)
                .collect(Collectors.toSet());
        job.setSkills(listSkillContainsInDB);
        List<String> listNameSkill = listSkillContainsInDB.stream().map(skill -> skill.getName())
                .collect(Collectors.toList());

        Job newJob = jobRepository.save(job);
        JobDTO jobDTO = null;
        if (newJob != null) {
            jobDTO = new JobDTO(newJob.getId(),
                    newJob.getName(),
                    newJob.getLocation(),
                    newJob.getSalary(),
                    newJob.getQuantity(),
                    newJob.getLevel(),
                    newJob.getDescription(),
                    newJob.getStartDate(),
                    newJob.getEndDate(),
                    newJob.isActive(),
                    newJob.getCreatedAt(),
                    newJob.getUpdatedAt(),
                    newJob.getCreatedBy(),
                    newJob.getUpdatedBy(),
                    newJob.getSkills(),
                    newJob.getCompany());
        }

        return jobDTO;

    }

    public JobDTO updateJob(Job job) throws IdInvalidException {
        Optional<Job> jobOptional = jobRepository.findById(job.getId());
        if (!jobOptional.isPresent()) {
            throw new IdInvalidException("Job id is not exists !!!");
        }
        Set<Skill> listSkillContainsInDB = job.getSkills().stream()
                .map(skill -> skillRepository.findSkillBySkillId(skill.getId()))
                .filter(skill -> skill != null)
                .collect(Collectors.toSet());
        List<String> listNameSkill = listSkillContainsInDB.stream().map(skill -> skill.getName())
                .collect(Collectors.toList());
        if (job.getName() != null) {
            jobOptional.get().setName(job.getName());
        }
        if (job.getLocation() != null) {
            jobOptional.get().setLocation(job.getLocation());
        }
        if (job.getSalary() != 0) {
            jobOptional.get().setSalary(job.getSalary());
        }
        if (job.getQuantity() != 0) {
            jobOptional.get().setQuantity(job.getQuantity());
        }
        if (job.getLevel() != null) {
            jobOptional.get().setLevel(job.getLevel());
        }
        if (job.getDescription() != null) {
            jobOptional.get().setDescription(job.getDescription());
        }
        if (job.getStartDate() != null) {
            jobOptional.get().setStartDate(job.getStartDate());
        }
        if (job.getEndDate() != null) {
            jobOptional.get().setEndDate(job.getEndDate());
        }
        jobOptional.get().setActive(job.isActive());
        if (job.getCreatedAt() != null) {
            jobOptional.get().setCreatedAt(job.getCreatedAt());
        }
        if (job.getCreatedAt() != null) {
            jobOptional.get().setCreatedAt(job.getCreatedAt());
        }
        if (job.getCreatedBy() != null) {
            jobOptional.get().setCreatedBy(job.getCreatedBy());
        }
        jobOptional.get().setSkills(listSkillContainsInDB);

        Job updateJob = jobRepository.save(jobOptional.get());
        JobDTO jobDTO = null;
        if (updateJob != null) {
            jobDTO = new JobDTO(updateJob.getId(),
                    updateJob.getName(),
                    updateJob.getLocation(),
                    updateJob.getSalary(),
                    updateJob.getQuantity(),
                    updateJob.getLevel(),
                    updateJob.getDescription(),
                    updateJob.getStartDate(),
                    updateJob.getEndDate(),
                    updateJob.isActive(),
                    updateJob.getCreatedAt(),
                    updateJob.getUpdatedAt(),
                    updateJob.getCreatedBy(),
                    updateJob.getUpdatedBy(),
                    updateJob.getSkills(),
                    updateJob.getCompany());
        }

        return jobDTO;

    }

    public JobDTO findById(Long id) throws IdInvalidException {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (!jobOptional.isPresent()) {
            throw new IdInvalidException("Job id is not exists !!!");
        }
        JobDTO jobDTO = null;
        if (jobOptional.isPresent()) {
            // List<String> listSkillContainsInDB = jobOptional.get().getSkills().stream()
            // .map(skill -> skillRepository.findNameBySkillId(skill.getId()))
            // .filter(skill -> skill != null)
            // .collect(Collectors.toList());

            jobDTO = new JobDTO(jobOptional.get().getId(),
                    jobOptional.get().getName(),
                    jobOptional.get().getLocation(),
                    jobOptional.get().getSalary(),
                    jobOptional.get().getQuantity(),
                    jobOptional.get().getLevel(),
                    jobOptional.get().getDescription(),
                    jobOptional.get().getStartDate(),
                    jobOptional.get().getEndDate(),
                    jobOptional.get().isActive(),
                    jobOptional.get().getCreatedAt(),
                    jobOptional.get().getUpdatedAt(),
                    jobOptional.get().getCreatedBy(),
                    jobOptional.get().getUpdatedBy(),
                    jobOptional.get().getSkills(),
                    jobOptional.get().getCompany());
        }

        return jobDTO;

    }

    public ResultPaginationDTO handleGetJob(Specification<Job> spec, Pageable pageable) {
        Page<Job> pJob = this.jobRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pJob.getTotalPages());
        mt.setTotal(pJob.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pJob.getContent());
        return rs;
    }

    public void deleteJobById(long id) throws IdInvalidException {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (!jobOptional.isPresent()) {
            throw new IdInvalidException("Job id is not exists !!!");
        }
        jobRepository.delete(jobOptional.get());
    }

}
