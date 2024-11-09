package vn.hoidanit.jobhunter.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.domain.response.email.ResEmailJob;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.repository.SubscriberRepository;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@Service
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;

    private final SkillRepository skillRepository;
    private final JobRepository jobRepository;
    private final EmailService emailService;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository,
            JobRepository jobRepository, EmailService emailService) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
        this.emailService = emailService;
    }

    public Subscriber createSubscriber(Subscriber subscriber) throws IdInvalidException {
        Optional<Subscriber> subscriberOptional = subscriberRepository.findById(subscriber.getId());

        if (subscriberOptional.isPresent()) {
            throw new IdInvalidException(" Subscriber is exists !!!");
        }

        if (subscriberRepository.existsByEmail(subscriber.getEmail())) {
            throw new IdInvalidException(" Subscriber  email is exists !!!");
        }

        if (subscriber.getSkills() == null || subscriber.getSkills().size() == 0) {
            throw new IdInvalidException(" List skill is not exists !!!");
        }

        List<Skill> skills = subscriber.getSkills().stream()
                .map(skill -> skillRepository.findById(skill.getId()).isPresent()
                        ? skillRepository.findById(skill.getId()).get()
                        : null)
                .filter(x -> x != null).collect(Collectors.toList());
        subscriber.setSkills(skills);
        return subscriberRepository.save(subscriber);
    }

    public Subscriber updateSubscriber(Subscriber subscriber) throws IdInvalidException {
        Optional<Subscriber> subscriberOptional = subscriberRepository.findById(subscriber.getId());

        if (!subscriberOptional.isPresent()) {
            throw new IdInvalidException(" Subscriber is not exists !!!");
        }

        if (subscriberRepository.existsByEmailAndIdNot(subscriber.getEmail(), subscriber.getId())) {
            throw new IdInvalidException(" Subscriber  email is exists !!!");
        }

        if (subscriber.getSkills() == null || subscriber.getSkills().size() == 0) {
            throw new IdInvalidException(" List skill is not exists !!!");
        }

        List<Skill> skills = subscriber.getSkills().stream()
                .map(skill -> skillRepository.findById(skill.getId()).isPresent()
                        ? skillRepository.findById(skill.getId()).get()
                        : null)
                .filter(x -> x != null).collect(Collectors.toList());
        subscriberOptional.get().setSkills(skills);
        if (SecurityUtil.isNotBlank(subscriber.getEmail())) {
            subscriberOptional.get().setName(subscriber.getEmail());
        }
        if (SecurityUtil.isNotBlank(subscriber.getName())) {
            subscriberOptional.get().setName(subscriber.getName());
        }

        return subscriberRepository.save(subscriberOptional.get());
    }

    public void sendSubscribersEmailJobs() {
        List<Subscriber> subscribers = this.subscriberRepository.findAll();
        if (subscribers != null && subscribers.size() > 0)
            for (Subscriber subscriber : subscribers) {
                if (subscriber.getSkills() != null && subscriber.getSkills().size() > 0) {
                    List<Job> jobs = this.jobRepository.findBySkillsIn(subscriber.getSkills());
                    if (jobs != null && jobs.size() > 0) {
                        List<ResEmailJob> arr = jobs.stream().map(job -> this.convertJobToSendEmail(job))
                                .collect(Collectors.toList());
                        this.emailService.sendEmailFromTemplateSync(subscriber.getEmail(),
                                "Cơ hội việc làm hot đang chờ đón bạn", "job",
                                subscriber.getName(), arr);
                    }
                }
            }
    }

    public ResEmailJob convertJobToSendEmail(Job job) {
        ResEmailJob res = new ResEmailJob();
        res.setName(job.getName());
        res.setSalary(job.getSalary());
        res.setCompany(new ResEmailJob.CompanyEmail(job.getCompany().getName()));
        Set<Skill> skills = job.getSkills();
        List<ResEmailJob.SkillEmail> s = skills.stream().map(skill -> new ResEmailJob.SkillEmail(skill.getName()))
                .collect(Collectors.toList());
        res.setSkills(s);
        return res;
    }

}
