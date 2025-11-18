package me.gustavoo.ecomerce.controllers.responseModels;

import lombok.Data;

@Data
public class ProductValueResponse {
    private final long id;
    private final long valueId;
    private final String value;
    private final long productId;
    private final boolean active;
}
