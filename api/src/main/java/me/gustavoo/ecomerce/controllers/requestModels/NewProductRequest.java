package me.gustavoo.ecomerce.controllers.requestModels;

import lombok.Data;

@Data
public class NewProductRequest {
    private final long pageId;
    private final String price;
    private final String imagePath;
}
