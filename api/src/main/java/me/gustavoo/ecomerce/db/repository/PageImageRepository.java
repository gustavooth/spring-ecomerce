package me.gustavoo.ecomerce.db.repository;

import me.gustavoo.ecomerce.db.models.PageImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageImageRepository extends JpaRepository<PageImageModel, Long> {
    List<PageImageModel> findAllByPageId(long pageId);
}
