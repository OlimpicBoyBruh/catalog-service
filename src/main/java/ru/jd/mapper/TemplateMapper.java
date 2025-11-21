package ru.jd.mapper;

import lombok.NoArgsConstructor;
import ru.jd.model.dto.MessageInfo;
import ru.jd.model.dto.template.CreateTemplateRequest;
import ru.jd.model.dto.template.CreateTemplateResponse;
import ru.jd.model.dto.template.TemplateDto;
import ru.jd.model.entity.Template;

import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class TemplateMapper {
    public static Template toEntity(CreateTemplateRequest createTemplateRequest) {
        Template template = new Template();
        template.setName(createTemplateRequest.getName());
        template.setDescription(createTemplateRequest.getDescription());
        return template;
    }
    public static CreateTemplateResponse toCreateTemplateResponse(Template template) {
        CreateTemplateResponse createTemplateResponse = new CreateTemplateResponse();

        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setMessageId(template.getId());
        messageInfo.setErrorCode(0);

        createTemplateResponse.setMessageInfo(messageInfo);

        return createTemplateResponse;
    }

    public static List<TemplateDto> toDto(List<Template> template) {

        return template.stream()
                .map(o -> new TemplateDto(o.getId(), o.getName(), o.getDescription(), o.getCreatedAt())).toList();

    }
}
