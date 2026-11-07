package com.futrack.application;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ApplicationRequest(
    @NotBlank @Size(max=120) String company,
    @NotBlank @Size(max=160) String title,
    @Size(max=160) String location,
    @Size(max=40) String type,
    @Size(max=40) String mode,
    @NotNull LocalDate date,
    @Size(max=600) String url,
    @Size(max=120) String recruiter,
    @Email @Size(max=180) String email,
    @Size(max=80) String salary,
    LocalDate followUp,
    @Pattern(regexp="(?i)Applied|Interview|Offer|Rejected|Withdrawn") @Size(max=30) String status,
    @Size(max=4000) String description,
    @Size(max=4000) String notes,
    @Size(max=255) String resume
) { }
