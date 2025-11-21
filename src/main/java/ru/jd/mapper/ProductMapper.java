package ru.jd.mapper;

import lombok.NoArgsConstructor;
import ru.jd.model.dto.group.ProductDto;
import ru.jd.model.dto.template.AddProductTemplateRequest;
import ru.jd.model.entity.Product;
import ru.jd.model.entity.Template;

import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ProductMapper {
    public static Product toEntity(AddProductTemplateRequest request, Template template, String imagePath) {
        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setDetails(request.getDetails());
        product.setTemplate(template);
        product.setImageUrl(imagePath);

        return product;
    }

    public static List<ProductDto> toDto(List<Product> products) {
        return products.stream().map(p -> new ProductDto(p.getId(), p.getName(),
                p.getDescription(), p.getImageUrl(), p.getLine(), p.getDetails(), p.getCreatedAt())).toList();
    }
}
