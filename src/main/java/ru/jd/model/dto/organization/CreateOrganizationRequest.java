package ru.jd.model.dto.organization;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrganizationRequest {
    private String name;
    private String description;
}
