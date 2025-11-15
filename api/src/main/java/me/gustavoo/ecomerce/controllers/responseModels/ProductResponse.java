package me.gustavoo.ecomerce.controllers.responseModels;

import lombok.Data;

import java.util.List;

@Data
public class ProductResponse {
    private final int id;
    private final long pageId;
    private final Double price;
    private final String imagePath;
    private final List<ProductValueResponse> values;
}
