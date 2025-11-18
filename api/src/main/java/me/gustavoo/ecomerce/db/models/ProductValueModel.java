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
@Table(name = "product_values")
@Data
@NoArgsConstructor @RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ProductValueModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @ManyToOne
    private AttributeValueModel attributeValue;

    @NonNull
    @ManyToOne @JsonBackReference
    private ProductModel product;

    @NonNull
    @Column(nullable = false)
    private boolean active;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
