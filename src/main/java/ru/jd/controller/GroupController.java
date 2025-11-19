package ru.jd.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.jd.model.dto.group.AddProductGroupResponse;
import ru.jd.model.dto.group.CreateGroupRequest;
import ru.jd.model.dto.group.CreateGroupResponse;
import ru.jd.service.ManagerService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v0/group")
public class GroupController {

    private final ManagerService managerService;


    @PostMapping("/create")
    public CreateGroupResponse createGroup(@RequestBody CreateGroupRequest createGroupRequest) {
        return managerService.createGroup(createGroupRequest);
    }

    @PostMapping("/add/product")
    public AddProductGroupResponse addProduct(@RequestParam("productId") Long productId, @RequestParam("groupId") Long groupId) {
        return managerService.addProductToGroup(productId, groupId);
    }

    @GetMapping("/{groupId}/products")
    public GetProductsByGroupResponse getProductsByGroup(@RequestParam("groupId") Long groupId) {
        return managerService.getProductsByGroup(groupId);
    }
}
