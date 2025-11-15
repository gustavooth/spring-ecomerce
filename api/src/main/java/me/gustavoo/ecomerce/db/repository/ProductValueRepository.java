package me.gustavoo.ecomerce.db.repository;

import me.gustavoo.ecomerce.db.models.ProductValueModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductValueRepository extends JpaRepository<ProductValueModel, Long> {
    List<ProductValueModel> findAllByProductId(long productId);
}
