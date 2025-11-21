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
import ru.jd.model.dto.template.AddProductTemplateRequest;
import ru.jd.model.dto.template.CreateTemplateRequest;
import ru.jd.model.dto.template.TemplateDto;
import ru.jd.model.entity.Template;
import ru.jd.service.GroupService;
import ru.jd.service.ManagerService;
import ru.jd.service.TemplateService;
import ru.jd.web.form.ProductForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PortalController {

    private final ManagerService managerService;
    private final TemplateService templateService;
    private final GroupService groupService;

    @GetMapping("/")
    public String dashboard(Model model,
                            @ModelAttribute("toastError") String toastError,
                            @ModelAttribute("toastSuccess") String toastSuccess) {
        var response = managerService.getAllTemplate();
        List<TemplateDto> template = response.getTemplate() != null
                ? response.getTemplate()
                : List.of();

        if (!model.containsAttribute("templateForm")) {
            model.addAttribute("templateForm", new CreateTemplateRequest());
        }
        model.addAttribute("template", template);
        model.addAttribute("toastError", StringUtils.hasText(toastError) ? toastError : null);
        model.addAttribute("toastSuccess", StringUtils.hasText(toastSuccess) ? toastSuccess : null);

        return "dashboard";
    }

    @PostMapping("/template")
    public String createTemplate(@ModelAttribute CreateTemplateRequest templateForm,
                                     RedirectAttributes redirectAttributes) {
        try {
            managerService.saveTemplate(templateForm);
            redirectAttributes.addFlashAttribute("toastSuccess", "Шаблон успешно создана");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("toastError", "Не удалось создать шаблон: " + ex.getMessage());
            redirectAttributes.addFlashAttribute("templateForm", templateForm);
        }
        return "redirect:/";
    }

    @GetMapping("/template/{templateId}")
    public String viewTemplate(@PathVariable Long templateId, Model model,
                                   @ModelAttribute("toastError") String toastError,
                                   @ModelAttribute("toastSuccess") String toastSuccess,
                                   @RequestParam(value = "tab", defaultValue = "products") String tab,
                                   @ModelAttribute("productForm") ProductForm productForm) {
        String activeTab = normalizeTab(tab);
        Template template = templateService.getById(templateId);

        if (template == null) {
            return "redirect:/";
        }

        var groupsResponse = managerService.getGroupsTemplate(templateId);
        List<GroupDto> groups = groupsResponse.getGroups() != null ? groupsResponse.getGroups() : List.of();

        Map<Long, Integer> productCounts = loadProductsCountForGroups(groups);

        model.addAttribute("template", template);
        model.addAttribute("groups", groups);
        model.addAttribute("groupProductCounts", productCounts);
        model.addAttribute("toastError", StringUtils.hasText(toastError) ? toastError : null);
        model.addAttribute("toastSuccess", StringUtils.hasText(toastSuccess) ? toastSuccess : null);
        model.addAttribute("activeTab", activeTab);

        if (!model.containsAttribute("groupForm")) {
            CreateGroupRequest groupForm = new CreateGroupRequest();
            groupForm.setTemplateId(templateId);
            model.addAttribute("groupForm", groupForm);
        }

        model.addAttribute("allProducts", managerService.getProductsForTemplate(templateId));
        ProductForm effectiveProductForm = productForm;
        if (effectiveProductForm == null || effectiveProductForm.getTemplateId() == null) {
            effectiveProductForm = new ProductForm();
            effectiveProductForm.setTemplateId(templateId);
        }
        effectiveProductForm.ensureDetailRows(1);
        model.addAttribute("productForm", effectiveProductForm);

        return "template";
    }

    @PostMapping("/template/{templateId}/groups/{groupId}/delete")
    public String deleteGroup(@PathVariable Long templateId,
                              @PathVariable Long groupId,
                              RedirectAttributes redirectAttributes) {
        try {
            managerService.deleteGroup(groupId);
            redirectAttributes.addFlashAttribute("toastSuccess", "Группа успешно удалена");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("toastError", "Не удалось удалить группу: " + ex.getMessage());
        }
        return redirectToTemplate(templateId, "products");
    }

    @PostMapping("/template/{templateId}/groups")
    public String createGroup(@PathVariable Long templateId,
                              @ModelAttribute("groupForm") CreateGroupRequest groupForm,
                              RedirectAttributes redirectAttributes) {
        try {
            groupForm.setTemplateId(templateId);
            managerService.createGroup(groupForm);
            redirectAttributes.addFlashAttribute("toastSuccess", "Группа создана");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("toastError", "Ошибка при создании группы: " + ex.getMessage());
            redirectAttributes.addFlashAttribute("groupForm", groupForm);
        }
        return redirectToTemplate(templateId, "products");
    }

    @PostMapping("/template/{templateId}/products")
    public String addProduct(@PathVariable Long templateId,
                             @ModelAttribute("productForm") ProductForm productForm,
                             RedirectAttributes redirectAttributes) {
        try {
            var request = new AddProductTemplateRequest();
            request.setName(productForm.getName());
            request.setDescription(productForm.getDescription());
            request.setTemplateId(templateId);
            request.setDetails(productForm.toDetailsMap());
            request.setImage(productForm.getImage());
            request.setLine(productForm.getLine());

            managerService.addProductToTemplate(templateId, request);
            redirectAttributes.addFlashAttribute("toastSuccess", "Продукт добавлен");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("toastError", "Не удалось добавить продукт: " + ex.getMessage());
            redirectAttributes.addFlashAttribute("productForm", sanitizeProductForm(productForm));
        }
        return redirectToTemplate(templateId, "products");
    }

    @PostMapping("/template/{templateId}/groups/{groupId}/products")
    public String attachProductToGroup(@PathVariable Long templateId,
                                       @PathVariable Long groupId,
                                       @RequestParam("productId") Long productId,
                                       RedirectAttributes redirectAttributes) {
        try {
            managerService.addProductToGroup(productId, groupId);
            redirectAttributes.addFlashAttribute("toastSuccess", "Продукт привязан к группе");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("toastError", "Не удалось привязать продукт: " + ex.getMessage());
        }
        return redirectToTemplate(templateId, "products");
    }

    @GetMapping("/template/{templateId}/groups/{groupId}")
    public String viewGroup(@PathVariable Long templateId,
                            @PathVariable Long groupId,
                            Model model,
                            @ModelAttribute("toastError") String toastError,
                            @ModelAttribute("toastSuccess") String toastSuccess) {
        Template template = templateService.getById(templateId);
        if (template == null) {
            return "redirect:/";
        }
        var group = groupService.getById(groupId);
        if (group == null || !group.getTemplate().getId().equals(templateId)) {
            return redirectToTemplate(templateId, "products");
        }

        GetProductsByGroupResponse productsResponse = managerService.getProductsByGroup(groupId);
        List<ProductDto> products = productsResponse.getProducts() != null ? productsResponse.getProducts() : List.of();
        List<ProductDto> allProducts = managerService.getProductsForTemplate(templateId);

        model.addAttribute("template", template);
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

    private String redirectToTemplate(Long templateId, String tab) {
        return "redirect:/template/" + templateId + "?tab=" + normalizeTab(tab);
    }

    private String normalizeTab(String tab) {
        return "products";
    }

    private ProductForm sanitizeProductForm(ProductForm source) {
        ProductForm clone = new ProductForm();
        clone.setTemplateId(source.getTemplateId());
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