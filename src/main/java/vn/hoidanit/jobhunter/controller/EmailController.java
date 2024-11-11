package vn.hoidanit.jobhunter.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.service.EmailService;
import vn.hoidanit.jobhunter.service.SubscriberService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    private final SubscriberService subscriberService;

    public EmailController(EmailService emailService,
            SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @GetMapping("/email")
    @ApiMessage("send mail")
    // @Scheduled(cron = "0 */1 * * * *")
    // @Transactional
    public String sendMail() {
        this.subscriberService.sendSubscribersEmailJobs();
        return "ok";
    }

}
