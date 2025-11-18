package me.gustavoo.ecomerce.db.repository;

import me.gustavoo.ecomerce.db.models.AttributeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeRepository extends JpaRepository<AttributeModel, Long> {
    List<AttributeModel> findAllByPageIdAndActive(long pageId, boolean active);
}
