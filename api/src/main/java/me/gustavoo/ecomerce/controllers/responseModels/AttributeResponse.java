package me.gustavoo.ecomerce.controllers.responseModels;

import lombok.Data;

import java.util.List;

@Data
public class AttributeResponse {
    private final long id;
    private final String name;
    private final long pageId;
    private final List<AttributeValueResponse> values;
}
