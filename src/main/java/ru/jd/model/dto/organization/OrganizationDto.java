package ru.jd.model.dto.organization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
public class OrganizationDto {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}
