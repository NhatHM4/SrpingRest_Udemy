package vn.hoidanit.jobhunter.service;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SubscriberRepository;

@Service
public class EmailService {

    private final MailSender mailSender;

    private final JavaMailSender javaMailSender;

    private final SpringTemplateEngine templateEngine;

    private final JobRepository jobRepository;
    private final SubscriberRepository subscriberRepository;

    public EmailService(MailSender mailSender, JavaMailSender javaMailSender, SpringTemplateEngine templateEngine,
            JobRepository jobRepository, SubscriberRepository subscriberRepository) {
        this.mailSender = mailSender;
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.jobRepository = jobRepository;
        this.subscriberRepository = subscriberRepository;
    }

    public void sendSimpleMail() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("hanhat123123@gmail.com");
        msg.setSubject("Test mail ");
        msg.setText("test ok");
        this.mailSender.send(msg);
    }

    public void sendEmailSync(String to, String subject, String content, boolean isMultipart,
            boolean isHtml) {
        // Prepare message using a Spring helper
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
                    isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content, isHtml);
            this.javaMailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            System.out.println("ERROR SEND EMAIL: " + e);
        }
    }

    public void sendEmailFromTemplateSync(String to, String subject, String templateName, String username,
            Object jobs) {
        Context context = new Context();
        context.setVariable("name1", username);
        context.setVariable("jobs", jobs);
        String content = this.templateEngine.process(templateName, context);
        this.sendEmailSync(to, subject, content, false, true);
    }

    public void sendSubscribersEmailJobs() {
        List<Subscriber> subscribers = this.subscriberRepository.findAll();
        if (subscribers != null && subscribers.size() > 0)
            for (Subscriber subscriber : subscribers) {
                if (subscriber.getSkills() != null && subscriber.getSkills().size() > 0) {
                    List<Job> jobs = this.jobRepository.findBySkillsIn(subscriber.getSkills());
                    if (jobs != null && jobs.size() > 0) {
                        sendEmailFromTemplateSync(subscriber.getEmail(), "Cơ hội việc làm hot đang chờ đón bạn", "job",
                                subscriber.getName(), jobs);
                    }
                }
            }
    }
}
