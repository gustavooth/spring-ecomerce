package me.gustavoo.ecomerce.db.repository;

import me.gustavoo.ecomerce.db.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductModel, Long> {
}
