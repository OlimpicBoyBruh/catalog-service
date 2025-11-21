package ru.jd.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.jd.mapper.GroupMapper;
import ru.jd.mapper.ProductMapper;
import ru.jd.mapper.TemplateMapper;
import ru.jd.model.dto.MessageInfo;
import ru.jd.model.dto.group.AddProductGroupResponse;
import ru.jd.model.dto.group.CreateGroupRequest;
import ru.jd.model.dto.group.CreateGroupResponse;
import ru.jd.model.dto.group.GetProductsByGroupResponse;
import ru.jd.model.dto.group.ProductDto;
import ru.jd.model.dto.template.AddProductTemplateRequest;
import ru.jd.model.dto.template.AddProductTemplateResponse;
import ru.jd.model.dto.template.CreateTemplateRequest;
import ru.jd.model.dto.template.CreateTemplateResponse;
import ru.jd.model.dto.template.GetAllTemplateResponse;
import ru.jd.model.dto.template.GetGroupsTemplateResponse;
import ru.jd.model.dto.template.TemplateDto;
import ru.jd.model.entity.Group;
import ru.jd.model.entity.Product;
import ru.jd.model.entity.Template;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final TemplateService templateService;
    private final GroupService groupService;
    private final ProductService productService;
    @Value("${path.image}")
    private String pathStorageImage;

    public CreateTemplateResponse saveTemplate(CreateTemplateRequest createtemplateRequest) {
        Template template = templateService.saveTemplate(createtemplateRequest);
        CreateTemplateResponse createtemplateResponse = new CreateTemplateResponse();

        createtemplateResponse.setMessageInfo(getMessageInfo(template.getId()));

        return createtemplateResponse;

    }

    public void deleteGroup(Long groupId) {
        Group group = groupService.getReferenceById(groupId);
        groupService.deleteGroup(group);
    }

    public GetAllTemplateResponse getAllTemplate() {
        List<TemplateDto> template = TemplateMapper.toDto(templateService.getAllTemplate());
        GetAllTemplateResponse getAllTemplateResponse = new GetAllTemplateResponse();

        getAllTemplateResponse.setTemplate(template);

        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setErrorCode(0);


        getAllTemplateResponse.setMessageInfo(messageInfo);
        return getAllTemplateResponse;
    }

    public CreateGroupResponse createGroup(CreateGroupRequest createGroupRequest) {
        Template template = templateService.getReferenceById(createGroupRequest.getTemplateId());
        groupService.createGroup(GroupMapper.toEntity(createGroupRequest, template));
        CreateGroupResponse createGroupResponse = new CreateGroupResponse();

        createGroupResponse.setMessageInfo(getMessageInfo(template.getId()));

        return createGroupResponse;
    }

    public GetGroupsTemplateResponse getGroupsTemplate(Long templateId) {
        Template template = templateService.getById(templateId);

        GetGroupsTemplateResponse getGroupstemplateResponse = new GetGroupsTemplateResponse();
        if (template != null) {
            getGroupstemplateResponse.setGroups(GroupMapper.toDto(template.getGroups()));

            getGroupstemplateResponse.setMessageInfo(getMessageInfo(templateId));
        } else {
            throw new RuntimeException("template not found");
        }

        return getGroupstemplateResponse;
    }

    public AddProductTemplateResponse addProductToTemplate(Long templateId,
                                                           AddProductTemplateRequest addProducttemplateRequest) {
        Template template = templateService.getReferenceById(templateId);


        Path storedImage = addProducttemplateRequest.getImage() != null && !addProducttemplateRequest.getImage().isEmpty()
                ? saveImage(addProducttemplateRequest.getImage())
                : null;

        Product product = ProductMapper.toEntity(addProducttemplateRequest, template,
                storedImage != null ? buildPublicImagePath(storedImage) : null);

        productService.saveProduct(product);

        AddProductTemplateResponse addProductTemplateResponse = new AddProductTemplateResponse();

        addProductTemplateResponse.setMessageInfo(getMessageInfo(product.getId()));

        return addProductTemplateResponse;
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

    private static MessageInfo getMessageInfo(Long templateId) {
        MessageInfo messageInfo = new MessageInfo();

        messageInfo.setErrorCode(0);
        messageInfo.setMessageId(templateId);

        return messageInfo;
    }

    private static MessageInfo getMessageInfo(Long templateId, String message) {
        MessageInfo messageInfo = new MessageInfo();

        messageInfo.setErrorCode(0);
        messageInfo.setMessageId(templateId);
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

    public List<ProductDto> getProductsForTemplate(Long templateId) {
        List<Product> products = productService.getProductsByTemplate(templateId);
        return ProductMapper.toDto(products);
    }
}
