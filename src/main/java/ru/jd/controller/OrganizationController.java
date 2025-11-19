package ru.jd.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.jd.model.dto.organization.AddProductOrganizationRequest;
import ru.jd.model.dto.organization.AddProductOrganizationResponse;
import ru.jd.model.dto.organization.CreateOrganizationRequest;
import ru.jd.model.dto.organization.CreateOrganizationResponse;
import ru.jd.model.dto.organization.GetAllOrganizationsResponse;
import ru.jd.model.dto.organization.GetGroupsOrganizationsResponse;
import ru.jd.service.ManagerService;

@RestController
@RequestMapping("/api/v0/organization")
@RequiredArgsConstructor
public class OrganizationController {
    private final ManagerService managerService;

    @PostMapping("/create")
    public CreateOrganizationResponse createOrganization(@RequestBody CreateOrganizationRequest createOrganizationRequest) {
        return managerService.saveOrganization(createOrganizationRequest);
    }

    @GetMapping("/")
    public GetAllOrganizationsResponse getAllOrganizations() {
        return managerService.getAllOrganizations();
    }

    @GetMapping("/{organizationId}/groups")
    public GetGroupsOrganizationsResponse getGroupsOrganizations(@PathVariable("organizationId") Long organizationId) {
        return managerService.getGroupsOrganizations(organizationId);
    }

    @PostMapping(value = "/{organizationId}/product/add",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AddProductOrganizationResponse addProductToOrganization(
            @PathVariable("organizationId") Long organizationId,
            @ModelAttribute AddProductOrganizationRequest addProductOrganizationRequest
            ) {
        return managerService.addProductToOrganization(organizationId, addProductOrganizationRequest);
    }
}
