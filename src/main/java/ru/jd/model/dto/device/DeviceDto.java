package ru.jd.model.dto.device;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
@AllArgsConstructor
public class DeviceDto {
    private Long id;
    private String name;
    private String deviceCode;
    private String location;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private Long groupId;
    private String groupName;
}
