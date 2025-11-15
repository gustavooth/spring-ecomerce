package me.gustavoo.ecomerce.controllers.responseModels;

import lombok.Data;

import java.util.List;

@Data
public class PublicProductResponse {
    private final Double price;
    private final String imagePath;
    private final List<PublicProductValueResponse> values;
}
