package me.gustavoo.ecomerce.controllers.responseModels;

import lombok.Data;

@Data
public class AttributeValueResponse {
    private final long id;
    private final long attributeId;
    private final String value;
    private final boolean active;
}
