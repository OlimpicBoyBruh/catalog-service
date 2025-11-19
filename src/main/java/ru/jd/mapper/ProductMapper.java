package ru.jd.mapper;

import lombok.NoArgsConstructor;
import ru.jd.model.dto.organization.AddProductOrganizationRequest;
import ru.jd.model.entity.Organization;
import ru.jd.model.entity.Product;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ProductMapper {
    public static Product toEntity(AddProductOrganizationRequest request, Organization organization, String imagePath) {
        Product product = new Product();

        product.setName(request.getName());
        product.setBasePrice(request.getBasePrice());
        product.setDetails(request.getDetails());
        product.setOrganization(organization);
        product.setImageUrl(imagePath);

        return product;
    }
}
