package me.gustavoo.ecomerce.controllers.requestModels;

import lombok.Data;

import java.util.List;

@Data
public class UpdateAttributeRequest {
    private final long id;
    private final String name;
    private final boolean showImage;
    private final List<NewAttributeValueRequest> newValues;
    private final boolean active;
}
