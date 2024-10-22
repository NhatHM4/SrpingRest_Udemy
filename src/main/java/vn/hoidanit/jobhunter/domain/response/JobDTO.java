package vn.hoidanit.jobhunter.domain.response;

import java.time.Instant;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.util.constant.LevelEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobDTO {
    private long id;

    private String name;
    private String location;
    private double salary;
    private int quantity;
    private LevelEnum level;

    private String description;

    private Instant startDate;
    private Instant endDate;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private Set<Skill> skills;
    private Company company;
}
