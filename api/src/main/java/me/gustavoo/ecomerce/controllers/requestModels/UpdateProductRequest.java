package me.gustavoo.ecomerce.controllers.requestModels;

import lombok.Data;

import java.util.List;

@Data
public class UpdateProductRequest {
    private final long id;
    private final String price;
    private final String stock;
    private final String imagePath;
    private final List<String> values;
    private final boolean active;
}
