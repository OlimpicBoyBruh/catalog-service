package ru.jd.model.dto.organization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.jd.model.dto.MessageInfo;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class GetAllOrganizationsResponse {
    private List<OrganizationDto> organizations;
    private MessageInfo messageInfo;
}
