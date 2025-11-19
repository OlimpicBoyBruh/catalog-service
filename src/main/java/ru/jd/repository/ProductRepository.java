package ru.jd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.jd.model.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
