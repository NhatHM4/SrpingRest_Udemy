package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.service.EmailService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    private EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/email")
    @ApiMessage("send mail")
    public String sendMail() {
        // this.emailService.sendEmailSync("hanhat123123@gmail.com", "test sendmail",
        // "<h1><b>hello</></h1>", false,
        // true);
        this.emailService.sendEmailFromTemplateSync("hanhat123123@gmail.com", "test sendmail ahihi", "job");
        return "ok";
    }

}
