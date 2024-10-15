package vn.hoidanit.jobhunter.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResumeDTO;
import vn.hoidanit.jobhunter.domain.response.ResumeDTO.UserDTO;
import vn.hoidanit.jobhunter.domain.response.ResumeDTO.JobDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.ResumeRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;

    private final UserRepository userRepository;

    private final JobRepository jobRepository;

    private ResumeService(ResumeRepository resumeRepository, UserRepository userRepository,
            JobRepository jobRepository) {
        this.resumeRepository = resumeRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    public ResumeDTO insertResume(Resume resume) throws IdInvalidException {
        if (resumeRepository.existsById(resume.getId())) {
            throw new IdInvalidException(" Resume ID is exists !!! ");
        }
        Optional<User> user = userRepository.findById(resume.getUser().getId());
        if (user.isPresent()) {
            resume.setUser(user.get());
        } else {
            throw new IdInvalidException(" User ID is exists !!! ");
        }
        Optional<Job> resumeOptional = jobRepository.findById(resume.getJob().getId());
        if (resumeOptional.isPresent()) {
            resume.setJob(resumeOptional.get());
        } else {
            throw new IdInvalidException(" Job ID is exists !!! ");
        }
        Resume newResume = resumeRepository.save(resume);
        UserDTO userDTO = new UserDTO(newResume.getUser().getId(), newResume.getUser().getEmail());
        JobDTO jobDTO = new JobDTO(newResume.getJob().getId(), newResume.getJob().getName());
        ResumeDTO resumeDTO = new ResumeDTO(newResume.getId(), newResume.getEmail(), newResume.getUrl(),
                newResume.getStatus(), newResume.getCreatedAt(),
                newResume.getUpdatedAt(),
                newResume.getCreatedBy(),
                newResume.getUpdatedBy(),
                userDTO,
                jobDTO);

        return resumeDTO;
    }

    public ResumeDTO updateResume(Resume resume) throws IdInvalidException {
        Optional<Resume> resumeOptional = resumeRepository.findById(resume.getId());
        if (!resumeOptional.isPresent()) {
            throw new IdInvalidException(" Resume ID is not exists !!! ");
        }
        if (resume.getStatus() != null) {
            resumeOptional.get().setStatus(resume.getStatus());
        }
        Resume newResume = resumeRepository.save(resumeOptional.get());
        UserDTO userDTO = new UserDTO(newResume.getUser().getId(), newResume.getUser().getEmail());
        JobDTO jobDTO = new JobDTO(newResume.getJob().getId(), newResume.getJob().getName());
        ResumeDTO resumeDTO = new ResumeDTO(newResume.getId(), newResume.getEmail(), newResume.getUrl(),
                newResume.getStatus(), newResume.getCreatedAt(),
                newResume.getUpdatedAt(),
                newResume.getCreatedBy(),
                newResume.getUpdatedBy(),
                userDTO,
                jobDTO);
        return resumeDTO;

    }

}
