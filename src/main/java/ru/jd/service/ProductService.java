package ru.jd.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.jd.model.entity.Product;
import ru.jd.repository.ProductRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
    public List<Product> getAllProductsToGroup(Long groupId) {
        return productRepository.findProductByGroupsId(groupId);
    }

    public List<Product> getProductsByTemplate(Long templateId) {
        return productRepository.findByTemplateId(templateId);
    }
}
