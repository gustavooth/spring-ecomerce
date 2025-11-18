package me.gustavoo.ecomerce.db.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Data @NoArgsConstructor @RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ProductModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @ManyToOne @JsonBackReference
    private ProductPageModel page;

    @NonNull
    @Column(nullable = false)
    private Double price;

    @NonNull
    @Column(nullable = false)
    private int stock;

    @NonNull
    @Column(nullable = false, length = 255)
    private String imagePath;

    @NonNull
    @Column(nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductValueModel> values;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
