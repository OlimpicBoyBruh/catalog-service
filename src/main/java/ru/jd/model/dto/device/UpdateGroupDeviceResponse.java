package ru.jd.model.dto.device;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import ru.jd.model.dto.MessageInfo;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
public class UpdateGroupDeviceResponse {
    private MessageInfo messageInfo;
}
