package ru.jd.model.dto.device;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
public class UpdateGroupDeviceRequest {
    private Long deviceId;
    private Long groupId;
    private Long organizationId;

}
