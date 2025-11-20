package ru.jd.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.jd.model.entity.Group;
import ru.jd.repository.GroupRepository;

@RequiredArgsConstructor
@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }

    public Group getReferenceById(Long id) {
        return groupRepository.getReferenceById(id);
    }

    public Group getById(Long id) {
        return groupRepository.findById(id).orElse(null);
    }

    public void deleteGroup(Group group) {
        groupRepository.delete(group);
    }
}
