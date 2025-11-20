package ru.jd.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.jd.mapper.GroupMapper;
import ru.jd.mapper.OrganizationMapper;
import ru.jd.mapper.ProductMapper;
import ru.jd.model.dto.MessageInfo;
import ru.jd.model.dto.group.AddProductGroupResponse;
import ru.jd.model.dto.group.CreateGroupRequest;
import ru.jd.model.dto.group.CreateGroupResponse;
import ru.jd.model.dto.group.GetProductsByGroupResponse;
import ru.jd.model.dto.group.ProductDto;
import ru.jd.model.dto.organization.AddProductOrganizationRequest;
import ru.jd.model.dto.organization.AddProductOrganizationResponse;
import ru.jd.model.dto.organization.CreateOrganizationRequest;
import ru.jd.model.dto.organization.CreateOrganizationResponse;
import ru.jd.model.dto.organization.GetAllOrganizationsResponse;
import ru.jd.model.dto.organization.GetGroupsOrganizationsResponse;
import ru.jd.model.dto.organization.OrganizationDto;
import ru.jd.model.entity.Group;
import ru.jd.model.entity.Organization;
import ru.jd.model.entity.Product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final OrganizationService organizationService;
    private final GroupService groupService;
    private final ProductService productService;
    @Value("${path.image}")
    private String pathStorageImage;

    public CreateOrganizationResponse saveOrganization(CreateOrganizationRequest createOrganizationRequest) {
        Organization organization = organizationService.saveOrganization(createOrganizationRequest);
        CreateOrganizationResponse createOrganizationResponse = new CreateOrganizationResponse();

        createOrganizationResponse.setMessageInfo(getMessageInfo(organization.getId()));

        return createOrganizationResponse;

    }

    public void deleteGroup(Long groupId) {
        Group group = groupService.getReferenceById(groupId);
        groupService.deleteGroup(group);
    }

    public GetAllOrganizationsResponse getAllOrganizations() {
        List<OrganizationDto> organizations = OrganizationMapper.toDto(organizationService.getAllOrganizations());
        GetAllOrganizationsResponse getAllOrganizationsResponse = new GetAllOrganizationsResponse();

        getAllOrganizationsResponse.setOrganizations(organizations);

        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setErrorCode(0);


        getAllOrganizationsResponse.setMessageInfo(messageInfo);
        return getAllOrganizationsResponse;
    }

    public CreateGroupResponse createGroup(CreateGroupRequest createGroupRequest) {
        Organization organization = organizationService.getReferenceById(createGroupRequest.getOrganizationId());
        groupService.createGroup(GroupMapper.toEntity(createGroupRequest, organization));
        CreateGroupResponse createGroupResponse = new CreateGroupResponse();

        createGroupResponse.setMessageInfo(getMessageInfo(organization.getId()));

        return createGroupResponse;
    }

    public GetGroupsOrganizationsResponse getGroupsOrganizations(Long organizationId) {
        Organization organization = organizationService.getById(organizationId);

        GetGroupsOrganizationsResponse getGroupsOrganizationsResponse = new GetGroupsOrganizationsResponse();
        if (organization != null) {
            getGroupsOrganizationsResponse.setGroups(GroupMapper.toDto(organization.getGroups()));

            getGroupsOrganizationsResponse.setMessageInfo(getMessageInfo(organizationId));
        } else {
            throw new RuntimeException("Organization not found");
        }

        return getGroupsOrganizationsResponse;
    }

    public AddProductOrganizationResponse addProductToOrganization(Long organizationId,
                                                                   AddProductOrganizationRequest addProductOrganizationRequest) {
        Organization organization = organizationService.getReferenceById(organizationId);


        Path storedImage = addProductOrganizationRequest.getImage() != null && !addProductOrganizationRequest.getImage().isEmpty()
                ? saveImage(addProductOrganizationRequest.getImage())
                : null;

        Product product = ProductMapper.toEntity(addProductOrganizationRequest, organization,
                storedImage != null ? buildPublicImagePath(storedImage) : null);

        productService.saveProduct(product);

        AddProductOrganizationResponse addProductOrganizationResponse = new AddProductOrganizationResponse();

        addProductOrganizationResponse.setMessageInfo(getMessageInfo(product.getId()));

        return addProductOrganizationResponse;
    }

    private Path saveImage(MultipartFile image) {
        Path storageDir = Paths.get(pathStorageImage);
        Path targetPath = storageDir.resolve(image.getOriginalFilename());

        try {
            Files.createDirectories(storageDir);
            image.transferTo(targetPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return targetPath;
    }

    private String buildPublicImagePath(Path storedImage) {
        return "/uploads/" + storedImage.getFileName();
    }

    private static MessageInfo getMessageInfo(Long organizationId) {
        MessageInfo messageInfo = new MessageInfo();

        messageInfo.setErrorCode(0);
        messageInfo.setMessageId(organizationId);

        return messageInfo;
    }

    private static MessageInfo getMessageInfo(Long organizationId, String message) {
        MessageInfo messageInfo = new MessageInfo();

        messageInfo.setErrorCode(0);
        messageInfo.setMessageId(organizationId);
        messageInfo.setMessage(message);

        return messageInfo;
    }


    public AddProductGroupResponse addProductToGroup(Long productId, Long groupId) {
        Product product = productService.getProductById(productId);

        Group group = groupService.getReferenceById(groupId);
        product.getGroups().add(group);
        productService.saveProduct(product);

        AddProductGroupResponse addProductGroupResponse = new AddProductGroupResponse();

        addProductGroupResponse.setMessageInfo(getMessageInfo(product.getId()));

        return addProductGroupResponse;
    }

    public GetProductsByGroupResponse getProductsByGroup(Long groupId) {
        List<Product> products = productService.getAllProductsToGroup(groupId);

        GetProductsByGroupResponse response = new GetProductsByGroupResponse();

        response.setMessageInfo(getMessageInfo(groupId));
        response.setProducts(ProductMapper.toDto(products));

        return response;
    }

    public List<ProductDto> getProductsForOrganization(Long organizationId) {
        List<Product> products = productService.getProductsByOrganization(organizationId);
        return ProductMapper.toDto(products);
    }
}
