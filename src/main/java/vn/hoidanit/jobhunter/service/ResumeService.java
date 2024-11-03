package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.ParseContextImpl;
import com.turkraft.springfilter.parser.node.FilterNode;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.ResumeDTO;
import vn.hoidanit.jobhunter.domain.response.ResumeDTO.JobDTO;
import vn.hoidanit.jobhunter.domain.response.ResumeDTO.UserDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.ResumeRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;

    private final UserRepository userRepository;

    private final JobRepository jobRepository;

    @Autowired
    private FilterParser filterParser;

    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;

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

    public void deleteResumeById(long id) throws IdInvalidException {
        Optional<Resume> resumeOptional = resumeRepository.findById(id);
        if (!resumeOptional.isPresent()) {
            throw new IdInvalidException(" Resume ID is not exists !!! ");
        }
        resumeRepository.deleteById(id);
    }

    public ResumeDTO getResumeById(long id) throws IdInvalidException {
        Optional<Resume> resumeOptional = resumeRepository.findById(id);
        if (!resumeOptional.isPresent()) {
            throw new IdInvalidException(" Resume ID is not exists !!! ");
        }
        UserDTO userDTO = new UserDTO(resumeOptional.get().getUser().getId(),
                resumeOptional.get().getUser().getEmail());
        JobDTO jobDTO = new JobDTO(resumeOptional.get().getJob().getId(), resumeOptional.get().getJob().getName());
        ResumeDTO resumeDTO = new ResumeDTO(resumeOptional.get().getId(), resumeOptional.get().getEmail(),
                resumeOptional.get().getUrl(),
                resumeOptional.get().getStatus(), resumeOptional.get().getCreatedAt(),
                resumeOptional.get().getUpdatedAt(),
                resumeOptional.get().getCreatedBy(),
                resumeOptional.get().getUpdatedBy(),
                userDTO,
                jobDTO);
        return resumeDTO;
    }

    public ResultPaginationDTO handleGetAllResume(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> pResume = this.resumeRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pResume.getTotalPages());
        mt.setTotal(pResume.getTotalElements());

        rs.setMeta(mt);

        List<ResumeDTO> listResumeConverted = pResume.getContent().stream()
                .map(resume -> new ResumeDTO(resume.getId(), resume.getEmail(),
                        resume.getUrl(),
                        resume.getStatus(), resume.getCreatedAt(),
                        resume.getUpdatedAt(),
                        resume.getCreatedBy(),
                        resume.getUpdatedBy(),
                        new UserDTO(resume.getUser().getId(), resume.getUser().getEmail()),
                        new JobDTO(resume.getJob().getId(), resume.getJob().getName())))
                .collect(Collectors.toList());
        rs.setResult(listResumeConverted);
        return rs;
    }

    public ResultPaginationDTO fetchResumeByUser(Pageable pageable) {

        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        @SuppressWarnings("deprecation")
        // FilterNode node = filterParser.parse("email='" + email + "' and age = 0", new
        // ParseContextImpl(this::getPath));
        FilterNode node = filterParser.parse("email='" + email + "'", new ParseContextImpl(this::getPath));
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        Page<Resume> pResume = this.resumeRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pResume.getTotalPages());
        mt.setTotal(pResume.getTotalElements());

        rs.setMeta(mt);

        List<ResumeDTO> listResumeConverted = pResume.getContent().stream()
                .map(resume -> new ResumeDTO(resume.getId(), resume.getEmail(),
                        resume.getUrl(),
                        resume.getStatus(), resume.getCreatedAt(),
                        resume.getUpdatedAt(),
                        resume.getCreatedBy(),
                        resume.getUpdatedBy(),
                        new UserDTO(resume.getUser().getId(), resume.getUser().getEmail()),
                        new JobDTO(resume.getJob().getId(), resume.getJob().getName())))
                .collect(Collectors.toList());
        rs.setResult(listResumeConverted);
        return rs;
    }

    private String getPath(String daoPath) {
        return switch (daoPath) {
            case "age" -> "user.age";
            case "city" -> "user.city";
            case "street" -> "user.street";
            default -> daoPath;
        };
    }

}
