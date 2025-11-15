package me.gustavoo.ecomerce.db.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "attribute_values")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class AttributeValueModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @JsonBackReference
    @ManyToOne
    private AttributeModel attribute;

    @NonNull
    @Column(nullable = false, length = 50)
    private String value;
}
