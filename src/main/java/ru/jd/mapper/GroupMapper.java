package ru.jd.mapper;

import lombok.NoArgsConstructor;
import ru.jd.model.dto.group.CreateGroupRequest;
import ru.jd.model.dto.group.GroupDto;
import ru.jd.model.entity.Group;
import ru.jd.model.entity.Organization;

import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class GroupMapper {
    public static Group toEntity(CreateGroupRequest createGroupRequest, Organization organization) {
        Group group = new Group();

        group.setName(createGroupRequest.getName());
        group.setDescription(createGroupRequest.getDescription());
        group.setOrganization(organization);

        return group;
    }

    public static List<GroupDto> toDto(List<Group> groups) {
        return groups.stream()
                .map(group ->
                        new GroupDto(group.getId(),
                                group.getName(),
                                group.getDescription(),
                                group.getSubgroups(),
                                group.getCreatedAt()))
                .toList();
    }
}
