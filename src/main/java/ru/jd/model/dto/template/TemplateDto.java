package ru.jd.model.dto.template;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
public class TemplateDto {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}
