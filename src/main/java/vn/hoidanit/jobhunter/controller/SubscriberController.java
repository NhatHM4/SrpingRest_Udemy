package vn.hoidanit.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.service.SubscriberService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {

    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers")
    @ApiMessage(" create subscribers")
    public ResponseEntity<Subscriber> createSubscriber(@Valid @RequestBody Subscriber subscriber)
            throws IdInvalidException {
        Subscriber newSubscriber = subscriberService.createSubscriber(subscriber);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSubscriber);
    }

    @PutMapping("/subscribers")
    @ApiMessage(" update subscribers")
    public ResponseEntity<Subscriber> updateSubscriber(@Valid @RequestBody Subscriber subscriber)
            throws IdInvalidException {
        Subscriber updatedSubscriber = subscriberService.updateSubscriber(subscriber);
        return ResponseEntity.ok(updatedSubscriber);
    }

    @PostMapping("/subscribers/skills")
    @ApiMessage(" find subscriber by email")
    public ResponseEntity<Subscriber> findByEmail()
            throws IdInvalidException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        return ResponseEntity.ok(this.subscriberService.findByEmail(email));
    }
}
