package vn.hoidanit.jobhunter.service;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final MailSender mailSender;

    public EmailService(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleMail() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("hanhat123123@gmail.com");
        msg.setSubject("Test mail ");
        msg.setText("test ok");
        this.mailSender.send(msg);
    }
}
