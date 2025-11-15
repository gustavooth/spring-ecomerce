package me.gustavoo.ecomerce.controllers.requestModels;

import lombok.Data;
import me.gustavoo.ecomerce.db.models.ProductPageModel;

@Data
public class NewPageImageRequest {
    private final long pageId;
    private final String path;
    private final int index;
}
