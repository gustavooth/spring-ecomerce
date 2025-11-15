package me.gustavoo.ecomerce.db.repository;

import me.gustavoo.ecomerce.db.models.AttributeValueModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface AttributeValuesRepository extends JpaRepository<AttributeValueModel, Long> {
    ArrayList<AttributeValueModel> findAllByAttributeId(long attributeId);
}
