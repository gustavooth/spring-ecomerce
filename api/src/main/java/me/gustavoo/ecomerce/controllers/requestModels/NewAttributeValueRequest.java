package me.gustavoo.ecomerce.controllers.requestModels;

import lombok.Data;
import me.gustavoo.ecomerce.db.models.AttributeModel;

@Data
public class NewAttributeValueRequest {
    private final long attributeId;
    private final String value;
}
