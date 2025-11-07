package me.gustavoo.ecomerce.db.repository;

import me.gustavoo.ecomerce.db.models.ProductPageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPageRepository extends JpaRepository<ProductPageModel, Long> {
    public ProductPageModel findBySlug(String slug);
}
