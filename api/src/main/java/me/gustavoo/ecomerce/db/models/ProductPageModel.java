package me.gustavoo.ecomerce.db.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product_page")
@Data @NoArgsConstructor @RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ProductPageModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull @Column(length = 255, nullable = false)
    private String title;

    @NonNull @Column(length = 255, nullable = false, unique = true)
    private String slug;

    @NonNull @Column(name = "short_description", length = 500, nullable = false)
    private String shortDescription;

    @Lob
    @NonNull @Column(nullable = false)
    private String description;

    @NonNull
    @Column(nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<ProductModel> products;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<AttributeModel> attributes;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<PageImageModel> images;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
