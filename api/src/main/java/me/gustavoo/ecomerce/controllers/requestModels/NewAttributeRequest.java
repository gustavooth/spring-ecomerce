package me.gustavoo.ecomerce.controllers.requestModels;

import lombok.Data;

@Data
public class NewAttributeRequest {
    private final String name;
    private final long pageId;
}
