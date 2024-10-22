package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.response.JobDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("create job ")
    public ResponseEntity<JobDTO> createJob(@Valid @RequestBody Job job) throws IdInvalidException {
        JobDTO jobDTO = jobService.createJob(job);
        return ResponseEntity.ok(jobDTO);
    }

    @PutMapping("/jobs")
    @ApiMessage("update job ")
    public ResponseEntity<JobDTO> updateJob(@Valid @RequestBody Job job) throws IdInvalidException {
        JobDTO jobDTO = jobService.updateJob(job);
        return ResponseEntity.ok(jobDTO);
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("get job by id ")
    public ResponseEntity<JobDTO> findById(@PathVariable("id") Long id) throws IdInvalidException {
        JobDTO jobDTO = jobService.findById(id);
        return ResponseEntity.ok(jobDTO);
    }

    @GetMapping("/jobs")
    @ApiMessage("fetch all job")
    public ResponseEntity<ResultPaginationDTO> findAllJob(@Filter Specification<Job> spec, Pageable pageable)
            throws IdInvalidException {
        ResultPaginationDTO pagination = jobService.handleGetJob(spec, pageable);
        return ResponseEntity.ok(pagination);
    }

    @DeleteMapping("/jobs/{id}")
    @ApiMessage("delete job by id ")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) throws IdInvalidException {
        jobService.deleteJobById(id);
        return ResponseEntity.ok().body(null);
    }

}
