package me.gustavoo.ecomerce.controllers.requestModels;

import lombok.Data;

@Data
public class NewProductValueRequest {
    private final long attributeValueId;
    private final long productId;
}
