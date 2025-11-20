package ru.jd.mapper;

import lombok.NoArgsConstructor;
import ru.jd.model.dto.group.ProductDto;
import ru.jd.model.dto.organization.AddProductOrganizationRequest;
import ru.jd.model.entity.Organization;
import ru.jd.model.entity.Product;

import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ProductMapper {
    public static Product toEntity(AddProductOrganizationRequest request, Organization organization, String imagePath) {
        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setBasePrice(request.getBasePrice());
        product.setDetails(request.getDetails());
        product.setOrganization(organization);
        product.setImageUrl(imagePath);

        return product;
    }

    public static List<ProductDto> toDto(List<Product> products) {
        return products.stream().map(p -> new ProductDto(p.getId(), p.getName(),
                p.getDescription(), p.getBasePrice(), p.getImageUrl(), p.getDetails(), p.getCreatedAt())).toList();
    }
}
