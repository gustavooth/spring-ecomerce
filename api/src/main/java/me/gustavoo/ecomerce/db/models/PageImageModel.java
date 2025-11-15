package me.gustavoo.ecomerce.db.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "page_images")
@Data @NoArgsConstructor @RequiredArgsConstructor
public class PageImageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @ManyToOne @JsonBackReference
    private ProductPageModel page;

    @NonNull @Column(nullable = false, length = 255)
    private String path;

    @NonNull @Column(nullable = false)
    private int index;
}
