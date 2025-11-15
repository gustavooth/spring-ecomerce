package me.gustavoo.ecomerce.controllers.responseModels;

import lombok.Data;

import java.util.List;

@Data
public class PublicAttributeResponse {
    private final String name;
    private final List<PublicAttributeValueResponse> values;
}
