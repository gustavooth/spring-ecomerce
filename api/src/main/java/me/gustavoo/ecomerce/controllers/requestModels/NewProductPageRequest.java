package me.gustavoo.ecomerce.controllers.requestModels;

import lombok.Data;

@Data
public class NewProductPageRequest {
    private final String title;
    private final String slug;
    private final String shortDescription;
    private final String description;
}
