package me.gustavoo.ecomerce.db.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Table(name = "product_values")
@Data
@NoArgsConstructor @RequiredArgsConstructor
public class ProductValueModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @ManyToOne
    private AttributeValueModel attributeValue;

    @NonNull
    @ManyToOne @JsonBackReference
    private ProductModel product;
}
