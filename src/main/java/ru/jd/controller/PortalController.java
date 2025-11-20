package ru.jd.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.jd.model.dto.group.CreateGroupRequest;
import ru.jd.model.dto.group.GetProductsByGroupResponse;
import ru.jd.model.dto.group.GroupDto;
import ru.jd.model.dto.group.ProductDto;
import ru.jd.model.dto.organization.AddProductOrganizationRequest;
import ru.jd.model.dto.organization.CreateOrganizationRequest;
import ru.jd.model.dto.organization.OrganizationDto;
import ru.jd.model.entity.Organization;
import ru.jd.service.GroupService;
import ru.jd.service.ManagerService;
import ru.jd.service.OrganizationService;
import ru.jd.web.form.ProductForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PortalController {

    private final ManagerService managerService;
    private final OrganizationService organizationService;
    private final GroupService groupService;

    @GetMapping("/")
    public String dashboard(Model model,
                            @ModelAttribute("toastError") String toastError,
                            @ModelAttribute("toastSuccess") String toastSuccess) {
        var response = managerService.getAllOrganizations();
        List<OrganizationDto> organizations = response.getOrganizations() != null
                ? response.getOrganizations()
                : List.of();

        if (!model.containsAttribute("organizationForm")) {
            model.addAttribute("organizationForm", new CreateOrganizationRequest());
        }
        model.addAttribute("organizations", organizations);
        model.addAttribute("toastError", StringUtils.hasText(toastError) ? toastError : null);
        model.addAttribute("toastSuccess", StringUtils.hasText(toastSuccess) ? toastSuccess : null);

        return "dashboard";
    }

    @PostMapping("/organizations")
    public String createOrganization(@ModelAttribute CreateOrganizationRequest organizationForm,
                                     RedirectAttributes redirectAttributes) {
        try {
            managerService.saveOrganization(organizationForm);
            redirectAttributes.addFlashAttribute("toastSuccess", "Организация успешно создана");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("toastError", "Не удалось создать организацию: " + ex.getMessage());
            redirectAttributes.addFlashAttribute("organizationForm", organizationForm);
        }
        return "redirect:/";
    }

    @GetMapping("/organizations/{organizationId}")
    public String viewOrganization(@PathVariable Long organizationId, Model model,
                                   @ModelAttribute("toastError") String toastError,
                                   @ModelAttribute("toastSuccess") String toastSuccess,
                                   @RequestParam(value = "tab", defaultValue = "products") String tab,
                                   @ModelAttribute("productForm") ProductForm productForm) {
        String activeTab = normalizeTab(tab);
        Organization organization = organizationService.getById(organizationId);

        if (organization == null) {
            return "redirect:/";
        }

        var groupsResponse = managerService.getGroupsOrganizations(organizationId);
        List<GroupDto> groups = groupsResponse.getGroups() != null ? groupsResponse.getGroups() : List.of();

        Map<Long, Integer> productCounts = loadProductsCountForGroups(groups);

        model.addAttribute("organization", organization);
        model.addAttribute("groups", groups);
        model.addAttribute("groupProductCounts", productCounts);
        model.addAttribute("toastError", StringUtils.hasText(toastError) ? toastError : null);
        model.addAttribute("toastSuccess", StringUtils.hasText(toastSuccess) ? toastSuccess : null);
        model.addAttribute("activeTab", activeTab);

        if (!model.containsAttribute("groupForm")) {
            CreateGroupRequest groupForm = new CreateGroupRequest();
            groupForm.setOrganizationId(organizationId);
            model.addAttribute("groupForm", groupForm);
        }

        model.addAttribute("allProducts", managerService.getProductsForOrganization(organizationId));
        ProductForm effectiveProductForm = productForm;
        if (effectiveProductForm == null || effectiveProductForm.getOrganizationId() == null) {
            effectiveProductForm = new ProductForm();
            effectiveProductForm.setOrganizationId(organizationId);
        }
        effectiveProductForm.ensureDetailRows(1);
        model.addAttribute("productForm", effectiveProductForm);

        return "organization";
    }

    @PostMapping("/organizations/{organizationId}/groups/{groupId}/delete")
    public String deleteGroup(@PathVariable Long organizationId,
                              @PathVariable Long groupId,
                              RedirectAttributes redirectAttributes) {
        try {
            managerService.deleteGroup(groupId);
            redirectAttributes.addFlashAttribute("toastSuccess", "Группа успешно удалена");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("toastError", "Не удалось удалить группу: " + ex.getMessage());
        }
        return redirectToOrganization(organizationId, "products");
    }

    @PostMapping("/organizations/{organizationId}/groups")
    public String createGroup(@PathVariable Long organizationId,
                              @ModelAttribute("groupForm") CreateGroupRequest groupForm,
                              RedirectAttributes redirectAttributes) {
        try {
            groupForm.setOrganizationId(organizationId);
            managerService.createGroup(groupForm);
            redirectAttributes.addFlashAttribute("toastSuccess", "Группа создана");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("toastError", "Ошибка при создании группы: " + ex.getMessage());
            redirectAttributes.addFlashAttribute("groupForm", groupForm);
        }
        return redirectToOrganization(organizationId, "products");
    }

    @PostMapping("/organizations/{organizationId}/products")
    public String addProduct(@PathVariable Long organizationId,
                             @ModelAttribute("productForm") ProductForm productForm,
                             RedirectAttributes redirectAttributes) {
        try {
            var request = new AddProductOrganizationRequest();
            request.setName(productForm.getName());
            request.setDescription(productForm.getDescription());
            request.setOrganizationId(organizationId);
            request.setDetails(productForm.toDetailsMap());
            request.setImage(productForm.getImage());

            managerService.addProductToOrganization(organizationId, request);
            redirectAttributes.addFlashAttribute("toastSuccess", "Продукт добавлен");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("toastError", "Не удалось добавить продукт: " + ex.getMessage());
            redirectAttributes.addFlashAttribute("productForm", sanitizeProductForm(productForm));
        }
        return redirectToOrganization(organizationId, "products");
    }

    @PostMapping("/organizations/{organizationId}/groups/{groupId}/products")
    public String attachProductToGroup(@PathVariable Long organizationId,
                                       @PathVariable Long groupId,
                                       @RequestParam("productId") Long productId,
                                       RedirectAttributes redirectAttributes) {
        try {
            managerService.addProductToGroup(productId, groupId);
            redirectAttributes.addFlashAttribute("toastSuccess", "Продукт привязан к группе");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("toastError", "Не удалось привязать продукт: " + ex.getMessage());
        }
        return redirectToOrganization(organizationId, "products");
    }

    @GetMapping("/organizations/{organizationId}/groups/{groupId}")
    public String viewGroup(@PathVariable Long organizationId,
                            @PathVariable Long groupId,
                            Model model,
                            @ModelAttribute("toastError") String toastError,
                            @ModelAttribute("toastSuccess") String toastSuccess) {
        Organization organization = organizationService.getById(organizationId);
        if (organization == null) {
            return "redirect:/";
        }
        var group = groupService.getById(groupId);
        if (group == null || !group.getOrganization().getId().equals(organizationId)) {
            return redirectToOrganization(organizationId, "products");
        }

        GetProductsByGroupResponse productsResponse = managerService.getProductsByGroup(groupId);
        List<ProductDto> products = productsResponse.getProducts() != null ? productsResponse.getProducts() : List.of();
        List<ProductDto> allProducts = managerService.getProductsForOrganization(organizationId);

        model.addAttribute("organization", organization);
        model.addAttribute("group", group);
        model.addAttribute("products", products);
        model.addAttribute("allProducts", allProducts);
        model.addAttribute("productCount", products.size());
        model.addAttribute("toastError", StringUtils.hasText(toastError) ? toastError : null);
        model.addAttribute("toastSuccess", StringUtils.hasText(toastSuccess) ? toastSuccess : null);

        return "group";
    }

    private Map<Long, Integer> loadProductsCountForGroups(List<GroupDto> groups) {
        Map<Long, Integer> productsCount = new HashMap<>();
        if (groups == null) {
            return productsCount;
        }
        for (GroupDto group : groups) {
            var response = managerService.getProductsByGroup(group.getId());
            int count = response.getProducts() != null ? response.getProducts().size() : 0;
            productsCount.put(group.getId(), count);
        }
        return productsCount;
    }

    private String redirectToOrganization(Long organizationId, String tab) {
        return "redirect:/organizations/" + organizationId + "?tab=" + normalizeTab(tab);
    }

    private String normalizeTab(String tab) {
        return "products";
    }

    private ProductForm sanitizeProductForm(ProductForm source) {
        ProductForm clone = new ProductForm();
        clone.setOrganizationId(source.getOrganizationId());
        clone.setName(source.getName());
        clone.setDescription(source.getDescription());
        if (source.getDetails() != null) {
            List<ProductForm.DetailRow> rows = new ArrayList<>();
            for (ProductForm.DetailRow row : source.getDetails()) {
                if (row == null) {
                    continue;
                }
                ProductForm.DetailRow copy = new ProductForm.DetailRow();
                copy.setKey(row.getKey());
                copy.setValue(row.getValue());
                rows.add(copy);
            }
            clone.setDetails(rows);
        }
        clone.ensureDetailRows(1);
        return clone;
    }
}