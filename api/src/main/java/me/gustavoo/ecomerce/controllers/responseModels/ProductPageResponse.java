package me.gustavoo.ecomerce.controllers.responseModels;

import lombok.Data;
import me.gustavoo.ecomerce.db.models.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductPageResponse {
    private final long id;
    private final String title;
    private final String slug;
    private final String shortDescription;
    private final String description;
    private final List<ProductResponse> products;
    private final List<AttributeResponse> attributes;
    private final List<PageImageResponse> images;

    public ProductPageResponse(ProductPageModel productPageModel) {
        this.id = productPageModel.getId();
        this.title = productPageModel.getTitle();
        this.slug = productPageModel.getSlug();
        this.shortDescription = productPageModel.getShortDescription();
        this.description = productPageModel.getDescription();

        List<ProductModel> _products = productPageModel.getProducts();
        List<ProductResponse> products = new ArrayList<>(_products.size());
        for (ProductModel _product : _products) {
            List<ProductValueModel> _values = _product.getValues();
            List<ProductValueResponse> values = new ArrayList<>(_values.size());
            for (ProductValueModel _value : _values) {
                AttributeValueModel _attribute_value = _value.getAttributeValue();
                values.add(new ProductValueResponse(_value.getId(), _attribute_value.getId(), _attribute_value.getValue(), _product.getId()));
            }

            products.add(new ProductResponse(_product.getId(), productPageModel.getId(), _product.getPrice(), _product.getImagePath(), values));
        }

        List<AttributeModel> _attributes = productPageModel.getAttributes();
        List<AttributeResponse> attributes = new ArrayList<>(_attributes.size());
        for (AttributeModel _attribute : _attributes) {
            List<AttributeValueModel> _values = _attribute.getValues();

            List<AttributeValueResponse> values = new ArrayList<>(_values.size());
            for (AttributeValueModel _value : _values) {
                values.add(new AttributeValueResponse(_value.getId(), _attribute.getId(), _value.getValue()));
            }

            attributes.add(new AttributeResponse(_attribute.getId(), _attribute.getName(), productPageModel.getId(), values));
        }

        List<PageImageModel> _images = productPageModel.getImages();
        List<PageImageResponse> images = new ArrayList<>(_images.size());
        for (PageImageModel _image : _images) {
            images.add(new PageImageResponse(_image.getId(), productPageModel.getId(), _image.getPath(), _image.getIndex()));
        }

        this.products = products;
        this.attributes = attributes;
        this.images = images;
    }
}
