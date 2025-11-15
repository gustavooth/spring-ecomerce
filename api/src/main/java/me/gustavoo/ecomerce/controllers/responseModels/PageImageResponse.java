package me.gustavoo.ecomerce.controllers.responseModels;

import lombok.Data;
import me.gustavoo.ecomerce.db.models.PageImageModel;

@Data
public class PageImageResponse {
    private final long id;
    private final long pageId;
    private final String path;
    private final int index;
}
