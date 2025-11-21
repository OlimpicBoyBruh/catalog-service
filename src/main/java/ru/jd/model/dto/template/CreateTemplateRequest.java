package ru.jd.model.dto.template;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTemplateRequest {
    private String name;
    private String description;
}
