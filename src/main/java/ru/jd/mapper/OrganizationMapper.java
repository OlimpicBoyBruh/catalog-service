package ru.jd.mapper;

import lombok.NoArgsConstructor;
import ru.jd.model.dto.organization.CreateOrganizationRequest;
import ru.jd.model.dto.organization.CreateOrganizationResponse;
import ru.jd.model.dto.MessageInfo;
import ru.jd.model.dto.organization.OrganizationDto;
import ru.jd.model.entity.Organization;

import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class OrganizationMapper {
    public static Organization toEntity(CreateOrganizationRequest createOrganizationRequest) {
        Organization organization = new Organization();
        organization.setName(createOrganizationRequest.getName());
        organization.setDescription(createOrganizationRequest.getDescription());
        return organization;
    }
    public static CreateOrganizationResponse toCreateOrganizationResponse(Organization organization) {
        CreateOrganizationResponse createOrganizationResponse = new CreateOrganizationResponse();

        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setMessageId(organization.getId());
        messageInfo.setErrorCode(0);

        createOrganizationResponse.setMessageInfo(messageInfo);

        return createOrganizationResponse;
    }

    public static List<OrganizationDto> toDto(List<Organization> organizations) {

        return organizations.stream()
                .map(o -> new OrganizationDto(o.getId(), o.getName(), o.getDescription(), o.getCreatedAt())).toList();

    }
}
