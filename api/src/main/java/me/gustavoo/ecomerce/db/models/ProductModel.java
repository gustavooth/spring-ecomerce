package me.gustavoo.ecomerce.db.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Product")
public class ProductModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int Id;

    @Column(nullable = false)
    public long pageId;

    @Column(nullable = false)
    public Double price;

    @Column(length = 255, nullable = false)
    public String shortImagePath;

    @Column(length = 100, nullable = false)
    public String variationTypes;

    public ProductModel (long pageId, String shortImagePath, String variationTypes) {
        this.pageId = pageId;
        this.shortImagePath = shortImagePath;
        this.variationTypes = variationTypes;
    }
}
