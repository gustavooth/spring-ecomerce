package me.gustavoo.ecomerce.controllers.requestModels;

import lombok.Data;

import java.util.List;

@Data
public class UpdateProductPageRequest {
    private final long id;
    private final String title;
    private final String slug;
    private final String shortDescription;
    private final String description;
    private final List<NewProductRequest> products;
    private final List<NewAttributeRequest> attributes;
    private final List<NewPageImageRequest> images;
    private final List<UpdateProductRequest> updateProducts;
    private final List<UpdateAttributeRequest> updateAttributes;
    private final List<UpdateAttributeValueRequest> updateAttributeValues;
}
