package me.gustavoo.ecomerce.controllers.requestModels;

import lombok.Data;

@Data
public class UpdateAttributeValueRequest {
    private final long id;
    private final String value;
    private final boolean active;
}
