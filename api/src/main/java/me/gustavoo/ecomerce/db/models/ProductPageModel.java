package me.gustavoo.ecomerce.db.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Entity
@Table(name = "product_page")
@Data @NoArgsConstructor @RequiredArgsConstructor
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

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<ProductModel> products;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<AttributeModel> attributes;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<PageImageModel> images;
}
