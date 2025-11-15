package me.gustavoo.ecomerce.db.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Table(name = "attributes")
@Data @NoArgsConstructor @RequiredArgsConstructor
public class AttributeModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Column(nullable = false, length = 50)
    private String name;

    @NonNull
    @ManyToOne @JsonBackReference
    private ProductPageModel page;

    @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL)
    private List<AttributeValueModel> values;
}
