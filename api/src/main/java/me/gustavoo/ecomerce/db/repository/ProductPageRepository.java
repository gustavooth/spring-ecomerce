package me.gustavoo.ecomerce.db.repository;

import me.gustavoo.ecomerce.db.models.ProductPageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductPageRepository extends JpaRepository<ProductPageModel, Long> {
    public Optional<ProductPageModel> findBySlug(String slug);
}
