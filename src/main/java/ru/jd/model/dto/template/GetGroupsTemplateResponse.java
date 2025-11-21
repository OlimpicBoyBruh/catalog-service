package ru.jd.model.dto.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import ru.jd.model.dto.MessageInfo;
import ru.jd.model.dto.group.GroupDto;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class GetGroupsTemplateResponse {
    private List<GroupDto> groups;
    private MessageInfo messageInfo;
}
