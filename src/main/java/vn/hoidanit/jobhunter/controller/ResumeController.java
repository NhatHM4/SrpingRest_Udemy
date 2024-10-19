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
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.ResumeDTO;
import vn.hoidanit.jobhunter.service.ResumeService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    @ApiMessage("create resume")
    public ResponseEntity<ResumeDTO> postMethodName(@Valid @RequestBody Resume resume) throws IdInvalidException {
        ResumeDTO resumeDTO = resumeService.insertResume(resume);
        return ResponseEntity.status(HttpStatus.CREATED).body(resumeDTO);
    }

    @PutMapping("/resumes")
    @ApiMessage("update resume")
    public ResponseEntity<ResumeDTO> updateResume(@RequestBody Resume resume) throws IdInvalidException {
        ResumeDTO resumeDTO = resumeService.updateResume(resume);

        return ResponseEntity.ok(resumeDTO);
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("delete by id")
    public ResponseEntity<Void> deleteResumeById(@PathVariable long id) throws IdInvalidException {
        resumeService.deleteResumeById(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("get by id")
    public ResponseEntity<ResumeDTO> getResumeById(@PathVariable long id) throws IdInvalidException {
        ResumeDTO resumeDTO = resumeService.getResumeById(id);
        return ResponseEntity.ok(resumeDTO);
    }

    @GetMapping("/resumes")
    @ApiMessage("get all resume")
    public ResponseEntity<ResultPaginationDTO> getAllResumes(@Filter Specification<Resume> spec, Pageable pageable) {
        ResultPaginationDTO resultPaginationDTO = resumeService.handleGetAllResume(spec, pageable);
        return ResponseEntity.ok(resultPaginationDTO);
    }

}
