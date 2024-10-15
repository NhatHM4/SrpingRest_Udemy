package vn.hoidanit.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Resume;
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

    @PutMapping("resumes")
    @ApiMessage("update resume")
    public ResponseEntity<ResumeDTO> updateResume(@RequestBody Resume resume) throws IdInvalidException {
        ResumeDTO resumeDTO = resumeService.updateResume(resume);

        return ResponseEntity.ok(resumeDTO);
    }

}
