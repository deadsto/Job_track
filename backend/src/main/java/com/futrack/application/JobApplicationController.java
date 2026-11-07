package com.futrack.application;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {
    private final JobApplicationRepository repository;
    public JobApplicationController(JobApplicationRepository repository) { this.repository=repository; }

    @GetMapping public List<JobApplication> list(@RequestParam(required=false) String q,
        @RequestParam(required=false) String status, @RequestParam(required=false) String type,
        @RequestParam(defaultValue="newest") String sort) {
        var stream=repository.findAll().stream();
        if(q!=null&&!q.isBlank()){String term=q.toLowerCase();stream=stream.filter(a->a.getCompany().toLowerCase().contains(term)||a.getTitle().toLowerCase().contains(term));}
        if(status!=null&&!status.isBlank())stream=stream.filter(a->a.getStatus().equalsIgnoreCase(status));
        if(type!=null&&!type.isBlank())stream=stream.filter(a->type.equalsIgnoreCase(a.getType()));
        Comparator<JobApplication> order="company".equalsIgnoreCase(sort)?Comparator.comparing(JobApplication::getCompany,String.CASE_INSENSITIVE_ORDER):Comparator.comparing(JobApplication::getDate,Comparator.nullsLast(Comparator.naturalOrder()));
        if(!"oldest".equalsIgnoreCase(sort)&&!"company".equalsIgnoreCase(sort))order=order.reversed();
        return stream.sorted(order).toList();
    }
    @GetMapping("/{id}") public JobApplication get(@PathVariable UUID id){return repository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Application not found"));}
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public JobApplication create(@Valid @RequestBody ApplicationRequest request){return repository.save(new JobApplication(UUID.randomUUID(),request));}
    @PutMapping("/{id}") public JobApplication update(@PathVariable UUID id,@Valid @RequestBody ApplicationRequest request){var app=get(id);app.apply(request);return repository.save(app);}
    @PatchMapping("/{id}/status") public JobApplication updateStatus(@PathVariable UUID id,@Valid @RequestBody StatusRequest request){var app=get(id);final String normalized;try{String value=ApplicationStatus.valueOf(request.status().toUpperCase()).name();normalized=value.charAt(0)+value.substring(1).toLowerCase();}catch(Exception e){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid application status");}app.apply(new ApplicationRequest(app.getCompany(),app.getTitle(),app.getLocation(),app.getType(),app.getMode(),app.getDate(),app.getUrl(),app.getRecruiter(),app.getEmail(),app.getSalary(),app.getFollowUp(),normalized,app.getDescription(),app.getNotes(),app.getResume()));return repository.save(app);}
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable UUID id){if(!repository.existsById(id))throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Application not found");repository.deleteById(id);}
    public record StatusRequest(@jakarta.validation.constraints.NotBlank String status) { }
}
