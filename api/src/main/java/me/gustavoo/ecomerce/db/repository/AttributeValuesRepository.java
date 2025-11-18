package me.gustavoo.ecomerce.db.repository;

import me.gustavoo.ecomerce.db.models.AttributeValueModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeValuesRepository extends JpaRepository<AttributeValueModel, Long> {
    List<AttributeValueModel> findAllByAttributeIdAndActive(long attributeId, boolean active);
}
