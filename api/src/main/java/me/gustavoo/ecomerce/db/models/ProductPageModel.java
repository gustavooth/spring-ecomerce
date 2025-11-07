package me.gustavoo.ecomerce.db.models;

import jakarta.persistence.*;

@Entity
@Table(name = "ProductPage")
public class ProductPageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(length = 255, nullable = false)
    public String title;

    @Column(length = 255, nullable = false, unique = true)
    public String slug;

    @Column(length = 500, nullable = false)
    public String imagePaths;

    @Column(length = 500, nullable = false)
    public String shotDescription;

    @Column(nullable = false)
    public String description;

    public ProductPageModel(String title, String slug, String imagePaths, String shotDescription, String description) {
        this.title = title;
        this.slug = slug;
        this.imagePaths = imagePaths;
        this.shotDescription = shotDescription;
        this.description = description;
    }
}
