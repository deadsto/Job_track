package com.futrack.application;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "job_applications")
public class JobApplication {
    @Id @Column(nullable = false, updatable = false) private UUID id;
    @Column(nullable = false, length = 120) private String company;
    @Column(nullable = false, length = 160) private String title;
    @Column(length = 160) private String location;
    @Column(name = "job_type", length = 40) private String type;
    @Column(name = "work_mode", length = 40) private String mode;
    @Column(name = "application_date", nullable = false) private LocalDate date;
    @Column(length = 600) private String url;
    @Column(length = 120) private String recruiter;
    @Column(length = 180) private String email;
    @Column(length = 80) private String salary;
    @Column(name = "follow_up_date") private LocalDate followUp;
    @Column(length = 30, nullable = false) private String status;
    @Column(length = 4000) private String description;
    @Column(length = 4000) private String notes;
    @Column(length = 255) private String resume;

    protected JobApplication() { }
    public JobApplication(UUID id, ApplicationRequest request) { this.id=id; apply(request); }
    public void apply(ApplicationRequest r) {
        company=r.company().trim(); title=r.title().trim(); location=r.location(); type=r.type(); mode=r.mode();
        date=r.date(); url=r.url(); recruiter=r.recruiter(); email=r.email(); salary=r.salary(); followUp=r.followUp();
        status=(r.status()==null||r.status().isBlank()?"Applied":r.status()); description=r.description(); notes=r.notes(); resume=r.resume();
    }
    public UUID getId(){return id;} public String getCompany(){return company;} public String getTitle(){return title;}
    public String getLocation(){return location;} public String getType(){return type;} public String getMode(){return mode;}
    public LocalDate getDate(){return date;} public String getUrl(){return url;} public String getRecruiter(){return recruiter;}
    public String getEmail(){return email;} public String getSalary(){return salary;} public LocalDate getFollowUp(){return followUp;}
    public String getStatus(){return status;} public String getDescription(){return description;} public String getNotes(){return notes;} public String getResume(){return resume;}
}
