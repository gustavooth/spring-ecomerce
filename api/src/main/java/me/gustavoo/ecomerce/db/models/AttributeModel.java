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
@Table(name = "attributes")
@Data @NoArgsConstructor @RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AttributeModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Column(nullable = false, length = 50)
    private String name;

    @NonNull
    private boolean showImage;

    @NonNull
    @ManyToOne @JsonBackReference
    private ProductPageModel page;

    @NonNull
    @Column(nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL)
    private List<AttributeValueModel> values;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
