package ru.jd.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.jd.mapper.OrganizationMapper;
import ru.jd.model.dto.organization.CreateOrganizationRequest;
import ru.jd.model.entity.Organization;
import ru.jd.repository.OrganizationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    public Organization saveOrganization(CreateOrganizationRequest createOrganizationRequest) {
        return organizationRepository.save(OrganizationMapper.toEntity(createOrganizationRequest));

    }

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();

    }

    public Organization getReferenceById(Long organizationId) {
        return organizationRepository.getReferenceById(organizationId);
    }

    public Organization getById(Long organizationId) {
        return organizationRepository.findById(organizationId).orElse(null);
    }
}
